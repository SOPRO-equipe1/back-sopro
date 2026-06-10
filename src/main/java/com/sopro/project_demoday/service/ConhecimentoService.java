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


    private final String apiUrl = "https://ollama.com/v1/chat/completions";


    @Value("${OLLAMA_API_KEY:}")
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
            return "Erro: A chave OLLAMA_API_KEY não foi configurada no arquivo .env ou no ambiente.";
        }


        List<Conhecimento> todosConhecimentos = repository.findAll();
        String dadosFiltradosDoBanco = filtrarContextoRelevante(todosConhecimentos, mensagemUsuario);


        String promptSistema = "Você é o Soprinho, o assistente virtual oficial e carinhoso do projeto SOPRO. " +
                "Seu papel é responder perguntas de forma extremamente acolhedora, empática, clara e humanizada. Use emojis de forma sutil para transmitir calor humano (como 😊, ✨, 💙).\n\n" +
                "DIRETRIZES CRÍTICAS DE SEGURANÇA E CONTEXTO:\n" +
                "1. O usuário JÁ ESTÁ navegando na nossa página/plataforma oficial. Nunca diga para ele 'acessar o site oficial' ou procurar atualizações em outro link. Se quiser orientá-lo, diga para acompanhar as novidades por aqui mesmo, nesta página.\n" +
                "2. O projeto SOPRO NÃO possui newsletter, NÃO possui e-mails de contato externos (como contato@sopro.org) e NÃO possui formulários em outros locais. Nunca invente esses canais de comunicação.\n" +
                "3. Baseie sua resposta EXCLUSIVAMENTE nos fatos reais e verdadeiros listados abaixo. Se a informação não estiver presente de forma explícita (como preços ou links de compra), explique de forma muito doce e gentil que o protótipo está em fase de validação para o Demo Day e que em breve teremos novidades compartilhadas aqui na página.\n" +
                "4. Nunca mencione termos internos de programação como 'banco de dados', 'JSON', 'SQL' ou 'RAG'.\n\n" +
                "DADOS REAIS E VERDADEIROS DO PROJETO SOPRO:\n" +
                "\"\"\"\n" + dadosFiltradosDoBanco + "\n\"\"\"";


        Map<String, String> messageSystem = Map.of("role", "system", "content", promptSistema);
        Map<String, String> messageUser = Map.of("role", "user", "content", mensagemUsuario);

        Map<String, Object> corpoRequisicao = new HashMap<>();
        corpoRequisicao.put("model", "gpt-oss:120b"); // O modelo do gateway Ollama
        corpoRequisicao.put("messages", List.of(messageSystem, messageUser));


        corpoRequisicao.put("temperature", 0.1);

        try {
            // Dispara adicionando o cabeçalho "Authorization: Bearer" com a OLLAMA_API_KEY
            Map<?, ?> respostaRaw = restClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao)
                    .retrieve()
                    .body(Map.class);

            return extrairTextoDoOllamaOss(respostaRaw);
        } catch (Exception e) {
            System.err.println("⚠️ Alerta SOPRO: Instabilidade detectada na API externa do Ollama. Erro: " + e.getMessage());
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
            Conhecimento dadosLocais = correspondencias.get(0);
            return "Olá! 😊 O Soprinho está operando em um modo de segurança local no momento, mas com muito carinho te informo que, " +
                    "de acordo com as especificações oficiais: " + dadosLocais.getConteudo();
        }

        return "Olá! 💙 O Soprinho está operando em modo de segurança simplificado agora. " +
                "Para que eu possa te ajudar melhor, tente digitar termos mais simples como 'sensor', 'dispositivo' ou 'design'. Estou aqui por você!";
    }

    private String filtrarContextoRelevante(List<Conhecimento> base, String pergunta) {
        String perguntaMin = pergunta.toLowerCase();

        List<Conhecimento> filtrados = base.stream()
                .filter(c -> perguntaMin.contains(c.getTitulo().toLowerCase()) ||
                        (c.getMetadados() != null && verificarTags(c.getMetadados(), perguntaMin)))
                .collect(Collectors.toList());

        if (filtrados.isEmpty()) {
            filtrados = base;
        }

        return filtrados.stream()
                .map(c -> "Tópico: " + c.getTitulo() + " -> Informação: " + c.getConteudo())
                .collect(Collectors.joining("\n"));
    }

    private boolean verificarTags(String metadados, String pergunta) {
        for (String tag : metadados.split(",")) {
            if (pergunta.contains(tag.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String extrairTextoDoOllamaOss(Map<?, ?> respostaRaw) {
        try {
            if (respostaRaw != null && respostaRaw.containsKey("choices")) {
                List<?> choices = (List<?>) respostaRaw.get("choices");
                if (!choices.isEmpty()) {
                    Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
                    Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");
                    if (message != null && message.containsKey("content")) {
                        return (String) message.get("content");
                    }
                }
            }
        } catch (Exception e) {
            return "Erro ao ler a resposta estruturada do motor de inteligência artificial do Ollama.";
        }
        return "A API do Ollama não retornou um formato de texto válido.";
    }
}