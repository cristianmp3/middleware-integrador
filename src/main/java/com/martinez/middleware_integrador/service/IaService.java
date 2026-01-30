package com.martinez.middleware_integrador.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Map;

@Service
public class IaService {

    private final RestClient restClient;

    @Value("${ia.api.key}")
    private String apiKey;

    @Value("${ia.api.url}")
    private String apiUrl;

    public IaService() {
        this.restClient = RestClient.create();
    }

    public String analizarPedido(String sku, int cantidad) {
        try {
            // 1. Preparamos el Prompt con la lógica de negocio integrada
            String prompt = """
                Eres un analista de Supply Chain.
                Reglas:
                1. Si la cantidad es > 50 o el producto es de lujo (MacBook, Servidor, etc), empieza tu respuesta con: [PRIORIDAD ALTA].
                2. Si no, empieza con: [ESTÁNDAR].
                3. Luego añade una descripción técnica breve del producto.
                
                Analiza este pedido:
                Producto: %s
                Cantidad: %d
                """.formatted(sku, cantidad);

            // 2. Estructura del JSON específica para Gemini API
            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    )
            );

            // 3. Llamada a la API de Google
            // Nota: Gemini recibe la API Key en la URL como parámetro (?key=...)
            Map<String, Object> response = restClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            // 4. Navegamos el JSON de respuesta de Google
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

            return (String) parts.get(0).get("text");

        } catch (Exception e) {
            // Si falla, devolvemos el error para verlo en la base de datos
            return "Error Gemini: " + e.getMessage();
        }
    }
}