package com.sopro.project_demoday.model;


import jakarta.persistence.*;

@Table(name = "TB CONHECIMENTO")
@Entity

public class Conhecimento {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;
    private String metadados;


    // Constructor sem parâmetros
    public Conhecimento(){

    }

    // Constructor com parâmetros
    public Conhecimento(String titulo, String conhecimento, String metadados){
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.metadados = metadados;
    }


    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String getMetadados() {
        return metadados;
    }

    public void setId(){
        this.id= id;
    }


}
