package com.sopro.project_demoday.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.sopro.project_demoday.model.Usuario;

@Entity
@Table(name = "tb_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_pedido", nullable = false, unique = true)
    private String codigoPedido; // Ex: "#SP-2026-01"

    @Column(name = "produto_descricao", nullable = false)
    private String produtoDescricao; // Ex: "1x Dispositivo Sopro - Cor Preta"

    @Column(name = "status_status", nullable = false)
    private String statusStatus; // CONFIRMADO, PREPARANDO, EM_TRANSPORTE, ENTREGUE

    @Column(name = "codigo_rastreio")
    private String codigoRastreio; // Ex: "RU182121051419BR"

    @Column(name = "data_entrega_prevista")
    private LocalDate dataEntregaPrevista;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    public Pedido() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public String getProdutoDescricao() {
        return produtoDescricao;
    }

    public void setProdutoDescricao(String produtoDescricao) {
        this.produtoDescricao = produtoDescricao;
    }

    public String getStatusStatus() {
        return statusStatus;
    }

    public void setStatusStatus(String statusStatus) {
        this.statusStatus = statusStatus;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public LocalDate getDataEntregaPrevista() {
        return dataEntregaPrevista;
    }

    public void setDataEntregaPrevista(LocalDate dataEntregaPrevista) {
        this.dataEntregaPrevista = dataEntregaPrevista;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}