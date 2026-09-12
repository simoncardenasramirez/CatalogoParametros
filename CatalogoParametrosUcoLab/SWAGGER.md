# Swagger UI

Inicia la aplicación con Java 21 y la configuración habitual de Azure Key Vault y SurrealDB:

```sh
./mvnw spring-boot:run
```

- Interfaz para consultar y probar endpoints: http://localhost:8080/swagger-ui.html
- Especificación OpenAPI JSON: http://localhost:8080/v3/api-docs
- Especificación OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml

Si cambias el host o el puerto de la aplicación, ajusta estas direcciones.
Swagger documenta automáticamente los controladores bajo `/catalogo-parametros/api/v1/**`,
incluyendo sus parámetros y modelos de petición y respuesta. Usa **Try it out** y
**Execute** para realizar una petición contra la aplicación en ejecución; las operaciones
de creación, actualización y eliminación modifican los datos de esa instancia.

Los endpoints `/events` usan Server-Sent Events y mantienen una conexión abierta.
Para consumir estos flujos puedes usar `curl -N` o un cliente compatible con SSE.
