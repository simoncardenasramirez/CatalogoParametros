# Guía de adopción del Catálogo de Parámetros

## Administración web y microfrontend

El host monta el microfrontend con el UUID de la aplicación del Catálogo:

```ts
mount(element, {
  applicationId: 'UUID-APLICACION-CATALOGO',
  apiBaseUrl: 'https://catalogo.example/catalogo-parametros/api/v1',
  onAuthenticationRequired: () => iniciarSesionEnSeguridad()
});
```

El navegador usa la cookie de sesión. No se configura un secreto, token interno
ni `client_id` en `mount`, `environment.ts`, `localStorage` o una URL.

Desde la aplicación seleccionada se administran módulos, funcionalidades,
parámetros, metadatos y asociaciones de metadatos por ambiente. El servidor
comprueba el propietario de cada recurso y sus padres; Seguridad comprueba la
sesión, el rol y el método HTTP.

## Consumo backend a backend

1. Registrar en Security Baseline una identidad de servicio para el backend
   consumidor.
2. Crear el rol técnico con `GET /catalogo-parametros/api/v1/parametros/consulta/m2m`
   y asignarlo a la identidad.
3. En el Catálogo, asociar el `client_id` de esa identidad a la aplicación y al
   ambiente que debe consumir.
4. Guardar el secreto únicamente en el backend consumidor o en su gestor de
   secretos.
5. Obtener un JWT OAuth2 `client_credentials` y llamar:

```http
GET /catalogo-parametros/api/v1/parametros/consulta/m2m
Authorization: Bearer <JWT>
Accept: application/json
```

La consulta sin parámetros devuelve todos los parámetros activos del ambiente asociado.
También puede filtrarse por nombre sin cambiar la respuesta:

```http
GET /catalogo-parametros/api/v1/parametros/consulta/m2m?nombre=correo_______________
Authorization: Bearer <JWT>
Accept: application/json
```

El filtro conserva `aplicacion`, `ambiente` y la estructura completa del elemento dentro de
`parametros`. Si no existe un parámetro con ese nombre, la respuesta es `200` con `parametros: []`.

La petición no lleva `applicationId` ni ambiente alternativo: el alcance sale de
la asociación del `client_id`. Sin asociación o sin permiso técnico la respuesta
es `403`; sin JWT válido es `401`.

La respuesta contiene `aplicacion`, `ambiente` y `parametros`. Cada parámetro
incluye su funcionalidad, módulo, tipo, metadatos del ambiente y configuración
de la asociación. No se devuelven parámetros de otro ambiente.

## Eventos y errores

El microfrontend escucha SSE para refrescar la rama de la aplicación; no se conecta
directamente a Pulsar y nunca recibe secretos. Para consumir cambios de forma asíncrona,
los backends consumidores usan la integración Pulsar publicada por el Catálogo. El evento
incluye `resourceName` y `resourceValue` cuando aplica, pero nunca contiene secretos ni el
`client_secret`. Después de recibirlo, el backend debe consultar nuevamente el endpoint M2M
para obtener la configuración vigente.

El token de registro del PEP es una credencial del backend para registrarse en
Security; no es una credencial de consulta de parámetros y nunca debe enviarse
desde el frontend.
