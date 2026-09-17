---
id: EP-006
tipo: epica
titulo: Control administrativo y auditoría
estado: Pendiente de aprobación
historias: ["[[HU-024-consultar-bandeja-administrativa]]", "[[HU-025-auditar-cambios-de-estado]]"]
dependencias: ["[[EP-005-ciclo-de-vida-y-operacion]]"]
---
# EP-006 — Control administrativo y auditoría
## Objetivo
Dar al ADMIN visibilidad operativa y trazabilidad no editable de estados.
## Valor esperado
Decisiones administrables y verificables sobre solicitudes y cambios de ciclo de vida.
## Actores
- ADMIN
## Alcance
- Bandeja filtrable y consulta de historial.
## Fuera de alcance
- CRUD normal sobre registros de auditoría.
## Reglas de negocio
- Auditoría guarda cita, estado, actor cuando exista, fuente, fecha/hora y motivo opcional; el historial no se modifica como CRUD.
## Dependencias
- [[EP-005-ciclo-de-vida-y-operacion]]
## Historias de usuario
- [[HU-024-consultar-bandeja-administrativa]]
- [[HU-025-auditar-cambios-de-estado]]
## Criterio de completitud de la épica
- [ ] Las HU están completadas y cada transición cubierta resulta trazable.
## Riesgos e incógnitas
- Definir presentación de la auditoría sin exponer información fuera de la autorización.
