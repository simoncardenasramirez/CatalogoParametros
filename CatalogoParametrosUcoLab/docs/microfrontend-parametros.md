# Microfrontend de administración de parámetros

El microfrontend administra el catálogo de una aplicación usando la sesión web de
Seguridad. No recibe `client_id`, `client_secret`, token interno ni secreto de
consulta.

```html
<div id="parametros"></div>
<script type="module">
  import { mount } from 'http://localhost:4200/parametros-ui/parametros-administration.js';
  const ui = mount(document.querySelector('#parametros'), {
    applicationId: 'UUID-DE-LA-APLICACION-EN-EL-CATALOGO',
    apiBaseUrl: 'http://localhost:8080/catalogo-parametros/api/v1',
    refreshIntervalMs: 60000,
    onAuthenticationRequired: () => { /* iniciar sesión en Seguridad */ },
    onChange: event => console.log(event.resource, event.operation)
  });
  await ui.ready;
</script>
```

`applicationId` determina el único alcance administrable. El backend valida que
cada lectura, creación, actualización y eliminación pertenezca a esa aplicación;
la autorización del usuario la decide el PEP. Las peticiones usan cookies de
sesión (`credentials: include`) y el widget rechaza secretos en sus opciones.

La vista permite gestionar módulos, funcionalidades, parámetros, metadatos y
metadatos por ambiente. Los ambientes, tipos y estados auxiliares se muestran
para seleccionar la configuración, pero el microfrontend no registra credenciales
ni emite tokens propios.

## Consumo backend M2M

Un backend consumidor debe crear una identidad de servicio en Security Baseline,
obtener su `client_id` y secreto, y pedir a un administrador que asocie ese
`client_id` a una aplicación y ambiente dentro del Catálogo. El secreto nunca se
envía al navegador.

```http
GET /catalogo-parametros/api/v1/parametros/consulta/m2m
Authorization: Bearer <JWT-client_credentials>
Accept: application/json
```

El JWT debe tener el rol técnico de esa ruta. El Catálogo lee el `azp`/`client_id`
validado por Seguridad, busca su asociación y devuelve únicamente la aplicación
y ambiente vinculados. Una identidad asociada a Mensajes no puede leer el
ambiente de Seguridad si no existe esa asociación.

Para iniciar una sesión SSE el widget usa:
`GET /aplicaciones/{idAplicacion}/administracion/events`. El microfrontend usa la sesión web
del usuario y no necesita el `client_id` M2M ni un secreto. Los backends que necesiten recibir
eventos de negocio deben usar Pulsar con su propio `client_id`; no deben conectar Pulsar desde
el navegador.
