package com.sopro.project_demoday.service;

import com.sopro.project_demoday.dto.PreferenciasDTO;
import com.sopro.project_demoday.model.PreferenciasAcessibilidade;
import com.sopro.project_demoday.model.Usuario;
import com.sopro.project_demoday.repository.PreferenciasAcessibilidadeRepository;
import com.sopro.project_demoday.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PreferenciasService {
    
    @Autowired
    private PreferenciasAcessibilidadeRepository preferenciasRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public PreferenciasDTO buscarPorUsuario(Long usuarioId) {
        PreferenciasAcessibilidade preferencias = preferenciasRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Preferências não encontradas"));
        
        return converterParaDTO(preferencias);
    }
    
    public PreferenciasDTO salvar(Long usuarioId, PreferenciasDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        
        PreferenciasAcessibilidade preferencias = preferenciasRepository.findByUsuarioId(usuarioId)
                .orElse(new PreferenciasAcessibilidade(usuario));
        
        preferencias.setTamanhoFonte(dto.getTamanhoFonte());
        preferencias.setAltoContraste(dto.isAltoContraste());
        preferencias.setVelocidadeVoz(dto.getVelocidadeVoz());
        preferencias.setIdiomaVoz(dto.getIdiomaVoz());
        preferencias.setTipoEntrada(dto.getTipoEntrada());
        
        PreferenciasAcessibilidade preferenciasAtualizada = preferenciasRepository.save(preferencias);
        return converterParaDTO(preferenciasAtualizada);
    }
    
    private PreferenciasDTO converterParaDTO(PreferenciasAcessibilidade preferencias) {
        return new PreferenciasDTO(
            preferencias.getTamanhoFonte(),
            preferencias.isAltoContraste(),
            preferencias.getVelocidadeVoz(),
            preferencias.getIdiomaVoz(),
            preferencias.getTipoEntrada()
        );
    }
}
