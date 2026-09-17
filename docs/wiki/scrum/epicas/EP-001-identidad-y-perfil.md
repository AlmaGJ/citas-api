---
id: EP-001
tipo: epica
titulo: Identidad y perfil
estado: Pendiente de aprobación
historias: ["[[HU-001-registrar-usuario]]", "[[HU-002-iniciar-y-cerrar-sesion]]", "[[HU-003-recuperar-contrasena]]", "[[HU-004-gestionar-perfil-y-afiliacion]]"]
dependencias: []
---
# EP-001 — Identidad y perfil
## Objetivo
Permitir que un usuario ficticio se registre, se autentique, recupere acceso y mantenga su perfil y afiliación.
## Valor esperado
Acceso seguro y datos personales mínimos confiables para la gestión de citas.
## Actores
- USER
## Alcance
- Registro, JWT access/refresh, logout, recuperación, perfil y afiliación.
## Fuera de alcance
- Integración SMTP obligatoria, datos reales y gestión de roles por el usuario.
## Reglas de negocio
- Email y documento únicos; contraseña con hash adaptativo; refresh separado; afiliación sin duplicar EPS/régimen/plan.
## Dependencias
- Ninguna.
## Historias de usuario
- [[HU-001-registrar-usuario]]
- [[HU-002-iniciar-y-cerrar-sesion]]
- [[HU-003-recuperar-contrasena]]
- [[HU-004-gestionar-perfil-y-afiliacion]]
## Criterio de completitud de la épica
- [ ] Todas sus HU están `Completada` con evidencia.
- [ ] Las rutas y datos sensibles aplican autorización, ownership y no exponen secretos.
## Riesgos e incógnitas
- Política concreta de caducidad JWT y canal de recuperación por acordar sin contradecir el PRD.
