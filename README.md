# DOCUMENTACIÓN TÉCNICA: MIDDLEWARE DE INTEGRACIÓN DE PEDIDOS CON IA
**Proyecto:** Middleware Integrador Java Spring Boot & Google Gemini  
**Autor:** Cristian P. Martinez  
**Fecha:** 30 de Enero, 2026  
**Versión:** 1.0.0

---

## 1. RESUMEN EJECUTIVO

Este proyecto consiste en un **Middleware de Integración B2B** diseñado para automatizar la recepción, análisis y persistencia de pedidos comerciales. 

A diferencia de un CRUD tradicional, este sistema implementa **Enriquecimiento de Datos en Tiempo Real** utilizando Inteligencia Artificial Generativa (Google Gemini 1.5 Flash). El sistema no solo guarda datos, sino que "entiende" el contexto del pedido, clasificando automáticamente la prioridad y generando descripciones técnicas sin intervención humana.

### Valor para el Negocio
* **Automatización de Decisiones:** Detecta pedidos VIP o de alto volumen automáticamente.
* **Reducción de Error Humano:** Estandariza las descripciones de productos basadas en SKUs.
* **Escalabilidad:** Arquitectura basada en microservicios (Spring Boot) lista para alta demanda.

---

## 2. STACK TECNOLÓGICO

* **Lenguaje:** Java 17+ (LTS).
* **Framework:** Spring Boot 3.4.x (Estándar moderno para microservicios).
* **Base de Datos:** MySQL 8.0 (Persistencia relacional).
* **ORM:** Hibernate / JPA (Manejo de datos sin SQL manual).
* **Inteligencia Artificial:** Google Gemini 1.5 Flash (vía REST API).
* **Documentación API:** OpenAPI / Swagger UI.
* **Cliente HTTP:** Spring RestClient.
* **Herramientas:** Maven, Lombok, IntelliJ IDEA.

---

## 3. ARQUITECTURA DEL SISTEMA

El sistema sigue una **Arquitectura en Capas (Layered Architecture)** para garantizar la separación de responsabilidades y la mantenibilidad.

### Flujo de Datos (Pipeline)
1.  **Entrada:** El cliente envía un JSON vía HTTP POST.
2.  **Controller:** Valida la estructura de la petición.
3.  **Service (Capa Lógica):**
    * Llama al adaptador de IA (`IaService`) para analizar el SKU.
    * Recibe el análisis (Prioridad + Descripción).
    * Aplica reglas de negocio (Alertas).
4.  **Repository:** Persiste el objeto enriquecido en MySQL.
5.  **Salida:** Retorna el objeto creado con ID y Timestamp.

---

## 4. DETALLE DE COMPONENTES Y LÓGICA

### A. Modelo de Datos (`Pedido.java`)
Representa la estructura de la información. Se utiliza la anotación `@Entity` para mapear la clase directamente a una tabla SQL.

* **`sku`**: Identificador único del producto.
* **`cantidad`**: Variable crítica para la lógica de negocio.
* **`descripcionIa`**: Campo de texto largo (`LONGTEXT` o `VARCHAR(2000)`) donde se almacena la respuesta generativa de la IA.
* **Auditoría**: Uso de `@PrePersist` para registrar automáticamente la fecha de creación.

### B. Servicio de Inteligencia Artificial (`IaService.java`)
Este componente actúa como un adaptador de integración. Su función es comunicarse con la API externa de Google.

**Lógica Clave (Prompt Engineering):**
No se envía una petición vacía. Se inyecta un "System Prompt" (instrucción de comportamiento) dentro de la llamada:
> *"Si la cantidad es > 50 o el producto es de lujo, empieza tu respuesta con: [PRIORIDAD ALTA]. De lo contrario, usa: [ESTÁNDAR]."*

**Implementación Técnica:**
* Uso de `RestClient` para realizar llamadas HTTP seguras.
* Manejo manual de la estructura JSON anidada requerida por Gemini (`contents` -> `parts` -> `text`).
* Configuración de URL apuntando al modelo `gemini-1.5-flash` para optimizar latencia.

### C. Servicio de Negocio (`PedidoService.java`)
El núcleo de la aplicación. Orquesta la interacción entre la IA y la Base de Datos.

**Lógica de "Programación Defensiva":**
```java
pedido.setId(null);
