package com.sopro.project_demoday.controller;


import com.sopro.project_demoday.service.SoprinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/soprinho")
public class SoprinhoController {

    @Autowired
    private SoprinhoService service;

    @GetMapping("/pergunta")
    public ResponseEntity<String> obterResposta(@RequestParam("q") String termo) {
        String resposta = service.processarPergunta(termo);
        return ResponseEntity.ok(resposta);
    }
}
