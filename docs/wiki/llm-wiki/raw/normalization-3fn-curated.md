# Snapshot curado — Normalización 3FN

Fuente canónica: `database/REQUISITOS_NORMALIZACION_3FN.md` (SRC-DB-001).

El modelo debe soportar usuarios y roles, profesionales, especialidades/sedes N:M, EPS/régimen/plan/afiliación, disponibilidad, citas de 30/60 minutos, estados, auditoría, reprogramaciones y tokens.

1FN exige atributos atómicos y sin listas. 2FN exige dependencia de la clave completa en claves compuestas y tablas puente para N:M. 3FN prohíbe dependencias transitivas y repetición de nombres de catálogos dentro de usuarios, profesionales o citas.

Deben justificarse claves, cardinalidades, doble reserva, slots de 60 minutos, conservación de la cita original durante una reprogramación, auditoría, snapshots/FK e índices de agenda.
