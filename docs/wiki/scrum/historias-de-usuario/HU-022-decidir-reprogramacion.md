---
id: HU-022
tipo: historia-de-usuario
titulo: Decidir reprogramación
estado: Pendiente de aprobación
epica: "[[EP-005-ciclo-de-vida-y-operacion]]"
esfuerzo: Alto
sprint_sugerido: Sprint 4
dependencias: ["[[HU-021-solicitar-reprogramacion]]"]
relacionadas: ["[[HU-024-consultar-bandeja-administrativa]]", "[[HU-025-auditar-cambios-de-estado]]"]
---
# HU-022 — Decidir reprogramación
## Historia de usuario
**COMO** ADMIN **QUIERO** aprobar o rechazar una reprogramación pendiente **PARA** actualizar o preservar la cita con integridad de horarios.
## Alcance
- Decisión `PENDING`, motivo cuando corresponda, liberación de franja correcta.
## Fuera de alcance
- Reasignar profesional.
## Reglas de negocio
- Aprobar libera slots antiguos, asigna nuevos y actualiza cita; rechazar libera nueva reserva y mantiene original.
## Dependencias y relaciones
- Épica: [[EP-005-ciclo-de-vida-y-operacion]]; depende de [[HU-021-solicitar-reprogramacion]]; relacionadas: [[HU-024-consultar-bandeja-administrativa]], [[HU-025-auditar-cambios-de-estado]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** transición atómica sobre franjas antiguas/nuevas.
## Tareas de desarrollo
- [ ] **T-01 — Decisión ADMIN.** Autorizar y validar `PENDING`/motivo aplicable.
- [ ] **T-02 — Movimiento de slots.** Implementar aprobar/rechazar sin pérdida de la cita original.
- [ ] **T-03 — UI administrativa.** Mostrar comparación, motivo y resultado.
## Criterios de aceptación
### CA-01 — Aprobación consistente
**Dado** una reprogramación `PENDING` **cuando** ADMIN aprueba **entonces** se liberan slots antiguos, se asignan nuevos y se actualiza la cita.
### CA-02 — Rechazo conservador
**Dado** una reprogramación `PENDING` **cuando** ADMIN rechaza con motivo requerido **entonces** se libera solo la nueva reserva y la cita original conserva su franja.
## Definition of Done
- [ ] CA-01/CA-02 tienen pruebas de transición/integridad y concurrencia relevante.
- [ ] Auditoría, contratos y UI son consistentes en ambos resultados.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- La política de motivo para rechazo se alinea con el PRD y se concreta en contrato.
