---
name: test-runner
description: Ejecuta y analiza los tests (JUnit 5 / Surefire) del backend CatalogoParametrosUcoLab con mvnw.cmd, interpreta la salida (Tests run / Failures / Errors / stack traces) y valida las convenciones de nombres debe<Escenario>Cuando<Condición>. Úsala tras generar código, para depurar un test que falla o para verificar la suite completa.
---

# Test Runner Skill

Ejecución y análisis de tests del backend `CatalogoParametrosUcoLab` (JUnit 5 vía `spring-boot-starter-test`, Maven Surefire).

## 1. Comandos

```powershell
# Suite completa
.\mvnw.cmd test

# Test específico por clase
.\mvnw.cmd test -Dtest=PropertiesHelperTest
.\mvnw.cmd test -Dtest=CatalogoParametrosUcoLabApplicationTests
```

## 2. Ubicación y convenciones de los tests

- `src/test/java/co/edu/uco/CatalogoParametrosUcoLab/`
- Tests existentes: `CatalogoParametrosUcoLabApplicationTests` (contexto `@SpringBootTest`) y `crosscutting/helpers/PropertiesHelperTest`.
- Nombres de métodos: `debe<Escenario>Cuando<Condición>()`.
- Usar `org.junit.jupiter.api.Assertions` (imports estáticos) y excepciones del proyecto en `assertThrows`: `ValidationException` (400), `NotFoundException` (404), `ConflictException` (409), `TechnicalException` (500).
- Para controladores reactivos se recomienda `WebTestClient`; verificar códigos 201/200/404/400 y `mensajes` de la respuesta.
- **Escribir tests no es competencia de esta skill**: usa `test-builder` para las plantillas de escritura y `sub-agents/test-writer` para generarlas. Esta skill solo ejecuta y analiza.

## 3. Análisis de salida

- `Tests run: X, Failures: Y, Errors: Z, Skipped: W` — por clase y total.
- `BUILD SUCCESS` / `BUILD FAILURE`.
- **Failure** → aserción fallida: revisa `expected:` vs `actual:` y la línea.
- **Error** → excepción inesperada: busca `Caused by:`.
- **BUILD FAILURE por compilación** → `[ERROR] ...Archivo.java:[linea,col]`.

## 4. Reglas

- Siempre con `.\mvnw.cmd` desde la raíz del backend.
- Si un test falla y no está claro, aislar con `-Dtest=<Clase>`.