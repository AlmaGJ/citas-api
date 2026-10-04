# Registro de decisiones

## DECISIÓN — 2026-09-17

La raíz no se convierte en un tercer repositorio. La única LLM Wiki se versiona dentro de `citas-api/docs/FCV Dev/llm-wiki/`.

## DECISIÓN — 2026-09-17 · Identidad backend

El usuario aprobó el incremento mínimo de HU-001/002/004, el seed parcial de HU-003 y el backend completo de HU-005/006/007. Email se compara sin distinguir mayúsculas y documento por tipo+número. El refresh va en cookie HttpOnly para sitios distintos y rota en cada uso. Logout revoca el refresh de esa sesión. Las obligaciones de interfaz se trasladan a HU-033.

## PREGUNTA ABIERTA

No se han aprobado todavía estados exhaustivos de citas, contratos de las demás HU, zona horaria ni estrategia de reserva concurrente.

## DECISIÓN — 2026-09-22 · Catálogo de subagentes

Los ocho subagentes especializados se mantienen como archivos Markdown versionados en `docs/FCV Dev/subagents/`. El orquestador selecciona el perfil más específico, separa implementación de verificación y conserva la responsabilidad de coordinar cambios cross-repo y actualizar la Wiki.

## HECHO — 2026-09-22 · Frontend

React es el framework detectado en `citas-web`; deja de ser una pregunta abierta. La aprobación visual y la verificación del incremento auth continúan pendientes de evidencia.

## DECISIÓN — 2026-10-04 · Automatizaciones n8n (WF-001, WF-002, WF-003)

- WF-002 (notificaciones) queda diseñado para el contrato real del emisor; notifica cancelaciones, rechazos/aprobaciones de especialidad y reprogramaciones aprobadas/rechazadas. Se añadió el evento `AppointmentRescheduleDecided` porque la reprogramación no emitía webhook.
- WF-001 y WF-003 dependen de una URL pública de citas-api. Hasta tenerla, se dejan inactivos. Los tres workflows quedan inactivos según el plan.
- La deduplicación usa Data Tables de n8n (`wf001_sent_reminders`, `wf002_processed_events`), no static data, porque static data no persiste en ejecuciones manuales.
- Brechas del plan: B1 (correo del paciente no expuesto) resuelta con buzón de laboratorio; B2 refutada, el historial no guarda motivo de reprogramación y el evento nuevo lo reemplaza; B3 resuelta con `AppointmentRescheduleDecided`; B4 WF-003 reporta "no disponible por contrato" para COMPLETED/NO_SHOW/CANCELLED; B5 abierta (URL pública estable pendiente); B6 parámetros propuestos (ventana 24 h, resumen 06:00 Bogotá) pendientes de aprobación.
- Riesgos residuales: token del webhook en `.env` y en la credencial de n8n; el buzón de laboratorio recibe correos reales de prueba; el túnel temporal expone la API mientras esté activo.
