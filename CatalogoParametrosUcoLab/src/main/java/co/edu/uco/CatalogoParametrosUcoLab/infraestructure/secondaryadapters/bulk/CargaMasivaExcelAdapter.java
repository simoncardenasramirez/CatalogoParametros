package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.bulk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaError;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaFila;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaLibro;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.bulk.CargaMasivaExcelPort;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.JsonNodeFactory;

@Component
public final class CargaMasivaExcelAdapter implements CargaMasivaExcelPort {
    public static final int MAX_BYTES = 5 * 1024 * 1024;
    public static final int MAX_FILAS = 1_000;
    private static final String HOJA = "Catalogo";
    private static final List<String> ENCABEZADOS = List.of(
            "Aplicacion", "Modulo", "Funcionalidad", "Fecha inicio funcionalidad",
            "Fecha final funcionalidad", "Activo funcionalidad", "Parametro", "Tipo parametro",
            "Activo parametro", "Tipo metadato", "Ambiente", "Estado metadato ambiente", "Valor");
    private static final List<String> BOOLEANOS = List.of("true", "false");

    private final ObjectMapper objectMapper;

    public CargaMasivaExcelAdapter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] plantilla() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet(HOJA);
            var header = sheet.createRow(0);
            for (var index = 0; index < ENCABEZADOS.size(); index++) {
                header.createCell(index).setCellValue(ENCABEZADOS.get(index));
                sheet.setColumnWidth(index, index == 12 ? 20_000 : 8_000);
            }
            agregarValidacionBooleanos(sheet, 5);
            agregarValidacionBooleanos(sheet, 8);
            var instructions = workbook.createSheet("Instrucciones");
            instructions.createRow(0).createCell(0).setCellValue("Una fila crea o reutiliza funcionalidad y parametro, y crea metadato y asociacion de ambiente.");
            instructions.createRow(1).createCell(0).setCellValue("Aplicacion, modulo, tipos, ambiente y estado deben existir previamente. No se crean desde el archivo.");
            instructions.createRow(2).createCell(0).setCellValue("Las fechas deben usar ISO-8601 con zona, por ejemplo 2026-01-01T00:00:00-05:00.");
            instructions.createRow(3).createCell(0).setCellValue("Para tipo JSON use un objeto o arreglo JSON. Para alfanumerico y date use texto.");
            instructions.createRow(4).createCell(0).setCellValue("Maximo 1.000 filas. No use formulas ni modifique los encabezados.");
            workbook.write(output);
            return output.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible generar la plantilla de carga masiva.", exception);
        }
    }

    @Override
    public CargaMasivaLibro leer(final byte[] archivo) {
        var errores = new ArrayList<CargaMasivaError>();
        var filas = new ArrayList<CargaMasivaFila>();
        if (archivo == null || archivo.length == 0) {
            errores.add(error(0, "file", "", "EMPTY_FILE", "El archivo esta vacio."));
            return new CargaMasivaLibro(filas, errores);
        }
        if (archivo.length > MAX_BYTES) {
            errores.add(error(0, "file", "", "FILE_TOO_LARGE", "El archivo supera el limite de 5 MB."));
            return new CargaMasivaLibro(filas, errores);
        }
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(archivo))) {
            var sheet = workbook.getSheet(HOJA);
            if (sheet == null) {
                errores.add(error(0, "file", "", "MISSING_SHEET", "Falta la hoja Catalogo."));
                return new CargaMasivaLibro(filas, errores);
            }
            if (!encabezadosValidos(sheet.getRow(0))) {
                errores.add(error(1, "headers", "", "INVALID_HEADERS", "Los encabezados no corresponden a la plantilla vigente."));
                return new CargaMasivaLibro(filas, errores);
            }
            if (sheet.getLastRowNum() > MAX_FILAS) {
                errores.add(error(0, "file", "", "TOO_MANY_ROWS", "El archivo supera las 1.000 filas de datos."));
            }
            for (var index = 1; index <= Math.min(sheet.getLastRowNum(), MAX_FILAS); index++) {
                var row = sheet.getRow(index);
                var values = new String[ENCABEZADOS.size()];
                var nonEmpty = false;
                for (var column = 0; column < ENCABEZADOS.size(); column++) {
                    values[column] = celda(row, column, index + 1, errores);
                    nonEmpty |= !values[column].isBlank();
                }
                if (!nonEmpty) continue;
                filas.add(new CargaMasivaFila(index + 1, values[0], values[1], values[2], values[3], values[4],
                        values[5], values[6], values[7], values[8], values[9], values[10], values[11], valor(values[12], index + 1, errores)));
            }
            if (filas.isEmpty()) errores.add(error(0, "file", "", "EMPTY_FILE", "El archivo no contiene filas de datos."));
            return new CargaMasivaLibro(filas, errores);
        } catch (Exception exception) {
            errores.add(error(0, "file", "", "INVALID_WORKBOOK", "El archivo no es un libro Excel XLSX valido."));
            return new CargaMasivaLibro(filas, errores);
        }
    }

    @Override
    public byte[] anotar(final byte[] archivo, final List<CargaMasivaError> errores) {
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(archivo));
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            var sheet = workbook.getSheet(HOJA);
            if (sheet == null) throw new IllegalArgumentException("Falta la hoja Catalogo.");
            var header = sheet.getRow(0);
            header.createCell(ENCABEZADOS.size()).setCellValue("Codigo de error");
            header.createCell(ENCABEZADOS.size() + 1).setCellValue("Descripcion de error");
            Map<Integer, List<CargaMasivaError>> porFila = new HashMap<>();
            for (var error : errores) if (error.fila() > 0) porFila.computeIfAbsent(error.fila(), ignored -> new ArrayList<>()).add(error);
            for (var entry : porFila.entrySet()) {
                var row = sheet.getRow(entry.getKey() - 1);
                if (row == null) row = sheet.createRow(entry.getKey() - 1);
                row.createCell(ENCABEZADOS.size()).setCellValue(entry.getValue().stream().map(CargaMasivaError::codigo).distinct().reduce((a, b) -> a + ", " + b).orElse(""));
                row.createCell(ENCABEZADOS.size() + 1).setCellValue(entry.getValue().stream().map(CargaMasivaError::descripcion).distinct().reduce((a, b) -> a + " | " + b).orElse(""));
            }
            workbook.write(output);
            return output.toByteArray();
        } catch (Exception exception) {
            throw new IllegalArgumentException("No fue posible generar el reporte de errores.", exception);
        }
    }

    private void agregarValidacionBooleanos(final Sheet sheet, final int column) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(BOOLEANOS.toArray(String[]::new));
        DataValidation validation = helper.createValidation(constraint, new CellRangeAddressList(1, MAX_FILAS, column, column));
        validation.setSuppressDropDownArrow(false);
        sheet.addValidationData(validation);
    }

    private boolean encabezadosValidos(final Row row) {
        if (row == null) return false;
        for (var index = 0; index < ENCABEZADOS.size(); index++) {
            if (!ENCABEZADOS.get(index).equals(celdaTexto(row.getCell(index)))) return false;
        }
        return true;
    }

    private String celda(final Row row, final int column, final int fila, final List<CargaMasivaError> errores) {
        if (row == null) return "";
        Cell cell = row.getCell(column, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null || cell.getCellType() == CellType.BLANK) return "";
        if (cell.getCellType() == CellType.FORMULA || cell.getCellType() == CellType.ERROR) {
            errores.add(error(fila, ENCABEZADOS.get(column), "", "UNSUPPORTED_CELL", "No se admiten formulas ni celdas de error."));
            return "";
        }
        return celdaTexto(cell);
    }

    private String celdaTexto(final Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return "";
        return new org.apache.poi.ss.usermodel.DataFormatter().formatCellValue(cell).trim();
    }

    private JsonNode valor(final String raw, final int fila, final List<CargaMasivaError> errores) {
        if (raw == null || raw.isBlank()) return null;
        if (!raw.trim().startsWith("{") && !raw.trim().startsWith("[")) return JsonNodeFactory.instance.stringNode(raw.trim());
        try {
            return objectMapper.readTree(raw.trim());
        } catch (Exception exception) {
            errores.add(error(fila, "Valor", raw, "INVALID_JSON", "El valor JSON no tiene un formato valido."));
            return null;
        }
    }

    private static CargaMasivaError error(final int fila, final String campo, final String valor,
            final String codigo, final String descripcion) {
        var seguro = valor == null ? "" : valor;
        return new CargaMasivaError(fila, HOJA, campo, seguro.length() > 100 ? seguro.substring(0, 100) : seguro, codigo, descripcion);
    }
}
