package com.martinez.middleware_integrador.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

// 1. @RestController: Le dice a Spring "Esta clase maneja peticiones web"
@RestController
// 2. @RequestMapping: Define la ruta base. Todas las llamadas empezarán por /api/v1
@RequestMapping("/api/v1")
public class HealthController {

    // 3. @GetMapping: Cuando alguien visite /status, ejecuta esto.
    @GetMapping("/status")
    public Map<String, String> checkStatus() {
        // Retornamos un JSON simple. Spring convierte el Map a JSON automáticamente.
        return Map.of(
                "sistema", "Middleware Integrador B2B",
                "estado", "OPERATIVO",
                "version", "1.0.0"
        );
    }
}