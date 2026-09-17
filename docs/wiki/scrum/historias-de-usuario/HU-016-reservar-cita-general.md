---
id: HU-016
tipo: historia-de-usuario
titulo: Reservar cita general
estado: Pendiente de aprobación
epica: "[[EP-004-reserva-y-consulta-de-citas]]"
esfuerzo: Alto
sprint_sugerido: Sprint 3
dependencias: ["[[HU-015-consultar-disponibilidad]]"]
relacionadas: ["[[HU-020-cancelar-cita]]", "[[HU-025-auditar-cambios-de-estado]]"]
---
# HU-016 — Reservar cita general
## Historia de usuario
**COMO** USER **QUIERO** confirmar una cita de Medicina General **PARA** obtener atención sin aprobación administrativa.
## Alcance
- Selección de profesional general/franja y confirmación atómica de cita.
## Fuera de alcance
- Solicitud especializada y reprogramación.
## Reglas de negocio
- La cita general nace `APPROVED`; se revalida disponibilidad; no hay doble reserva.
## Dependencias y relaciones
- Épica: [[EP-004-reserva-y-consulta-de-citas]]; depende de [[HU-015-consultar-disponibilidad]]; relacionadas: [[HU-020-cancelar-cita]], [[HU-025-auditar-cambios-de-estado]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** confirmación concurrente, estado y auditoría.
## Tareas de desarrollo
- [ ] **T-01 — Caso de reserva.** Revalidar y reservar los slots requeridos de forma consistente.
- [ ] **T-02 — Transición/auditoría.** Crear `APPROVED` y registrar cambio.
- [ ] **T-03 — Confirmación UI.** Mostrar resultado o pérdida de disponibilidad y evitar doble envío.
## Criterios de aceptación
### CA-01 — Aprobación automática
**Dado** una franja general aún disponible **cuando** USER confirma **entonces** se crea una cita `APPROVED` con profesional y duración válidos.
### CA-02 — Concurrencia segura
**Dado** que la franja fue ocupada antes de confirmar **cuando** USER intenta reservar **entonces** no se crea una cita duplicada y se informa que debe elegir otra franja.
## Definition of Done
- [ ] CA-01/CA-02 tienen evidencia de pruebas de aplicación/integración concurrente relevante.
- [ ] Reserva, auditoría, migración aplicable y contrato frontend/backend son coherentes.
- [ ] No se exponen datos ajenos; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- El mecanismo de atomicidad se decidirá y probará, no se presupone aquí.
