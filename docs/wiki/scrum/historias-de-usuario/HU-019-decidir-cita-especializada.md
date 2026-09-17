---
id: HU-019
tipo: historia-de-usuario
titulo: Decidir cita especializada
estado: Pendiente de aprobación
epica: "[[EP-005-ciclo-de-vida-y-operacion]]"
esfuerzo: Alto
sprint_sugerido: Sprint 4
dependencias: ["[[HU-017-solicitar-cita-especializada]]"]
relacionadas: ["[[HU-024-consultar-bandeja-administrativa]]", "[[HU-025-auditar-cambios-de-estado]]"]
---
# HU-019 — Decidir cita especializada
## Historia de usuario
**COMO** ADMIN **QUIERO** aprobar o rechazar una solicitud especializada **PARA** decidir la atención y liberar su horario si no procede.
## Alcance
- Decisión sobre `REQUESTED`, motivo obligatorio al rechazar y liberación correspondiente.
## Fuera de alcance
- Decidir una solicitud ajena al estado requerido.
## Reglas de negocio
- Aprobar → `APPROVED`; rechazar → `REJECTED` con motivo y slots liberados.
## Dependencias y relaciones
- Épica: [[EP-005-ciclo-de-vida-y-operacion]]; depende de [[HU-017-solicitar-cita-especializada]]; relacionadas: [[HU-024-consultar-bandeja-administrativa]], [[HU-025-auditar-cambios-de-estado]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** transición autorizada, liberación y auditoría.
## Tareas de desarrollo
- [ ] **T-01 — Transición ADMIN.** Limitar decisión a `REQUESTED` y validar motivo.
- [ ] **T-02 — Integridad de slots.** Conservar o liberar retención coherentemente.
- [ ] **T-03 — UI de decisión.** Requerir motivo al rechazar y mostrar resultado.
## Criterios de aceptación
### CA-01 — Aprobación
**Dado** una solicitud `REQUESTED` **cuando** ADMIN aprueba **entonces** la cita pasa a `APPROVED` y conserva su franja.
### CA-02 — Rechazo con liberación
**Dado** una solicitud `REQUESTED` **cuando** ADMIN rechaza con motivo **entonces** pasa a `REJECTED`, el motivo queda disponible y los slots se liberan; sin motivo no se rechaza.
## Definition of Done
- [ ] CA-01/CA-02 tienen pruebas de transición, motivo y liberación.
- [ ] Auditoría contiene actor/fuente/fecha/estado y no es editable como CRUD.
- [ ] Contrato/UI y trazabilidad actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- La matriz completa de transiciones se documentará con el contrato.
