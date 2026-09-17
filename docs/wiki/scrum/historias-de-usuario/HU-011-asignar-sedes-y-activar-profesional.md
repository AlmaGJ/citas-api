---
id: HU-011
tipo: historia-de-usuario
titulo: Asignar sedes y activar profesional
estado: Pendiente de aprobación
epica: "[[EP-002-catalogos-y-oferta-asistencial]]"
esfuerzo: Medio
sprint_sugerido: Sprint 2
dependencias: ["[[HU-005-consultar-catalogos-fijos]]", "[[HU-009-crear-profesional]]"]
relacionadas: ["[[HU-012-crear-bloques-disponibilidad]]"]
---
# HU-011 — Asignar sedes y activar profesional
## Historia de usuario
**COMO** ADMIN **QUIERO** asignar una o ambas sedes y activar/desactivar a un profesional **PARA** controlar dónde y cuándo puede publicar atención.
## Alcance
- Relación N:M profesional-sede y estado activo.
## Fuera de alcance
- Alterar el catálogo fijo de sedes.
## Reglas de negocio
- Un profesional solo publica bloques en sedes asignadas; desactivación controla disponibilidad futura.
## Dependencias y relaciones
- Épica: [[EP-002-catalogos-y-oferta-asistencial]]; depende de [[HU-005-consultar-catalogos-fijos]], [[HU-009-crear-profesional]]; relacionada: [[HU-012-crear-bloques-disponibilidad]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** N:M y consecuencias sobre agenda.
## Tareas de desarrollo
- [ ] **T-01 — Relación normalizada.** Modelar asignaciones de sede y estado.
- [ ] **T-02 — Administración protegida.** Validar rol, sedes fijas y estado.
- [ ] **T-03 — UI de asignación.** Mostrar ambas sedes y estado sin permitir combinaciones inválidas.
## Criterios de aceptación
### CA-01 — Sedes autorizadas
**Dado** un profesional creado **cuando** ADMIN le asigna una o ambas sedes **entonces** las asignaciones quedan disponibles para validar su agenda.
### CA-02 — Activación operativa
**Dado** un profesional desactivado **cuando** se intenta habilitar nueva oferta para él **entonces** la operación se rechaza según las reglas de disponibilidad.
## Definition of Done
- [ ] CA-01/CA-02 probados con rol e integridad de relación.
- [ ] Migración/modelo 3FN y validación de disponibilidad están conectados.
- [ ] UI/API y trazabilidad actualizadas.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- El efecto de desactivar con citas existentes requiere decisión operativa explícita.
