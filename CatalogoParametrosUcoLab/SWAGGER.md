# Swagger UI y contrato OpenAPI

El contrato acordado para consumo entre módulos está versionado en
[`docs/openapi/catalogo-parametros-v1.yaml`](docs/openapi/catalogo-parametros-v1.yaml).
La guía de integración, ejemplos, errores y política de evolución está en
[`docs/CONSUMO-SERVICIOS.md`](docs/CONSUMO-SERVICIOS.md).

El documento generado por Springdoc es una vista **code-first** útil para comparar la
implementación con el contrato. No debe utilizarse como fuente de verdad para diseñar
integraciones nuevas.

Inicia la aplicación con Java 21 y la configuración habitual de Azure Key Vault y SurrealDB:

```sh
./mvnw spring-boot:run
```

- Interfaz para consultar y probar endpoints: http://localhost:8080/swagger-ui.html
- Contrato OpenAPI versionado servido por la aplicación: http://localhost:8080/openapi/catalogo-parametros-v1.yaml
- Especificación OpenAPI JSON: http://localhost:8080/v3/api-docs
- Especificación OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml

Si cambias el host o el puerto de la aplicación, ajusta estas direcciones.
Swagger UI carga el contrato versionado bajo `docs/openapi`; los endpoints `/v3/api-docs`
siguen mostrando el documento inferido automáticamente de los controladores para poder
detectar diferencias. Usa **Try it out** y
**Execute** para realizar una petición contra la aplicación en ejecución; las operaciones
de creación, actualización y eliminación modifican los datos de esa instancia.

Los endpoints `/events` usan Server-Sent Events y mantienen una conexión abierta.
Para consumir estos flujos puedes usar `curl -N` o un cliente compatible con SSE.
