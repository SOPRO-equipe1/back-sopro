package com.sopro.project_demoday.service;

import com.sopro.project_demoday.dto.UsuarioCadastroDTO;
import com.sopro.project_demoday.dto.UsuarioAtualizacaoDTO;
import com.sopro.project_demoday.dto.UsuarioRespostaDTO;
import com.sopro.project_demoday.model.Usuario;
import com.sopro.project_demoday.model.PreferenciasAcessibilidade;
import com.sopro.project_demoday.repository.UsuarioRepository;
import com.sopro.project_demoday.repository.PreferenciasAcessibilidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PreferenciasAcessibilidadeRepository preferenciasRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioRespostaDTO cadastrar(UsuarioCadastroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));


        if (dto.getNome() != null) {
            usuario.setNomeCompleto(dto.getNome());
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);


        PreferenciasAcessibilidade preferencias = new PreferenciasAcessibilidade(usuarioSalvo);
        preferenciasRepository.save(preferencias);

        return converterParaDTO(usuarioSalvo);
    }

    public UsuarioRespostaDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return converterParaDTO(usuario);
    }

    public List<UsuarioRespostaDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public UsuarioRespostaDTO atualizar(Long id, UsuarioAtualizacaoDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }

        usuario.setNomeCompleto(dto.getNome());
        usuario.setEmail(dto.getEmail());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return converterParaDTO(usuarioAtualizado);
    }

    public void desativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));


        usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        preferenciasRepository.deleteByUsuarioId(id);
        usuarioRepository.deleteById(id);
    }

    public UsuarioRespostaDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return converterParaDTO(usuario);
    }

    private UsuarioRespostaDTO converterParaDTO(Usuario usuario) {

        return new UsuarioRespostaDTO(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                null,
                true,
                LocalDateTime.now()
        );
    }
}