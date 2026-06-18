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
import org.springframework.transaction.annotation.Propagation;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processarCheckout(String email, CheckoutDTO dto) {


        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para processamento do checkout."));


        Endereco endereco = usuario.getEndereco();
        if (endereco == null) {
            endereco = new Endereco();
        }

        // Se o DTO trouxer dados de endereço, popula. Caso contrário, e o usuário já possuir endereço, mantém.
        if (dto.cep() != null && !dto.cep().isBlank()) {
            endereco.setCep(dto.cep());
            endereco.setLogradouro(dto.produtoDescricao() != null ? "Endereço de Entrega Sopro" : "Logradouro Padrão Sopro");
            endereco.setNumero(dto.numero() != null && !dto.numero().isBlank() ? dto.numero() : "S/N");
            endereco.setComplemento(dto.complemento());
            endereco.setBairro(dto.bairro() != null && !dto.bairro().isBlank() ? dto.bairro() : "Bairro Padrão");
            endereco.setCidade(dto.cidade() != null && !dto.cidade().isBlank() ? dto.cidade() : "Cidade Padrão");
            endereco.setEstado(dto.estado() != null && !dto.estado().isBlank() ? dto.estado() : "SP");
        } else if (endereco.getCep() == null) {
            // Fallback preventivo absoluto para não quebrar a restrição de NOT NULL do banco de dados
            endereco.setCep("00000-000");
            endereco.setLogradouro("Logradouro de Contingência");
            endereco.setNumero("S/N");
            endereco.setBairro("Centro");
            endereco.setCidade("São Paulo");
            endereco.setEstado("SP");
        }

        // Sincroniza bidirecionalmente a referência da relação e persiste o endereço primeiro
        endereco.setUsuario(usuario);
        Endereco enderecoPersistido = enderecoRepository.saveAndFlush(endereco);

        usuario.setEndereco(enderecoPersistido);
        Usuario usuarioSalvo = usuarioRepository.saveAndFlush(usuario);


        Pagamento pagamento = new Pagamento(
                usuarioSalvo,
                dto.valor(),
                dto.formaPagamento(),
                "PAGO",
                dto.transactionId() != null ? dto.transactionId() : "SP-" + System.currentTimeMillis(),
                LocalDateTime.now()
        );
        pagamentoRepository.saveAndFlush(pagamento);


        Assinatura assinatura = assinaturaRepository.findByUsuarioEmail(email).orElse(new Assinatura());
        assinatura.setUsuario(usuarioSalvo);
        assinatura.setPlano(dto.plano());
        assinatura.setStatus("ATIVO");
        assinatura.setDataInicio(LocalDateTime.now());
        assinatura.setDataExpiracao(LocalDateTime.now().plusMonths(1));
        assinaturaRepository.saveAndFlush(assinatura);


        Pedido novoPedido = new Pedido();
        novoPedido.setUsuario(usuarioSalvo);
        novoPedido.setCodigoPedido(dto.transactionId() != null ? dto.transactionId() : "SP-" + System.currentTimeMillis());
        novoPedido.setProdutoDescricao(dto.produtoDescricao() != null ? dto.produtoDescricao() : "1x Dispositivo Sopro");
        novoPedido.setStatusStatus("PREPARANDO");
        novoPedido.setCodigoRastreio("RU" + (new Random().nextInt(90000000) + 10000000) + "BR");
        novoPedido.setDataEntregaPrevista(LocalDate.now().plusDays(7));
        novoPedido.setValorTotal(dto.valor());

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