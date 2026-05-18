package com.sopro.project_demoday.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_enderecos")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String logradouro; // Guardará: "Rua do Suspiro Profundo, 42"

    @Column(nullable = false)
    private String complemento; // Guardará: "Ao lado da Suspiro News"


    public Endereco() {
    }


    public Endereco(String logradouro, String complemento) {
        this.logradouro = logradouro;
        this.complemento = complemento;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}