---
id: HU-001
tipo: historia-de-usuario
titulo: Registrar usuario
estado: Pendiente de aprobación
epica: "[[EP-001-identidad-y-perfil]]"
esfuerzo: Medio
sprint_sugerido: Sprint 1
dependencias: []
relacionadas: ["[[HU-002-iniciar-y-cerrar-sesion]]"]
---
# HU-001 — Registrar usuario
## Historia de usuario
**COMO** visitante **QUIERO** crear una cuenta USER con mis datos mínimos **PARA** solicitar citas.
## Alcance
- Nombres, apellidos, tipo/número de documento, email, teléfono y contraseña; pantalla y API REST directa.
## Fuera de alcance
- Registro de ADMIN o PROFESSIONAL y datos reales.
## Reglas de negocio
- Email y documento únicos; la contraseña no se almacena ni expone en texto plano.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-perfil]]; relacionadas: [[HU-002-iniciar-y-cerrar-sesion]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** coordinación de formulario, validación segura, persistencia 3FN y contrato.
## Tareas de desarrollo
- [ ] **T-01 — Contrato de registro.** Validar campos, respuestas y errores sin exponer secretos.
- [ ] **T-02 — Alta segura.** Aplicar unicidad, hash adaptativo y rol USER en dominio/persistencia.
- [ ] **T-03 — Interfaz de registro.** Implementar formulario y estados de envío/error en el framework que se importe.
## Criterios de aceptación
### CA-01 — Alta válida
**Dado** datos mínimos válidos y únicos **cuando** el visitante se registra **entonces** se crea una cuenta USER sin devolver su contraseña.
### CA-02 — Duplicados y validación
**Dado** un email o documento ya existente, o datos inválidos **cuando** se intenta registrar **entonces** no se crea la cuenta y se informa un error seguro y utilizable.
## Definition of Done
- [ ] CA-01 y CA-02 tienen pruebas relevantes y evidencia de resultado.
- [ ] Persistencia 3FN y migración Flyway aplicable justifican unicidad; no hay secretos en código/logs.
- [ ] Formulario y contrato REST directo están integrados y documentados.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- Caducidad/sesión se cubre en [[HU-002-iniciar-y-cerrar-sesion]].
