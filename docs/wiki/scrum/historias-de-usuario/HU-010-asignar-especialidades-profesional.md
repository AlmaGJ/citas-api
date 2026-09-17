---
id: HU-010
tipo: historia-de-usuario
titulo: Asignar especialidades al profesional
estado: Pendiente de aprobación
epica: "[[EP-002-catalogos-y-oferta-asistencial]]"
esfuerzo: Medio
sprint_sugerido: Sprint 2
dependencias: ["[[HU-008-gestionar-especialidades]]", "[[HU-009-crear-profesional]]"]
relacionadas: ["[[HU-015-consultar-disponibilidad]]"]
---
# HU-010 — Asignar especialidades al profesional
## Historia de usuario
**COMO** ADMIN **QUIERO** asignar una o más especialidades y una primaria a un profesional **PARA** habilitar la atención que puede ofrecer.
## Alcance
- Relación N:M profesional-especialidad y marca de primaria.
## Fuera de alcance
- Cambiar duración de especialidad desde el profesional.
## Reglas de negocio
- Solo especialidades activas; una primaria dentro de las especialidades asignadas.
## Dependencias y relaciones
- Épica: [[EP-002-catalogos-y-oferta-asistencial]]; depende de [[HU-008-gestionar-especialidades]], [[HU-009-crear-profesional]]; relacionada: [[HU-015-consultar-disponibilidad]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** N:M con invariante de primaria.
## Tareas de desarrollo
- [ ] **T-01 — Relación 3FN.** Modelar puente y regla de primaria.
- [ ] **T-02 — Caso ADMIN.** Validar actividad/asociación antes de guardar.
- [ ] **T-03 — Gestión visual.** Permitir selección múltiple y primaria con feedback.
## Criterios de aceptación
### CA-01 — Asignación múltiple
**Dado** un profesional y especialidades activas **cuando** ADMIN asigna varias **entonces** quedan asociadas sin listas en columnas.
### CA-02 — Primaria válida
**Dado** especialidades asignadas **cuando** ADMIN designa la primaria **entonces** esta pertenece al conjunto; una especialidad inactiva no puede habilitar nuevas reservas.
## Definition of Done
- [ ] CA-01/CA-02 con evidencia de pruebas de relación/reglas.
- [ ] Migración Flyway/modelo 3FN y contrato/UI consistentes.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- La restricción técnica de “una primaria” debe quedar verificable en dominio/datos.
