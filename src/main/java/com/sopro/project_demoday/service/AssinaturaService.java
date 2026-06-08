package com.sopro.project_demoday.service;

import com.sopro.project_demoday.dto.CheckoutDTO;
import com.sopro.project_demoday.model.Assinatura;
import com.sopro.project_demoday.model.Pagamento;
import com.sopro.project_demoday.model.Usuario;
import com.sopro.project_demoday.repository.AssinaturaRepository;
import com.sopro.project_demoday.repository.PagamentoRepository;
import com.sopro.project_demoday.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssinaturaService {

    @Autowired
    private AssinaturaRepository assinaturaRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void processarCheckout(String email, CheckoutDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));


        Pagamento pagamento = new Pagamento(
                usuario,
                dto.valor(),
                dto.formaPagamento(),
                "PAGO", // Em produção, este status viria dinamicamente do webhook do gateway
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
    }

    public Assinatura obterAssinaturaPorEmail(String email) {
        return assinaturaRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new RuntimeException("Nenhuma assinatura ativa encontrada para este usuário."));
    }

    public List<Pagamento> obterHistoricoPagamentos(String email) {
        return pagamentoRepository.findByUsuarioEmail(email);
    }
}