TECHNICAL DOCUMENTATION: AI-POWERED ORDER INTEGRATION MIDDLEWARE
Project: Java Spring Boot & Google Gemini Integrator Middleware
Author: Cristian P. Martinez
Date: January 30, 2026
Version: 1.0.0

1. EXECUTIVE SUMMARY
This project consists of a B2B Integration Middleware designed to automate the receipt, analysis, and persistence of business orders.

Unlike a traditional CRUD, this system implements Real-Time Data Enrichment using Generative Artificial Intelligence (Google Gemini 1.5 Flash). The system not only saves data but also "understands" the context of the order, automatically classifying priority and generating technical descriptions without human intervention.

Business Value
Decision Automation: Automatically detects VIP or high-volume orders.
Reduction of Human Error: Standardizes product descriptions based on SKUs.

Scalability: Microservices-based architecture (Spring Boot) ready for high demand.

2. TECHNOLOGY STACK
Language: Java 17+ (LTS).
Framework: Spring Boot 3.4.x (Modern standard for microservices).
Database: MySQL 8.0 (Relational persistence).
ORM: Hibernate / JPA (Data management without manual SQL).
Artificial Intelligence: Google Gemini 1.5 Flash (via REST API).
API Documentation: OpenAPI / Swagger UI.
HTTP Client: Spring RestClient.
Tools: Maven, Lombok, IntelliJ IDEA.

3. SYSTEM ARCHITECTURE
The system follows a Layered Architecture to ensure separation of responsibilities and maintainability.

Data Pipeline
Input: The client sends a JSON request via HTTP POST.
Controller: Validates the request structure.
Service (Logic Layer):
Calls the AI ​​adapter (IaService) to analyze the SKU.
Receives the analysis (Priority + Description).
Applies business rules (Alerts).
Repository: Persists the enriched object in MySQL.
Output: Returns the created object with ID and Timestamp.

4. COMPONENT AND LOGIC DETAILS
A. Data Model (Order.java)
Represents the information structure. The @Entity annotation is used to map the class directly to an SQL table.

sku: Unique identifier of the product.
quantity: Critical variable for the business logic.
descriptionIa: Long text field (LONGTEXT or VARCHAR(2000)) where the AI's generative response is stored.
Auditing: Use of @PrePersist to automatically record the creation date.

B. Artificial Intelligence Service (IaService.java)
This component acts as an integration adapter. Its function is to communicate with the external Google API.

Key Logic (Prompt Engineering): An empty request is not sent. A "System Prompt" (behavioral instruction) is injected into the call:

"If the quantity is > 50 or the product is premium, start your response with: [HIGH PRIORITY]. Otherwise, use: [STANDARD]."

Technical Implementation:

Use of RestClient to make secure HTTP calls.
Manual handling of the nested JSON structure required by Gemini (contents -> parts -> text).
URL configuration pointing to the gemini-1.5-flash model to optimize latency.

C. Business Service (PedidoService.java)
The core of the application. It orchestrates the interaction between the AI ​​and the database.

