package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.PreferenciasAcessibilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PreferenciasAcessibilidadeRepository extends JpaRepository<PreferenciasAcessibilidade, Long> {
    Optional<PreferenciasAcessibilidade> findByUsuarioId(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}
