package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.dto.PreferenciasDTO;
import com.sopro.project_demoday.service.PreferenciasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/preferencias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PreferenciasController {
    
    @Autowired
    private PreferenciasService preferenciasService;
    
    @GetMapping
    public ResponseEntity<PreferenciasDTO> buscar(@PathVariable Long usuarioId) {
        PreferenciasDTO response = preferenciasService.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping
    public ResponseEntity<PreferenciasDTO> salvar(@PathVariable Long usuarioId, @Valid @RequestBody PreferenciasDTO dto) {
        PreferenciasDTO response = preferenciasService.salvar(usuarioId, dto);
        return ResponseEntity.ok(response);
    }
}
