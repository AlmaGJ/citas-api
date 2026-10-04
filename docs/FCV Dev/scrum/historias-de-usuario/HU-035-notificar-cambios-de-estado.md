---
id: HU-035
tipo: historia-de-usuario
titulo: "Notificar cambios de estado"
estado: Implementada
epica: "[[EP-008-cliente-web-y-automatizaciones-posteriores]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 7"
dependencias: ["[[HU-032-consultar-auditoria-de-estados]]"]
relacionadas: ["[[HU-034-automatizar-recordatorios]]"]
---
# HU-035 — Notificar cambios de estado
## Historia de usuario
**COMO** USER afectado por una cita  
**QUIERO** recibir una notificación cuando cambie su estado  
**PARA** conocer decisiones y actualizaciones relevantes.
## Contexto y descripción
Automatización posterior mediante webhook + Gmail, sin alterar el núcleo de citas.
## Alcance
- Evento/webhook autorizado para cambio de estado, workflow n8n y envío Gmail con exportación segura.
## Fuera de alcance
- SMS/WhatsApp, credenciales versionadas o modificar transiciones existentes.
## Reglas de negocio
- Auditoría es fuente de cambios; workflow no cambia estado; credenciales externas.
## Dependencias y relaciones
- Épica: [[EP-008-cliente-web-y-automatizaciones-posteriores]]
- Dependencias: [[HU-032-consultar-auditoria-de-estados]].
- Relacionadas: [[HU-034-automatizar-recordatorios]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** enlaza transición confiable, webhook externo y seguridad de comunicación.
## Tareas de desarrollo
- [ ] **T-01 — Definir evento seguro.** Dificultad: Alto. Acordar datos mínimos que el núcleo expone al webhook.
- [ ] **T-02 — Crear workflow de notificación.** Dificultad: Alto. Configurar Gmail/n8n externo y manejar errores.
- [ ] **T-03 — Exportar y probar.** Dificultad: Medio. Versionar JSON sin secretos y simular cambio sintético.
## Criterios de aceptación
### CA-01 — Disparo por cambio
**Dado** un cambio de estado auditado, **cuando** se emite el evento autorizado, **entonces** n8n recibe información mínima para notificar.
### CA-02 — Núcleo preservado
**Dado** una notificación exitosa o fallida, **cuando** se verifica la cita, **entonces** el workflow no cambia estado, slots ni auditoría.
### CA-03 — Seguridad del workflow
**Dado** la exportación versionada, **cuando** se revisa, **entonces** no hay credenciales y reside en `automations/n8n/`.
## Definition of Done
- [ ] CA-01 a CA-03 tienen evidencia de evento controlado, workflow y revisión de seguridad.
- [ ] El contrato del evento minimiza exposición de datos y usa credenciales externas.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Cumplido | `evidence/S5-S6-n8n-2026-10-04.md` (ejecuciones 368, 371, 372) | Eventos reales desde citas-api: cita especializada rechazada y reprogramación aprobada/rechazada. |
| CA-02 | Cumplido | `evidence/S5-S6-n8n-2026-10-04.md` | El workflow solo notifica; no cambia estado, slots ni auditoría. |
| CA-03 / DoD | Cumplido | `automations/n8n/WF-002-status-notifications.json` | JSON saneado. Workflow inactivo. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-10-04 — Implementada. Se añadió el evento `AppointmentRescheduleDecided` en citas-api para notificar reprogramaciones. Workflow dejado inactivo.
## Notas y decisiones
- El formato del evento se aprueba como cambio de contrato cross-repo.
