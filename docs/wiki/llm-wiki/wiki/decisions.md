# Decisiones

## Aprobadas por las fuentes

- La raíz no se convierte en un tercer repositorio.
- El frontend consume REST directamente desde Spring Boot.
- Backend y frontend mantienen responsabilidades separadas.
- La Wiki global vive en `citas-api/docs/wiki/llm-wiki/`.
- Los secretos viven en variables de entorno y los JSON de n8n no contienen credenciales.

## No decididas

No fijar todavía framework frontend, contrato REST, esquema exacto de estados, estrategia de concurrencia de slots, TTL/rotación de JWT ni política de zona horaria.
