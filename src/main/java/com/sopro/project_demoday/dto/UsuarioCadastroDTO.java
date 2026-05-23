package com.sopro.project_demoday.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.sopro.project_demoday.model.enums.RoleUsuario;

public class UsuarioCadastroDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
    private String senha;
    
    private RoleUsuario role;
    
    public UsuarioCadastroDTO() {
    }
    
    public UsuarioCadastroDTO(String nome, String email, String senha, RoleUsuario role) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
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
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public RoleUsuario getRole() {
        return role;
    }
    
    public void setRole(RoleUsuario role) {
        this.role = role;
    }
}
