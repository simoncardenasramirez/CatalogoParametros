package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.bulk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
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
            "Organizacion", "Aplicacion", "Modulo", "Funcionalidad", "Fecha inicio funcionalidad",
            "Fecha final funcionalidad", "Activo funcionalidad", "Parametro", "Tipo parametro",
            "Activo parametro", "Tipo metadato", "Ambiente", "Estado metadato ambiente", "Valor");
    private static final List<String> BOOLEANOS = List.of("true", "false");
    private static final List<String> TIPOS_METADATO = List.of("json", "alfanumerico", "date");

    private final ObjectMapper objectMapper;

    public CargaMasivaExcelAdapter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] plantilla() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet(HOJA);
            var header = sheet.createRow(0);
            var headerStyle = crearEstiloEncabezado(workbook);
            for (var index = 0; index < ENCABEZADOS.size(); index++) {
                header.createCell(index).setCellValue(ENCABEZADOS.get(index));
                header.getCell(index).setCellStyle(headerStyle);
                sheet.setColumnWidth(index, anchoColumna(index));
            }
            header.setHeightInPoints(32);
            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, MAX_FILAS, 0, ENCABEZADOS.size() - 1));
            sheet.setDisplayGridlines(false);
            agregarValidacionBooleanos(sheet, 6);
            agregarValidacionBooleanos(sheet, 9);
            agregarValidacionLista(sheet, 10, TIPOS_METADATO);
            crearHojaInstrucciones(workbook, headerStyle);
            crearHojaEjemplo(workbook, headerStyle);
            crearHojaListas(workbook, headerStyle);
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
                        values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12],
                        valor(values[13], index + 1, errores)));
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
        agregarValidacionLista(sheet, column, BOOLEANOS);
    }

    private void agregarValidacionLista(final Sheet sheet, final int column, final List<String> values) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(values.toArray(String[]::new));
        DataValidation validation = helper.createValidation(constraint, new CellRangeAddressList(1, MAX_FILAS, column, column));
        validation.setSuppressDropDownArrow(false);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }

    private void crearHojaInstrucciones(final Workbook workbook, final CellStyle headerStyle) {
        var instructions = workbook.createSheet("Instrucciones");
        var headers = List.of("Campo", "Obligatorio", "Ejemplo", "Descripción");
        var header = instructions.createRow(0);
        for (var index = 0; index < headers.size(); index++) {
            header.createCell(index).setCellValue(headers.get(index));
            header.getCell(index).setCellStyle(headerStyle);
        }
        var rows = List.of(
                List.of("Organizacion, Aplicacion, Modulo, Funcionalidad", "Sí", "UCO / Catalogo Parametros / Configuracion / Cargar parametros", "Deben existir previamente en el catálogo. La aplicacion se busca dentro de la organizacion indicada."),
                List.of("Fecha inicio funcionalidad", "No", "2026-01-01T00:00:00-05:00", "Formato ISO-8601 con zona horaria."),
                List.of("Fecha final funcionalidad", "No", "2026-12-31T23:59:59-05:00", "No puede ser anterior a la fecha de inicio."),
                List.of("Activo funcionalidad, Activo parametro", "Sí", "true", "Seleccione true o false."),
                List.of("Parametro", "Sí", "tiempo-espera", "Nombre del parámetro que se creará o reutilizará."),
                List.of("Tipo parametro", "Sí", "configuracion", "Debe coincidir con un tipo existente."),
                List.of("Tipo metadato", "Sí", "alfanumerico", "Valores admitidos: json, alfanumerico o date."),
                List.of("Ambiente, Estado metadato ambiente", "Sí", "Develop / Activo", "Deben existir previamente en el catálogo."),
                List.of("Valor", "Sí", "30 o {\"limite\":30}", "Use texto para alfanumerico/date y JSON válido para json."),
                List.of("Archivo completo", "—", "Máximo 1.000 filas", "No cambie los encabezados, no use fórmulas y deje la hoja Catalogo como primera hoja."));
        for (var rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            var row = instructions.createRow(rowIndex + 1);
            var values = rows.get(rowIndex);
            for (var column = 0; column < values.size(); column++) row.createCell(column).setCellValue(values.get(column));
        }
        instructions.setColumnWidth(0, 20_000);
        instructions.setColumnWidth(1, 4_500);
        instructions.setColumnWidth(2, 19_000);
        instructions.setColumnWidth(3, 45_000);
        instructions.createFreezePane(0, 1);
        instructions.setDisplayGridlines(false);
    }

    private void crearHojaEjemplo(final Workbook workbook, final CellStyle headerStyle) {
        var example = workbook.createSheet("Ejemplo");
        var header = example.createRow(0);
        for (var index = 0; index < ENCABEZADOS.size(); index++) {
            header.createCell(index).setCellValue(ENCABEZADOS.get(index));
            header.getCell(index).setCellStyle(headerStyle);
        }
        var rows = List.of(
                List.of("UCO", "Catalogo Parametros", "Configuracion", "Cargar parametros", "2026-01-01T00:00:00-05:00",
                        "", "true", "tiempo-espera", "configuracion", "true", "alfanumerico", "Develop", "Activo", "30"),
                List.of("UCO", "Catalogo Parametros", "Configuracion", "Cargar parametros", "2026-01-01T00:00:00-05:00",
                        "", "true", "max-intentos", "configuracion", "true", "json", "Develop", "Activo", "{\"maxIntentos\":3,\"habilitado\":true}"),
                List.of("UCO", "Catalogo Parametros", "Configuracion", "Cargar parametros", "2026-01-01T00:00:00-05:00",
                        "", "true", "fecha-corte", "configuracion", "true", "date", "Develop", "Activo", "2026-12-31"));
        for (var rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            var row = example.createRow(rowIndex + 1);
            var values = rows.get(rowIndex);
            for (var index = 0; index < values.size(); index++) row.createCell(index).setCellValue(values.get(index));
        }
        for (var index = 0; index < ENCABEZADOS.size(); index++) example.setColumnWidth(index, anchoColumna(index));
        example.createFreezePane(0, 1);
        example.setAutoFilter(new CellRangeAddress(0, rows.size(), 0, ENCABEZADOS.size() - 1));
        example.setDisplayGridlines(false);
    }

    private void crearHojaListas(final Workbook workbook, final CellStyle headerStyle) {
        var lists = workbook.createSheet("Listas");
        var header = lists.createRow(0);
        header.createCell(0).setCellValue("Catálogo");
        header.createCell(1).setCellValue("Valores de ejemplo");
        header.createCell(2).setCellValue("Nota");
        for (var index = 0; index < 3; index++) header.getCell(index).setCellStyle(headerStyle);
        var rows = List.of(
                List.of("Organizacion", "UCO", "Use el nombre exacto de una organizacion existente."),
                List.of("Booleano", "true, false", "Se usa en Activo funcionalidad y Activo parametro."),
                List.of("Tipo metadato", "json, alfanumerico, date", "El tipo debe coincidir con el valor cargado."),
                List.of("Ambiente", "Develop", "Use el nombre real de un ambiente existente."),
                List.of("Estado", "Activo", "Use el nombre real de un estado existente."));
        for (var rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            var row = lists.createRow(rowIndex + 1);
            for (var column = 0; column < 3; column++) row.createCell(column).setCellValue(rows.get(rowIndex).get(column));
        }
        lists.setColumnWidth(0, 18_000);
        lists.setColumnWidth(1, 25_000);
        lists.setColumnWidth(2, 42_000);
        lists.setDisplayGridlines(false);
    }

    private CellStyle crearEstiloEncabezado(final Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        var style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    private int anchoColumna(final int index) {
        return switch (index) {
            case 0, 1, 2, 3, 7, 8, 10, 11, 12 -> 13_000;
            case 4, 5 -> 24_000;
            case 13 -> 22_000;
            default -> 10_000;
        };
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
