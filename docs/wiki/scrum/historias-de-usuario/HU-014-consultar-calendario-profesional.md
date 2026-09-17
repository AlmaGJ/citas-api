---
id: HU-014
tipo: historia-de-usuario
titulo: Consultar calendario profesional
estado: Pendiente de aprobación
epica: "[[EP-003-disponibilidad-profesional]]"
esfuerzo: Bajo
sprint_sugerido: Sprint 2
dependencias: ["[[HU-012-crear-bloques-disponibilidad]]"]
relacionadas: ["[[HU-013-modificar-bloques-futuros]]"]
---
# HU-014 — Consultar calendario profesional
## Historia de usuario
**COMO** PROFESSIONAL **QUIERO** consultar mi calendario de bloques **PARA** conocer mi disponibilidad publicada.
## Alcance
- Vista de bloques propios por fecha/sede aplicable.
## Fuera de alcance
- Ver calendario de otros profesionales.
## Reglas de negocio
- Solo datos de agenda propia y sedes asignadas.
## Dependencias y relaciones
- Épica: [[EP-003-disponibilidad-profesional]]; depende de [[HU-012-crear-bloques-disponibilidad]]; relacionada: [[HU-013-modificar-bloques-futuros]].
## Esfuerzo
**Nivel:** Bajo. **Justificación:** consulta autorizada sobre bloques existentes.
## Tareas de desarrollo
- [ ] **T-01 — Consulta propia.** Exponer bloques con ownership.
- [ ] **T-02 — Calendario UI.** Representar carga, vacío, error y bloques.
## Criterios de aceptación
### CA-01 — Agenda propia
**Dado** un profesional con bloques **cuando** abre su calendario **entonces** visualiza sus bloques y sedes correspondientes.
### CA-02 — Aislamiento
**Dado** otro profesional **cuando** intenta consultar esa agenda **entonces** no obtiene sus bloques.
## Definition of Done
- [ ] CA-01/CA-02 probados con ownership.
- [ ] UI cubre loading/vacío/error y contrato está documentado.
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
- Visualización exacta depende del diseño aprobado.
