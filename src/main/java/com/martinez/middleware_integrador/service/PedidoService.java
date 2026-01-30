package com.martinez.middleware_integrador.service;

import com.martinez.middleware_integrador.model.Pedido;
import com.martinez.middleware_integrador.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final IaService iaService;

    public PedidoService(PedidoRepository repository, IaService iaService) {
        this.repository = repository;
        this.iaService = iaService;
    }

    public Pedido guardarPedido(Pedido pedido) {
        pedido.setId(null);
        // 1. Llamamos a la IA
        // Si ya arreglaste el application.properties, esto traerá el texto de Gemini
        String analisis = iaService.analizarPedido(pedido.getSku(), pedido.getCantidad());
        pedido.setDescripcionIa(analisis);

        // 2. Guardamos en MySQL
        Pedido pedidoGuardado = repository.save(pedido);

        // --- AGREGAMOS ESTO: LA REGLA DE NEGOCIO ---
        // Verificamos si la IA detectó que es Prioridad Alta
        if (analisis != null && analisis.contains("PRIORIDAD ALTA")) {
            enviarAlertaGerencia(pedidoGuardado);
        }
        // -------------------------------------------

        return pedidoGuardado;
    }

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    // --- AGREGAMOS ESTO: EL MÉTODO QUE IMPRIME LA ALERTA ---
    private void enviarAlertaGerencia(Pedido pedido) {
        System.out.println("\n");
        System.out.println("==================================================");
        System.out.println("🚨  ALERTA DE NEGOCIO: PEDIDO VIP DETECTADO  🚨");
        System.out.println("==================================================");
        System.out.println("👉 Cliente: " + pedido.getCliente());
        System.out.println("👉 Producto: " + pedido.getSku());
        System.out.println("👉 Cantidad: " + pedido.getCantidad());
        System.out.println("✅ Acción: Correo simulado enviado al Director de Ventas.");
        System.out.println("==================================================\n");
    }
}