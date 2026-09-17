---
id: HU-009
tipo: historia-de-usuario
titulo: Crear profesional
estado: Pendiente de aprobación
epica: "[[EP-002-catalogos-y-oferta-asistencial]]"
esfuerzo: Alto
sprint_sugerido: Sprint 2
dependencias: ["[[HU-002-iniciar-y-cerrar-sesion]]"]
relacionadas: ["[[HU-010-asignar-especialidades-profesional]]", "[[HU-011-asignar-sedes-y-activar-profesional]]"]
---
# HU-009 — Crear profesional
## Historia de usuario
**COMO** ADMIN **QUIERO** crear un usuario PROFESSIONAL con código y matrícula ficticia **PARA** preparar la oferta de atención.
## Alcance
- Alta de usuario PROFESSIONAL, código profesional y matrícula sintética.
## Fuera de alcance
- Auto-registro profesional o credenciales reales.
## Reglas de negocio
- Solo ADMIN crea profesionales; datos de laboratorio sintéticos; credenciales protegidas.
## Dependencias y relaciones
- Épica: [[EP-002-catalogos-y-oferta-asistencial]]; depende de [[HU-002-iniciar-y-cerrar-sesion]]; relacionadas: [[HU-010-asignar-especialidades-profesional]], [[HU-011-asignar-sedes-y-activar-profesional]].
## Esfuerzo
**Nivel:** Alto. **Justificación:** identidad/rol y perfil especializado coordinados.
## Tareas de desarrollo
- [ ] **T-01 — Modelo profesional.** Extender usuario mediante relación normalizada y reglas de unicidad necesarias.
- [ ] **T-02 — Caso ADMIN.** Crear profesional con autorización, validación y password segura.
- [ ] **T-03 — Interfaz CRUD.** Proveer alta con estados y mensajes seguros.
## Criterios de aceptación
### CA-01 — Alta por ADMIN
**Dado** un ADMIN y datos sintéticos válidos **cuando** crea un profesional **entonces** se registra un usuario con rol PROFESSIONAL, código y matrícula.
### CA-02 — Protección de creación
**Dado** un actor no ADMIN o datos inválidos/duplicados **cuando** intenta crear el profesional **entonces** no se crea y se recibe un error autorizado y seguro.
## Definition of Done
- [ ] CA-01/CA-02 comprobados con pruebas de rol, unicidad y hash.
- [ ] Modelo/migración respetan 3FN y no almacenan credenciales en texto plano.
- [ ] Pantalla/contrato están integrados; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — Creada en `Pendiente de aprobación`.
## Notas y decisiones
- Asignaciones se realizan en HU posteriores para mantener el alta pequeña.
