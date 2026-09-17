---
id: HU-004
tipo: historia-de-usuario
titulo: Gestionar perfil y afiliación
estado: Pendiente de aprobación
epica: "[[EP-001-identidad-y-perfil]]"
esfuerzo: Medio
sprint_sugerido: Sprint 1
dependencias: ["[[HU-002-iniciar-y-cerrar-sesion]]", "[[HU-005-consultar-catalogos-fijos]]", "[[HU-006-gestionar-eps]]", "[[HU-007-gestionar-planes-eps]]"]
relacionadas: []
---
# HU-004 — Gestionar perfil y afiliación
## Historia de usuario
**COMO** USER autenticado **QUIERO** consultar y actualizar mi perfil y afiliación **PARA** mantener datos permitidos y consistentes para mi atención.
## Alcance
- Consulta/edición autorizada de datos permitidos y asociación EPS/plan/régimen.
## Fuera de alcance
- Cambio de datos ajenos o duplicación de nombres de catálogos en usuario.
## Reglas de negocio
- Ownership obligatorio; no duplicar EPS, régimen y plan en la entidad de usuario.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-perfil]]; depende de [[HU-002-iniciar-y-cerrar-sesion]], [[HU-005-consultar-catalogos-fijos]], [[HU-006-gestionar-eps]], [[HU-007-gestionar-planes-eps]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** ownership, relaciones normalizadas y formulario integrado.
## Tareas de desarrollo
- [ ] **T-01 — Modelo/contrato de perfil.** Separar atributos permitidos y afiliación por FK.
- [ ] **T-02 — Casos de uso autorizados.** Aplicar validación y ownership.
- [ ] **T-03 — Vista de perfil.** Mostrar/editar estados loading, error, éxito y campos deshabilitados aplicables.
## Criterios de aceptación
### CA-01 — Perfil propio
**Dado** un USER autenticado **cuando** consulta o actualiza datos permitidos **entonces** solo opera su perfil y ve el resultado persistido.
### CA-02 — Afiliación normalizada
**Dado** catálogos válidos **cuando** asocia EPS, plan y régimen **entonces** la afiliación referencia catálogos sin repetir sus nombres ni aceptar combinaciones inválidas.
## Definition of Done
- [ ] CA-01/CA-02 validados con pruebas de ownership y relaciones.
- [ ] Esquema 3FN, índices/reglas aplicables y migración Flyway están justificados.
- [ ] UI y contrato REST reflejan errores seguros; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- La cardinalidad exacta de afiliación se justificará en el modelo 3FN.
