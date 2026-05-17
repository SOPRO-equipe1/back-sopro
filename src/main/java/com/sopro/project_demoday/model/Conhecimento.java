package com.sopro.project_demoday.model;

import jakarta.persistence.*;

@Table(name = "TB_CONHECIMENTO")
@Entity
public class Conhecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    private String metadados;

    public Conhecimento() {
    }

    public Conhecimento(String titulo, String conteudo, String metadados) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.metadados = metadados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getMetadados() {
        return metadados;
    }

    public void setMetadados(String metadados) {
        this.metadados = metadados;
    }
}