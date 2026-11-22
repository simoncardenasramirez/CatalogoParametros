# OpenAPI del Catálogo de Parámetros UcoLab

El contrato OpenAPI está en
[catalogo-parametros-v1.yaml](catalogo-parametros-v1.yaml). El prefijo común es
`/catalogo-parametros/api/v1`.

El catálogo expone operaciones CRUD para organizaciones, aplicaciones, módulos,
funcionalidades, parámetros, metadatos, ambientes y asociaciones por ambiente.
La administración embebida se documenta en
[catalogo-administracion-v1.yaml](catalogo-administracion-v1.yaml).

## Consumo M2M

Las aplicaciones consumidoras usan una identidad de servicio creada por
Security Baseline y asociada a una aplicación y ambiente del Catálogo:

```http
GET /catalogo-parametros/api/v1/parametros/consulta/m2m
Authorization: Bearer <JWT-client_credentials>
```

La asociación y el rol técnico son requisitos independientes. El JWT determina
el `client_id`; no se envían tokens internos, secretos ni IDs de ambiente en la
petición.
