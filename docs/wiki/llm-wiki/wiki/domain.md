# Dominio

Actores: `USER`, `PROFESSIONAL` y `ADMIN`. El PRD cubre registro, login/refresh/logout, recuperación de contraseña, perfil y afiliación, catálogos, profesionales, disponibilidad, citas generales y especializadas, cancelación, reprogramación, agenda profesional, cierre de atención, bandeja administrativa y auditoría.

Reglas críticas: no doble reserva; citas generales aprobadas automáticamente; citas especializadas requieren administración; rechazo exige motivo; duración de 30/60 minutos requiere slots consecutivos; no se permiten bloques ni citas en el pasado; cancelar o rechazar libera reservas; reprogramar conserva la cita original hasta decisión administrativa.

La normalización mínima requerida es 3FN. Las relaciones N:M y los estados deben modelarse explícitamente, sin listas ni textos duplicados de catálogos.
