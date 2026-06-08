package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {

    // Este método precisa retornar Optional para o .orElse(new Assinatura()) funcionar
    Optional<Assinatura> findByUsuarioEmail(String email);
}