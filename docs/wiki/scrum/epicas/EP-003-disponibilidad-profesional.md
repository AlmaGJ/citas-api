---
id: EP-003
tipo: epica
titulo: Disponibilidad profesional
estado: Pendiente de aprobación
historias: ["[[HU-012-crear-bloques-disponibilidad]]", "[[HU-013-modificar-bloques-futuros]]", "[[HU-014-consultar-calendario-profesional]]", "[[HU-015-consultar-disponibilidad]]"]
dependencias: ["[[EP-002-catalogos-y-oferta-asistencial]]"]
---
# EP-003 — Disponibilidad profesional
## Objetivo
Permitir publicar agenda válida y exponer solo franjas reservables.
## Valor esperado
Base verificable para reservas sin solapamientos ni citas en sedes no habilitadas.
## Actores
- PROFESSIONAL
- USER
## Alcance
- Bloques, discretización en slots y consulta filtrada de disponibilidad.
## Fuera de alcance
- Publicación de bloques pasados o modificación de bloques futuros comprometidos.
## Reglas de negocio
- Bloques no pasados, sin solape, en sede asignada; slots de 30 minutos; 60 minutos requiere dos slots consecutivos.
## Dependencias
- [[EP-002-catalogos-y-oferta-asistencial]]
## Historias de usuario
- [[HU-012-crear-bloques-disponibilidad]]
- [[HU-013-modificar-bloques-futuros]]
- [[HU-014-consultar-calendario-profesional]]
- [[HU-015-consultar-disponibilidad]]
## Criterio de completitud de la épica
- [ ] Todas sus HU tienen evidencia de validación y no se ofrece una franja incompleta.
## Riesgos e incógnitas
- Debe acordarse la representación persistente de slots sin romper 3FN.
