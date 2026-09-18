# Gobierno de la LLM Wiki

`raw/` contiene fuentes admitidas e inmutables. Una revisión genera una nueva versión o ID; no se edita silenciosamente una fuente previa.

`wiki/` contiene afirmaciones sintetizadas, cada una enlazada a una o más fuentes. Las inferencias deben etiquetarse como tales.

Toda operación INGEST, QUERY, LEARN o LINT añade una entrada a `wiki/log.md`. No se persisten secretos, credenciales, passwords, tokens ni PII innecesaria.

Antes de cerrar una modificación, ejecutar el checklist de `lint-checklist.md` y comprobar enlaces relativos, páginas huérfanas y decisiones no aprobadas.
