¡Claro que sí! Aquí tienes la información formateada en Markdown, lista para copiar y pegar directamente en tu archivo `README.md` de GitHub. He añadido iconografía, estructura jerárquica con encabezados, bloques de código en línea para los términos técnicos y listas para que sea mucho más fácil de leer.

---

# 🤖 AI-Powered Order Integration Middleware

> **Project:** Java Spring Boot & Google Gemini Integrator Middleware
> **Author:** Cristian P. Martinez
> **Date:** January 30, 2026
> **Version:** 1.0.0

---

## 📑 1. Executive Summary

This project consists of a **B2B Integration Middleware** designed to automate the receipt, analysis, and persistence of business orders.

Unlike a traditional CRUD, this system implements **Real-Time Data Enrichment** using Generative Artificial Intelligence (Google Gemini 1.5 Flash). The system not only saves data but also "understands" the context of the order, automatically classifying priority and generating technical descriptions without human intervention.

### 💎 Business Value

* **Decision Automation:** Automatically detects VIP or high-volume orders.
* **Reduction of Human Error:** Standardizes product descriptions based on SKUs.
* **Scalability:** Microservices-based architecture (Spring Boot) ready for high demand.

---

## 🛠️ 2. Technology Stack

* **Language:** Java 17+ (LTS)
* **Framework:** Spring Boot 3.4.x *(Modern standard for microservices)*
* **Database:** MySQL 8.0 *(Relational persistence)*
* **ORM:** Hibernate / JPA *(Data management without manual SQL)*
* **Artificial Intelligence:** Google Gemini 1.5 Flash *(via REST API)*
* **API Documentation:** OpenAPI / Swagger UI
* **HTTP Client:** Spring `RestClient`
* **Tools:** Maven, Lombok, IntelliJ IDEA

---

## 🏗️ 3. System Architecture

The system follows a **Layered Architecture** to ensure separation of responsibilities and maintainability.

### 🔄 Data Pipeline

1. **Input:** The client sends a JSON request via HTTP `POST`.
2. **Controller:** Validates the request structure.
3. **Service (Logic Layer):**
* Calls the AI adapter (`IaService`) to analyze the SKU.
* Receives the analysis (Priority + Description).
* Applies business rules (Alerts).


4. **Repository:** Persists the enriched object in MySQL.
5. **Output:** Returns the created object with `ID` and `Timestamp`.

---

## 🧩 4. Component and Logic Details

### A. Data Model (`Order.java`)

Represents the information structure. The `@Entity` annotation is used to map the class directly to an SQL table.

* `sku`: Unique identifier of the product.
* `quantity`: Critical variable for the business logic.
* `descriptionIa`: Long text field (`LONGTEXT` or `VARCHAR(2000)`) where the AI's generative response is stored.
* **Auditing:** Use of `@PrePersist` to automatically record the creation date.

### B. Artificial Intelligence Service (`IaService.java`)

This component acts as an integration adapter. Its function is to communicate with the external Google API.

**Key Logic (Prompt Engineering):** An empty request is not sent. A "System Prompt" (behavioral instruction) is injected into the call:

> *"If the quantity is > 50 or the product is premium, start your response with: [HIGH PRIORITY]. Otherwise, use: [STANDARD]."*

**Technical Implementation:**

* Use of `RestClient` to make secure HTTP calls.
* Manual handling of the nested JSON structure required by Gemini (`contents` -> `parts` -> `text`).
* URL configuration pointing to the `gemini-1.5-flash` model to optimize latency.

### C. Business Service (`PedidoService.java`)

The core of the application. It orchestrates the interaction between the AI and the database.
