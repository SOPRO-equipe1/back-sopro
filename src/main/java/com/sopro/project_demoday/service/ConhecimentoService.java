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

    @Value("${ollama.api.url:http://thomas.proxy.rlwy.net:19693}")
    private String olllamaBaseUrl;

    @Value("${ollama.api.key:}")
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
        String dadosFiltradosDoBanco = filtrarContextoRelevante(todosConhecimentos, mensagemUsuario);

        String promptSistema = "Você é o Soprinho, o assistente virtual oficial e carinhoso do projeto SOPRO.\n\n" +
                "REGRAS CRÍTICAS DE CONTEXTO:\n" +
                "1. Baseie sua resposta UNICAMENTE nos dados reais fornecidos abaixo. É PROIBIDO inventar fatos, utilidades, sensores ou funções para o dispositivo.\n" +
                "2. Se o usuário perguntar algo que não está explicitamente respondido nos dados abaixo, ou se for um assunto completamente fora do escopo do projeto (como receitas, piadas ou culinária), responda EXATAMENTE com o texto: \"Olá! 💙 No momento, o nosso protótipo está em fase de validação para o DemoDay e eu ainda não tenho essa resposta. Continue acompanhando as novidades por aqui mesmo!\"\n" +
                "3. Nunca diga que o SOPRO mede poeira, qualidade do ar ou temperatura. Ele é um dispositivo focado em comunicação assistiva.\n\n" +
                "REGRAS DE FORMATAÇÃO:\n" +
                "1. Responda APENAS em texto corrido e parágrafos normais. É PROIBIDO usar asteriscos ou marcações Markdown.\n" +
                "2. Traduza termos técnicos: em vez de 'sensor de pressão', use 'o medidor que sente o seu sopro'.\n\n" +
                "DADOS REAIS DO PROJETO SOPRO:\n" +
                "\"\"\"\n" + dadosFiltradosDoBanco + "\n\"\"\"";


        Map<String, Object> systemMessage = Map.of("role", "system", "content", promptSistema);
        Map<String, Object> userMessage = Map.of("role", "user", "content", mensagemUsuario);

        Map<String, Object> corpoRequisicao = new HashMap<>();
        corpoRequisicao.put("model", "llama3");
        corpoRequisicao.put("messages", List.of(systemMessage, userMessage));
        corpoRequisicao.put("temperature", 0.1);


        String urlCompleta = olllamaBaseUrl.endsWith("/") ? olllamaBaseUrl + "v1/chat/completions" : olllamaBaseUrl + "/v1/chat/completions";

        try {
            RestClient.RequestBodySpec requestSpec = restClient.post()
                    .uri(urlCompleta)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao);

            if (this.apiKey != null && !this.apiKey.trim().isEmpty()) {
                requestSpec.header("Authorization", "Bearer " + apiKey);
            }

            String respostaRawJson = requestSpec.retrieve().body(String.class);
            return extrairTextoDoOllama(respostaRawJson);

        } catch (Exception e) {
            System.err.println("Alerta SOPRO: Instabilidade no Ollama. Ativando contingência local. Erro: " + e.getMessage());
            return gerarRespostaDeContingenciaLocal(todosConhecimentos, mensagemUsuario);
        }
    }

    private String gerarRespostaDeContingenciaLocal(List<Conhecimento> base, String perguntaUsuario) {
        String perguntaMin = perguntaUsuario.toLowerCase();

        for (Conhecimento c : base) {
            if (c.getTitulo() == null || c.getTitulo().trim().isEmpty()) {
                continue;
            }

            String tituloMin = c.getTitulo().toLowerCase();


            if (perguntaMin.contains(tituloMin) && tituloMin.length() > 3) {
                return c.getConteudo();
            }

            if (c.getMetadados() != null) {
                for (String tag : c.getMetadados().split(",")) {
                    String tagLimpa = tag.trim().toLowerCase();
                    if (!tagLimpa.isEmpty() && perguntaMin.contains(tagLimpa) && tagLimpa.length() > 2) {
                        return c.getConteudo();
                    }
                }
            }
        }


        return "Olá! 💙 No momento, o nosso protótipo está em fase de validação para o DemoDay. O SOPRO é um dispositivo de comunicação assistiva focado em devolver autonomia através do sopro. Como posso te ajudar com o projeto hoje?";
    }

    private String filtrarContextoRelevante(List<Conhecimento> base, String pergunta) {
        String perguntaMin = pergunta.toLowerCase();

        List<Conhecimento> filtrados = base.stream()
                .filter(c -> c.getTitulo().toLowerCase().contains(perguntaMin) ||
                        perguntaMin.contains(c.getTitulo().toLowerCase()) ||
                        (c.getMetadados() != null && verificarTags(c.getMetadados(), perguntaMin)))
                .collect(Collectors.toList());


        if (filtrados.isEmpty()) {
            return "Nenhum dado correspondente encontrado para esta pergunta específica.";
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

    private String extrairTextoDoOllama(String jsonString) {
        try {
            com.google.gson.JsonObject jsonObject = com.google.gson.JsonParser.parseString(jsonString).getAsJsonObject();
            if (jsonObject.has("choices")) {
                com.google.gson.JsonArray choices = jsonObject.getAsJsonArray("choices");
                if (choices.size() > 0) {
                    com.google.gson.JsonObject firstChoice = choices.get(0).getAsJsonObject();
                    if (firstChoice.has("message")) {
                        com.google.gson.JsonObject message = firstChoice.getAsJsonObject("message");
                        if (message.has("content")) {
                            return message.get("content").getAsString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "Erro ao processar a resposta do motor local.";
        }
        return "Formato de resposta inválido do motor de IA.";
    }
}