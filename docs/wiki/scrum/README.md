---
tipo: indice-scrum
estado: Pendiente de aprobación
---

# Mapa Scrum / Spec-Driven Development — Citas FCV (laboratorio)

## Propósito y límites

Mapa de capacidades derivado exclusivamente de `PRD.md`, `RESTRICCIONES_TECNICAS.md` y `database/REQUISITOS_NORMALIZACION_3FN.md`. No es una implementación ni una aprobación de alcance. Todos los datos son sintéticos; no se permiten secretos ni datos privados reales.

**Estado global:** todas las HU están en `Pendiente de aprobación`. Ninguna puede iniciar desarrollo hasta aprobación explícita de la persona revisora.

## Arquitectura y contratos condicionantes

- Backend: Java 21, Spring Boot 3.5.x, Maven, arquitectura hexagonal, MySQL 8.4, Spring Data JPA, Flyway, Spring Security y JWT access/refresh.
- Frontend: TypeScript; **React o Angular por decidir** después de Stitch/Google AI Studio. Consumirá REST directamente; no habrá Express/BFF.
- Datos: 3FN, catálogos fijos sembrados, configurables administrables, claves/cardinalidades/dependencias funcionales justificadas.
- Para cada HU cross-repo, el contrato REST debe acordarse y documentarse antes de integración; no se prescriben endpoints, DTO ni tablas en este mapa.

## Épicas

- [[EP-001-identidad-y-perfil]] — Identidad, sesión y afiliación del usuario.
- [[EP-002-catalogos-y-oferta-asistencial]] — Catálogos de soporte y profesionales habilitados.
- [[EP-003-disponibilidad-profesional]] — Publicación y consulta de agenda.
- [[EP-004-reserva-y-consulta-de-citas]] — Búsqueda, reserva y consulta de citas.
- [[EP-005-ciclo-de-vida-y-operacion]] — Decisiones, cancelación, reprogramación y atención.
- [[EP-006-control-administrativo-y-auditoria]] — Bandeja administrativa y trazabilidad inmutable.

## Incrementos / sprints sugeridos

| Incremento | Resultado funcional comprobable | HU secuenciales |
|---|---|---|
| Sprint 1 | Acceso seguro, perfil y catálogos base disponibles | [[HU-001-registrar-usuario]], [[HU-002-iniciar-y-cerrar-sesion]], [[HU-003-recuperar-contrasena]], [[HU-004-gestionar-perfil-y-afiliacion]], [[HU-005-consultar-catalogos-fijos]], [[HU-006-gestionar-eps]], [[HU-007-gestionar-planes-eps]], [[HU-008-gestionar-especialidades]] |
| Sprint 2 | Oferta profesional y agenda publicable | [[HU-009-crear-profesional]], [[HU-010-asignar-especialidades-profesional]], [[HU-011-asignar-sedes-y-activar-profesional]], [[HU-012-crear-bloques-disponibilidad]], [[HU-013-modificar-bloques-futuros]], [[HU-014-consultar-calendario-profesional]] |
| Sprint 3 | Usuario encuentra, reserva y consulta sus citas | [[HU-015-consultar-disponibilidad]], [[HU-016-reservar-cita-general]], [[HU-017-solicitar-cita-especializada]], [[HU-018-consultar-mis-citas]] |
| Sprint 4 | Operación completa del ciclo de cita | [[HU-019-decidir-cita-especializada]], [[HU-020-cancelar-cita]], [[HU-021-solicitar-reprogramacion]], [[HU-022-decidir-reprogramacion]], [[HU-023-consultar-y-cerrar-atencion]] |
| Sprint 5 | Administración operable y estados auditables | [[HU-024-consultar-bandeja-administrativa]], [[HU-025-auditar-cambios-de-estado]] |

## Decisiones e incógnitas a resolver durante la aprobación

- El contrato REST, los payloads, errores y versionado aún no están diseñados (RF-20).
- La forma concreta de garantizar atomicidad ante reservas concurrentes debe justificarse en diseño y pruebas; el PRD exige impedir doble reserva, no prescribe mecanismo.
- Deben definirse los estados terminales aplicables y la matriz exacta de transiciones, conservando las reglas del PRD.
- Debe justificarse qué información de cita se conserva como snapshot frente a FK, los índices de agenda y las dependencias funcionales para 3FN.
