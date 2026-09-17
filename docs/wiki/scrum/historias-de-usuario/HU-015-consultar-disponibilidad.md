---
id: HU-015
tipo: historia-de-usuario
titulo: Consultar disponibilidad
estado: Pendiente de aprobación
epica: "[[EP-003-disponibilidad-profesional]]"
esfuerzo: Alto
sprint_sugerido: Sprint 3
dependencias: ["[[HU-008-gestionar-especialidades]]", "[[HU-010-asignar-especialidades-profesional]]", "[[HU-012-crear-bloques-disponibilidad]]"]
relacionadas: ["[[HU-016-reservar-cita-general]]", "[[HU-017-solicitar-cita-especializada]]"]
---
# HU-015 — Consultar disponibilidad
## Historia de usuario
**COMO** USER **QUIERO** filtrar horarios disponibles **PARA** elegir una franja que complete mi cita.
## Alcance
- Filtros sede, tipo general/especializada, especialidad, profesional y fecha; solo franjas completas.
## Fuera de alcance
- Retener/confirmar una franja (HU de reserva).
## Reglas de negocio
- Especialidad activa y asignada; 60 minutos exige dos slots consecutivos; no mostrar retenidos/reservados.
## Dependencias y relaciones
- Épica: [[EP-003-disponibilidad-profesional]]; depende de [[HU-008-gestionar-especialidades]], [[HU-010-asignar-especialidades-profesional]], [[HU-012-crear-bloques-disponibilidad]]; relacionadas: [[HU-016-reservar-cita-general]], [[HU-017-solicitar-cita-especializada]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** filtros combinados y cálculo de franjas consecutivas válidas.
## Tareas de desarrollo
- [ ] **T-01 — Consulta de slots.** Diseñar criterio de disponibilidad sin exponer retenidos.
- [ ] **T-02 — Validación de duración.** Aplicar 30/60 y asociación profesional-especialidad.
- [ ] **T-03 — Buscador UI.** Implementar filtros y estados loading/vacío/error.
## Criterios de aceptación
### CA-01 — Filtro de oferta
**Dado** filtros válidos **cuando** USER busca disponibilidad **entonces** ve horarios de profesionales/sedes/especialidades que los cumplen.
### CA-02 — Duración completa
**Dado** una especialidad de 60 minutos **cuando** se listan horarios **entonces** solo se muestran inicios con dos slots consecutivos libres; no aparecen franjas ocupadas o retenidas.
## Definition of Done
- [ ] CA-01/CA-02 tienen pruebas de consulta y duración.
- [ ] Índices/consulta de agenda y contrato REST están justificados.
- [ ] UI no simula disponibilidad ni duplica autorización; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- La consulta se vuelve a validar al confirmar una reserva.
