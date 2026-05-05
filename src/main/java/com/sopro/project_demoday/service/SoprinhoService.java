package com.sopro.project_demoday.service;


import com.sopro.project_demoday.model.Conhecimento;
import com.sopro.project_demoday.repository.ReconhecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoprinhoService {

    @Autowired
    private ReconhecimentoRepository repository;

    public String processarPergunta(String termo) {

        return repository.findByTituloContainingIgnoreCase(termo)
                .map(Conhecimento::getConteudo)
                .orElse("Ainda estou aprendendo sobre isso! Tente perguntar sobre 'produto' ou 'compra'.");
    }
}

