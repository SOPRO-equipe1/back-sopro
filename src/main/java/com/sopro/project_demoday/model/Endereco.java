package com.sopro.project_demoday.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = true, length = 9)
    private String cep;

    @Column(nullable = true)
    private String logradouro;

    @Column(nullable = true, length = 20)
    private String numero;

    private String complemento;

    @Column(nullable = true)
    private String bairro;

    @Column(nullable = true)
    private String cidade;

    @Column(nullable = true, length = 2)
    private String estado;

    @OneToOne(mappedBy = "endereco")
    @JsonIgnore
    private Usuario usuario;

    public Endereco() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}