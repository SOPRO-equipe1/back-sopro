package com.sopro.project_demoday.model;


import jakarta.persistence.*;

@Table(name= "TB USUARIO")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;


    public Usuario(){
    }

    public Usuario(Long id, String nome, String email, String senha){
        this.id = id;
        this.nome=nome;
        this.email=email;
        this.senha=senha;

    }


    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

