# Trazabilidad

## HECHO

Las sesiones S2-S6 requieren commits y evidencias específicas. El backend y frontend deben mantener historial trazable; las pruebas y la evidencia cross-repo son parte de la evaluación.

## HECHO — 2026-09-17

Las HU-001 a HU-036 existen en `docs/FCV Dev/scrum/`. HU-001/002/003 tienen avance parcial; HU-004 define por ahora solo el contrato de identidad. La implementación backend de HU-005/006/007 cuenta con `AuthIntegrationTest`, `IdentityTest` y `AuthRequestGuardTest`; la integración web se controla mediante HU-033.

## HECHO — 2026-09-22

El catálogo de ocho subagentes fue versionado en `docs/FCV Dev/subagents/` y enlazado desde el orquestador. `citas-web` contiene trabajo local React/Vite de autenticación y pruebas; no debe declararse completado hasta ejecutar build, typecheck, tests y verificación cross-repo.

## HECHO — 2026-10-04

HU-034, HU-035 y HU-036 quedan `Implementada` con evidencia en `docs/FCV Dev/evidence/S5-S6-n8n-2026-10-04.md`. Artefactos: `citas-api/automations/n8n/WF-001-appointment-reminders.json`, `WF-002-status-notifications.json`, `WF-003-daily-operational-summary.json`; backend `AppointmentEventPublisher` y `AppointmentLifecycleController.rescheduleDecision` (evento `AppointmentRescheduleDecided`).
