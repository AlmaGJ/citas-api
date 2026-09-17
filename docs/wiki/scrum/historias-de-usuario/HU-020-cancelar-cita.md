---
id: HU-020
tipo: historia-de-usuario
titulo: Cancelar cita
estado: Pendiente de aprobación
epica: "[[EP-005-ciclo-de-vida-y-operacion]]"
esfuerzo: Medio
sprint_sugerido: Sprint 4
dependencias: ["[[HU-018-consultar-mis-citas]]"]
relacionadas: ["[[HU-025-auditar-cambios-de-estado]]"]
---
# HU-020 — Cancelar cita
## Historia de usuario
**COMO** USER **QUIERO** cancelar una cita futura no terminal **PARA** liberar su horario cuando ya no la necesito.
## Alcance
- Cancelación propia de cita futura no terminal y registro de estado.
## Fuera de alcance
- Reactivación directa de una cita cancelada.
## Reglas de negocio
- `CANCELLED` libera slots; solo cita propia, futura y no terminal; historial obligatorio.
## Dependencias y relaciones
- Épica: [[EP-005-ciclo-de-vida-y-operacion]]; depende de [[HU-018-consultar-mis-citas]]; relacionada: [[HU-025-auditar-cambios-de-estado]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** transición, ownership y liberación.
## Tareas de desarrollo
- [ ] **T-01 — Validar cancelabilidad.** Verificar actor, futuro y estado.
- [ ] **T-02 — Aplicar cancelación.** Cambiar estado, liberar slots y auditar.
- [ ] **T-03 — Acción UI.** Mostrar opción solo cuando aplica, confirmación y resultado.
## Criterios de aceptación
### CA-01 — Cancelación válida
**Dado** una cita propia futura no terminal **cuando** USER cancela **entonces** queda `CANCELLED`, se liberan sus slots y se registra historial.
### CA-02 — Cancelación prohibida
**Dado** una cita ajena, pasada o terminal **cuando** se intenta cancelar **entonces** no cambia de estado ni libera indebidamente horarios.
## Definition of Done
- [ ] CA-01/CA-02 probados con transiciones y ownership.
- [ ] Auditoría/liberación/contrato están verificados; UI comunica restricción.
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
- Cancelación posterior a un rechazo de reprogramación queda permitida por el PRD si se cumplen reglas.
