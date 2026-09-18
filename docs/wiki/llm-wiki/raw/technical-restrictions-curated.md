# Snapshot curado — Restricciones técnicas

Fuente canónica: `RESTRICCIONES_TECNICAS.md` (SRC-TECH-001).

Backend: Java 21, Spring Boot 3.5.x, Maven, arquitectura hexagonal, Spring Data JPA, MySQL 8.4, Flyway, Spring Security/JWT y REST JSON.

Frontend: Node.js 24 LTS, TypeScript, React o Angular según el proyecto real de Stitch/Google AI Studio, REST directo y sin Express/BFF.

Seguridad: hash adaptativo, secretos por variables de entorno, access/refresh separados, autorización por rol y ownership, CORS explícito y sin logging de credenciales.

Repositorios: solo `citas-api` y `citas-web`; `main` estable, `develop` de trabajo; no reescribir historial para ocultar progreso.
