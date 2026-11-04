package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.bulk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaError;
import tools.jackson.databind.ObjectMapper;

class CargaMasivaExcelAdapterTest {
    private final CargaMasivaExcelAdapter adapter = new CargaMasivaExcelAdapter(new ObjectMapper());

    @Test
    void debeGenerarYLeerPlantillaCuandoElArchivoEsValido() {
        var libro = adapter.leer(adaptarPlantilla("App", "Modulo", "Funcion", "Parametro",
                "tipo", "json", "dev", "vigente", "{\"clave\":\"valor\"}"));

        assertEquals(1, libro.filas().size());
        assertTrue(libro.errores().isEmpty());
        assertEquals("App", libro.filas().getFirst().aplicacion());
        assertEquals("json", libro.filas().getFirst().tipoMetadato());
        assertEquals("valor", libro.filas().getFirst().valor().get("clave").asText());
    }

    @Test
    void debeRechazarArchivoVacioYHojaInexistente() throws Exception {
        var vacio = adapter.leer(new byte[0]);
        assertEquals("EMPTY_FILE", vacio.errores().getFirst().codigo());

        byte[] sinCatalogo;
        try (var workbook = new XSSFWorkbook(); var output = new ByteArrayOutputStream()) {
            workbook.createSheet("Otra");
            workbook.write(output);
            sinCatalogo = output.toByteArray();
        }
        assertEquals("MISSING_SHEET", adapter.leer(sinCatalogo).errores().getFirst().codigo());
    }

    @Test
    void debeReportarEncabezadosJsonYFormulaCuandoElLibroEsInvalido() throws Exception {
        byte[] archivo;
        try (var workbook = new XSSFWorkbook(); var output = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("Catalogo");
            sheet.createRow(0).createCell(0).setCellValue("incorrecto");
            workbook.write(output);
            archivo = output.toByteArray();
        }
        assertEquals("INVALID_HEADERS", adapter.leer(archivo).errores().getFirst().codigo());

        archivo = adaptarPlantilla("App", "Modulo", "Funcion", "Parametro", "tipo", "json", "dev", "vigente", "{invalido");
        var resultado = adapter.leer(archivo);
        assertTrue(resultado.errores().stream().anyMatch(error -> "INVALID_JSON".equals(error.codigo())));

        try (var workbook = WorkbookFactory.create(new ByteArrayInputStream(adaptarPlantilla(
                "App", "Modulo", "Funcion", "Parametro", "tipo", "json", "dev", "vigente", "{}")))) {
            workbook.getSheet("Catalogo").getRow(1).getCell(0).setCellFormula("1+1");
            try (var output = new ByteArrayOutputStream()) {
                workbook.write(output);
                var conFormula = adapter.leer(output.toByteArray());
                assertTrue(conFormula.errores().stream().anyMatch(error -> "UNSUPPORTED_CELL".equals(error.codigo())));
            }
        }
    }

    @Test
    void debeAnotarReporteDeErroresCuandoHayErroresPorFila() {
        var archivo = adaptarPlantilla("App", "Modulo", "Funcion", "Parametro", "tipo", "json", "dev", "vigente", "{}");
        var reporte = adapter.anotar(archivo, List.of(new CargaMasivaError(2, "Catalogo", "Aplicacion", "App",
                "APPLICATION_NOT_FOUND", "La aplicacion no existe.")));
        var libro = adapter.leer(reporte);
        assertFalse(reporte.length == 0);
        assertNotNull(libro);
    }

    private byte[] adaptarPlantilla(final String aplicacion, final String modulo, final String funcionalidad,
            final String parametro, final String tipoParametro, final String tipoMetadato, final String ambiente,
            final String estado, final String valor) {
        try (var workbook = WorkbookFactory.create(new ByteArrayInputStream(adapter.plantilla()));
                var output = new ByteArrayOutputStream()) {
            var row = workbook.getSheet("Catalogo").createRow(1);
            String[] values = { aplicacion, modulo, funcionalidad, "", "", "true", parametro, tipoParametro,
                    "true", tipoMetadato, ambiente, estado, valor };
            for (var index = 0; index < values.length; index++) row.createCell(index).setCellValue(values[index]);
            workbook.write(output);
            return output.toByteArray();
        } catch (Exception exception) {
            throw new AssertionError(exception);
        }
    }
}
