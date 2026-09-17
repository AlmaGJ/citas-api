---
id: HU-002
tipo: historia-de-usuario
titulo: Iniciar y cerrar sesión
estado: Pendiente de aprobación
epica: "[[EP-001-identidad-y-perfil]]"
esfuerzo: Alto
sprint_sugerido: Sprint 1
dependencias: ["[[HU-001-registrar-usuario]]"]
relacionadas: ["[[HU-003-recuperar-contrasena]]"]
---
# HU-002 — Iniciar y cerrar sesión
## Historia de usuario
**COMO** usuario autenticable **QUIERO** iniciar, renovar y cerrar mi sesión **PARA** acceder de forma segura a las funciones de mi rol.
## Alcance
- Login por email/contraseña, access/refresh JWT separados, refresh, logout y rutas protegidas.
## Fuera de alcance
- Login social y gestión manual de permisos.
## Reglas de negocio
- Roles en contexto de autorización; refresh revocable; no registrar tokens/contraseñas.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-perfil]]; depende de [[HU-001-registrar-usuario]]; relacionada: [[HU-003-recuperar-contrasena]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** seguridad transversal backend/frontend, autorización y revocación.
## Tareas de desarrollo
- [ ] **T-01 — Contrato de sesión.** Diseñar flujos login/refresh/logout y errores seguros.
- [ ] **T-02 — Seguridad backend.** Implementar autenticación, emisión/revocación y autorización por rol.
- [ ] **T-03 — Cliente autenticado.** Gestionar sesión, renovación y cierre sin hardcodear tokens.
## Criterios de aceptación
### CA-01 — Login y autorización
**Dado** credenciales válidas **cuando** se inicia sesión **entonces** se obtiene contexto autorizado con access y refresh separados; credenciales inválidas no dan acceso.
### CA-02 — Renovación y logout
**Dado** una sesión vigente **cuando** se renueva o cierra **entonces** el refresh permite renovar según política y, tras logout, no permite nuevas renovaciones.
## Definition of Done
- [ ] CA-01 y CA-02 se prueban en API y en rutas/UI aplicables.
- [ ] Configuración usa variables de entorno, CORS explícito y nunca expone tokens o contraseñas.
- [ ] Roles y ownership se comprueban en recursos protegidos relevantes.
- [ ] Contrato y trazabilidad Scrum actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- Duración concreta de tokens queda pendiente de definición segura.
