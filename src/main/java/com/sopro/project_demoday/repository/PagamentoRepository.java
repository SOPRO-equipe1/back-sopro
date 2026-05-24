package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    // O Spring Boot vai entrar em Pagamento -> buscar o Usuario -> e filtrar pelo Email dele
    List<Pagamento> findByUsuarioEmail(String email);
}