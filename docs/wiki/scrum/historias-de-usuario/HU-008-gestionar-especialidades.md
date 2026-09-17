---
id: HU-008
tipo: historia-de-usuario
titulo: Gestionar especialidades
estado: Pendiente de aprobación
epica: "[[EP-002-catalogos-y-oferta-asistencial]]"
esfuerzo: Medio
sprint_sugerido: Sprint 1
dependencias: ["[[HU-002-iniciar-y-cerrar-sesion]]"]
relacionadas: ["[[HU-010-asignar-especialidades-profesional]]", "[[HU-015-consultar-disponibilidad]]"]
---
# HU-008 — Gestionar especialidades
## Historia de usuario
**COMO** ADMIN **QUIERO** administrar especialidades y su duración de atención **PARA** configurar reservas válidas de 30 o 60 minutos.
## Alcance
- CRUD, activación/desactivación y duración 30/60 minutos.
## Fuera de alcance
- Que el profesional cambie la duración.
## Reglas de negocio
- Duración exclusivamente 30 o 60; especialidad inactiva no se reserva; referencia no se borra físicamente.
## Dependencias y relaciones
- Épica: [[EP-002-catalogos-y-oferta-asistencial]]; depende de [[HU-002-iniciar-y-cerrar-sesion]]; relacionadas: [[HU-010-asignar-especialidades-profesional]], [[HU-015-consultar-disponibilidad]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** regla de duración impacta disponibilidad y reserva.
## Tareas de desarrollo
- [ ] **T-01 — Catálogo normalizado.** Definir restricción de duración y estado.
- [ ] **T-02 — Administración protegida.** Gestionar especialidad con integridad referencial.
- [ ] **T-03 — UI/contrato.** Presentar duración y estado en flujos ADMIN/consulta.
## Criterios de aceptación
### CA-01 — Configuración limitada
**Dado** un ADMIN **cuando** crea o modifica una especialidad **entonces** solo puede seleccionar duración de 30 o 60 minutos.
### CA-02 — Disponibilidad de reserva
**Dado** una especialidad inactiva o referenciada **cuando** se retira de operación **entonces** no se ofrece para nuevas reservas y no se destruyen referencias.
## Definition of Done
- [ ] CA-01/CA-02 con pruebas de validación/rol/referencia.
- [ ] Migración y modelo 3FN coherentes; contrato se consume sin duplicar reglas sensibles.
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
- Medicina General deberá estar identificable por el catálogo, sin inventar código concreto.
