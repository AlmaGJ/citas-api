---
id: HU-025
tipo: historia-de-usuario
titulo: Auditar cambios de estado
estado: Pendiente de aprobación
epica: "[[EP-006-control-administrativo-y-auditoria]]"
esfuerzo: Alto
sprint_sugerido: Sprint 5
dependencias: ["[[HU-016-reservar-cita-general]]", "[[HU-017-solicitar-cita-especializada]]", "[[HU-019-decidir-cita-especializada]]", "[[HU-020-cancelar-cita]]", "[[HU-022-decidir-reprogramacion]]", "[[HU-023-consultar-y-cerrar-atencion]]"]
relacionadas: ["[[HU-024-consultar-bandeja-administrativa]]"]
---
# HU-025 — Auditar cambios de estado
## Historia de usuario
**COMO** ADMIN **QUIERO** consultar el historial de estados de una cita **PARA** verificar quién, cuándo y desde qué fuente produjo cada cambio.
## Alcance
- Registro y consulta de cita, estado nuevo, actor cuando exista, fuente `SYSTEM`/`USER`/`ADMIN`, fecha/hora y motivo opcional.
## Fuera de alcance
- Edición/borrado CRUD normal de auditoría.
## Reglas de negocio
- Toda transición explícita deja historial; datos de auditoría no se modifican como CRUD normal.
## Dependencias y relaciones
- Épica: [[EP-006-control-administrativo-y-auditoria]]; depende de [[HU-016-reservar-cita-general]], [[HU-017-solicitar-cita-especializada]], [[HU-019-decidir-cita-especializada]], [[HU-020-cancelar-cita]], [[HU-022-decidir-reprogramacion]], [[HU-023-consultar-y-cerrar-atencion]]; relacionada: [[HU-024-consultar-bandeja-administrativa]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** obligación transversal de todas las transiciones y protección de inmutabilidad.
## Tareas de desarrollo
- [ ] **T-01 — Modelo de historial.** Justificar 3FN, FK, fuente, actor opcional, índices y migración.
- [ ] **T-02 — Integración transaccional.** Registrar cada transición cubierta sin tratar el historial como CRUD.
- [ ] **T-03 — Consulta ADMIN.** Exponer/presentar historial bajo autorización sin incluir secretos.
## Criterios de aceptación
### CA-01 — Registro completo
**Dado** una transición de estado de cita cubierta **cuando** se ejecuta **entonces** se registra cita, estado nuevo, actor cuando exista, fuente, fecha/hora y motivo opcional.
### CA-02 — Integridad del historial
**Dado** un ADMIN consultando una cita **cuando** revisa su historial **entonces** puede verlo, pero ninguna operación normal permite editar o borrar sus registros.
## Definition of Done
- [ ] CA-01/CA-02 tienen evidencia para todas las transiciones de HU dependientes.
- [ ] Migración/modelo 3FN, transaccionalidad y pruebas relevantes son verificables.
- [ ] Consulta aplica rol; historial no expone secretos; trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- La auditoría no reemplaza los listados funcionales del USER o PROFESSIONAL.
