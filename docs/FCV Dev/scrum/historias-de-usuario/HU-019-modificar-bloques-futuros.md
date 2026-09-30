---
id: HU-019
tipo: historia-de-usuario
titulo: "Modificar bloques futuros"
estado: Implementada
epica: "[[EP-004-disponibilidad-del-profesional]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 4"
dependencias: ["[[HU-018-crear-bloques-de-disponibilidad]]"]
relacionadas: ["[[HU-020-consultar-calendario-de-disponibilidad]]"]
---
# HU-019 — Modificar bloques futuros
## Historia de usuario
**COMO** PROFESSIONAL  
**QUIERO** editar o eliminar mis bloques futuros sin citas comprometidas  
**PARA** mantener mi disponibilidad correcta.
## Contexto y descripción
El PRD limita la modificación/eliminación a bloques futuros sin citas comprometidas.
## Alcance
- Editar/eliminar bloque propio que cumpla las condiciones y recalcular slots coherentes.
## Fuera de alcance
- Alterar pasado, bloque ajeno o bloque con cita comprometida.
## Reglas de negocio
- Ownership, futuro, no solapamiento y protección de citas comprometidas.
## Dependencias y relaciones
- Épica: [[EP-004-disponibilidad-del-profesional]]
- Dependencias: [[HU-018-crear-bloques-de-disponibilidad]].
- Relacionadas: [[HU-020-consultar-calendario-de-disponibilidad]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** modifica disponibilidad sin afectar reservas existentes.
## Tareas de desarrollo
- [ ] **T-01 — Detectar compromisos y ownership.** Dificultad: Alto. Consultar reservas/retenciones aplicables.
- [ ] **T-02 — Aplicar edición/eliminación segura.** Dificultad: Alto. Revalidar futuro, sede y solapamiento.
- [ ] **T-03 — Actualizar calendario y pruebas.** Dificultad: Medio. Reflejar éxito/rechazo y probar protección.
## Criterios de aceptación
### CA-01 — Edición permitida
**Dado** un bloque propio futuro sin citas comprometidas, **cuando** PROFESSIONAL lo edita de forma válida, **entonces** la disponibilidad refleja los nuevos slots.
### CA-02 — Eliminación permitida
**Dado** un bloque propio futuro sin citas comprometidas, **cuando** PROFESSIONAL lo elimina, **entonces** sus slots dejan de ofrecerse.
### CA-03 — Protección de compromisos
**Dado** un bloque pasado, ajeno o con citas comprometidas, **cuando** se intenta editar/eliminar, **entonces** se rechaza y las citas no cambian.
## Definition of Done
- [ ] CA-01 a CA-03 probados con reglas de agenda y autorización.
- [ ] Persistencia/índices aplicables, calendario cliente y contrato REST verificados.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | PASS | Smoke REST 2026-09-29: `PATCH /api/v1/professional/availability-blocks/{id}` sobre bloque propio futuro sin citas → `204`, `GET` refleja el nuevo horario. | — |
| CA-02 | PASS | Smoke REST 2026-09-29: `DELETE` sobre bloque propio futuro sin citas → `204`, `GET` ya no lo lista. | — |
| CA-03 / DoD | PASS | Smoke REST 2026-09-29: bloque ajeno → `404`; bloque pasado → `400`; bloque con cita `APPROVED` comprometida (slot con `appointment_id`) → `409` en `PATCH` y `DELETE` sin alterar la cita. Unitarias `SchedulingServiceTest` (6/6 PASS) cubren validación de fecha/boundary. Persistencia sin migración nueva (columnas ya existentes). | Testcontainers no ejecutable en este entorno (sin docker socket); no hay prueba de integración automatizada, solo unitaria + smoke manual. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-09-29 — Implementada: `Scheduling.AvailabilityBlock`, `Ports.SchedulingPort.blocksOf/updateBlock/deleteBlock`, `SchedulingService`, `SchedulingJpaAdapter`, endpoints `GET/PATCH/DELETE /api/v1/professional/availability-blocks[/{id}]`. Verificada con pruebas unitarias y smoke REST contra Docker; estado pasa a `Implementada`.
## Notas y decisiones
- “Cita comprometida” se verifica contra `professional_slots.appointment_id` no nulo para los slots del bloque.
- Eliminar un bloque hace borrado físico de la fila y de sus slots libres (no queda histórico de bloques eliminados, ya que no hay requisito de auditoría de disponibilidad en el PRD).
