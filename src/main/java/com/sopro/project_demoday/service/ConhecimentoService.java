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

    private final String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";

    @Value("${GEMINI_API_KEY:}")
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
            return "Erro: A chave GEMINI_API_KEY não foi configurada no arquivo application.properties ou no ambiente.";
        }

        // 1. Coleta a base de dados reais do projeto para alimentar o RAG
        List<Conhecimento> todosConhecimentos = repository.findAll();
        String dadosFiltradosDoBanco = filtrarContextoRelevante(todosConhecimentos, mensagemUsuario);

        // 2. Engenharia de Prompt: Define a persona do Soprinho e injeta os dados do MySQL
        String promptSistema = "Você é o Soprinho, o assistente virtual oficial de tecnologia assistiva do projeto SOPRO. " +
                "Seu papel é auxiliar pessoas com limitações motoras ou de fala, além de seus familiares, com respostas empáticas, claras e extremamente precisas.\n\n" +
                "DIRETRIZES DE SEGURANÇA E CONTEXTO:\n" +
                "1. Baseie sua resposta prioritariamente nos fatos reais do projeto listados abaixo.\n" +
                "2. Se a informação necessária não estiver presente nos dados reais fornecidos abaixo, responda cordialmente explicando que o projeto está em constante evolução e que você ainda não possui esse detalhe específico, evitando inventar características técnicas ou comerciais não existentes.\n" +
                "3. Nunca mencione termos técnicos internos como 'banco de dados', 'JSON' ou 'SQL' para o usuário final.\n\n" +
                "DADOS REAIS DO PROJETO SOPRO:\n" +
                "\"\"\"\n" + dadosFiltradosDoBanco + "\n\"\"\"\n\n" +
                "Mensagem enviada pelo usuário: " + mensagemUsuario;

        // 3. Montagem do Payload estruturado exigido pela API do Gemini
        Map<String, Object> textPart = Map.of("text", promptSistema);
        Map<String, Object> contentObject = Map.of(
                "role", "user",
                "parts", List.of(textPart)
        );

        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.2);
        generationConfig.put("maxOutputTokens", 2000);

        Map<String, Object> corpoRequisicao = new HashMap<>();
        corpoRequisicao.put("contents", List.of(contentObject));
        corpoRequisicao.put("generationConfig", generationConfig);

        try {

            Map<?, ?> respostaRaw = restClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao)
                    .retrieve()
                    .body(Map.class);

            return extrairTextoDoGemini(respostaRaw);

        } catch (Exception e) {

            System.err.println("⚠️ Alerta SOPRO: Instabilidade detectada na API externa. Acionando base local... Erro: " + e.getMessage());
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
            return "Olá! O Soprinho está operando em modo de contingência local devido à alta demanda nos nossos servidores centrais. " +
                    "Com base nas especificações do projeto: " + dadosLocais.getConteudo();
        }


        return "Olá! O Soprinho está operando em modo de segurança local no momento devido a uma instabilidade temporária nos servidores da nuvem. " +
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

    private String extrairTextoDoGemini(Map<?, ?> respostaRaw) {
        try {
            if (respostaRaw != null && respostaRaw.containsKey("candidates")) {
                List<?> candidates = (List<?>) respostaRaw.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<?, ?> firstCandidate = (Map<?, ?>) candidates.get(0);
                    Map<?, ?> content = (Map<?, ?>) firstCandidate.get("content");
                    if (content != null && content.containsKey("parts")) {
                        List<?> parts = (List<?>) content.get("parts");
                        if (!parts.isEmpty()) {
                            Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);
                            if (firstPart.containsKey("text")) {
                                return (String) firstPart.get("text");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "Erro ao processar a estrutura de resposta do motor de IA.";
        }
        return "O Gemini não conseguiu gerar uma resposta estruturada válida.";
    }
}