package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.dto.MensagemChatDTO;
import com.sopro.project_demoday.service.ConhecimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conhecimento")
@CrossOrigin
public class ConhecimentoController {


    private final ConhecimentoService conhecimentoService;


    public ConhecimentoController(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> responderPergunta(@RequestParam String email, @RequestBody MensagemChatDTO dto) {

        String respostaDoGemini = conhecimentoService.sussurrarParaIA(dto.mensagem());


        return ResponseEntity.ok(respostaDoGemini);
    }
}