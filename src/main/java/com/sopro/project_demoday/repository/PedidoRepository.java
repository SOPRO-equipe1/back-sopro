package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findFirstByUsuarioEmailOrderByIdDesc(String email);
}