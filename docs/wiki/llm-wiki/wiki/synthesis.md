# Síntesis

El laboratorio construye un sistema ficticio de agendamiento de citas para practicar desarrollo asistido por agentes. El backend es Java 21/Spring Boot 3.5.x con arquitectura hexagonal, MySQL/Flyway y REST; el frontend será React o Angular según el proyecto importado desde Stitch/Google AI Studio.

La raíz coordina dos repositorios Git independientes y no es un tercer repositorio. La única LLM Wiki global vive en este repositorio backend.

El dominio, las reglas funcionales y los límites de seguridad provienen del PRD. La implementación no debe usar datos privados reales de FCV.
