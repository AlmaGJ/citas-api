---
id: HU-013
tipo: historia-de-usuario
titulo: Modificar bloques futuros
estado: Pendiente de aprobación
epica: "[[EP-003-disponibilidad-profesional]]"
esfuerzo: Medio
sprint_sugerido: Sprint 2
dependencias: ["[[HU-012-crear-bloques-disponibilidad]]"]
relacionadas: ["[[HU-014-consultar-calendario-profesional]]"]
---
# HU-013 — Modificar bloques futuros
## Historia de usuario
**COMO** PROFESSIONAL **QUIERO** editar o eliminar mis bloques futuros sin citas comprometidas **PARA** ajustar mi disponibilidad.
## Alcance
- Edición/eliminación de bloques propios futuros disponibles.
## Fuera de alcance
- Alterar bloques con citas comprometidas o agenda de otro profesional.
## Reglas de negocio
- Solo futuro, propio y sin citas comprometidas.
## Dependencias y relaciones
- Épica: [[EP-003-disponibilidad-profesional]]; depende de [[HU-012-crear-bloques-disponibilidad]]; relacionada: [[HU-014-consultar-calendario-profesional]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** integridad de agenda y reglas de cambio.
## Tareas de desarrollo
- [ ] **T-01 — Detectar compromiso.** Resolver bloqueo de edición contra reserva/retención.
- [ ] **T-02 — Caso autorizado.** Aplicar ownership y validaciones al editar/eliminar.
- [ ] **T-03 — Interacción de calendario.** Mostrar acciones habilitadas/deshabilitadas y errores.
## Criterios de aceptación
### CA-01 — Cambio permitido
**Dado** un bloque propio futuro sin citas comprometidas **cuando** el profesional lo edita o elimina **entonces** su calendario refleja el cambio.
### CA-02 — Cambio protegido
**Dado** un bloque pasado, ajeno o comprometido **cuando** intenta modificarlo **entonces** el sistema lo rechaza sin afectar reservas.
## Definition of Done
- [ ] CA-01/CA-02 validados con pruebas de ownership/compromiso.
- [ ] Integridad de slots y contrato/UI están verificados.
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
- “Comprometida” incluye reserva o retención según el flujo de cita.
