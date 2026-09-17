---
id: EP-002
tipo: epica
titulo: Catálogos y oferta asistencial
estado: Pendiente de aprobación
historias: ["[[HU-005-consultar-catalogos-fijos]]", "[[HU-006-gestionar-eps]]", "[[HU-007-gestionar-planes-eps]]", "[[HU-008-gestionar-especialidades]]", "[[HU-009-crear-profesional]]", "[[HU-010-asignar-especialidades-profesional]]", "[[HU-011-asignar-sedes-y-activar-profesional]]"]
dependencias: ["[[EP-001-identidad-y-perfil]]"]
---
# EP-002 — Catálogos y oferta asistencial
## Objetivo
Administrar catálogos y configurar profesionales sintéticos aptos para publicar atención.
## Valor esperado
Oferta consistente que soporta filtros, disponibilidad y reserva sin duplicaciones ni referencias inválidas.
## Actores
- ADMIN
## Alcance
- Catálogos fijos/configurables, profesionales, especialidades y sedes.
## Fuera de alcance
- Borrado físico de catálogos referenciados y datos reales de FCV.
## Reglas de negocio
- Catálogos fijos solo lectura; configurables desactivables; especialidad 30/60 min; profesional N:M con especialidades y sedes.
## Dependencias
- [[EP-001-identidad-y-perfil]]
## Historias de usuario
- [[HU-005-consultar-catalogos-fijos]]
- [[HU-006-gestionar-eps]]
- [[HU-007-gestionar-planes-eps]]
- [[HU-008-gestionar-especialidades]]
- [[HU-009-crear-profesional]]
- [[HU-010-asignar-especialidades-profesional]]
- [[HU-011-asignar-sedes-y-activar-profesional]]
## Criterio de completitud de la épica
- [ ] Sus HU están completadas y los catálogos/modelo cumplen 3FN.
## Riesgos e incógnitas
- Criterio de activación/desactivación y restricciones de referencia a concretar en contrato de datos.
