package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Conhecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConhecimentoRepository extends JpaRepository<Conhecimento, Long> {

    Optional<Conhecimento> findByTituloContainingIgnoreCase(String titulo);
}
