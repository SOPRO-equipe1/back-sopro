package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.dto.PerfilResponseDTO;
import com.sopro.project_demoday.dto.SalvarPerfilDTO;
import com.sopro.project_demoday.service.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }


    @GetMapping
    public ResponseEntity<PerfilResponseDTO> obterPerfil(@RequestParam String email) {
        PerfilResponseDTO response = perfilService.obterPerfilPorEmail(email);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/dados-pessoais")
    public ResponseEntity<String> salvarDadosPessoais(@RequestParam String email, @RequestBody SalvarPerfilDTO dto) {
        try {
            perfilService.atualizarDadosPessoais(email, dto);
            return ResponseEntity.ok("Informações pessoais atualizadas com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar dados pessoais: " + e.getMessage());
        }
    }


    @PutMapping("/endereco")
    public ResponseEntity<String> salvarEndereco(@RequestParam String email, @RequestBody SalvarPerfilDTO dto) {
        try {
            perfilService.atualizarEndereco(email, dto);
            return ResponseEntity.ok("Endereço de entrega cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar endereço: " + e.getMessage());
        }
    }
}