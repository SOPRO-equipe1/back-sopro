package com.sopro.project_demoday.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "telefone_celular", nullable = false, length = 15)
    private String telefoneCelular;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "cidade_estado", nullable = false)
    private String cidadeEstado;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Assinatura assinatura;


    public Usuario() {
    }


    public Usuario(String email, String nomeCompleto, String cpf, String telefoneCelular, LocalDate dataNascimento, String cityState, Endereco endereco) {
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.telefoneCelular = telefoneCelular;
        this.dataNascimento = dataNascimento;
        this.cidadeEstado = cityState;
        this.endereco = endereco;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCidadeEstado() {
        return cidadeEstado;
    }

    public void setCidadeEstado(String cidadeEstado) {
        this.cidadeEstado = cidadeEstado;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Assinatura getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(Assinatura assinatura) {
        this.assinatura = assinatura;
    }
}