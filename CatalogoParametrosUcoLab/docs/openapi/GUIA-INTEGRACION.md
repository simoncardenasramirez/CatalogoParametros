# Guía de integración API

Las operaciones administrativas requieren sesión web y autorización PEP. Para
consumo servidor a servidor use la identidad de servicio creada en Security
Baseline:

```http
GET /catalogo-parametros/api/v1/parametros/consulta/m2m
Authorization: Bearer <JWT-client_credentials>
Accept: application/json
```

El administrador asocia previamente el `client_id` de Seguridad con una
aplicación y un ambiente en:

```http
POST /catalogo-parametros/api/v1/aplicaciones/{idAplicacion}/identidades-servicio
```

La petición M2M no recibe IDs ni secretos del Catálogo. El backend obtiene el
`client_id` del JWT validado, resuelve su asociación y limita la respuesta al
ambiente registrado. `401` indica JWT ausente o inválido; `403` indica falta de
rol técnico o asociación.

El Catálogo no expone ni configura tokens internos. El `PEP_REGISTRATION_TOKEN`
solo registra el backend ante Security y nunca debe enviarse como Bearer de
consulta.
