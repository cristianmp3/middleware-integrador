package com.martinez.middleware_integrador.repository;

import com.martinez.middleware_integrador.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Al extender de JpaRepository, ya tenemos métodos como save(), findAll(), findById()
}