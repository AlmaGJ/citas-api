---
id: HU-005
tipo: historia-de-usuario
titulo: Consultar catálogos fijos
estado: Pendiente de aprobación
epica: "[[EP-002-catalogos-y-oferta-asistencial]]"
esfuerzo: Medio
sprint_sugerido: Sprint 1
dependencias: []
relacionadas: ["[[HU-004-gestionar-perfil-y-afiliacion]]"]
---
# HU-005 — Consultar catálogos fijos
## Historia de usuario
**COMO** usuario de la aplicación **QUIERO** consultar los catálogos fijos habilitados **PARA** seleccionar valores consistentes en las funcionalidades aplicables.
## Alcance
- Roles, estados de cita, estados de reprogramación, regímenes y dos sedes del laboratorio, precargados y solo lectura.
## Fuera de alcance
- CRUD administrativo sobre estos catálogos.
## Reglas de negocio
- Son catálogos fijos sembrados; HIC e ICV se mantienen como sedes fijas del laboratorio.
## Dependencias y relaciones
- Épica: [[EP-002-catalogos-y-oferta-asistencial]]; relacionada: [[HU-004-gestionar-perfil-y-afiliacion]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** seeds, modelo 3FN y contrato reutilizable.
## Tareas de desarrollo
- [ ] **T-01 — Modelar catálogos.** Definir claves, cardinalidades y datos seed sintéticos/fijos.
- [ ] **T-02 — Exponer consulta.** Autorizar y devolver solo los datos necesarios.
- [ ] **T-03 — Consumir en UI.** Mostrar opciones y estados de carga/error aplicables.
## Criterios de aceptación
### CA-01 — Disponibilidad de catálogo
**Dado** la aplicación inicializada **cuando** una función necesita un catálogo fijo **entonces** encuentra roles, estados, regímenes y las dos sedes precargados.
### CA-02 — Solo lectura
**Dado** un ADMIN **cuando** intenta cambiar un catálogo fijo mediante la funcionalidad de catálogos **entonces** no existe operación de edición o borrado para ese catálogo.
## Definition of Done
- [ ] CA-01/CA-02 comprobados y seeds reproducibles mediante migración Flyway aplicable.
- [ ] Catálogos cumplen 3FN y no contienen datos privados reales.
- [ ] Contrato/consumidores documentados y trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- Direcciones de sedes se usan únicamente como catálogo del laboratorio según PRD.
