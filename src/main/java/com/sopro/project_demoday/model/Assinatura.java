package com.sopro.project_demoday.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Table (name = "TB_ASSINATURA")
@Entity

public class Assinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAssinatura status;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_expericao", nullable = false)
    private LocalDate expiracao;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    public Assinatura() {
    }

    public Assinatura (StatusAssinatura status, LocalDate dataInicio, LocalDate expiracao, Usuario usuario) {

        this.status = status;
        this.dataInicio = dataInicio;
        this.expiracao = expiracao;
        this.usuario = usuario;

    }

    public Long getId() {
        return id;
    }

    public StatusAssinatura getStatus() {
        return status;
    }

    public void setStatus(StatusAssinatura status) {
        this.status = status;
    }

    public LocalDate getExpiracao() {
        return expiracao;
    }

    public void setExpericao(LocalDate expericao) {
        this.expiracao = expericao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
}
