package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.dto.CheckoutDTO;
import com.sopro.project_demoday.model.Assinatura;
import com.sopro.project_demoday.model.Pagamento;
import com.sopro.project_demoday.service.AssinaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assinaturas")
@CrossOrigin
public class AssinaturaController {

    @Autowired
    private AssinaturaService assinaturaService;

    // Endpoint para processar a assinatura quando o usuário clica em pagar no Checkout
    @PostMapping("/checkout")
    public ResponseEntity<String> realizarCheckout(@RequestParam String email, @RequestBody CheckoutDTO dto) {
        try {
            assinaturaService.processarCheckout(email, dto);
            return ResponseEntity.ok("Pagamento processado e assinatura ativada com sucesso!");
        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro ao processar assinatura: " + e.getMessage());
        }
    }

    //  Endpoint para o React checar se o usuário atual é PRO ou FREE e se está ATIVO
    @GetMapping("/status")
    public ResponseEntity<Assinatura> buscarStatus(@RequestParam String email) {
        try {
            Assinatura assinatura = assinaturaService.obterAssinaturaPorEmail(email);
            return ResponseEntity.ok(assinatura);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //  listar o histórico financeiro de pagamentos do usuário
    @GetMapping("/pagamentos")
    public ResponseEntity<List<Pagamento>> listarHistorico(@RequestParam String email) {
        List<Pagamento> historico = assinaturaService.obterHistoricoPagamentos(email);
        return ResponseEntity.ok(historico);
    }
}