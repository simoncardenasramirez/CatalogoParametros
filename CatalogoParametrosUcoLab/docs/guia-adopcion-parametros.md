# Guía de adopción del Catálogo de Parámetros y su microfrontend

Esta guía está dirigida al equipo que integra el catálogo en una aplicación propia. Cubre la preparación del catálogo, la consulta desde el backend consumidor y la administración embebida mediante el microfrontend **Gestión de parámetros**.

## 1. Qué se integra

Existen dos flujos con autenticación distinta:

| Necesidad | Recorrido | Autenticación |
| --- | --- | --- |
| Consultar los parámetros de la aplicación | Frontend consumidor → backend consumidor → API del catálogo | El backend consumidor usa ID de token y secreto de consulta |
| Administrar parámetros dentro de la aplicación | Frontend consumidor → microfrontend → API del catálogo | Sesión del usuario en Seguridad y permisos sobre las rutas administrativas |

Ambos trabajan con la misma base de datos central. El microfrontend incluye sus formularios, validaciones de presentación y estilos; las reglas y la persistencia permanecen en el backend del catálogo. No se replica la base de datos ni se copian sus CRUD a la aplicación consumidora.

El secreto de consulta **no autoriza administración**. La sesión del usuario **no sustituye** la credencial que necesita el backend para consultar por token.

## 2. Requisitos y responsabilidades

### El equipo que opera el catálogo debe proporcionar

- Backend del catálogo operativo y con acceso a sus dependencias, incluida Seguridad/PEP.
- Frontend del catálogo o alojamiento del paquete del microfrontend.
- URLs del API, del paquete ESM y del inicio de sesión de Seguridad para cada entorno.
- Un administrador autorizado para registrar aplicaciones, preparar los catálogos y conceder permisos.
- Configuración de orígenes permitidos, sesión, cookies y retornos de autenticación para el dominio consumidor.

Si se despliega también el proveedor, su configuración de SurrealDB, Redis, secretos y Seguridad pertenece al despliegue del catálogo; consultar el [README del backend](../README.md). Esas credenciales de infraestructura no se entregan al consumidor.

### El equipo consumidor necesita

- Un backend con cliente HTTP capaz de enviar `Authorization: Bearer` y leer JSON. Puede usar Spring u otra tecnología.
- Un frontend con un elemento HTML donde montar el componente y control de su ciclo de vida. No tiene que ser Angular: el paquete ESM incluye Angular compilado.
- Navegador con módulos ESM, Shadow DOM y EventSource/SSE.
- Acceso de red al API desde su backend y al paquete y API administrativos desde el navegador o su proxy.
- Un lugar privado para configurar el secreto: variables de entorno o gestor de secretos del backend.

Para ejecutar los ejemplos del repositorio: Docker Desktop con contenedores Linux, o Java 21+ y Maven para `BackPrueba`, y Node compatible con el Angular del ejemplo para `FrontPrueba` (22.22.3+ dentro de la rama 22; consultar su `package.json`). Usar el ESM ya publicado no exige instalar Java, Maven ni Angular en el navegador consumidor.

## 3. Identificadores y credenciales: no confundirlos

| Dato | Lo obtiene de | Dónde se utiliza |
| --- | --- | --- |
| UUID de aplicación del catálogo | Aplicaciones en el frontend de Parámetros | `applicationId` del microfrontend y configuración del backend consumidor |
| UUID del ambiente | Ambientes del catálogo | Metadatos, asociación parámetro–ambiente y creación del token |
| ID del token de consulta | Pantalla Tokens, después de crearlo | Ruta de consulta del backend consumidor |
| Secreto del token de consulta | Se genera en el frontend del catálogo; se entrega al crearlo | Solo en el backend consumidor, como Bearer |
| ID de aplicación y credencial PEP de Seguridad | Administración de Seguridad | Configuración del servicio protegido correspondiente; no son el token de consulta del catálogo |
| Sesión del usuario | Inicio de sesión de Seguridad | Administración desde el microfrontend |

Registrar una aplicación propia en Seguridad no concede por sí solo acceso a la API del catálogo. El administrador debe autorizar los recursos del servicio del catálogo que protegerá PEP.

## 4. Preparar los datos en el catálogo

Antes de emitir el token de consulta:

1. Crear o seleccionar una organización y una aplicación del catálogo. Guardar el UUID de la aplicación.
2. Crear los módulos y funcionalidades que agruparán los parámetros.
3. Crear cada parámetro con su tipo y una funcionalidad de esa aplicación.
4. Crear o seleccionar los ambientes necesarios, por ejemplo desarrollo y producción.
5. En las asociaciones por ambiente, vincular el parámetro al ambiente y elegir un estado activo.
6. Crear sus metadatos seleccionando **parámetro, tipo de metadato, ambiente y valor**. El ambiente del valor es obligatorio.
7. Comprobar que los registros con estado estén disponibles y que sus padres y fechas de vigencia permitan utilizarlos.

La relación queda así:

```text
Organización → Aplicación → Módulo → Funcionalidad → Parámetro
                                                       ├─ Metadato → Ambiente + tipo + valor
                                                       └─ Asociación → Ambiente + estado
```

Un mismo parámetro puede tener valores diferentes por ambiente. El metadato describe el valor de un ambiente; la asociación indica la disponibilidad del parámetro en ese ambiente. Son relaciones distintas y ambas deben configurarse cuando corresponda.

Los tipos de metadato actuales incluyen JSON, alfanumérico y fecha. El valor debe respetar su tipo. En el editor del microfrontend se escribe JSON: para un texto usar `"mi valor"`; para un objeto, `{"clave":"valor"}`. El catálogo valida el contenido en el servidor.

Los metadatos históricos sin ambiente se pueden consultar administrativamente y corregir. No se les asigna un ambiente por inferencia y no se entregan en la consulta por token hasta que se editen para asignárselo.

## 5. Generar y entregar la credencial de consulta

En el frontend central, abrir **Tokens → Nuevo**:

1. Seleccionar la aplicación y el ambiente que consumirá el backend.
2. Elegir estado activo y una expiración futura.
3. Crear el token. El frontend genera un secreto aleatorio de 256 bits.
4. Copiar el **ID del token**, el **secreto** y el bloque `properties` mostrado al confirmar.
5. Guardar el secreto en el entorno o gestor de secretos del backend consumidor antes de cerrar la tarjeta.

El servidor guarda el hash del secreto; no se puede recuperar posteriormente desde la lista de tokens. Si se pierde, crear una nueva credencial, actualizar el consumidor y revocar la anterior mediante su estado. Un token para desarrollo no debe reutilizarse como token de producción: cada credencial vincula una aplicación y un ambiente.

No publicar el secreto en Git, archivos servidos por el frontend, `environment.ts`, `localStorage`, parámetros de URL, capturas ni logs.

## 6. Configurar el backend consumidor

### Configuración Spring

En `application.properties`, con los valores privados tomados del entorno:

```properties
catalogo.parametros.base-url=${CATALOGO_PARAMETROS_BASE_URL}
catalogo.parametros.application-id=${CATALOGO_PARAMETROS_APPLICATION_ID}
catalogo.parametros.token-id=${CATALOGO_PARAMETROS_TOKEN_ID}
catalogo.parametros.secreto=${CATALOGO_PARAMETROS_SECRETO}
```

Ejemplo de URL local al ejecutar Spring en la máquina: `http://localhost:8080/catalogo-parametros/api/v1`.

Desde un contenedor, `localhost` identifica ese contenedor. Usar el nombre de servicio de la red Docker o, en este laboratorio con Docker Desktop, `http://host.docker.internal:8080/catalogo-parametros/api/v1`. En producción usar la URL HTTPS proporcionada por el operador.

Estos nombres de propiedades son una convención del ejemplo: se debe implementar el cliente HTTP que las utiliza. No instalan un SDK ni hacen consultas automáticamente. Spring no carga `.env` por sí solo; Compose lo carga en el ejemplo. Si se ejecuta Spring directamente, definir las variables en su proceso o en su configuración de despliegue.

### Petición de consulta

El backend consumidor realiza:

```http
GET /catalogo-parametros/api/v1/tokens/{ID_DEL_TOKEN}/parametros
Accept: application/json
Authorization: Bearer SECRETO_DEL_TOKEN
```

No agregar una aplicación ni un ambiente alternativos a la petición: el token define el alcance. No enviar la credencial PEP en este Bearer.

La respuesta contiene `mensajes`, `aplicacion`, `ambiente` y la lista `parametros`. Cada elemento incluye `parametro`, `tipoParametro`, `funcionalidad`, `modulo`, `metadatos` y `configuracionAmbiente`. Cada metadato contiene su entidad, incluido `idAmbiente`, y su tipo. Leer los valores desde `parametros[].metadatos[].metadato.valor`.

El catálogo filtra los parámetros por aplicación, ambiente, disponibilidad y estado; filtra los metadatos por el ambiente del token. Puede devolver una lista vacía o un parámetro sin valores en ese ambiente. No se debe sustituir esa ausencia por valores de otro ambiente.

### Cliente HTTP y API propia

El ejemplo completo y ejecutable está en [BackPrueba](../../../BackPrueba/README.md). Sus puntos de integración son:

- [Propiedades](../../../BackPrueba/src/main/resources/application.properties).
- [Puerto del cliente](../../../BackPrueba/src/main/java/co/edu/uco/backprueba/application/secondaryports/CatalogoParametrosPort.java).
- [Adaptador WebClient](../../../BackPrueba/src/main/java/co/edu/uco/backprueba/infraestructure/secondaryadapters/HttpCatalogoParametrosAdapter.java).
- [Caso de uso](../../../BackPrueba/src/main/java/co/edu/uco/backprueba/application/usecase/ParametrosPruebaInteractorImpl.java).

Este ejemplo limita tiempo y tamaño de respuesta, evita devolver cuerpos de error externos y comprueba que la aplicación recibida coincida con la configurada. Su API propia de consulta entrega datos de negocio a Angular, sin exponer la credencial del catálogo.

En una aplicación real, proteger esa API propia con la autenticación de la aplicación consumidora. No reintentar indefinidamente credenciales inválidas. Si se agrega caché, definir su vigencia e invalidación para no seguir utilizando valores que el catálogo ya desactivó.

## 7. Autorizar la administración en Seguridad

Antes de montar el microfrontend, el usuario debe tener una sesión válida y permisos sobre los recursos administrativos del catálogo. El UUID del componente no es una credencial.

Sea `BASE` la ruta `/catalogo-parametros/api/v1/aplicaciones/{idAplicacion}/administracion`:

| Método | Recurso que debe registrar y conceder |
| --- | --- |
| GET | `BASE` |
| GET | `BASE/events` |
| POST | `BASE/modulos`, `BASE/funcionalidades`, `BASE/parametros`, `BASE/metadatos`, `BASE/metadatos-ambiente` |
| PUT | Cada una de las cinco rutas anteriores con `/{id}` al final |
| DELETE | Cada una de las cinco rutas anteriores con `/{id}` al final |

Registrar las rutas completas, sustituyendo `BASE` por su prefijo real; conservar los segmentos variables admitidos por Seguridad. Asociar los recursos a un rol o perfil y asignarlo al usuario dentro de la aplicación protegida del catálogo. Registrar el recurso sin concederlo no basta.

Para preparar datos desde el frontend central también se necesitan sus recursos REST y SSE: organizaciones, aplicaciones, ambientes, módulos, funcionalidades, parámetros, metadatos, asociaciones, tipos, estados y tokens según las operaciones que realice ese administrador. Los permisos del microfrontend no conceden automáticamente todos los permisos de la consola central.

El laboratorio incluye [un script de referencia](../../../FrontPrueba/scripts/configurar-permisos-demo.mjs) para el rol de prueba. No copiar su nombre, usuarios o alcance como configuración de producción: el administrador de cada despliegue debe asignar sus propios permisos. No desactivar PEP ni declarar públicas las rutas administrativas para resolver un 403.

## 8. Sesión, dominios, HTTPS y CORS

El microfrontend envía la sesión del navegador mediante peticiones con credenciales. El host debe resolver el inicio de sesión y ejecutar su flujo de autenticación cuando reciba `onAuthenticationRequired`. El widget no crea usuarios, no inicia sesión automáticamente y no recibe secretos de consulta.

En este laboratorio las aplicaciones usan `localhost` con distintos puertos, por lo que se pudo iniciar sesión desde la consola y reutilizar la cookie. Esto **no garantiza** compartir sesión entre dominios diferentes en producción. Coordinar con el equipo de Seguridad el BFF, dominio y ruta de cookies, `SameSite`, `Secure`, orígenes y destinos de retorno permitidos. Un proxy no hace que una cookie de otro dominio aparezca automáticamente en el navegador.

Usar HTTPS para los dominios publicados. La configuración del widget solo admite HTTP para direcciones de loopback en desarrollo y desde una página HTTP. Rechaza credenciales, query strings y fragmentos en `apiBaseUrl`.

En el despliegue del catálogo, agregar el origen exacto del host, por ejemplo `https://mi-aplicacion.example`, a `FRONTEND_ALLOWED_ORIGINS`. Incluir esquema y puerto cuando corresponda. En el Compose del catálogo se pueden separar varios orígenes con comas; recrear el servicio después de cambiar la configuración. No usar `*` para un API con credenciales.

El CORS público del archivo JavaScript permite descargar el paquete; no concede permisos sobre los datos ni resuelve la sesión del API.

## 9. Publicar o consumir el paquete del microfrontend

El operador entrega estos archivos del mismo build:

```text
parametros-ui/
  parametros-administration.js
  parametros-administration.d.ts
  types.d.ts
  manifest.json
  ...avisos de licencias generados
```

En el laboratorio el ESM se sirve desde `http://localhost:4200/parametros-ui/parametros-administration.js`. Para compilarlo desde el código del proveedor: `npm ci` y `npm run build:microfrontend` dentro del frontend central, usando su versión compatible de Node. El resultado queda en `dist/parametros-ui`; su Dockerfile ya lo publica.

El consumidor puede cargar la URL proporcionada o alojar una copia controlada de esos archivos en su dominio. En ambos casos sigue apuntando al backend central del catálogo. Servir el JS como JavaScript, sin devolver `index.html` para archivos inexistentes. Conservar los avisos de licencia y actualizar los archivos de distribución juntos.

Si el host usa Content Security Policy, permitir el origen del módulo en sus reglas de scripts y el destino del API/SSE en `connect-src`. El widget inserta estilos dentro de Shadow DOM; la política de estilos debe admitir el CSS distribuido. La API actual no ofrece una opción para suministrar un nonce de estilos.

## 10. Montar el componente

Ejemplo independiente del framework, cargado solo al pulsar un botón. Reemplazar el UUID y las URLs por los del despliegue:

```html
<button id="abrir-parametros" type="button">Gestionar parámetros</button>
<button id="cerrar-parametros" type="button">Cerrar gestión</button>
<p id="estado-parametros" role="status"></p>
<div id="gestion-parametros"></div>

<script type="module">
  const container = document.querySelector('#gestion-parametros');
  const estado = document.querySelector('#estado-parametros');
  let handle;
  let generation = 0;

  document.querySelector('#abrir-parametros').onclick = async () => {
    const actual = ++generation;
    handle?.unmount();
    handle = undefined;
    estado.textContent = 'Cargando gestión de parámetros…';
    try {
      const remote = await import('/parametros-ui/parametros-administration.js');
      if (actual !== generation) return;
      handle = remote.mount(container, {
        applicationId: 'UUID_REAL_DE_LA_APLICACION_DEL_CATALOGO',
        apiBaseUrl: location.origin + '/catalogo-parametros/api/v1',
        refreshIntervalMs: 60000,
        onAuthenticationRequired: () => {
          // Aquí el host activa su flujo de inicio de sesión de Seguridad.
          estado.textContent = 'Inicie sesión para administrar los parámetros.';
        },
        onChange: event => {
          // Refrescar la consulta de negocio mediante el backend propio.
          console.log('Cambio de parámetros:', event.resource, event.operation);
        }
      });
      await handle.ready;
      if (actual === generation) estado.textContent = '';
    } catch (error) {
      if (actual === generation) estado.textContent = 'No se pudo abrir la gestión. Revise conexión, sesión y permisos.';
    }
  };

  document.querySelector('#cerrar-parametros').onclick = () => {
    generation++;
    handle?.unmount();
    handle = undefined;
    estado.textContent = '';
  };
</script>
```

El ejemplo supone que el host publica o enruta `/parametros-ui/` y `/catalogo-parametros/`. Si se usan URLs externas, configurar sesión y CORS según el apartado anterior. El texto `UUID_REAL_...` es un marcador y debe reemplazarse por el UUID real; el componente lo valida.

### Contrato de montaje

| Opción | Uso |
| --- | --- |
| `applicationId` | Obligatoria: UUID de la aplicación del catálogo |
| `apiBaseUrl` | Obligatoria: raíz del API, incluida `/catalogo-parametros/api/v1`, sin query ni fragmento |
| `refreshIntervalMs` | Opcional: 60000 por defecto; cero desactiva el refresco periódico; de 30000 a 86400000 para otros intervalos |
| `onAuthenticationRequired` | Callback del host ante una consulta que recibe 401 |
| `onChange` | Notifica cambios propios y cambios externos efectivos tras actualizar los datos; no entrega credenciales |
| `theme` | Opcional: `accent`, `fontFamily` y `radius` para adaptar el aspecto |

`mount()` retorna un handle con `ready`, `refresh()`, `unmount()` y el elemento montado. Esperar `ready` para conocer el resultado de la carga inicial. `refresh()` permite volver a intentar la consulta. Invocar `unmount()` al cerrar la pantalla o destruir el componente del host, también cuando `ready` haya fallado. El módulo no usa el router del host y aísla sus estilos con Shadow DOM.

`onChange` incluye `applicationId`, `resource`, `operation` (`created`, `updated` o `deleted`) e `id` cuando aplica. Es una notificación para sincronizar vistas, no una bitácora persistente de todos los cambios.

### Integración Angular

El ejemplo Angular completo está en [FrontPrueba](../../../FrontPrueba/README.md). Revisar:

- [Loader ESM](../../../FrontPrueba/src/app/features/parametros/infrastructure/microfrontend.loader.ts): importa la URL en tiempo de ejecución.
- [Componente host](../../../FrontPrueba/src/app/app.component.ts): crea el montaje al pulsar el botón, espera `ready`, maneja sesión y llama `unmount()` al ocultar o destruir.
- [Plantilla](../../../FrontPrueba/src/app/app.component.html): contiene el elemento del montaje y la consulta propia.
- [Servicio de consulta](../../../FrontPrueba/src/app/features/parametros/infrastructure/parametros.service.ts): llama al backend propio, sin Bearer del catálogo en Angular.

En una aplicación Angular existente, crear un componente contenedor que haga lo mismo: obtener un `HTMLElement` mediante `ViewChild`, importar el módulo después de que exista la vista y desmontarlo en `ngOnDestroy`. Si la importación es asíncrona, ignorar su resultado cuando la vista haya sido destruida. No copiar el paquete Angular del proveedor dentro del árbol de componentes del host: utilizar su API `mount()`.

## 11. Configurar el proxy y los eventos SSE

Ejemplo Nginx para un host con el API y paquete en rutas del mismo origen. Reemplazar los servidores de ejemplo:

```nginx
location /parametros-ui/ {
    proxy_pass https://catalogo-front.example;
    proxy_set_header Host $proxy_host;
    proxy_ssl_server_name on;
}

location /catalogo-parametros/ {
    proxy_pass https://catalogo-api.example;
    proxy_set_header Host $proxy_host;
    proxy_ssl_server_name on;
    proxy_http_version 1.1;
    proxy_set_header Connection "";
    proxy_buffering off;
    proxy_cache off;
    proxy_read_timeout 75s;
}
```

Conservar las rutas y los encabezados de sesión/XSRF que necesite el despliegue. No agregar el secreto de consulta como Authorization a las peticiones administrativas del navegador. No almacenar en caché respuestas administrativas ni el flujo de eventos.

El widget escucha `GET .../administracion/events`, evento `catalogo-aplicacion`. El servidor envía latidos cada 15 segundos y filtra las notificaciones por la aplicación; también notifica cambios de ambientes auxiliares. No envía valores de metadatos en esas notificaciones: el cliente vuelve a consultar su rama autorizada.

El cliente agrupa ráfagas durante 250 ms, reconecta con espera creciente hasta 30 segundos y conserva un refresco periódico como respaldo. No sobrescribe formularios abiertos: aplica los cambios pendientes al cerrarlos. SSE sigue activo aunque `refreshIntervalMs` sea cero. El frontend central también escucha los eventos, de modo que una edición se refleja en ambas interfaces sin recargar.

## 12. Vigencia, estados y fechas

- Enviar las fechas y horas con zona ISO-8601; el catálogo normaliza los instantes a UTC. Los formularios muestran hora local y conservan el instante al guardar.
- El instante final de la vigencia ya no permite utilizar el registro.
- La tarea automática revisa vencimientos cada minuto; la consulta también comprueba disponibilidad, por lo que no se debe asumir que un registro vencido sigue siendo utilizable hasta el siguiente ciclo.
- La desactivación manual o por vencimiento de un padre propaga los estados existentes de sus dependientes. No se agregan estados a entidades que no los tienen.
- La reactivación es individual, con padres disponibles y vigencia válida; no reactiva toda una rama automáticamente.

Consultar el detalle en [Vigencias y estados](vigencias-y-estados.md).

## 13. Verificación antes de entregar la integración

1. Con el backend consumidor configurado, consultar sus parámetros y comprobar aplicación y ambiente recibidos.
2. Crear valores diferentes del mismo parámetro en desarrollo y producción. Confirmar que cada token recibe solo los valores de su ambiente.
3. Montar el microfrontend con sesión autorizada y comprobar carga, selectores y creación con ambiente obligatorio.
4. Editar un metadato desde el widget y comprobar su actualización en el frontend central y en la consulta propia.
5. Editarlo desde el frontend central y comprobar el cambio por SSE en el widget y la consulta propia, sin recargar.
6. Probar un usuario sin permisos y una sesión ausente: la administración debe rechazarse. Probar un token inválido, inactivo o vencido: la consulta debe rechazarse.
7. Desactivar un parámetro o su padre y comprobar que la consulta deja de entregarlo.
8. Abrir y cerrar la gestión varias veces: no deben quedar widgets, conexiones o temporizadores duplicados.
9. Revisar que no se exponga el secreto en el bundle, almacenamiento del navegador, URLs ni respuestas de configuración.

El laboratorio ejecutó la consulta real desde Spring, aislamiento por ambiente y sincronización SSE en ambos sentidos. El script [probar-integracion-real.mjs](../../../FrontPrueba/scripts/probar-integracion-real.mjs) permite repetir esa comprobación con la cuenta administradora de su entorno. Sus credenciales se suministran por variables de entorno; no copiarlas a este documento ni al código.

## 14. Problemas frecuentes

| Síntoma | Qué revisar |
| --- | --- |
| No carga el módulo o aparece un error de MIME | URL del JS, publicación de archivos, tipo JavaScript, CORS del módulo y reglas del proxy; no debe devolver el HTML de la SPA |
| `applicationId` inválido o 404 administrativo | Usar el UUID de Aplicaciones del catálogo, no el ID de Seguridad ni el token |
| 401 administrativo | Sesión y cookies disponibles para el API; ejecutar el flujo de inicio de sesión del host |
| 403 administrativo | Origen permitido y recursos, rol/perfil, usuario y ambiente autorizados en Seguridad |
| Funciona consultar, pero no crear o editar | Permisos POST/PUT/DELETE correspondientes; sesión/XSRF y ambiente obligatorio del metadato |
| Consulta vacía | Aplicación y ambiente del token, asociación activa del parámetro al ambiente, estados y vigencias de padres |
| Parámetro sin valores | Metadatos asignados al ambiente del token; los históricos sin ambiente se excluyen |
| No actualiza en tiempo real | GET `/events` autorizado, respuesta SSE, proxy sin buffering/caché y tiempo de espera superior a los latidos |
| Error de contenido mixto | Host y API publicados con HTTPS; no usar un API HTTP desde una página HTTPS |
| Token rechazado | ID y secreto emparejados, estado activo y expiración futura; comprobar que no se está usando la credencial PEP |
| El ejemplo Spring devuelve 424/502/503 | 424: credencial rechazada/dependencia fallida; 502: conexión/respuesta/alcance externo inválidos; 503: configuración pendiente. Son códigos del consumidor de ejemplo, no una traducción universal del API del catálogo |

## 15. Referencias y prueba local rápida

- [Contrato administrativo OpenAPI](openapi/catalogo-administracion-v1.yaml).
- [Contrato general OpenAPI](openapi/catalogo-parametros-v1.yaml).
- [Subcontrato de consulta por token usado por el ejemplo](../../../BackPrueba/postman/contratos/consulta-catalogo.yaml).
- [Contrato TypeScript del microfrontend](../../../CatalogoParametrosFrontend/CatalogoParametros-FrontEnd/catalogo-parametros-front/src/microfrontend/types.ts).
- [Guía breve del microfrontend](microfrontend-parametros.md).

Con el catálogo y Seguridad funcionando, desde `BackPrueba`:

```powershell
docker compose up -d --build
```

Abrir `http://localhost:4300`, pulsar **Consultar parámetros** y luego **Administrar parámetros**. El ejemplo local ya preparado usa su propia aplicación y credencial de consulta. Para otra instalación, generar una nueva credencial y completar `BackPrueba/.env` o ejecutar `configurar.ps1` siguiendo su README.

Para detener solo el consumidor de prueba, ejecutar `docker compose down` desde `BackPrueba`. No elimina ni detiene los datos y servicios del catálogo central.
