package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // Garanta este import

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Altere de Usuario para Optional<Usuario>
    Optional<Usuario> findByEmail(String email);
}