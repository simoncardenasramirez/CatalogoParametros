# Revision de arquitectura: catalogo embebido

Resultado: **APROBADO** para el catalogo embebido, la relacion
metadato-ambiente y los eventos SSE.

| Regla | Evidencia |
| --- | --- |
| Aislamiento | El microfrontend recibe `applicationId` y llama a `/aplicaciones/{id}/administracion`. El backend valida que cada recurso pertenezca a esa aplicacion. |
| Ambiente | Parametros y metadatos muestran y editan `idAmbiente`; la consulta administrativa devuelve los ambientes de la aplicacion. |
| Autorizacion | El widget usa la sesion web con cookies. No recibe `client_id`, `client_secret`, secretos ni tokens internos. |
| Consumidores | Las consultas M2M ocurren en backends consumidores con `client_credentials`; el `client_id` se asocia previamente a aplicacion y ambiente en el catalogo. |
| Eventos | SSE notifica cambios del catalogo y el widget actualiza su vista sin transportar valores sensibles. |
| Ciclo de vida | El desmontaje libera SSE, temporizadores, formularios y el montaje anterior. |

El secreto de una identidad de servicio solo debe existir en el backend que
consume parametros y nunca en el navegador o en la configuracion del
microfrontend.
