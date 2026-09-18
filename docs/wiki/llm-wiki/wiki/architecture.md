# Arquitectura

`citas-api` concentra dominio, aplicación, puertos, adaptadores REST, persistencia JPA, seguridad JWT y migraciones Flyway. El dominio no debe depender de Spring, JPA ni HTTP.

`citas-web` concentra UI, estado visual, formularios, rutas y cliente REST. No se permite Express/BFF ni duplicar reglas de negocio como autoridad en el cliente.

Los cambios de contrato REST son cross-repo y requieren plan, evidencia y validación en ambos lados. Los workflows n8n se versionan como JSON en `citas-api/automations/n8n/` y nunca contienen credenciales.
