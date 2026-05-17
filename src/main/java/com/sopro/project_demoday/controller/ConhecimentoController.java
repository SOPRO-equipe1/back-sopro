package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.service.ConhecimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/conhecimento")

public class ConhecimentoController {

    @Autowired
    private ConhecimentoService chatbotService;


    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> conversarComIA(@RequestBody Map<String, String> payload) {
        String pergunta = payload.get("pergunta");
        String resposta = chatbotService.sussurrarParaIA(pergunta);
        return ResponseEntity.ok(Map.of("resposta", resposta));
    }
}