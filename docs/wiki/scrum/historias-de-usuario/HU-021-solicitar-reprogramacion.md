---
id: HU-021
tipo: historia-de-usuario
titulo: Solicitar reprogramación
estado: Pendiente de aprobación
epica: "[[EP-005-ciclo-de-vida-y-operacion]]"
esfuerzo: Alto
sprint_sugerido: Sprint 4
dependencias: ["[[HU-018-consultar-mis-citas]]", "[[HU-015-consultar-disponibilidad]]"]
relacionadas: ["[[HU-022-decidir-reprogramacion]]"]
---
# HU-021 — Solicitar reprogramación
## Historia de usuario
**COMO** USER **QUIERO** solicitar una nueva fecha y hora para una cita aprobada futura **PARA** conservar mi atención mientras ADMIN evalúa el cambio.
## Alcance
- Solicitud `PENDING` con misma especialidad/profesional y retención de nueva franja.
## Fuera de alcance
- Cambiar profesional (es nueva cita) o liberar la cita original al solicitar.
## Reglas de negocio
- Solo `APPROVED` futura; conserva profesional/especialidad; nueva franja retenida; original se mantiene.
## Dependencias y relaciones
- Épica: [[EP-005-ciclo-de-vida-y-operacion]]; depende de [[HU-018-consultar-mis-citas]], [[HU-015-consultar-disponibilidad]]; relacionada: [[HU-022-decidir-reprogramacion]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** dos franjas coordinadas y protección del original.
## Tareas de desarrollo
- [ ] **T-01 — Validar elegibilidad.** Comprobar cita, profesional/especialidad y disponibilidad nueva.
- [ ] **T-02 — Retención transaccional.** Crear solicitud `PENDING` sin liberar franja original.
- [ ] **T-03 — Flujo UI.** Permitir elegir nueva franja y mostrar estado pendiente.
## Criterios de aceptación
### CA-01 — Solicitud válida
**Dado** una cita propia `APPROVED` futura **cuando** USER escoge nueva franja disponible del mismo profesional/especialidad **entonces** se crea reprogramación `PENDING` y se retiene la nueva franja.
### CA-02 — Conservación del original
**Dado** una reprogramación `PENDING` **cuando** se consulta la cita original **entonces** conserva su franja hasta una decisión; cambiar profesional no se procesa como reprogramación.
## Definition of Done
- [ ] CA-01/CA-02 cubiertos por pruebas de integridad/retención.
- [ ] Migración 3FN, contrato y UI reflejan ambas franjas sin exponer datos ajenos.
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
- La decisión se realiza exclusivamente en [[HU-022-decidir-reprogramacion]].
