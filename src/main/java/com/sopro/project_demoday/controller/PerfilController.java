package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.dto.PerfilResponseDTO;
import com.sopro.project_demoday.dto.SalvarPerfilDTO;
import com.sopro.project_demoday.service.PerfilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PerfilController {

    private static final Logger logger = LoggerFactory.getLogger(PerfilController.class.getName());

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public ResponseEntity<PerfilResponseDTO> obterPerfil(@RequestParam String email) {
        try {
            PerfilResponseDTO response = perfilService.obterPerfilPorEmail(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao buscar informações do perfil para o e-mail solicitado.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/dados-pessoais")
    public ResponseEntity<String> salvarDadosPessoais(@RequestParam String email, @RequestBody SalvarPerfilDTO dto) {
        try {
            perfilService.atualizarDadosPessoais(email, dto);
            return ResponseEntity.ok("Informações pessoais atualizadas com sucesso!");
        } catch (Exception e) {
            logger.error("Falha ao atualizar dados pessoais do usuário.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Não foi possível salvar as informações pessoais devido a um erro interno no servidor.");
        }
    }

    @PutMapping("/endereco")
    public ResponseEntity<String> salvarEndereco(@RequestParam String email, @RequestBody SalvarPerfilDTO dto) {
        try {
            perfilService.atualizarEndereco(email, dto);
            return ResponseEntity.ok("Endereço de entrega cadastrado com sucesso!");
        } catch (Exception e) {
            logger.error("Falha ao registrar dados de logística e endereço de entrega.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Não foi possível registrar o endereço de entrega devido a um erro interno no servidor.");
        }
    }
}