package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Conhecimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReconhecimentoRepository extends JpaRepository<Conhecimento, Long> {
}
