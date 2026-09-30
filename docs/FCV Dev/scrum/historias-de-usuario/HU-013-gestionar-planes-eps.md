---
id: HU-013
tipo: historia-de-usuario
titulo: "Gestionar planes de EPS"
estado: Implementada
epica: "[[EP-003-administracion-de-catalogos-y-profesionales]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 3"
dependencias: ["[[HU-012-gestionar-eps]]"]
relacionadas: ["[[HU-011-gestionar-afiliacion]]"]
---
# HU-013 — Gestionar planes de EPS
## Historia de usuario
**COMO** ADMIN  
**QUIERO** gestionar planes asociados a una EPS  
**PARA** que USER seleccione combinaciones de afiliación válidas.
## Contexto y descripción
El plan depende de EPS y tampoco se borra físicamente si está referenciado.
## Alcance
- CRUD lógico de planes, asociación a EPS y filtros de planes activos aplicables.
## Fuera de alcance
- Plan sin EPS, borrado físico referenciado o reglas comerciales reales.
## Reglas de negocio
- Un plan se relaciona con EPS; las referencias mantienen integridad.
## Dependencias y relaciones
- Épica: [[EP-003-administracion-de-catalogos-y-profesionales]]
- Dependencias: [[HU-012-gestionar-eps]].
- Relacionadas: [[HU-011-gestionar-afiliacion]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** exige integridad referencial y gestión administrativa segura.
## Tareas de desarrollo
- [ ] **T-01 — Definir contrato y relación plan-EPS.** Dificultad: Medio. Validar EPS existente/activa según regla aprobada.
- [ ] **T-02 — Implementar baja lógica.** Dificultad: Medio. Preservar planes referenciados.
- [ ] **T-03 — Entregar UI/pruebas.** Dificultad: Medio. Cubrir relación inválida, rol y consulta.
## Criterios de aceptación
### CA-01 — Plan asociado
**Dado** una EPS válida, **cuando** ADMIN crea o actualiza un plan, **entonces** el plan queda asociado a esa EPS.
### CA-02 — Integridad de catálogo
**Dado** un plan referenciado, **cuando** ADMIN intenta eliminarlo, **entonces** se conserva y se permite desactivación cuando aplique.
### CA-03 — Selección consistente
**Dado** un consumidor de afiliación, **cuando** consulta planes de una EPS, **entonces** solo puede seleccionar planes válidos de ella conforme al contrato.
## Definition of Done
- [ ] CA-01 a CA-03 probados en persistencia/REST y cliente aplicable.
- [ ] Relación 3FN, migración aplicable y autorización ADMIN verificadas.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | PASS | Smoke REST 2026-09-29: `POST /api/v1/admin/eps/{epsId}/plans` crea el plan asociado a esa EPS; `regimeId` obtenido de `GET /api/v1/admin/insurance-regimes`. | — |
| CA-02 | PASS | Unicidad `(eps_id, code)` ya existente en BD mapeada a `409`; `POST /{id}/deactivate` conserva el plan sin borrarlo. | — |
| CA-03 | PASS | `GET /api/v1/catalogs/active-plans` (ya existente, usado por registro USER) sigue filtrando solo planes/EPS activos; no se tocó ese contrato. | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-09-29 — Implementada: `InsurancePlanEntity` ampliada con `epsId/regimeId/code/name`, `Ports.Insurance` ampliado, endpoints bajo `AdminInsuranceController`. Verificada con smoke REST y navegador integrado (`EpsPlansManager.tsx`). Estado pasa a `Implementada`.
## Notas y decisiones
- No se presupone un atributo comercial para el plan.
