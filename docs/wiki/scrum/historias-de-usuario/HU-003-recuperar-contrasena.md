---
id: HU-003
tipo: historia-de-usuario
titulo: Recuperar contraseña
estado: Pendiente de aprobación
epica: "[[EP-001-identidad-y-perfil]]"
esfuerzo: Medio
sprint_sugerido: Sprint 1
dependencias: ["[[HU-001-registrar-usuario]]"]
relacionadas: ["[[HU-002-iniciar-y-cerrar-sesion]]"]
---
# HU-003 — Recuperar contraseña
## Historia de usuario
**COMO** usuario **QUIERO** solicitar y completar el cambio de mi contraseña **PARA** recuperar acceso de forma segura.
## Alcance
- Solicitud por email, token temporal de un uso y cambio de contraseña; canal controlado de desarrollo opcional.
## Fuera de alcance
- SMTP obligatorio.
## Reglas de negocio
- El token es temporal, de un uso; cambiar contraseña lo consume/invalida.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-perfil]]; depende de [[HU-001-registrar-usuario]]; relacionada: [[HU-002-iniciar-y-cerrar-sesion]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** tokens sensibles y dos flujos coordinados.
## Tareas de desarrollo
- [ ] **T-01 — Solicitud segura.** Generar/almacenar token temporal sin filtrarlo indebidamente.
- [ ] **T-02 — Restablecimiento.** Validar token y actualizar hash de contraseña.
- [ ] **T-03 — Flujos de UI.** Presentar solicitud y cambio con errores no reveladores.
## Criterios de aceptación
### CA-01 — Solicitud controlada
**Dado** un email registrado **cuando** se solicita recuperación **entonces** se habilita un token temporal de uso único por un canal seguro/controlado.
### CA-02 — Consumo único
**Dado** un token vigente **cuando** se define una nueva contraseña válida **entonces** se actualiza la contraseña y el token deja de ser utilizable; token inválido/consumido no cambia nada.
## Definition of Done
- [ ] CA-01 y CA-02 tienen evidencia de pruebas relevantes.
- [ ] Token, contraseña y respuestas no se registran ni exponen de forma insegura.
- [ ] Persistencia/migración y contrato aplicables están documentados; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- El medio real de envío queda fuera de alcance obligatorio.
