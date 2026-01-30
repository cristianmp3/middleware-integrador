package com.martinez.middleware_integrador.controller;

import com.martinez.middleware_integrador.model.Pedido;
import com.martinez.middleware_integrador.service.PedidoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // Endpoint para RECIBIR un pedido (POST)
    @PostMapping
    public Pedido crearPedido(@RequestBody Pedido pedido) {
        return service.guardarPedido(pedido);
    }

    // Endpoint para VER todos los pedidos (GET)
    @GetMapping
    public List<Pedido> obtenerTodos() {
        return service.listarTodos();
    }
}