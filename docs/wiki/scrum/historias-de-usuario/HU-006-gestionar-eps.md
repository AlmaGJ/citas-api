---
id: HU-006
tipo: historia-de-usuario
titulo: Gestionar EPS
estado: Pendiente de aprobación
epica: "[[EP-002-catalogos-y-oferta-asistencial]]"
esfuerzo: Medio
sprint_sugerido: Sprint 1
dependencias: ["[[HU-002-iniciar-y-cerrar-sesion]]"]
relacionadas: ["[[HU-007-gestionar-planes-eps]]", "[[HU-004-gestionar-perfil-y-afiliacion]]"]
---
# HU-006 — Gestionar EPS
## Historia de usuario
**COMO** ADMIN **QUIERO** crear, consultar, actualizar y activar/desactivar EPS **PARA** mantener un catálogo seleccionable sin perder referencias históricas.
## Alcance
- CRUD administrativo y activación/desactivación de EPS.
## Fuera de alcance
- Borrado físico de EPS referenciada.
## Reglas de negocio
- Una EPS referenciada no se borra físicamente; se desactiva cuando aplique.
## Dependencias y relaciones
- Épica: [[EP-002-catalogos-y-oferta-asistencial]]; depende de [[HU-002-iniciar-y-cerrar-sesion]]; relacionadas: [[HU-007-gestionar-planes-eps]], [[HU-004-gestionar-perfil-y-afiliacion]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** CRUD autorizado y restricciones referenciales.
## Tareas de desarrollo
- [ ] **T-01 — Modelo y migración.** Crear catálogo normalizado con reglas de referencia.
- [ ] **T-02 — Administración API.** Implementar operaciones ADMIN y errores de integridad.
- [ ] **T-03 — Pantalla CRUD.** Integrar listado/formulario/estado activo sin sustituir validación backend.
## Criterios de aceptación
### CA-01 — Gestión autorizada
**Dado** un ADMIN autenticado **cuando** gestiona una EPS válida **entonces** puede crear, consultar, actualizar o cambiar su estado.
### CA-02 — Conservación de referencias
**Dado** una EPS usada por una afiliación **cuando** se pretende retirarla **entonces** no se borra físicamente y queda desactivable según la regla.
## Definition of Done
- [ ] CA-01/CA-02 tienen evidencia de pruebas de rol e integridad.
- [ ] Migración Flyway y relaciones 3FN son verificables.
- [ ] UI/API manejan error, éxito y estado activo; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- Reglas de unicidad de nombre/código se definirán al diseñar el contrato.
