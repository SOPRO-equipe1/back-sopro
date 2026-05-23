package com.sopro.project_demoday.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioAtualizacaoDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
    public UsuarioAtualizacaoDTO() {
    }
    
    public UsuarioAtualizacaoDTO(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
