package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // Garanta este import

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // O retorno PRECISA ser Optional<Usuario> para aceitar o .orElseThrow()
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
