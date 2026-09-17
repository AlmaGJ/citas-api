---
id: EP-004
tipo: epica
titulo: Reserva y consulta de citas
estado: Pendiente de aprobación
historias: ["[[HU-016-reservar-cita-general]]", "[[HU-017-solicitar-cita-especializada]]", "[[HU-018-consultar-mis-citas]]"]
dependencias: ["[[EP-003-disponibilidad-profesional]]"]
---
# EP-004 — Reserva y consulta de citas
## Objetivo
Permitir reservar atención general o solicitar especializada y consultar su resultado.
## Valor esperado
El USER obtiene una reserva fiable, visible y ajustada a la modalidad de atención.
## Actores
- USER
## Alcance
- Reserva general automática, solicitud especializada retenida y listado/detalle del usuario.
## Fuera de alcance
- Decisión administrativa y reprogramación, tratadas en [[EP-005-ciclo-de-vida-y-operacion]].
## Reglas de negocio
- Sin doble reserva; general `APPROVED`; especializada `REQUESTED`; especialidad activa y asignada al profesional.
## Dependencias
- [[EP-003-disponibilidad-profesional]]
## Historias de usuario
- [[HU-016-reservar-cita-general]]
- [[HU-017-solicitar-cita-especializada]]
- [[HU-018-consultar-mis-citas]]
## Criterio de completitud de la épica
- [ ] Sus HU están completadas y el usuario solo accede a sus propias citas.
## Riesgos e incógnitas
- El control concurrente de reserva requiere evidencia específica en S3.
