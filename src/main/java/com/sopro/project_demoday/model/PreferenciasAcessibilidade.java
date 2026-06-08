package com.sopro.project_demoday.model;

import jakarta.persistence.*;

@Entity
@Table(name = "preferencias_acessibilidade")
public class PreferenciasAcessibilidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
    
    @Column(nullable = false)
    private String tamanhoFonte = "MEDIO";
    
    @Column(nullable = false)
    private boolean altoContraste = false;
    
    @Column(nullable = false)
    private Double velocidadeVoz = 1.0;
    
    @Column(nullable = false)
    private String idiomaVoz = "pt-BR";
    
    @Column(nullable = false)
    private String tipoEntrada = "TOQUE";
    
    public PreferenciasAcessibilidade() {
    }
    
    public PreferenciasAcessibilidade(Usuario usuario) {
        this.usuario = usuario;
        this.tamanhoFonte = "MEDIO";
        this.altoContraste = false;
        this.velocidadeVoz = 1.0;
        this.idiomaVoz = "pt-BR";
        this.tipoEntrada = "TOQUE";
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getTamanhoFonte() {
        return tamanhoFonte;
    }
    
    public void setTamanhoFonte(String tamanhoFonte) {
        this.tamanhoFonte = tamanhoFonte;
    }
    
    public boolean isAltoContraste() {
        return altoContraste;
    }
    
    public void setAltoContraste(boolean altoContraste) {
        this.altoContraste = altoContraste;
    }
    
    public Double getVelocidadeVoz() {
        return velocidadeVoz;
    }
    
    public void setVelocidadeVoz(Double velocidadeVoz) {
        this.velocidadeVoz = velocidadeVoz;
    }
    
    public String getIdiomaVoz() {
        return idiomaVoz;
    }
    
    public void setIdiomaVoz(String idiomaVoz) {
        this.idiomaVoz = idiomaVoz;
    }
    
    public String getTipoEntrada() {
        return tipoEntrada;
    }
    
    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }
}
