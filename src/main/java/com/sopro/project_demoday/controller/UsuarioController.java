package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.dto.UsuarioCadastroDTO;
import com.sopro.project_demoday.dto.UsuarioAtualizacaoDTO;
import com.sopro.project_demoday.dto.UsuarioRespostaDTO;
import com.sopro.project_demoday.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class.getName());

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioRespostaDTO> cadastrar(@Valid @RequestBody UsuarioCadastroDTO dto) {
        try {
            UsuarioRespostaDTO response = usuarioService.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Erro ao realizar o cadastro de novo usuário no sistema.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> buscarPorId(@PathVariable Long id) {
        try {
            UsuarioRespostaDTO response = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao buscar informações do usuário pelo ID informado.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioRespostaDTO>> listarTodos() {
        try {
            List<UsuarioRespostaDTO> response = usuarioService.listarTodos();
            if (response.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Falha ao listar todos os usuários cadastrados na base de dados.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioAtualizacaoDTO dto) {
        try {
            UsuarioRespostaDTO response = usuarioService.atualizar(id, dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao tentar atualizar as informações cadastrais do usuário.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        try {
            usuarioService.desativar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erro ao processar a desativação lógica da conta do usuário.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            usuarioService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Falha grave na exclusão física do registro de usuário do sistema.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}