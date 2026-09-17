---
id: HU-012
tipo: historia-de-usuario
titulo: Crear bloques de disponibilidad
estado: Pendiente de aprobación
epica: "[[EP-003-disponibilidad-profesional]]"
esfuerzo: Alto
sprint_sugerido: Sprint 2
dependencias: ["[[HU-002-iniciar-y-cerrar-sesion]]", "[[HU-011-asignar-sedes-y-activar-profesional]]"]
relacionadas: ["[[HU-013-modificar-bloques-futuros]]", "[[HU-015-consultar-disponibilidad]]"]
---
# HU-012 — Crear bloques de disponibilidad
## Historia de usuario
**COMO** PROFESSIONAL **QUIERO** crear bloques futuros por sede **PARA** publicar horarios reservables.
## Alcance
- Múltiples bloques por día, sede por bloque y discretización en slots de 30 minutos.
## Fuera de alcance
- Bloques pasados, solapados o en sedes no asignadas.
## Reglas de negocio
- No pasado; no solape del mismo profesional; sede asignada; slots de 30 min.
## Dependencias y relaciones
- Épica: [[EP-003-disponibilidad-profesional]]; depende de [[HU-002-iniciar-y-cerrar-sesion]], [[HU-011-asignar-sedes-y-activar-profesional]]; relacionadas: [[HU-013-modificar-bloques-futuros]], [[HU-015-consultar-disponibilidad]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** intervalos, autorización y base de concurrencia para reservas.
## Tareas de desarrollo
- [ ] **T-01 — Modelo de bloque/slot.** Justificar representación 3FN, índices y migración.
- [ ] **T-02 — Reglas de agenda.** Validar fecha, solape, sede y ownership del profesional.
- [ ] **T-03 — Calendario de creación.** Integrar formulario con errores y resultado visible.
## Criterios de aceptación
### CA-01 — Bloque válido
**Dado** un profesional activo con sede asignada **cuando** registra un bloque futuro no solapado **entonces** queda publicado con slots de 30 minutos.
### CA-02 — Bloque inválido
**Dado** un bloque pasado, solapado o en sede no asignada **cuando** se intenta crear **entonces** no se publica y se informa la causa.
## Definition of Done
- [ ] CA-01/CA-02 tienen pruebas de dominio/integración relevantes.
- [ ] Migración, 3FN e índices de agenda están justificados.
- [ ] UI/REST aplican rol y ownership; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- La forma de materializar slots queda abierta, pero debe impedir doble reserva.
