package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Conhecimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConhecimentoRepository extends JpaRepository<Conhecimento, Long> {

    Optional<Conhecimento> findByTituloContainingIgnoreCase(String titulo);
}
