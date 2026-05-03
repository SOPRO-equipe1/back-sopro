package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
}
