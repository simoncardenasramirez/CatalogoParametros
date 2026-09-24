# Catálogo de Parámetros UCO Lab

Backend reactivo para administrar el catálogo de organizaciones, aplicaciones, módulos,
funcionalidades, parámetros y metadatos. Está construido con Java 21, Spring Boot y
Spring WebFlux, usa SurrealDB para persistencia y publica eventos en tiempo real mediante
Server-Sent Events (SSE).

## Contenido

- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Configuración](#configuración)
- [Ejecución local](#ejecución-local)
- [Ejecución con Docker Compose](#ejecución-con-docker-compose)
- [Servicios y URLs](#servicios-y-urls)
- [Pruebas y calidad](#pruebas-y-calidad)
- [Uso de la API](#uso-de-la-api)
- [Observabilidad](#observabilidad)
- [CI/CD](#cicd)
- [Solución de problemas](#solución-de-problemas)

## Tecnologías

- Java 21
- Spring Boot 4
- Spring WebFlux
- SurrealDB
- Maven Wrapper
- OpenAPI y Swagger UI
- OpenTelemetry, Micrometer y Spring Boot Actuator
- Prometheus, Grafana, Jaeger y Loki
- Docker y Docker Compose
- JUnit 5, Mockito, JaCoCo y SonarCloud

## Arquitectura

El proyecto sigue una organización inspirada en Clean Architecture y puertos y
adaptadores:

```text
src/main/java/co/edu/uco/CatalogoParametrosUcoLab/
├── application/
│   ├── features/             Casos de uso, reglas, DTO y puertos primarios
│   ├── secondaryports/       Contratos de repositorios, publicadores y servicios
│   └── usecase/              Abstracciones compartidas de dominio
├── crosscutting/             Constantes, excepciones y utilidades transversales
├── infraestructure/
│   ├── primaryadapters/      Controladores HTTP y manejo de errores
│   ├── secondaryadapters/    SurrealDB, Azure Key Vault y publicadores SSE
│   └── config/               CORS, OpenAPI e instrumentación
└── init/                     Punto de entrada de la aplicación
```

Los controladores dependen de puertos de aplicación y los detalles externos se
implementan en adaptadores de infraestructura.

## Requisitos

Para ejecutar el backend directamente:

- JDK 21
- Una instancia accesible de SurrealDB
- Acceso al Azure Key Vault configurado por el proyecto

Para ejecutar el stack de observabilidad completo:

- Docker con Docker Compose
- Credenciales de Azure disponibles como variables de entorno
- El frontend en la ruta hermana que espera `docker-compose.yml`:
  `../../CatalogoParametros-FrontEnd/catalogo-parametros-front`

No es necesario instalar Maven; el repositorio incluye Maven Wrapper (`mvnw`).

## Configuración

La aplicación obtiene secretos de Azure Key Vault. Antes de iniciarla, configura una
identidad con acceso al vault:

```sh
export AZURE_CLIENT_ID="<client-id>"
export AZURE_CLIENT_SECRET="<client-secret>"
export AZURE_TENANT_ID="<tenant-id>"
```

Las siguientes propiedades deben estar disponibles para el backend. En la configuración
actual pueden resolverse desde Azure Key Vault o sobrescribirse mediante configuración de
Spring para el ambiente correspondiente:

| Propiedad de Spring | Variable de entorno equivalente | Descripción |
| --- | --- | --- |
| `spring.application.name` | `SPRING_APPLICATION_NAME` | Nombre del servicio |
| `surrealdb.url` | `SURREALDB_URL` | URL HTTP de SurrealDB |
| `surrealdb.namespace` | `SURREALDB_NAMESPACE` | Namespace |
| `surrealdb.database` | `SURREALDB_DATABASE` | Base de datos |
| `surrealdb.username` | `SURREALDB_USERNAME` | Usuario |
| `surrealdb.password` | `SURREALDB_PASSWORD` | Contraseña |

> No guardes credenciales reales en Git. Para desarrollo utiliza variables de entorno o
> un archivo local excluido del control de versiones.

## Ejecución local

1. Inicia SurrealDB o verifica que la instancia configurada esté disponible.
2. Exporta las credenciales y propiedades necesarias.
3. Ejecuta el backend:

```sh
./mvnw spring-boot:run
```

Para compilar el artefacto ejecutable:

```sh
./mvnw clean package
java -jar target/CatalogoParametrosUcoLab-0.0.1-SNAPSHOT.jar
```

## Ejecución con Docker Compose

El archivo `docker-compose.yml` levanta el backend, frontend, SurrealDB y el stack de
observabilidad. Asegúrate de que el repositorio del frontend exista en la ruta indicada y
de haber exportado las credenciales de Azure.

```sh
docker compose up --build
```

Para ejecutar en segundo plano:

```sh
docker compose up --build -d
```

Para detener los contenedores sin eliminar sus volúmenes:

```sh
docker compose down
```

Las credenciales incluidas para Grafana y SurrealDB son únicamente de desarrollo. Deben
reemplazarse antes de desplegar el stack en un ambiente compartido o productivo.

## Servicios y URLs

| Servicio | URL local |
| --- | --- |
| Frontend | <http://localhost:4200> |
| Backend | <http://localhost:8080> |
| Swagger UI | <http://localhost:8080/swagger-ui.html> |
| Contrato OpenAPI versionado | <http://localhost:8080/openapi/catalogo-parametros-v1.yaml> |
| OpenAPI generado | <http://localhost:8080/v3/api-docs> |
| Health check | <http://localhost:8080/actuator/health> |
| Métricas Prometheus | <http://localhost:8080/actuator/prometheus> |
| Prometheus | <http://localhost:9090> |
| Grafana | <http://localhost:3000> |
| Jaeger | <http://localhost:16686> |
| SurrealDB | <http://localhost:8000> |

Grafana usa `admin/admin` en el entorno local definido por Docker Compose.

## Pruebas y calidad

Ejecuta las pruebas unitarias:

```sh
./mvnw test
```

Ejecuta la verificación completa, incluyendo el reporte y umbral de cobertura JaCoCo:

```sh
./mvnw clean verify
```

El build exige una cobertura de líneas mínima del 80 %. Después de una ejecución exitosa,
el reporte HTML queda en:

```text
target/site/jacoco/index.html
```

## Uso de la API

La URL base local es:

```text
http://localhost:8080/catalogo-parametros/api/v1
```

Ejemplo de consulta paginada:

```sh
curl -sS \
  'http://localhost:8080/catalogo-parametros/api/v1/parametros?page=1&pageSize=10' \
  -H 'Accept: application/json'
```

Ejemplo de suscripción a eventos SSE:

```sh
curl -N \
  'http://localhost:8080/catalogo-parametros/api/v1/parametros/events' \
  -H 'Accept: text/event-stream'
```

El flujo SSE es en vivo y no garantiza persistencia, replay ni recuperación de eventos
perdidos. Para conocer todos los recursos, ejemplos de escritura y códigos de error,
consulta [la guía de consumo](docs/CONSUMO-SERVICIOS.md).

El contrato [OpenAPI versionado](docs/openapi/catalogo-parametros-v1.yaml) es la fuente de
verdad para consumidores. El documento generado en `/v3/api-docs` sirve para detectar
divergencias con la implementación.

## Observabilidad

La aplicación incluye:

- logs correlacionados con `traceId` y `spanId`, recolectados por Promtail y Loki;
- métricas técnicas y de negocio con Actuator y Micrometer;
- métricas exportadas a Prometheus y visualizadas en Grafana;
- trazas distribuidas con OpenTelemetry y Jaeger;
- spans internos para interactores, casos de uso y repositorios.

La configuración y las métricas personalizadas se describen en
[TELEMETRY.md](TELEMETRY.md).

## CI/CD

`azure-pipelines.yml` define el pipeline para:

1. configurar Java 21;
2. restaurar las cachés de Maven y Sonar;
3. compilar y ejecutar las pruebas;
4. exigir al menos 80 % de cobertura;
5. publicar resultados y cobertura;
6. ejecutar SonarCloud y exigir el Quality Gate;
7. construir y publicar la imagen Docker desde la rama `develop`.

El pipeline requiere `SONAR_TOKEN` como variable secreta y una conexión de servicio de
Docker Hub denominada `DockerHubConnection`.

## Solución de problemas

### La aplicación no resuelve propiedades de SurrealDB

Verifica que las propiedades `SURREALDB_*` estén definidas o que la identidad de Azure
pueda leer los secretos correspondientes en Key Vault.

### Fallo de autenticación con Azure Key Vault

Comprueba `AZURE_CLIENT_ID`, `AZURE_CLIENT_SECRET` y `AZURE_TENANT_ID`, además de los
permisos de la identidad sobre el vault.

### Docker Compose no encuentra el frontend

El servicio `frontend` usa un contexto fuera de este repositorio. Clona o mueve el
frontend a `../../CatalogoParametros-FrontEnd/catalogo-parametros-front`, o ajusta el
contexto de construcción localmente.

### Mockito no puede inicializar Byte Buddy

Algunas distribuciones de JDK restringen la conexión dinámica de agentes. Si aparece
`Could not initialize inline Byte Buddy mock maker`, utiliza una distribución compatible
de JDK 21 o configura Mockito como agente de Java en Maven antes de interpretar el error
como un fallo funcional de las pruebas.

### No aparecen trazas en Jaeger

Confirma que `otel-collector` y `jaeger` estén saludables, realiza una petición HTTP al
backend y busca el servicio `catalogo-parametros` en Jaeger.

## Documentación adicional

- [Swagger y contrato OpenAPI](SWAGGER.md)
- [Consumo de servicios](docs/CONSUMO-SERVICIOS.md)
- [Telemetría y observabilidad](TELEMETRY.md)
