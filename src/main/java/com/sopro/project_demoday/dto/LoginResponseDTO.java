package com.sopro.project_demoday.dto;

public class LoginResponseDTO {
    
    private String token;
    private String tipo = "Bearer";
    private String email;
    private String role;
    
    public LoginResponseDTO() {
    }
    
    public LoginResponseDTO(String token, String email, String role) {
        this.token = token;
        this.tipo = "Bearer";
        this.email = email;
        this.role = role;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
