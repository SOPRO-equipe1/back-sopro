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
        //  Busca o usuário de forma limpa
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para processamento do checkout."));

        //  Cria ou recupera o endereço isoladamente para evitar loops de persistência
        Endereco endereco = usuario.getEndereco();
        if (endereco == null) {
            endereco = new Endereco();
        }

        // Popula com o que veio do DTO ou garante valores padrão
        endereco.setCep(dto.cep() != null && !dto.cep().isBlank() ? dto.cep() : "01310-100");
        endereco.setLogradouro(dto.produtoDescricao() != null ? "Avenida Paulista" : "Logradouro Padrão Sopro");
        endereco.setNumero(dto.numero() != null && !dto.numero().isBlank() ? dto.numero() : "1000");
        endereco.setComplemento(dto.complemento() != null ? dto.complemento() : "Apto 12");
        endereco.setBairro(dto.bairro() != null && !dto.bairro().isBlank() ? dto.bairro() : "Bela Vista");
        endereco.setCidade(dto.cidade() != null && !dto.cidade().isBlank() ? dto.cidade() : "São Paulo");
        endereco.setEstado(dto.estado() != null && !dto.estado().isBlank() ? dto.estado() : "SP");

        // Salva o endereço de forma independente primeiro
        endereco = enderecoRepository.saveAndFlush(endereco);

        // Vincula de volta no usuário e atualiza
        usuario.setEndereco(endereco);
        Usuario usuarioSalvo = usuarioRepository.saveAndFlush(usuario);

        //  Registra o pagamento de forma direta
        Pagamento pagamento = new Pagamento(
                usuarioSalvo,
                dto.valor(),
                dto.formaPagamento() != null ? dto.formaPagamento().toUpperCase() : "PIX",
                "PAGO",
                dto.transactionId() != null ? dto.transactionId() : "SP-" + System.currentTimeMillis(),
                LocalDateTime.now()
        );
        pagamentoRepository.saveAndFlush(pagamento);

        // Cria ou atualiza a assinatura
        Assinatura assinatura = assinaturaRepository.findByUsuarioEmail(email).orElse(new Assinatura());
        assinatura.setUsuario(usuarioSalvo);
        assinatura.setPlano(dto.plano() != null ? dto.plano().toUpperCase() : "PRO");
        assinatura.setStatus("ATIVO");
        assinatura.setDataInicio(LocalDateTime.now());
        assinatura.setDataExpiracao(LocalDateTime.now().plusMonths(1));
        assinaturaRepository.saveAndFlush(assinatura);

        //  Cria o Pedido forçando os vínculos corretos exigidos pelas constraints NOT NULL
        Pedido novoPedido = new Pedido();
        novoPedido.setUsuario(usuarioSalvo);
        novoPedido.setCodigoPedido(dto.transactionId() != null ? dto.transactionId() : "SP-" + System.currentTimeMillis());
        novoPedido.setProdutoDescricao(dto.produtoDescricao() != null ? dto.produtoDescricao() : "1x Dispositivo Sopro");
        novoPedido.setStatusStatus("PREPARANDO");
        novoPedido.setCodigoRastreio("RU" + (new Random().nextInt(90000000) + 10000000) + "BR");
        novoPedido.setDataEntregaPrevista(LocalDate.now().plusDays(7));
        novoPedido.setValorTotal(dto.valor() != null ? dto.valor() : java.math.BigDecimal.valueOf(200.97));

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