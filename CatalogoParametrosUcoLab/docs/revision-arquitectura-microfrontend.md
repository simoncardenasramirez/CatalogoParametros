# Revisión de arquitectura: catálogo embebido

Resultado: **APROBADO** para catálogo embebido, relación metadato–ambiente, eventos SSE y entrega de credenciales.

| Regla | Evidencia |
| --- | --- |
| Núcleo independiente de adaptadores | `application/features/catalogoaplicacion` importa puertos de repositorio, DTO/interactores existentes, telemetría y excepciones del núcleo; sin imports de infraestructura, SurrealDB ni Azure. |
| Controlador sin repositorios | `infraestructure/primaryadapters/controller/catalogoaplicacion/CatalogoAplicacionController.java` inyecta únicamente `AdministrarCatalogoAplicacionInteractor`; ejecuta trabajo síncrono mediante `Mono.fromCallable` y `boundedElastic`. |
| Dominio, regla y mapeo separados | El domain expresa alcance y operación; `RecursoPerteneceAplicacionRuleImpl` verifica registro actual y padre propuesto; el mapper obtiene la aplicación desde la ruta. |
| Reutilización de negocio | El interactor delega las mutaciones a los quince interactores CRUD existentes después de validar el alcance; conserva reglas, repositorios transaccionales, publishers y cascadas. Ninguna feature existente depende de la nueva. |
| Telemetría y excepciones | Consulta y validación usan `TelemetryService`; operaciones registran prefijos; validaciones utilizan `ValidationException`/`NotFoundException`. |
| Frontend por feature | Dominio, cliente HTTP, presentación y pruebas viven en `features/catalogo-embebido`, siguiendo la estructura feature-first existente. El contrato de distribución está en `src/microfrontend`. |
| Recursos y formularios | Reactive Forms tipados; no se agregan estados; fechas convertidas a UTC; sin router propio. Shadow DOM aísla estilos. `unmount()` destruye aplicación, vistas, suscripciones y temporizador; la sustitución limpia el montaje anterior. |
| Credenciales y autorización | Secreto criptográfico de consulta entregado desde Tokens, sin almacenamiento del navegador. Properties referencia variable de entorno. Widget usa sesión de usuario; endpoints nuevos conservan PEP. |
| Relación por ambiente | `MetadatoEntity`, DTO request/input, dominios, mappers, repositorios SurrealDB y caché Redis conservan `idAmbiente`. Crear/actualizar exige ambiente existente. El borrado de ambiente comprueba metadatos dependientes. |
| Aislamiento de consulta | `ConsultarParametrosPorTokenImpl` compara el ambiente de cada metadato con el ambiente del token, además de los filtros existentes de aplicación, estado y vigencia. Históricos sin ambiente se excluyen. |
| Eventos por alcance | El adaptador de salida agrega publishers existentes en un flujo vivo; `ConsultarCambiosCatalogoAplicacionImpl` filtra pertenencia mediante puertos. El interactor delega en ese caso de uso. Controller SSE solo depende de interactores. Las notificaciones no transportan valores ni secretos. |
| Reactividad y recursos | `EventStreamService` comparte reconexión con espera y cierre al cancelar. El widget agrupa ráfagas, preserva formularios abiertos y libera SSE/temporizadores al desmontar. El frontend central sincroniza metadatos, parámetros y ambientes auxiliares. |

Pruebas backend: 1.201 sin fallos. Frontend: 245 aprobadas y 80,88 % de cobertura de ramas (mínimo 80 %). Cubren aislamiento por ambiente, campos obligatorios, persistencia/caché, filtrado de eventos, reconexión y conservación de formularios. El navegador verifica además una aplicación consumidora real (`FrontPrueba` + `BackPrueba`) con token generado en el frontend central: consulta de desarrollo sin valores de producción, ambiente obligatorio, edición desde cada frontend reflejada en el otro por SSE y actualización de la consulta propia sin recargar. Evidencia local: `FrontPrueba/evidencias/resultado-integracion.json` e imagen de la pantalla; archivos ignorados para no versionar sesiones.

Límite: la consulta administrativa filtra listas obtenidas por puertos existentes en memoria. Si el volumen del catálogo crece, conviene añadir consultas filtradas de repositorio; el widget evita peticiones superpuestas y actualiza cada minuto solo cuando puede hacerlo. La autorización, la sesión del consumidor y sus orígenes deben configurarse en cada despliegue.
