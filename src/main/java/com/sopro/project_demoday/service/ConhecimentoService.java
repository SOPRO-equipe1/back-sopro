package com.sopro.project_demoday.service;

import com.sopro.project_demoday.model.Conhecimento;
import com.sopro.project_demoday.repository.ConhecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConhecimentoService {

    private final String baseUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash-latest:generateContent";

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Autowired
    private ConhecimentoRepository repository;

    private final RestClient restClient;

    public ConhecimentoService() {
        this.restClient = RestClient.builder().build();
    }

    public String sussurrarParaIA(String mensagemUsuario) {
        if (mensagemUsuario == null || mensagemUsuario.trim().isEmpty()) {
            return "Olá! 😊 Por favor, envie uma mensagem válida para que eu possa te ajudar.";
        }

        List<Conhecimento> todosConhecimentos = repository.findAll();

        // Tenta chamar o Gemini, se falhar ou der 404, a contingência assume sem quebrar a tela!
        try {
            if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
                throw new IllegalStateException("Chave ausente");
            }

            String dadosFiltradosDoBanco = filtrarContextoRelevante(todosConhecimentos, mensagemUsuario);

            String promptConsolidado = "INSTRUÇÕES DO SISTEMA:\n" +
                    "Você é o Soprinho, o assistente virtual oficial e carinhoso do projeto SOPRO.\n\n" +
                    "REGRAS CRÍTICAS DE CONTEXTO:\n" +
                    "1. Baseie sua resposta UNICAMENTE nos dados reais fornecidos abaixo.\n" +
                    "2. Nunca diga que o SOPRO mede poeira, qualidade do ar ou temperatura.\n\n" +
                    "DADOS REAIS DO PROJETO SOPRO:\n" + dadosFiltradosDoBanco + "\n\n" +
                    "PERGUNTA DO USUÁRIO: " + mensagemUsuario;

            Map<String, Object> textPart = Map.of("text", promptConsolidado);
            Map<String, Object> userContent = Map.of("parts", List.of(textPart));
            Map<String, Object> corpoRequisicao = new HashMap<>();
            corpoRequisicao.put("contents", List.of(userContent));

            String respostaRawJson = restClient.post()
                    .uri(baseUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao)
                    .retrieve()
                    .body(String.class);

            return extrairTextoDoGemini(respostaRawJson);

        } catch (Exception e) {
            System.err.println("Alerta SOPRO: Ativando Contingência Avançada para o DemoDay. Erro original: " + e.getMessage());
            return gerarRespostaDeContingenciaLocal(todosConhecimentos, mensagemUsuario);
        }
    }

    private String gerarRespostaDeContingenciaLocal(List<Conhecimento> base, String perguntaUsuario) {
        String perguntaMin = perguntaUsuario.toLowerCase();

        // Procura no banco se a pergunta bate com o título ou com alguma tag nos metadados
        for (Conhecimento c : base) {
            String tituloMin = c.getTitulo().toLowerCase();
            if (perguntaMin.contains(tituloMin) || tituloMin.contains(perguntaMin)) {
                return c.getConteudo();
            }
            if (c.getMetadados() != null) {
                for (String tag : c.getMetadados().split(",")) {
                    String tagLimpa = tag.trim().toLowerCase();
                    if (!tagLimpa.isEmpty() && perguntaMin.contains(tagLimpa)) {
                        return c.getConteudo();
                    }
                }
            }
        }

        // Se o jurado perguntar algo genérico ou fora do script, o Soprinho responde com carinho
        return "Olá! 💙 No momento, o nosso protótipo do SOPRO está em fase de validação para a banca do DemoDay! " +
                "O nosso dispositivo é focado em comunicação assistiva, utilizando sensores de sopro e processamento inteligente para devolver a autonomia a quem precisa. " +
                "O que mais você gostaria de saber sobre o nosso projeto?";
    }

    private String filtrarContextoRelevante(List<Conhecimento> base, String pergunta) {
        String perguntaMin = pergunta.toLowerCase();
        List<Conhecimento> filtrados = base.stream()
                .filter(c -> c.getTitulo().toLowerCase().contains(perguntaMin) ||
                        perguntaMin.contains(c.getTitulo().toLowerCase()) ||
                        (c.getMetadados() != null && verificarTags(c.getMetadados(), perguntaMin)))
                .collect(Collectors.toList());

        if (filtrados.isEmpty()) {
            filtrados = base.stream()
                    .filter(c -> c.getMetadados().contains("institucional") || c.getMetadados().contains("tecnico"))
                    .collect(Collectors.toList());
        }

        return filtrados.stream()
                .map(c -> "Tópico: " + c.getTitulo() + " -> Informação: " + c.getConteudo())
                .collect(Collectors.joining("\n"));
    }

    private boolean verificarTags(String metadados, String pergunta) {
        for (String tag : metadados.split(",")) {
            String tagLimpa = tag.trim().toLowerCase();
            if (!tagLimpa.isEmpty() && (pergunta.contains(tagLimpa) || tagLimpa.contains(pergunta))) {
                return true;
            }
        }
        return false;
    }

    private String extrairTextoDoGemini(String jsonString) {
        try {
            com.google.gson.JsonObject jsonObject = com.google.gson.JsonParser.parseString(jsonString).getAsJsonObject();
            if (jsonObject.has("candidates")) {
                com.google.gson.JsonArray candidates = jsonObject.getAsJsonArray("candidates");
                if (candidates.size() > 0) {
                    com.google.gson.JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                    if (firstCandidate.has("content")) {
                        com.google.gson.JsonObject content = firstCandidate.getAsJsonObject("content");
                        if (content.has("parts")) {
                            com.google.gson.JsonArray parts = content.getAsJsonArray("parts");
                            if (parts.size() > 0) {
                                return parts.get(0).getAsJsonObject().get("text").getAsString();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "Erro ao processar o JSON de resposta do Gemini.";
        }
        return "A API do Gemini não retornou um formato de texto válido.";
    }
}