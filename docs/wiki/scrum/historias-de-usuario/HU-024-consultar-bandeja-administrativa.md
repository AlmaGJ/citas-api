---
id: HU-024
tipo: historia-de-usuario
titulo: Consultar bandeja administrativa
estado: Pendiente de aprobación
epica: "[[EP-006-control-administrativo-y-auditoria]]"
esfuerzo: Medio
sprint_sugerido: Sprint 5
dependencias: ["[[HU-019-decidir-cita-especializada]]", "[[HU-022-decidir-reprogramacion]]"]
relacionadas: ["[[HU-025-auditar-cambios-de-estado]]"]
---
# HU-024 — Consultar bandeja administrativa
## Historia de usuario
**COMO** ADMIN **QUIERO** consultar solicitudes de citas y reprogramaciones pendientes con filtros **PARA** decidirlas ordenadamente.
## Alcance
- Citas especializadas `REQUESTED`, reprogramaciones `PENDING`, filtros sede/profesional/especialidad/fecha.
## Fuera de alcance
- Procesar solicitudes sin decisión explícita de las HU correspondientes.
## Reglas de negocio
- Solo ADMIN accede a la bandeja; se muestran estados pendientes definidos.
## Dependencias y relaciones
- Épica: [[EP-006-control-administrativo-y-auditoria]]; depende de [[HU-019-decidir-cita-especializada]], [[HU-022-decidir-reprogramacion]]; relacionada: [[HU-025-auditar-cambios-de-estado]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** combinación de dos clases de pendientes y filtros.
## Tareas de desarrollo
- [ ] **T-01 — Consulta ADMIN.** Componer pendientes y filtros con autorización.
- [ ] **T-02 — Contrato/paginación aplicable.** Acordar representación sin inventar datos ni filtrar indebidamente.
- [ ] **T-03 — Dashboard.** Mostrar pendientes, vacío/error y enlaces a decisión.
## Criterios de aceptación
### CA-01 — Pendientes visibles
**Dado** un ADMIN autenticado **cuando** abre la bandeja **entonces** visualiza citas especializadas `REQUESTED` y reprogramaciones `PENDING`.
### CA-02 — Filtros operativos
**Dado** solicitudes pendientes **cuando** filtra por sede, profesional, especialidad o fecha **entonces** visualiza solo los elementos que coinciden.
## Definition of Done
- [ ] CA-01/CA-02 probados con rol, estados y filtros.
- [ ] UI contempla carga/vacío/error y no expone la bandeja a otros roles.
- [ ] Contrato y trazabilidad Scrum actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- El orden y paginación quedan pendientes de contrato.
