package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.dto.LoginRequestDTO;
import com.sopro.project_demoday.dto.LoginResponseDTO;
import com.sopro.project_demoday.model.Usuario;
import com.sopro.project_demoday.repository.UsuarioRepository;
import com.sopro.project_demoday.security.JwtService;
import com.sopro.project_demoday.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginComGoogle(@RequestBody Map<String, String> request) {
        try {
            String tokenGoogle = request.get("token");


            com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier verifier =
                    new com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder(
                            new com.google.api.client.http.javanet.NetHttpTransport(),
                            new com.google.api.client.json.gson.GsonFactory())
                            .setAudience(Collections.singletonList("TEU_CLIENT_ID_DO_GOOGLE.apps.googleusercontent.com"))
                            .build();

            com.google.api.client.googleapis.auth.oauth2.GoogleIdToken idToken = verifier.verify(tokenGoogle);
            if (idToken != null) {
                com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String nome = (String) payload.get("name");


                if (!usuarioRepository.existsByEmail(email)) {
                    Usuario novoUsuario = new Usuario();
                    novoUsuario.setEmail(email);
                    novoUsuario.setNomeCompleto(nome);
                    novoUsuario.setSenha(passwordEncoder.encode(UUID.randomUUID().toString())); // Senha aleatória segura

                    Random random = new Random();
                    novoUsuario.setCpf(String.format("%03d.%03d.%03d-%02d", random.nextInt(1000), random.nextInt(1000), random.nextInt(1000), random.nextInt(100)));
                    novoUsuario.setTelefoneCelular("(00) 00000-0000");
                    novoUsuario.setDataNascimento(java.time.LocalDate.of(2000, 1, 1));
                    novoUsuario.setCidadeEstado("Não Informado");

                    usuarioRepository.save(novoUsuario);
                }


                Usuario usuarioCarregado = usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Erro ao carregar credenciais criadas."));


                String sistemaToken = jwtService.gerarToken(usuarioCarregado);

                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("token", sistemaToken);
                responseBody.put("email", email);
                responseBody.put("nome", nome);
                return ResponseEntity.ok(responseBody);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token do Google Inválido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}