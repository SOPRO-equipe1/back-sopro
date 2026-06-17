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
import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Endereco endereco = usuario.getEndereco();
        if (endereco == null) {
            endereco = new Endereco();
        }
        endereco.setCep(dto.cep() != null ? dto.cep() : "00000-000");
        endereco.setNumero(dto.numero() != null ? dto.numero() : "S/N");
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro() != null ? dto.bairro() : "Bairro não informado");
        endereco.setCidade(dto.cidade() != null ? dto.cidade() : "Cidade não informada");
        endereco.setEstado(dto.estado() != null ? dto.estado() : "SP");


        endereco = enderecoRepository.save(endereco);


        usuario.setEndereco(endereco);
        usuarioRepository.save(usuario);



        Pagamento pagamento = new Pagamento(
                usuario,
                dto.valor(),
                dto.formaPagamento(),
                "PAGO",
                dto.transactionId(),
                LocalDateTime.now()
        );
        pagamentoRepository.save(pagamento);


        Assinatura assinatura = assinaturaRepository.findByUsuarioEmail(email)
                .orElse(new Assinatura());

        assinatura.setUsuario(usuario);
        assinatura.setPlano(dto.plano());
        assinatura.setStatus("ATIVO");
        assinatura.setDataInicio(LocalDateTime.now());

        if ("MENSAL".equalsIgnoreCase(dto.plano())) {
            assinatura.setDataExpiracao(LocalDateTime.now().plusMonths(1));
        } else {
            assinatura.setDataExpiracao(LocalDateTime.now().plusYears(1));
        }
        assinaturaRepository.save(assinatura);


        Pedido novoPedido = new Pedido();
        novoPedido.setCodigoPedido(dto.transactionId() != null ? dto.transactionId() : "SP-" + System.currentTimeMillis());
        novoPedido.setProdutoDescricao(dto.produtoDescricao() != null ? dto.produtoDescricao() : "1x Dispositivo Sopro");
        novoPedido.setStatusStatus("PREPARANDO");
        novoPedido.setCodigoRastreio("RU" + (new java.util.Random().nextInt(900000000) + 100000000) + "BR");
        novoPedido.setDataEntregaPrevista(LocalDate.now().plusDays(7));
        novoPedido.setValorTotal(dto.valor());
        novoPedido.setUsuario(usuario);

        pedidoRepository.save(novoPedido);
    }

    public Assinatura obterAssinaturaPorEmail(String email) {
        return assinaturaRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new RuntimeException("Nenhuma assinatura ativa encontrada para este usuário."));
    }

    public List<Pagamento> obterHistoricoPagamentos(String email) {
        return pagamentoRepository.findByUsuarioEmail(email);
    }
}