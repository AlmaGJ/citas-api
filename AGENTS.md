# Agente principal de `citas-api`

## Estado comprobado del repositorio

Este repositorio todavía no contiene una aplicación Spring Boot, `pom.xml`, código fuente ni pruebas ejecutables. Antes de proponer comandos, paquetes o rutas, inspecciona la estructura real que se haya incorporado.

Las fuentes de alcance son `../PRD.md`, `../RESTRICCIONES_TECNICAS.md`, `../database/REQUISITOS_NORMALIZACION_3FN.md` y la documentación de este repositorio. La Wiki global está en `docs/wiki/llm-wiki/`, pero la mantiene el agente orquestador: este agente solo la consulta y no la actualiza.

## Responsabilidad

Trabaja únicamente en `citas-api` para construir y mantener:

- Java 21, Spring Boot 3.5.x y Maven.
- Arquitectura hexagonal, REST/JSON y contratos backend.
- Spring Security con JWT de access y refresh.
- MySQL 8.4, Spring Data JPA y migraciones Flyway.
- Reglas de negocio, validación y autorización del PRD.
- Pruebas de dominio, aplicación e integración REST/persistencia cuando exista la infraestructura.

No edites `../citas-web`. Si un cambio de contrato REST afecta al frontend, detén el cambio y notifícalo al agente orquestador para coordinar evidencia en ambos repositorios.

## Arquitectura obligatoria

- El dominio no depende de Spring, JPA ni HTTP.
- Los casos de uso pertenecen a la capa de aplicación.
- Los puertos expresan dependencias de entrada y salida.
- REST, seguridad, persistencia y configuración son adaptadores.
- Los controladores traducen HTTP y no concentran reglas de negocio.
- Los cambios de esquema se implementan mediante una migración Flyway justificada y compatible con 3FN.
- Los catálogos fijos se cargan mediante seed; los datos de laboratorio son sintéticos.

## Seguridad y datos

- Passwords con hash adaptativo compatible con Spring Security; nunca en texto plano.
- Secretos únicamente por variables de entorno. No abrir, reproducir ni registrar contenido de `.env`.
- No registrar passwords, access tokens, refresh tokens ni credenciales.
- Aplicar validación server-side, autorización por rol y ownership, y CORS explícito.
- No usar información privada real de FCV.

## Método de trabajo

1. Localiza la HU aprobada, criterios de aceptación y DoD en `docs/wiki/scrum/`. Si no existen, informa el bloqueo sin inventarlos.
2. Identifica reglas del PRD, contratos REST, entidades, puertos, adaptadores, migraciones y pruebas afectados.
3. Antes de editar, presenta un plan con archivos, riesgos y verificación prevista.
4. Implementa el cambio mínimo coherente con la arquitectura y las reglas de negocio.
5. Ejecuta las pruebas disponibles y documenta exactamente los comandos y resultados.
6. Verifica DoD, límites de arquitectura, seguridad y contrato.
7. Resume evidencia, pendientes y aspectos no verificados.

`main` es estable y `develop` es la rama de trabajo requerida. Si `develop` aún no existe, informa la condición antes de iniciar implementación; no la crees por inferencia.

## Reglas de dominio que requieren protección explícita

- No permitir doble reserva ni retención incompatible de slots.
- Las citas generales se aprueban automáticamente; las especializadas requieren decisión ADMIN y el rechazo exige motivo.
- Las especialidades de 60 minutos requieren slots consecutivos.
- No permitir bloques ni citas en el pasado.
- Un profesional solo publica agenda en sedes asignadas y con especialidad activa asociada.
- Cancelar o rechazar libera la reserva; una reprogramación no libera la cita original hasta aprobarse.
- Las transiciones de estado son explícitas, auditables y el historial no se trata como CRUD normal.

Los workflows n8n se versionan como JSON en `automations/n8n/` cuando corresponda a S5/S6; nunca contienen credenciales.
