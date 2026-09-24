# Contratos REST

## HECHO

El PRD exige REST/JSON entre `citas-web` y `citas-api` y validación cross-repo en funcionalidades clave.

## DECISIÓN — 2026-09-17 · HU-004/005/006/007

El contrato inicial cubre solamente autenticación bajo `/api/v1/auth`:

| Operación | Entrada | Éxito |
|---|---|---|
| `POST /register` | JSON `firstName`, `lastName`, `documentType`, `documentNumber`, `email`, `phone`, `password` | `201`, JSON con `id`, datos públicos y rol `USER`, sin contraseña |
| `POST /login` | JSON `email`, `password` | `200`, JSON `accessToken`, `tokenType=Bearer`, `expiresIn`; cookie `refresh_token` |
| `POST /refresh` | Cookie `refresh_token` | `200`, nuevo access en JSON y nueva cookie refresh; la anterior se revoca |
| `POST /logout` | Cookie `refresh_token` | `204`, revocación de la sesión y cookie borrada |

Email se normaliza con trim y minúsculas. Documento es único por `(documentType, documentNumber)` normalizados. La contraseña de registro es obligatoria y se limita a 72 bytes UTF-8 por el límite de BCrypt; sus espacios no se alteran. Solo se permite autoregistro `USER`. Errores: `400` validación, `409` duplicidad, `401` credencial/refresh inválido, `403` rol insuficiente, en formato Problem Details; login no revela qué credencial falló.

Access JWT y refresh JWT usan secretos distintos, tipo explícito y duraciones configurables (valores iniciales: 15 minutos y 7 días). El access lleva `sub` y roles. El refresh lleva `sub` y `jti`; su identificador se guarda solo como hash en una sesión persistida. Un refresh válido rota ambos tokens atómicamente. Logout revoca solo la sesión indicada; un access emitido conserva validez hasta su expiración.

Para sitios distintos, la cookie es `HttpOnly; Secure; SameSite=None`, con `Path=/api/v1/auth`. CORS permite credenciales únicamente al `FRONTEND_ORIGIN` configurado. Login, refresh y logout requieren `Origin` permitido cuando se envía y `X-Requested-With: XMLHttpRequest`; el perfil HTTP local usa cookie `SameSite=Lax` sin `Secure`. El cliente futuro deberá enviar credenciales y ese encabezado, guardar access únicamente según su diseño aprobado y eliminar su estado local al salir.

### Impacto cross-repo antes del cambio REST

- `citas-api`: nuevo `pom.xml`, código de dominio/aplicación/adaptadores, migración Flyway, configuración, pruebas y este contrato.
- `citas-web`: sin cambios en este incremento; HU-033 integrará las cuatro rutas, cookie y errores. Al ser endpoints nuevos, no hay cliente previo que migrar.
- Compatibilidad: `/api/v1` fija la versión de este contrato; cambios posteriores requieren revisión de ambas partes. Migración: esquema inicial de identidad por Flyway. Pruebas: REST, seguridad, persistencia y `mvn test` en backend; prueba cross-repo cuando exista el cliente.

## PREGUNTA ABIERTA

Las rutas, filtros, paginación y formatos de fecha/hora de las demás HU siguen sin contrato aprobado.

## DECISIÓN — 2026-09-24 · afiliación inicial opcional

La extensión compatible de `POST /api/v1/auth/register` acepta `insurancePlanId` opcional. Si se omite, el registro conserva el comportamiento previo. Si se informa, debe identificar un plan y una EPS activos; la API crea una afiliación por FK dentro de la misma transacción. Un identificador inexistente o inactivo responde `400` en Problem Details. No se aceptan ni almacenan nombres de EPS o plan en `users`.

`GET /api/v1/catalogs/active-plans` es público para permitir el registro y devuelve solamente `id`, `name`, `epsName` y `regime` de planes y EPS activos. El cliente no conserva el catálogo como fuente de verdad.

## DECISIÓN — 2026-09-24 · Core de agenda S3

Los catálogos fijos se exponen mediante `GET /api/v1/catalogs/locations` y `GET /api/v1/catalogs/specialties`. Una especialidad devuelve `durationMinutes`, `general` y `requiresAdminApproval`; las sedes devuelven solo identificador, código y nombre. La disponibilidad de un USER se consulta con `GET /api/v1/availability?specialtyId=&locationId=&professionalId?=&date=YYYY-MM-DD` y devuelve franjas `{ professionalId, locationId, startAt, endAt }` que ya cumplen la duración de la especialidad (30 o 60 min).

`POST /api/v1/appointments` requiere rol `USER` y `{ professionalId, locationId, specialtyId, startAt, reason? }`. La API deriva el tipo desde la especialidad, bloquea los slots atómicos dentro de la transacción y devuelve `201` con la cita. Medicina General queda `APPROVED`; una especialidad queda `REQUESTED`. Una franja que dejó de estar libre devuelve `409`; selección, fecha o límites de slot inválidos devuelven `400`.

Un `PROFESSIONAL` crea su disponibilidad con `POST /api/v1/professional/availability-blocks` y `{ locationId, date, start, end }`; solo se permiten futuros, límites cada 30 minutos, sedes asignadas y bloques no solapados. `ADMIN` decide una especializada pendiente usando `POST /api/v1/admin/appointments/{id}/decision` con `{ decision: APPROVE|REJECT, reason? }`; `REJECT` exige motivo y libera slots, y cada transición crea historial. Las rutas protegidas retornan `401` sin JWT y `403` cuando el rol no corresponde.
