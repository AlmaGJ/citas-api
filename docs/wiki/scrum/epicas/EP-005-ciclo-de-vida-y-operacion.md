---
id: EP-005
tipo: epica
titulo: Ciclo de vida y operación
estado: Pendiente de aprobación
historias: ["[[HU-019-decidir-cita-especializada]]", "[[HU-020-cancelar-cita]]", "[[HU-021-solicitar-reprogramacion]]", "[[HU-022-decidir-reprogramacion]]", "[[HU-023-consultar-y-cerrar-atencion]]"]
dependencias: ["[[EP-004-reserva-y-consulta-de-citas]]"]
---
# EP-005 — Ciclo de vida y operación
## Objetivo
Gestionar de forma explícita el cambio de una cita hasta su cancelación, reprogramación o cierre asistencial.
## Valor esperado
Las reservas conservan integridad y los actores operan solo las transiciones autorizadas.
## Actores
- USER
- ADMIN
- PROFESSIONAL
## Alcance
- Decisiones, cancelación, reprogramación, agenda y cierre.
## Fuera de alcance
- Reactivación directa de citas canceladas e historia clínica.
## Reglas de negocio
- Rechazo con motivo; cancelar/rechazar libera; original no se libera mientras reprogramación pendiente; profesional solo ve sus citas.
## Dependencias
- [[EP-004-reserva-y-consulta-de-citas]]
## Historias de usuario
- [[HU-019-decidir-cita-especializada]]
- [[HU-020-cancelar-cita]]
- [[HU-021-solicitar-reprogramacion]]
- [[HU-022-decidir-reprogramacion]]
- [[HU-023-consultar-y-cerrar-atencion]]
## Criterio de completitud de la épica
- [ ] Todas las transiciones de estas HU se validan y dejan auditoría.
## Riesgos e incógnitas
- La definición operativa de “pasada/aplicable” para cierre requiere aclaración durante el diseño.
