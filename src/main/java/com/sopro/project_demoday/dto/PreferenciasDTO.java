package com.sopro.project_demoday.dto;

public class PreferenciasDTO {
    
    private String tamanhoFonte;
    private boolean altoContraste;
    private Double velocidadeVoz;
    private String idiomaVoz;
    private String tipoEntrada;
    
    public PreferenciasDTO() {
    }
    
    public PreferenciasDTO(String tamanhoFonte, boolean altoContraste, Double velocidadeVoz, String idiomaVoz, String tipoEntrada) {
        this.tamanhoFonte = tamanhoFonte;
        this.altoContraste = altoContraste;
        this.velocidadeVoz = velocidadeVoz;
        this.idiomaVoz = idiomaVoz;
        this.tipoEntrada = tipoEntrada;
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
