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
        // 1. Busca o usuário gerenciado pelo Hibernate
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // 2. Vincula ou cria o endereço diretamente acoplado ao ciclo do Usuário
        if (usuario.getEndereco() == null) {
            usuario.setEndereco(new Endereco());
        }

        Endereco endereco = usuario.getEndereco();

        // Tratamento estrito contra strings vazias vindas do frontend
        endereco.setCep(dto.cep() != null && !dto.cep().isBlank() ? dto.cep() : "00000-000");
        endereco.setLogradouro(dto.produtoDescricao() != null ? "Logradouro Padrão" : "Não informado"); // Exemplo de fallback seguro
        endereco.setNumero(dto.numero() != null && !dto.numero().isBlank() ? dto.numero() : "S/N");
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro() != null && !dto.bairro().isBlank() ? dto.bairro() : "Bairro não informado");
        endereco.setCidade(dto.cidade() != null && !dto.cidade().isBlank() ? dto.cidade() : "Cidade não informada");
        endereco.setEstado(dto.estado() != null && !dto.estado().isBlank() ? dto.estado() : "SP");

        // Salva o usuário injetando o endereço pelo CascadeType.ALL e força o banco a gravar na hora
        Usuario usuarioSalvo = usuarioRepository.saveAndFlush(usuario);

        // 3. Salva o Pagamento usando a referência limpa gerada
        Pagamento pagamento = new Pagamento(
                usuarioSalvo,
                dto.valor(),
                dto.formaPagamento(),
                "PAGO",
                dto.transactionId() != null ? dto.transactionId() : "SP-" + System.currentTimeMillis(),
                LocalDateTime.now()
        );
        pagamentoRepository.saveAndFlush(pagamento);

        // 4. Salva/Atualiza a Assinatura
        Assinatura assinatura = assinaturaRepository.findByUsuarioEmail(email)
                .orElse(new Assinatura());

        assinatura.setUsuario(usuarioSalvo);
        assinatura.setPlano(dto.plano());
        assinatura.setStatus("ATIVO");
        assinatura.setDataInicio(LocalDateTime.now());

        if ("MENSAL".equalsIgnoreCase(dto.plano())) {
            assinatura.setDataExpiracao(LocalDateTime.now().plusMonths(1));
        } else {
            assinatura.setDataExpiracao(LocalDateTime.now().plusYears(1));
        }
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