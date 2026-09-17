---
id: HU-007
tipo: historia-de-usuario
titulo: Gestionar planes de EPS
estado: Pendiente de aprobación
epica: "[[EP-002-catalogos-y-oferta-asistencial]]"
esfuerzo: Medio
sprint_sugerido: Sprint 1
dependencias: ["[[HU-006-gestionar-eps]]"]
relacionadas: ["[[HU-004-gestionar-perfil-y-afiliacion]]"]
---
# HU-007 — Gestionar planes de EPS
## Historia de usuario
**COMO** ADMIN **QUIERO** administrar los planes asociados a cada EPS **PARA** que el usuario solo pueda afiliarse a combinaciones válidas.
## Alcance
- CRUD de planes por EPS y activación/desactivación.
## Fuera de alcance
- Borrado físico de planes referenciados.
## Reglas de negocio
- Plan depende de EPS; no se duplican nombres de plan en la afiliación.
## Dependencias y relaciones
- Épica: [[EP-002-catalogos-y-oferta-asistencial]]; depende de [[HU-006-gestionar-eps]]; relacionada: [[HU-004-gestionar-perfil-y-afiliacion]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** relación catálogo-padre e integridad de afiliación.
## Tareas de desarrollo
- [ ] **T-01 — Relación normalizada.** Modelar plan-EPS y migración aplicable.
- [ ] **T-02 — Casos ADMIN.** Validar EPS existente/activa y referencia de planes.
- [ ] **T-03 — UI dependiente.** Filtrar planes por EPS y comunicar combinaciones no disponibles.
## Criterios de aceptación
### CA-01 — Plan vinculado
**Dado** una EPS válida **cuando** ADMIN crea o actualiza un plan **entonces** el plan queda asociado a esa EPS y es consultable en su contexto.
### CA-02 — Integridad y retiro
**Dado** un plan referenciado **cuando** ADMIN intenta eliminarlo **entonces** se conserva la referencia y se aplica desactivación si corresponde.
## Definition of Done
- [ ] CA-01/CA-02 validados con pruebas de relación e integridad.
- [ ] Esquema/migración cumple 3FN; no se replica EPS/plan en afiliación.
- [ ] Contrato e interfaz administrable documentados; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- La política exacta de activación de padre/hijo se decidirá en diseño.
