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

        if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
            return "Erro: A chave GEMINI_API_KEY não foi configurada no ambiente do Azure.";
        }

        List<Conhecimento> todosConhecimentos = repository.findAll();
        String dadosFiltradosDoBanco = filtrarContextoRelevante(todosConhecimentos, mensagemUsuario);

        String promptConsolidado = "INSTRUÇÕES DO SISTEMA:\n" +
                "Você é o Soprinho, o assistente virtual oficial e carinhoso do projeto SOPRO.\n\n" +
                "REGRAS CRÍTICAS DE CONTEXTO:\n" +
                "1. Baseie sua resposta UNICAMENTE nos dados reais fornecidos abaixo. É PROIBIDO inventar fatos, utilidades, sensores ou funções para o dispositivo.\n" +
                "2. Se o usuário perguntar algo que não está explicitamente respondido nos dados abaixo, ou se você for tentar adivinhar, responda EXATAMENTE com o texto: \"Olá! 💙 No momento, o nosso protótipo está em fase de validação para o DemoDay e eu ainda não tenho essa resposta. Continue acompanhando as novidades por aqui mesmo!\"\n" +
                "3. Nunca diga que o SOPRO mede poeira, qualidade do ar, odores, poluição ou temperatura. Ele é um dispositivo focado em comunicação assistiva.\n\n" +
                "REGRAS DE FORMATAÇÃO:\n" +
                "1. Responda APENAS in texto corrido e parágrafos normais. É PROIBIDO usar asteriscos ou marcações Markdown.\n" +
                "2. Traduza termos técnicos: em vez de 'sensor de pressão', use 'o medidor que sente o seu sopro'.\n\n" +
                "DADOS REAIS DO PROJETO SOPRO:\n" +
                "\"\"\"\n" + dadosFiltradosDoBanco + "\n\"\"\"\n\n" +
                "PERGUNTA DO USUÁRIO: " + mensagemUsuario;

        Map<String, Object> textPart = Map.of("text", promptConsolidado);
        Map<String, Object> userContent = Map.of("parts", List.of(textPart));

        Map<String, Object> corpoRequisicao = new HashMap<>();
        corpoRequisicao.put("contents", List.of(userContent));

        Map<String, Object> generationConfig = Map.of("temperature", 0.1);
        corpoRequisicao.put("generationConfig", generationConfig);

        try {
            // Captura o JSON de resposta como String estável, evitando falhas com o Jackson do Spring
            String respostaRawJson = restClient.post()
                    .uri(baseUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao)
                    .retrieve()
                    .body(String.class);

            return extrairTextoDoGemini(respostaRawJson);
        } catch (Exception e) {
            System.err.println("Alerta SOPRO: Instabilidade no Gemini remoto. Erro: " + e.getMessage());
            return gerarRespostaDeContingenciaLocal(todosConhecimentos, mensagemUsuario);
        }
    }

    private String gerarRespostaDeContingenciaLocal(List<Conhecimento> base, String perguntaUsuario) {
        String perguntaMin = perguntaUsuario.toLowerCase();
        List<Conhecimento> correspondencias = base.stream()
                .filter(c -> perguntaMin.contains(c.getTitulo().toLowerCase()) ||
                        (c.getMetadados() != null && verificarTags(c.getMetadados(), perguntaMin)))
                .collect(Collectors.toList());

        if (!correspondencias.isEmpty()) {
            return "Olá! 😊 O Soprinho está operando em modo de segurança local no momento. De acordo com os dados oficiais: " + correspondencias.get(0).getConteudo();
        }
        return "Olá! 💙 O Soprinho está operando em modo de segurança simplificado agora. Tente pesquisar usando palavras-chave simples como 'sensor' ou 'dispositivo'.";
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
            // Deserialização segura utilizando a árvore estruturada do Gson
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