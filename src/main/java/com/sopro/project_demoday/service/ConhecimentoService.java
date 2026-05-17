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
        if (mensagemUsuario == null || mensagemUsuario.trim().isEmpty()) {
            return "Por favor, envie uma mensagem válida.";
        }

        // Se você não tiver configurado a chave ou ela estiver vazia, aciona a resposta local imediatamente
        if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
            return buscarRespostaLocalDeContingencia(mensagemUsuario);
        }

        Map<String, String> promptSistema = Map.of(
                "role", "system",
                "content", "Você é o Soprinho, assistente virtual inteligente do projeto SOPRO. Ajude pessoas com limitações na fala ou motoras de forma clara e empática."
        );

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
           //simulação
            return buscarRespostaLocalDeContingencia(mensagemUsuario);
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
        return "A IA não conseguiu gerar uma resposta válida no momento.";
    }


    private String buscarRespostaLocalDeContingencia(String pergunta) {
        String perguntaMin = pergunta.toLowerCase();

        if (perguntaMin.contains("comprar") || perguntaMin.contains("venda") || perguntaMin.contains("preço") || perguntaMin.contains("valor")) {
            return "O SOPRO pode ser adquirido através do nosso site oficial. Entre em contato com nosso time comercial para detalhes de valores e condições de aquisição.";
        }
        if (perguntaMin.contains("como funciona") || perguntaMin.contains("tecnologia") || perguntaMin.contains("produto") || perguntaMin.contains("sensor")) {
            return "O wearable do projeto SOPRO utiliza sensores de pressão de alta precisão e algoritmos de TinyML (Machine Learning embarcado) para traduzir os padrões e a intensidade dos sopros do usuário em frases completas e contextualizadas no aplicativo.";
        }
        if (perguntaMin.contains("quem pode usar") || perguntaMin.contains("indicado") || perguntaMin.contains("limitação") || perguntaMin.contains("mutismo")) {
            return "O dispositivo é especialmente indicado para pessoas com limitações motoras severas ou mutismo, desde que o usuário preserve o controle respiratório voluntário para interagir com os sensores.";
        }
        if (perguntaMin.contains("ajuda") || perguntaMin.contains("suporte")) {
            return "Olá! Eu sou o Soprinho. Posso te ajudar explicando como o wearable funciona, quem pode utilizar o dispositivo ou como realizar a aquisição do sistema.";
        }

        // Resposta padrão caso nenhuma palavra-chave mestre seja acionada
        return "Olá! Eu sou o Soprinho, o assistente do projeto SOPRO. No momento estou operando em modo de demonstração local. Posso te ajudar respondendo sobre o 'funcionamento do dispositivo', 'quem pode usar' ou 'como comprar'.";
    }
}