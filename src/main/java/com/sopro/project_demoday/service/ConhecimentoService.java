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
            return "Por favor, envie uma mensagem válida.";
        }


        if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
            return "Erro: A chave OLLAMA_API_KEY não foi configurada no arquivo .env ou no ambiente.";
        }


        List<Conhecimento> todosConhecimentos = repository.findAll();
        String dadosFiltradosDoBanco = filtrarContextoRelevante(todosConhecimentos, mensagemUsuario);


        String promptSistema = "Você é o Soprinho, o assistente virtual oficial de tecnologia assistiva do projeto SOPRO. " +
                "Seu papel é auxiliar pessoas com limitações motoras ou de fala, além de seus familiares, com respostas empáticas, claras e extremamente precisas.\n\n" +
                "DIRETRIZES DE SEGURANÇA E CONTEXTO:\n" +
                "1. Baseie sua resposta prioritariamente nos fatos reais do projeto listados abaixo.\n" +
                "2. Se a informação necessária não estiver presente nos dados reais fornecidos abaixo, responda cordialmente explicando que o projeto está em constante evolução e que você ainda não possui esse detalhe específico, evitando inventar características técnicas ou comerciais não existentes.\n" +
                "3. Nunca mencione termos técnicos internos como 'banco de dados', 'JSON' ou 'SQL' para o usuário final.\n\n" +
                "DADOS REAIS DO PROJETO SOPRO:\n" +
                "\"\"\"\n" + dadosFiltradosDoBanco + "\n\"\"\"";


        Map<String, String> messageSystem = Map.of("role", "system", "content", promptSistema);
        Map<String, String> messageUser = Map.of("role", "user", "content", mensagemUsuario);

        Map<String, Object> corpoRequisicao = new HashMap<>();
        corpoRequisicao.put("model", "gpt-oss:120b"); // <-- O modelo gigante que seu amigo descobriu!
        corpoRequisicao.put("messages", List.of(messageSystem, messageUser));


        corpoRequisicao.put("temperature", 0.1);

        try {

            Map<?, ?> respostaRaw = restClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao)
                    .retrieve()
                    .body(Map.class);

            return extrairTextoDoMundoOss(respostaRaw);

        } catch (Exception e) {

            System.err.println("⚠️ Alerta SOPRO: Instabilidade detectada na API remota. Erro: " + e.getMessage());
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
            return "Olá! O Soprinho está operando em modo de contingência de segurança no momento. " +
                    "Com base nas especificações do projeto: " + dadosLocais.getConteudo();
        }

        return "Olá! O Soprinho está operando em modo de segurança local no momento devido a uma instabilidade temporária na rede. " +
                "Para que eu possa te ajudar com precisão, tente utilizar palavras-chave simplificadas como 'sensor', 'dispositivo' ou 'plano'.";
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

    private String extrairTextoDoMundoOss(Map<?, ?> respostaRaw) {
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
            return "Erro ao processar a estrutura de resposta do motor de IA.";
        }
        return "O servidor remoto não conseguiu gerar uma resposta estruturada válida.";
    }
}