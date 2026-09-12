# Cobertura de pruebas

Desde `CatalogoParametrosUcoLab`, ejecutar `./mvnw.cmd clean verify` en Windows
o `bash ./mvnw clean verify` en Linux. JaCoCo instrumenta las pruebas, genera
`target/site/jacoco/index.html` y `target/site/jacoco/jacoco.xml`, y rechaza
la compilacion si la cobertura global de lineas es inferior al 80 %.
La regla incluye todas las clases de produccion, sin exclusiones de cobertura.

GitHub Actions publica la carpeta del reporte como artefacto `cobertura-jacoco`.
Descargarlo, descomprimirlo y abrir `index.html` para navegar por paquetes y clases.
El XML se envia a Sonar incluso cuando el control de cobertura de Maven falla;
ese fallo sigue dejando el workflow en rojo. El scanner espera el Quality Gate
con `sonar.qualitygate.wait=true` y falla si Sonar lo rechaza.

## Configuracion en SonarQube Cloud

El umbral del Quality Gate se configura en SonarQube Cloud; el `pom.xml` solo
configura el umbral de JaCoCo y la ubicacion del reporte que importa Sonar.

1. En la organizacion, abrir **Quality Gates** y crear o copiar un gate si es
   necesario. Configurar **Coverage** en **New Code** para fallar por debajo de
   `80 %`. Para exigirlo tambien en la rama principal, agregar la condicion
   equivalente en **Overall Code**.
2. En el proyecto, abrir **Quality Gate**, seleccionar ese gate y guardar.
3. Desmarcar **Ignore duplication and coverage on small changes** y guardar.
   De lo contrario, Sonar puede omitir la condicion en cambios pequenos.
4. En GitHub, agregar **Construir y analizar** como comprobacion obligatoria en
   la regla de proteccion de `master` si se quiere impedir el merge cuando falla.

Las condiciones globales de Sonar no sustituyen la comprobacion global de Maven
en los PR. Ademas, la metrica **Coverage** de Sonar combina lineas y condiciones,
por lo que puede diferir del porcentaje de lineas de JaCoCo.

Referencias:

- https://www.jacoco.org/jacoco/trunk/doc/check-mojo.html
- https://docs.sonarsource.com/sonarqube-cloud/enriching/test-coverage/java-test-coverage
- https://docs.sonarsource.com/sonarqube-cloud/managing-your-projects/project-analysis/changing-quality-gate
