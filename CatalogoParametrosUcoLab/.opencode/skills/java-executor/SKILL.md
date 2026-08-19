---
name: java-executor
description: Compila, empaqueta y ejecuta el backend Java/Maven de CatalogoParametrosUcoLab usando el wrapper mvnw.cmd (PowerShell/Windows). Úsala para compilar (compile), empaquetar (clean package -DskipTests), ejecutar la app (spring-boot:run) o validar un build tras generar o modificar código.
---

# Java Executor Skill

Compilación y empaquetado del backend `CatalogoParametrosUcoLab` con Maven. La shell es PowerShell en Windows → usar `.\mvnw.cmd`.

## 1. Contexto

- Java 21, Spring Boot 4.0.6 (WebFlux), Maven 3.9.x, JUnit 5 (Surefire).
- Directorio de trabajo: raíz del backend (`CatalogoParametrosUcoLab/`).
- Sin Maven en PATH → siempre el wrapper `.\mvnw.cmd`.
- Arquitectura hexagonal: un error de compilación con imports de `infraestructure`/SurrealDB/Azure dentro de `application` es violación de arquitectura (reportarlo, no "arreglarlo" a ciegas).

## 2. Comandos

```powershell
# Compilar (sin tests)
.\mvnw.cmd compile

# Compilar + tests
.\mvnw.cmd test

# Empaquetar JAR sin tests
.\mvnw.cmd clean package -DskipTests

# Ejecutar la aplicación (requiere Azure Key Vault + SurrealDB configurados)
.\mvnw.cmd spring-boot:run

# Test específico
.\mvnw.cmd test -Dtest=PropertiesHelperTest
.\mvnw.cmd test -Dtest=CatalogoParametrosUcoLabApplicationTests
```

## 3. Verificación de resultados

- `BUILD SUCCESS` / `BUILD FAILURE`.
- Errores de compilación: `[ERROR] ...\Archivo.java:[linea,col]` — tomar el primer `[ERROR]` relevante como causa raíz.
- Resumen de tests: `Tests run: X, Failures: Y, Errors: Z`.
- Artefacto generado: `target/CatalogoParametrosUcoLab-0.0.1-SNAPSHOT.jar`.

## 4. Reglas

- Usar siempre `.\mvnw.cmd`.
- `clean` solo si es necesario (más lento).
- No ejecutar `spring-boot:run` sin confirmar; la app necesita Azure/SurrealDB configurados.
- No editar archivos; solo compilar/empaquetar y reportar.