---
id: HU-017
tipo: historia-de-usuario
titulo: Solicitar cita especializada
estado: Pendiente de aprobación
epica: "[[EP-004-reserva-y-consulta-de-citas]]"
esfuerzo: Alto
sprint_sugerido: Sprint 3
dependencias: ["[[HU-015-consultar-disponibilidad]]"]
relacionadas: ["[[HU-019-decidir-cita-especializada]]"]
---
# HU-017 — Solicitar cita especializada
## Historia de usuario
**COMO** USER **QUIERO** solicitar una cita especializada en una franja disponible **PARA** que ADMIN pueda decidirla sin perder el horario.
## Alcance
- Selección especialidad/sede/profesional/horario; creación `REQUESTED` y retención.
## Fuera de alcance
- Aprobación/rechazo administrativo.
## Reglas de negocio
- Especialidad activa/asignada; no doble reserva; nace `REQUESTED`; horario queda retenido.
## Dependencias y relaciones
- Épica: [[EP-004-reserva-y-consulta-de-citas]]; depende de [[HU-015-consultar-disponibilidad]]; relacionada: [[HU-019-decidir-cita-especializada]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** retención segura y flujo diferido.
## Tareas de desarrollo
- [ ] **T-01 — Crear solicitud.** Validar oferta y retener slots atómicamente.
- [ ] **T-02 — Estado/auditoría.** Registrar `REQUESTED` con fuente correspondiente.
- [ ] **T-03 — UI de solicitud.** Confirmar solicitud y explicar estado pendiente.
## Criterios de aceptación
### CA-01 — Solicitud retenida
**Dado** una franja especializada válida libre **cuando** USER la solicita **entonces** se crea en `REQUESTED` y los slots quedan retenidos.
### CA-02 — Franja no reutilizable
**Dado** una franja retenida por solicitud **cuando** otro usuario busca o reserva **entonces** esa franja no se ofrece ni puede reservarse.
## Definition of Done
- [ ] CA-01/CA-02 probados, incluida competencia de retención.
- [ ] Estado, auditoría, contrato y migración aplicable son verificables.
- [ ] UI comunica éxito/error sin datos sensibles; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- La liberación se concreta en [[HU-019-decidir-cita-especializada]].
