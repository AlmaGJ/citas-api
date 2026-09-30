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

## DECISIÓN — 2026-09-29 · HU-019, editar/eliminar bloques futuros

`GET /api/v1/professional/availability-blocks` (rol `PROFESSIONAL`) lista los bloques propios activos como `{ id, locationId, date, start, end }`. `PATCH /api/v1/professional/availability-blocks/{id}` y `DELETE /api/v1/professional/availability-blocks/{id}` aplican las mismas reglas que la creación (futuro, límites de 30 minutos, sede asignada, sin solape con otro bloque propio) y además exigen que el bloque sea propio y no tenga slots con cita asignada. Errores: `400` bloque inválido/pasado, `404` bloque ajeno o inexistente, `409` bloque con citas comprometidas o solape. `PATCH` regenera los slots libres del bloque con el nuevo horario; `DELETE` borra físicamente el bloque y sus slots libres. Sin migración de esquema nueva.

## DECISIÓN — 2026-09-29 · Seguimiento de reprogramación (USER)

`GET /api/v1/appointments/{id}/reschedule-requests` (rol `USER`, misma verificación de ownership que `GET /api/v1/appointments/{id}`) devuelve las solicitudes de reprogramación de una cita propia, más recientes primero: `{ id, status, requestedLocationId, previousStartAt, previousEndAt, requestedStartAt, requestedEndAt, decisionReason, decidedAt, createdAt }`. `status` es uno de `PENDING|APPROVED|REJECTED|CANCELLED`. Sin migración nueva; reutiliza `reschedule_requests`/`reschedule_request_statuses` existentes.

## DECISIÓN — 2026-09-29 · Administración de catálogos (HU-012 a HU-017, EP-003)

Nuevos contratos ADMIN (`hasRole('ADMIN')`), sin migraciones nuevas (todas las columnas ya existían en `V2`/`V3`):

- `GET /api/v1/admin/insurance-regimes` → `{ id, code, name }[]`, catálogo fijo de régimen (solo lectura).
- `GET/POST /api/v1/admin/eps`, `PATCH /api/v1/admin/eps/{id}`, `POST /api/v1/admin/eps/{id}/activate|deactivate` → `{ id, code, name, active }`. Sin borrado físico. `code` único (`409` si se repite).
- `GET/POST /api/v1/admin/eps/{epsId}/plans`, `PATCH /api/v1/admin/eps/{epsId}/plans/{id}`, `POST .../activate|deactivate` → `{ id, epsId, regimeId, code, name, active }`. `(epsId, code)` único (`409`); `epsId` inexistente → `400`.
- `GET/POST /api/v1/admin/specialties`, `PATCH /api/v1/admin/specialties/{id}`, `POST .../activate|deactivate` → `{ id, code, name, durationMinutes, general, approvalRequired, active }`. `durationMinutes` solo `30|60` (`400` en otro caso); `GET` sin filtro de activo (a diferencia de `GET /api/v1/catalogs/specialties`, que sigue siendo solo-activas).
- `POST /api/v1/admin/professionals` → `{ firstName, lastName, documentType, documentNumber, email, phone, password, code, license }` crea identidad con rol `PROFESSIONAL` (mismo pipeline de hash/unicidad que el registro USER) más el perfil profesional; responde `{ id, userId, code, name, license, active, specialties: [], locationIds: [] }`. Email/documento duplicados → `409`; código/matrícula duplicados → `409`.
- `GET /api/v1/admin/professionals` → lista todos (incluye inactivos) con el mismo shape, incluyendo `specialties: [{ specialtyId, primary, active }]` y `locationIds`. Corrige un bug previo donde el nombre real del profesional no se resolvía (se mostraba el código); también corregido en `GET /api/v1/professionals` público.
- `POST /api/v1/admin/professionals/{id}/activate|deactivate`. Un profesional inactivo no puede publicar bloques (`404` ya existente en `createBlock`).
- `POST /api/v1/admin/professionals/{id}/specialties` `{ specialtyId, primary }` y `DELETE .../specialties/{specialtyId}`: la especialidad debe estar activa; marcar `primary=true` desmarca cualquier otra primaria del mismo profesional en la misma transacción (invariante de "exactamente una primaria").
- `POST /api/v1/admin/professionals/{id}/locations` `{ locationId }` y `DELETE .../locations/{locationId}`: `locationId` debe ser una de las sedes fijas activas; no hay endpoint para crear sedes.

## DECISIÓN — 2026-09-29 · Bandeja administrativa con filtros (HU-031)

`GET /api/v1/admin/inbox` (rol `ADMIN`) se amplió de forma compatible: acepta `locationId`, `professionalId`, `specialtyId` y `date` (todos opcionales) y cada fila ahora incluye `type: "APPOINTMENT"|"RESCHEDULE"`, `professionalId`, `locationId`, `specialtyId`, `specialty`, `professional`, `location` y `reason` (antes solo `id`, `status`, `scheduledStartAt`, `scheduledEndAt`, insuficiente para identificar o filtrar el pendiente). `type=APPOINTMENT` se decide con `POST /api/v1/admin/appointments/{id}/decision`; `type=RESCHEDULE` con `POST /api/v1/admin/reschedule-requests/{id}/decision` (ambos contratos sin cambios). Sin migración de esquema nueva.
