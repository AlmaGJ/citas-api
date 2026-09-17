---
id: HU-018
tipo: historia-de-usuario
titulo: Consultar mis citas
estado: Pendiente de aprobación
epica: "[[EP-004-reserva-y-consulta-de-citas]]"
esfuerzo: Medio
sprint_sugerido: Sprint 3
dependencias: ["[[HU-016-reservar-cita-general]]", "[[HU-017-solicitar-cita-especializada]]"]
relacionadas: ["[[HU-020-cancelar-cita]]", "[[HU-021-solicitar-reprogramacion]]"]
---
# HU-018 — Consultar mis citas
## Historia de usuario
**COMO** USER **QUIERO** consultar y filtrar mis citas **PARA** conocer su programación y estado.
## Alcance
- Lista/detalle propio con filtros estado/fecha y sede, profesional, especialidad, fecha/hora, duración, estado y motivo de rechazo.
## Fuera de alcance
- Consultar citas de otros usuarios.
## Reglas de negocio
- Ownership obligatorio; el motivo se muestra cuando exista.
## Dependencias y relaciones
- Épica: [[EP-004-reserva-y-consulta-de-citas]]; depende de [[HU-016-reservar-cita-general]], [[HU-017-solicitar-cita-especializada]]; relacionadas: [[HU-020-cancelar-cita]], [[HU-021-solicitar-reprogramacion]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** proyección de datos, filtros y aislamiento.
## Tareas de desarrollo
- [ ] **T-01 — Consulta con ownership.** Filtrar/proyectar solo citas propias.
- [ ] **T-02 — Contrato de detalle.** Incluir campos mínimos y motivo condicionado.
- [ ] **T-03 — Pantallas.** Implementar lista/detalle y estados loading/vacío/error.
## Criterios de aceptación
### CA-01 — Información mínima
**Dado** citas del USER **cuando** consulta “Mis citas” **entonces** ve sede, profesional, especialidad, fecha/hora, duración, estado y motivo de rechazo cuando exista.
### CA-02 — Filtro y aislamiento
**Dado** filtros por estado o fecha **cuando** los aplica **entonces** obtiene solo sus citas coincidentes y nunca las de otro USER.
## Definition of Done
- [ ] CA-01/CA-02 validados con pruebas de filtro/ownership.
- [ ] UI accesible refleja estados definidos y contrato directo REST.
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
- No sustituye el historial de auditoría administrativo.
