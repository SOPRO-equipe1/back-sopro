package com.sopro.project_demoday.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConhecimentoService {


    private final String apiUrl = "https://api.openai.com/v1/chat/completions";


    @Value("${OPENAI_API_KEY:}")
    private String apiKey;

    private final RestClient restClient;

    public ConhecimentoService() {
        this.restClient = RestClient.builder().build();
    }

    public String sussurrarParaIA(String mensagemUsuario) {
        // Prompt do Sistema para guiar o comportamento do Soprinho
        Map<String, String> promptSistema = Map.of(
                "role", "system",
                "content", "Você é o Soprinho, assistente virtual inteligente do projeto SOPRO. Ajude pessoas com limitações na fala ou motoras de forma clara e empática."
        );

        // Prompt com a pergunta que veio do utilizador
        Map<String, String> promptUsuario = Map.of(
                "role", "user",
                "content", mensagemUsuario
        );

        Map<String, Object> corpoRequisicao = new HashMap<>();
        corpoRequisicao.put("model", "gpt-4o-mini");
        corpoRequisicao.put("messages", List.of(promptSistema, promptUsuario));

        try {
            Map<?, ?> respostaRaw = restClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao)
                    .retrieve()
                    .body(Map.class);

            return extrairTextoDaResposta(respostaRaw);
        } catch (Exception e) {
            return "O motor do chatbot falhou a conectar com a IA: " + e.getMessage();
        }
    }

    private String extrairTextoDaResposta(Map<?, ?> respostaRaw) {
        if (respostaRaw != null && respostaRaw.containsKey("choices")) {
            List<?> choices = (List<?>) respostaRaw.get("choices");
            if (!choices.isEmpty()) {
                Map<?, ?> primeiraEscolha = (Map<?, ?>) choices.get(0);
                Map<?, ?> mensagem = (Map<?, ?>) primeiraEscolha.get("message");

                if (mensagem != null && mensagem.containsKey("content")) {
                    return (String) mensagem.get("content");
                }
            }
        }
        return "A IA não conseguiu gerar uma resposta válida.";
    }
}