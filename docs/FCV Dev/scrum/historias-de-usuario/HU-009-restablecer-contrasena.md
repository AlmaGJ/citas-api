---
id: HU-009
tipo: historia-de-usuario
titulo: "Restablecer contraseña"
estado: Implementada
epica: "[[EP-002-identidad-y-perfil-del-usuario]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias: ["[[HU-008-solicitar-recuperacion-de-contrasena]]"]
relacionadas: ["[[HU-007-renovar-y-cerrar-sesion]]"]
---
# HU-009 — Restablecer contraseña
## Historia de usuario
**COMO** usuario con token de recuperación válido  
**QUIERO** definir una nueva contraseña  
**PARA** recuperar mi acceso sin reutilizar el token.
## Contexto y descripción
Cambiar contraseña consume/invalida el token de recuperación.
## Alcance
- Validar token, cambiar hash, consumir token y actualizar sesión según política aprobada.
## Fuera de alcance
- Cambio de contraseña dentro de perfil o envío SMTP obligatorio.
## Reglas de negocio
- Token temporal de único uso; contraseña nunca se persiste en texto plano.
## Dependencias y relaciones
- Épica: [[EP-002-identidad-y-perfil-del-usuario]]
- Dependencias: [[HU-008-solicitar-recuperacion-de-contrasena]].
- Relacionadas: [[HU-007-renovar-y-cerrar-sesion]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** altera credenciales y debe impedir reutilización y sesiones indebidas.
## Tareas de desarrollo
- [ ] **T-01 — Validar/consumir token.** Dificultad: Alto. Aplicar vigencia y uso único de forma atómica.
- [ ] **T-02 — Actualizar contraseña segura.** Dificultad: Alto. Aplicar hash adaptativo y política de sesión aprobada.
- [ ] **T-03 — Integrar formulario y pruebas.** Dificultad: Medio. Validar nueva contraseña, token inválido y reutilización.
## Criterios de aceptación
### CA-01 — Restablecimiento válido
**Dado** un token vigente no usado y una contraseña válida, **cuando** se confirma el cambio, **entonces** la nueva contraseña permite autenticación posterior.
### CA-02 — Consumo del token
**Dado** un token usado, vencido o inválido, **cuando** se intenta restablecer, **entonces** se rechaza y no cambia la contraseña.
### CA-03 — Seguridad posterior
**Dado** una contraseña cambiada, **cuando** se revisa la persistencia y sesiones afectadas, **entonces** no hay texto plano y se aplica la invalidez de sesión definida en el contrato.
## Definition of Done
- [ ] CA-01 a CA-03 tienen pruebas de dominio/seguridad y REST aplicable.
- [ ] Token consumido y hash adaptativo verificados; migración si corresponde.
- [ ] Cliente y trazabilidad Scrum actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | PASS | Navegador integrado 2026-09-29: `ResetPasswordScreen` con token válido → `204`, pantalla "Contraseña actualizada", login posterior exitoso con la nueva contraseña. | — |
| CA-02 | PASS | `POST /api/v1/auth/password-reset` ya rechazaba token usado/vencido/inexistente con `400`; frontend muestra el error genérico vía `apiErrorMessage`. | — |
| CA-03 / DoD | PASS | Contraseña se persiste solo como hash (`passwords.hash`); `refresh_tokens` del usuario se revocan al cambiar contraseña (comportamiento backend ya existente). Frontend: `ResetPasswordScreen.tsx` nuevo, deep-link `?token=` cableado en `App.tsx`, verificado con Vitest y navegador integrado. | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-09-29 — Implementada la pantalla frontend (backend ya existía); verificada con pruebas Vitest y navegador integrado contra Docker, incluido login posterior con la nueva contraseña. Estado pasa a `Implementada`.
## Notas y decisiones
- La política de sesiones posteriores (revocar refresh tokens) ya estaba implementada en el backend y se confirma sin cambios.
