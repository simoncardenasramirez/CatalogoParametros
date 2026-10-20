# Integración del catálogo con Security Baseline

Implementación del manual `Manual-integracion-aplicaciones.pdf` del workspace.
El starter Maven existente se conserva en **0.1.3**, disponible en el repositorio
local; la versión del stack Docker del manual es **0.1.6** y es independiente.
No se sustituye por el SNAPSHOT antiguo mostrado en uno de los ejemplos del PDF.

## Configuración

| Variable backend | Uso / valor local |
| --- | --- |
| `PEP_URL` | `http://localhost:18081`; en Docker, `http://host.docker.internal:18081` |
| `SECURITY_APPLICATION_NAME` | `Catalogo`, nombre registrado; debe coincidir exactamente con Seguridad y el host |
| `SECURITY_ENVIRONMENT` | `local` |
| `PEP_REGISTRATION_TOKEN` | Credencial emitida por Seguridad; obligatoria, solo backend |
| `FRONTEND_ALLOWED_ORIGINS` | `http://localhost:4200`, lista de orígenes exactos separada por comas |
| `PEP_ALLOW_INSECURE_HTTP` | `true` exclusivamente para el laboratorio HTTP; por defecto `false` |

Al iniciar con Maven puede activar `--spring.profiles.active=local` para habilitar
HTTP local, o definir `PEP_ALLOW_INSECURE_HTTP=true`. El Compose ya inyecta esta
excepción y la URL accesible desde el contenedor. El PEP omite `/actuator/health` y la ruta
`/catalogo-parametros/api/v1/tokens/{id}/parametros`. Esta última conserva su
autenticación propia: valida el Bearer del catálogo, estado activo y expiración;
filtra los resultados por la aplicación y ambiente del token. Es una excepción
al flujo PEP del manual, necesaria para consumidores autenticados con tokens del
catálogo. No requiere registrar cada ID de token como recurso de Seguridad. Los controladores de negocio conservan sus reglas y el starter autoriza
antes de invocarlos. El filtro CORS se ejecuta antes para cubrir también los errores.

Mantenga `GITHUB_PACKAGES_USER` y `GITHUB_PACKAGES_TOKEN` en el entorno de Maven;
el servidor `github` de settings.xml debe leer `${env.GITHUB_PACKAGES_USER}` y
`${env.GITHUB_PACKAGES_TOKEN}`. Nunca use la credencial PEP como token de Maven.
El Dockerfile y el pipeline existentes usan secretos para descargar el starter.

## Arranque local

1. Levante `Seguridad/security-baseline-local` usando su `.env` del release fijo
   0.1.6: `docker compose pull` y `docker compose up -d`.
2. Verifique salud de PDP y PEP en los puertos 18080 y 18081. El BFF debe permitir
   `http://localhost:4200` tanto para CORS como para retorno del login; el Compose
   de Seguridad del workspace ya lo incluye.
3. En el frontend de administración de Seguridad (puerto 5173), registre la
   aplicación con URL base `http://localhost:8080`, conserve su nombre exacto y
   guarde la credencial una sola vez como `PEP_REGISTRATION_TOKEN`. Añada otro
   administrador. Este paso requiere una cuenta administradora y MFA si se exige.
4. Desde este directorio ejecute `docker compose -f docker-compose.yml up --build -d`.
   Se mantienen los requisitos existentes de Azure y los secretos Maven.
   El contexto adicional compila Security UI desde `../../Seguridad/security-ui`.
5. Abra `http://localhost:4200`. El host verifica la sesión y redirige al login;
   al regresar permite navegar a `/seguridad`. En Docker el bundle se sirve desde
   `/security-ui/security-administration.js`, sin servidor preview adicional.

Para desarrollo sin Docker, inicie el backend con perfil local, Security UI con
`npm run build && npm run preview` y Angular con `npm start`. El JSON público
`src/assets/security-config.json` del frontend apunta al bundle del puerto 5175.
Docker usa `security-config.docker.json`. Ambos aceptan `applicationId` opcional.
No se envían tokens a `mount`; el microfrontend usa la cookie BFF.

## Recursos y asignaciones

En `/seguridad`, cree recursos con el método y la ruta exactos, asígnelos a roles,
agrupe roles en perfiles cuando aplique y asigne el rol o perfil a la persona
para esta aplicación. Los recursos de solo consulta y los de escritura deben tener
permisos independientes. No basta con crear el rol o registrar un administrador.

El contrato en `docs/openapi/catalogo-parametros-v1.yaml` y los controladores son
la referencia de rutas; incluya los endpoints SSE `GET .../events` en los permisos
de consulta. No publique prefijos completos para resolver una denegación.

## Verificación

| Caso | Resultado |
| --- | --- |
| API sin sesión | 401, sin ejecutar negocio |
| Usuario sin recurso asignado | 403, sin ejecutar negocio |
| GET con permiso de lectura | 200 |
| Escritura sin permiso | 403 |
| Escritura con permiso | 2xx según las reglas del catálogo |
| PEP/PDP no disponible | 503, sin ejecutar negocio |
| Preflight del origen permitido | CORS con credenciales, sin exigir sesión |
| Salir de `/seguridad` | `unmount` libera el microfrontend |

Las pruebas `WebConfigTest` y `CatalogoPepIntegrationTest` verifican CORS y el
contrato del starter con decisiones simuladas, sin sustituir la prueba de identidad,
roles y MFA contra el stack real. Las pruebas del host verifican sesión, cookies,
retorno de login y presentación de errores. La prueba real de permisos necesita
una sesión de usuario y asignaciones registradas en Seguridad.

En producción use URLs HTTPS, `PEP_ALLOW_INSECURE_HTTP=false`, orígenes exactos
y cookies seguras del BFF. Sustituya el JSON público del host en el despliegue y
permita sus URLs en la CSP. Las credenciales se inyectan mediante secretos.

La consulta por token devuelve 401 ante un secreto ausente, incorrecto, expirado
o revocado. Ese 401 no redirige el host al login OIDC. La administración de tokens
y las demás rutas de negocio siguen pasando por PEP.
