package com.sopro.project_demoday.service;

import com.sopro.project_demoday.dto.CheckoutDTO;
import com.sopro.project_demoday.model.Assinatura;
import com.sopro.project_demoday.model.Pagamento;
import com.sopro.project_demoday.model.Pedido;
import com.sopro.project_demoday.model.Endereco;
import com.sopro.project_demoday.model.Usuario;
import com.sopro.project_demoday.repository.AssinaturaRepository;
import com.sopro.project_demoday.repository.PagamentoRepository;
import com.sopro.project_demoday.repository.PedidoRepository;
import com.sopro.project_demoday.repository.UsuarioRepository;
import com.sopro.project_demoday.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Random;

@Service
public class AssinaturaService {

    @Autowired
    private AssinaturaRepository assinaturaRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional
    public void processarCheckout(String email, CheckoutDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para processamento do checkout."));


        if (dto.cep() != null && !dto.cep().isBlank()) {
            Endereco endereco = usuario.getEndereco();
            if (endereco == null) {
                endereco = new Endereco();
            }

            endereco.setCep(dto.cep());
            // CORRIGIDO: usa o logradouro real enviado pelo front
            endereco.setLogradouro(dto.logradouro() != null && !dto.logradouro().isBlank()
                    ? dto.logradouro() : "Logradouro não informado");
            endereco.setNumero(dto.numero() != null && !dto.numero().isBlank() ? dto.numero() : "S/N");
            endereco.setComplemento(dto.complemento());
            endereco.setBairro(dto.bairro() != null && !dto.bairro().isBlank() ? dto.bairro() : "Bairro");
            endereco.setCidade(dto.cidade() != null && !dto.cidade().isBlank() ? dto.cidade() : "Cidade");
            endereco.setEstado(dto.estado() != null && !dto.estado().isBlank() ? dto.estado() : "SP");

            endereco.setUsuario(usuario);
            endereco = enderecoRepository.saveAndFlush(endereco);

            usuario.setEndereco(endereco);
            usuario = usuarioRepository.saveAndFlush(usuario);
        }



        String transactionId = dto.transactionId() != null
                ? dto.transactionId() : "SP-" + System.currentTimeMillis();

        Pagamento pagamento = new Pagamento(
                usuario,
                dto.valor() != null ? dto.valor() : java.math.BigDecimal.valueOf(200.97),
                dto.formaPagamento() != null ? dto.formaPagamento().toUpperCase() : "PIX",
                "PAGO",
                transactionId,
                LocalDateTime.now()
        );
        pagamentoRepository.saveAndFlush(pagamento);


        Assinatura assinatura = assinaturaRepository.findByUsuarioEmail(email).orElse(new Assinatura());
        assinatura.setUsuario(usuario);
        assinatura.setPlano(dto.plano() != null ? dto.plano().toUpperCase() : "DISPOSITIVO");
        assinatura.setStatus("ATIVO");
        assinatura.setDataInicio(LocalDateTime.now());
        assinatura.setDataExpiracao(LocalDateTime.now().plusMonths(1));
        assinaturaRepository.saveAndFlush(assinatura);


        Pedido novoPedido = new Pedido();
        novoPedido.setUsuario(usuario);
        novoPedido.setCodigoPedido(transactionId);
        novoPedido.setProdutoDescricao(dto.produtoDescricao() != null
                ? dto.produtoDescricao() : "1x Dispositivo Sopro");
        novoPedido.setStatusStatus("PREPARANDO");
        novoPedido.setCodigoRastreio("RU" + (new Random().nextInt(90000000) + 10000000) + "BR");
        novoPedido.setDataEntregaPrevista(LocalDate.now().plusDays(7));
        novoPedido.setValorTotal(dto.valor() != null
                ? dto.valor() : java.math.BigDecimal.valueOf(200.97));

        pedidoRepository.saveAndFlush(novoPedido);
    }

    public Assinatura obterAssinaturaPorEmail(String email) {
        return assinaturaRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new RuntimeException("Nenhuma assinatura ativa encontrada para este usuário."));
    }

    public java.util.List<Pagamento> obterHistoricoPagamentos(String email) {
        return pagamentoRepository.findByUsuarioEmail(email);
    }
}