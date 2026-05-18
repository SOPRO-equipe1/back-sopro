package com.sopro.project_demoday.repository;

import com.sopro.project_demoday.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {


    Optional<Pedido> findFirstByUsuarioEmailOrderByIdDesc(String email);
}