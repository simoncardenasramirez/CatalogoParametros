# Administración embebida y consulta desde otras aplicaciones

Guía completa para equipos consumidores: [Adopción del Catálogo de Parámetros y su microfrontend](guia-adopcion-parametros.md).

El mismo catálogo central sirve al frontend principal y a las aplicaciones consumidoras. En **Aplicaciones → Gestionar parámetros**, el frontend principal monta el mismo microfrontend que puede montar un tercero.

## Microfrontend

Paquete ESM autónomo: `http://localhost:4200/parametros-ui/parametros-administration.js`. Incluye Angular compilado AOT y estilos aislados mediante Shadow DOM; no requiere router, iframe ni Angular en el host.

```html
<div id="parametros"></div>
<script type="module">
  import { mount } from 'http://localhost:4200/parametros-ui/parametros-administration.js';
  const ui = mount(document.querySelector('#parametros'), {
    applicationId: 'UUID-DE-LA-APLICACION-EN-PARAMETROS',
    apiBaseUrl: 'http://localhost:8080/catalogo-parametros/api/v1',
    refreshIntervalMs: 60000,
    onAuthenticationRequired: () => { /* Iniciar sesión mediante Seguridad. */ },
    onChange: event => console.log(event.resource, event.operation)
  });
  await ui.ready;
  // Al abandonar la pantalla: ui.unmount();
  // Para refrescar explícitamente: await ui.refresh();
</script>
```

En Angular, React o Vue, montar cuando exista el elemento y llamar `unmount()` al destruirlo. El ID corresponde a la aplicación del catálogo, no al registro de Seguridad ni al token de consulta. Las declaraciones TypeScript y `manifest.json` se distribuyen junto al JS. Compilar mediante `npm run build:microfrontend` o construir la imagen frontend.

Gestiona módulos, funcionalidades, parámetros, metadatos y asociaciones por ambiente de esa aplicación. Cada metadato requiere `idAmbiente`, además de parámetro, tipo y valor. Ambientes, tipos y estados auxiliares se consultan para los selectores; no se editan como catálogos globales desde el widget. Las mutaciones reutilizan casos de uso existentes, validaciones, cascadas y eventos SSE.

El widget escucha `GET .../administracion/events`, con evento `catalogo-aplicacion`, credenciales de sesión y latidos cada 15 segundos. El servidor filtra los cambios por pertenencia a la aplicación; las notificaciones no contienen valores ni credenciales. Incluye cambios de su rama, de la aplicación y de ambientes auxiliares. Agrupa las ráfagas durante 250 ms, vuelve a consultar su alcance y comunica cambios efectivos mediante `onChange`, tanto propios como externos. El frontend central escucha sus eventos SSE; sus selectores de metadatos también reciben cambios de parámetros y ambientes.

Ambos clientes reintentan SSE con espera creciente hasta 30 segundos. El widget conserva un refresco cada minuto mientras está visible, sin edición ni otra operación pendiente. Los cambios externos se aplazan mientras haya un formulario abierto y se aplican al cerrarlo, sin sobrescribir la edición. `unmount()` libera conexiones, suscripciones y temporizadores. Se admiten cero para deshabilitar el refresco periódico o intervalos entre 30 segundos y 24 horas; SSE continúa activo.

La API comprueba en servidor tanto el propietario actual del registro como el padre propuesto. Intentar operar con un recurso de otra aplicación devuelve 404. Esta comprobación complementa los permisos de Seguridad, que siguen siendo obligatorios: el administrador debe autorizar al usuario para la aplicación y los métodos/rutas `/catalogo-parametros/api/v1/aplicaciones/{idAplicacion}/administracion` y sus subrutas de recursos, incluido GET `/events`. El UUID del widget no constituye una credencial.

Las peticiones del widget usan la sesión del usuario con `credentials: include`. Para otro origen, agregar el origen exacto del host a `FRONTEND_ALLOWED_ORIGINS` y recrear el backend. En producción usar HTTPS y desplegar mediante un proxy del mismo sitio o configurar correctamente las cookies de Seguridad; un inicio de sesión y sus permisos deben estar disponibles antes de montar. El CORS público del archivo JS permite descargarlo, pero no concede acceso al API.

## Token y secreto para el backend consumidor

En el frontend central abrir **Tokens → Nuevo**, seleccionar aplicación, ambiente, estado activo y vencimiento. El frontend genera un secreto aleatorio de 256 bits. Al confirmar muestra el ID del token, el secreto y el bloque properties. El secreto se muestra solo durante esa sesión; copiarlo antes de cerrar. El servidor conserva su hash y no permite recuperarlo desde la lista.

```properties
catalogo.parametros.base-url=http://localhost:8080/catalogo-parametros/api/v1
catalogo.parametros.application-id=UUID-APLICACION-CATALOGO
catalogo.parametros.token-id=UUID-TOKEN
catalogo.parametros.secreto=${CATALOGO_PARAMETROS_SECRETO}
```

Configurar `CATALOGO_PARAMETROS_SECRETO` en el entorno o gestor de secretos del **backend consumidor**. El bloque generado es configuración para su cliente HTTP: por sí solo no instala un SDK ni realiza consultas.

El backend consumidor solicita:

```http
GET /catalogo-parametros/api/v1/tokens/{catalogo.parametros.token-id}/parametros
Authorization: Bearer <catalogo.parametros.secreto>
```

El token ya vincula la aplicación y el ambiente; no se envían otros IDs para ampliar su alcance. La consulta conserva la validación de estado, vencimiento UTC, secreto y disponibilidad de la jerarquía. No entrega parámetros inactivos ni metadatos de otros ambientes. Los registros históricos sin `idAmbiente` no se exportan: deben editarse en la administración para asignarles un ambiente; no se les asigna uno automáticamente. No usar el token de registro PEP de Seguridad como token de consulta del catálogo.

El secreto de consulta no autoriza administración. No enviarlo al microfrontend, `localStorage`, URLs ni código JavaScript publicado. La configuración del widget rechaza campos de secreto. Las fechas se envían con zona y se normalizan a UTC; los formularios muestran la hora local y conservan el instante al guardar.

## Verificación

Contrato de administración: [OpenAPI](openapi/catalogo-administracion-v1.yaml).

Las pruebas unitarias cubren filtrado por aplicación, rechazo de recursos/padres ajenos, mapeo del alcance desde la ruta, operaciones del widget, fechas UTC, errores HTTP y generación de credenciales sin almacenamiento permanente. El acceso real de una aplicación consumidora requiere configurar su sesión, permisos y origen; las pruebas unitarias no conceden esos permisos.
