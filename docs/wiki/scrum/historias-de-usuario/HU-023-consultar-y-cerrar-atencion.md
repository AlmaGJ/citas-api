---
id: HU-023
tipo: historia-de-usuario
titulo: Consultar y cerrar atención
estado: Pendiente de aprobación
epica: "[[EP-005-ciclo-de-vida-y-operacion]]"
esfuerzo: Medio
sprint_sugerido: Sprint 4
dependencias: ["[[HU-016-reservar-cita-general]]", "[[HU-019-decidir-cita-especializada]]"]
relacionadas: ["[[HU-025-auditar-cambios-de-estado]]"]
---
# HU-023 — Consultar y cerrar atención
## Historia de usuario
**COMO** PROFESSIONAL **QUIERO** consultar mi agenda aprobada y cerrar una atención aplicable **PARA** reflejar si fue completada o hubo inasistencia.
## Alcance
- Agenda por día/semana/sede de citas `APPROVED` propias; transición a `COMPLETED` o `NO_SHOW`.
## Fuera de alcance
- Ver citas de otros profesionales, historia clínica o cambio de estados no aplicables.
## Reglas de negocio
- Solo citas propias; cierre pasado/aplicable; historial obligatorio.
## Dependencias y relaciones
- Épica: [[EP-005-ciclo-de-vida-y-operacion]]; depende de [[HU-016-reservar-cita-general]], [[HU-019-decidir-cita-especializada]]; relacionada: [[HU-025-auditar-cambios-de-estado]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** agenda filtrada, ownership y estados finales.
## Tareas de desarrollo
- [ ] **T-01 — Agenda profesional.** Consultar `APPROVED` propias por periodo/sede sin datos fuera de sus citas.
- [ ] **T-02 — Cierre autorizado.** Validar aplicabilidad y registrar `COMPLETED`/`NO_SHOW`.
- [ ] **T-03 — Dashboard profesional.** Presentar agenda/acciones con loading, vacío, error y confirmación.
## Criterios de aceptación
### CA-01 — Visibilidad propia
**Dado** un PROFESSIONAL **cuando** filtra por día, semana o sede **entonces** visualiza solo sus citas `APPROVED` correspondientes.
### CA-02 — Cierre trazable
**Dado** una cita propia pasada o aplicable **cuando** la marca completada o inasistencia **entonces** el estado cambia a `COMPLETED` o `NO_SHOW` y deja historial.
## Definition of Done
- [ ] CA-01/CA-02 validados con pruebas de filtro, ownership y transición.
- [ ] UI/REST no revelan datos ajenos y auditoría es verificable.
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
- “Aplicable” requiere definición operativa durante diseño.
