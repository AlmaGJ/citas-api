# Snapshot curado — PRD 1.0

Fuente canónica: `PRD.md` (SRC-PRD-001).

## Alcance admitido

Sistema ficticio de agendamiento para usuarios, profesionales y administradores. Incluye registro, JWT access/refresh, recuperación de contraseña, perfil/afiliación, catálogos, profesionales, disponibilidad, citas generales y especializadas, cancelación, reprogramación, agenda profesional, cierre de atención, bandeja administrativa y auditoría.

## Reglas invariantes

- No se permite doble reserva.
- Citas generales se aprueban automáticamente.
- Citas especializadas requieren aprobación administrativa.
- Un rechazo administrativo exige motivo.
- Una duración de 60 minutos requiere dos slots consecutivos.
- No se crean citas ni bloques en el pasado.
- Cancelar o rechazar libera las reservas correspondientes.
- Una reprogramación conserva la cita original hasta la decisión administrativa.

## Límites

No incluye historia clínica, facturación, pagos, diagnóstico, sistemas clínicos reales, SMS/WhatsApp ni SMTP obligatorio. Los datos de pacientes y profesionales son sintéticos.
