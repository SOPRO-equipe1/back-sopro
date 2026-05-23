package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.dto.UsuarioCadastroDTO;
import com.sopro.project_demoday.dto.UsuarioAtualizacaoDTO;
import com.sopro.project_demoday.dto.UsuarioRespostaDTO;
import com.sopro.project_demoday.service.UsuarioService;
import jakarta.validation.Valid;
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
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioRespostaDTO> cadastrar(@Valid @RequestBody UsuarioCadastroDTO dto) {
        UsuarioRespostaDTO response = usuarioService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> buscarPorId(@PathVariable Long id) {
        UsuarioRespostaDTO response = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioRespostaDTO>> listarTodos() {
        List<UsuarioRespostaDTO> response = usuarioService.listarTodos();
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioAtualizacaoDTO dto) {
        UsuarioRespostaDTO response = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
