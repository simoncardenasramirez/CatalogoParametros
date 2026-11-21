# Integración con Security Baseline

El Catálogo usa el starter PEP para proteger sus endpoints administrativos y el
starter M2M para consumir MessageUcoLab. Se conservan separados:

- `PEP_REGISTRATION_TOKEN`: credencial de registro del backend ante el PEP; solo backend.
- Identidad de servicio de Catálogo hacia MessageUcoLab: `client_id` y secreto en `security.pep.service-identity.*`.
- Identidades de servicio de los consumidores del Catálogo: las crea Seguridad; el Catálogo solo guarda la asociación `client_id` → aplicación + ambiente.

## Administración web

El frontend y el microfrontend usan la sesión web mediante cookies. El
microfrontend recibe únicamente `applicationId` y `apiBaseUrl`; no recibe
secretos, tokens internos ni credenciales de Seguridad. Sus operaciones se
realizan bajo:

```text
/catalogo-parametros/api/v1/aplicaciones/{idAplicacion}/administracion
/catalogo-parametros/api/v1/aplicaciones/{idAplicacion}/administracion/events
```

El PEP autoriza usuario, aplicación, recurso y método. El backend vuelve a
validar que el recurso pertenezca a la aplicación solicitada.

## Consumo M2M del Catálogo

Seguridad crea la identidad de servicio y entrega su `client_id`/secreto al
backend consumidor. En el frontend del Catálogo, un administrador asocia ese
`client_id` al ambiente de la aplicación mediante:

```text
POST /catalogo-parametros/api/v1/aplicaciones/{idAplicacion}/identidades-servicio
```

El rol técnico debe autorizar exactamente:

```text
GET /catalogo-parametros/api/v1/parametros/consulta/m2m
```

El consumidor obtiene un JWT con `client_credentials` y envía solo el Bearer:

```http
GET /catalogo-parametros/api/v1/parametros/consulta/m2m
Authorization: Bearer <JWT>
```

El PEP valida firma, vigencia y rol. Después el Catálogo extrae el `azp`/`client_id`
del JWT validado, busca la asociación y consulta solo el ambiente asociado.

## Verificación mínima

| Caso | Resultado esperado |
| --- | --- |
| API sin sesión | `401` |
| Usuario sin permiso | `403` |
| M2M sin Bearer | `401` |
| M2M sin asociación | `403` |
| M2M con asociación válida | `200`, solo su aplicación y ambiente |
| PEP no disponible | `503` |

El Catálogo ya no expone ni configura endpoints de tokens internos. Los secretos
históricos deben revocarse y eliminarse según la política de Seguridad.
