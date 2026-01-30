package com.martinez.middleware_integrador.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity // Le dice a Spring: "Esto es una tabla de base de datos"
@Table(name = "pedidos")
@Data   // Gracias a Lombok, no tenemos que escribir Getters/Setters
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private Integer cantidad;
    private String cliente;

    @Column(length = 1000)
    private String descripcionIa; // Aquí guardaremos lo que genere la IA después

    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}