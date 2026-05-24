package com.sopro.project_demoday.dto;

import com.sopro.project_demoday.model.enums.RoleUsuario;
import java.time.LocalDateTime;

public class UsuarioRespostaDTO {
    
    private Long id;
    private String nome;
    private String email;
    private RoleUsuario role;
    private boolean ativo;
    private LocalDateTime dataCadastro;
    
    public UsuarioRespostaDTO() {
    }
    
    public UsuarioRespostaDTO(Long id, String nome, String email, RoleUsuario role, boolean ativo, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public RoleUsuario getRole() {
        return role;
    }
    
    public void setRole(RoleUsuario role) {
        this.role = role;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
