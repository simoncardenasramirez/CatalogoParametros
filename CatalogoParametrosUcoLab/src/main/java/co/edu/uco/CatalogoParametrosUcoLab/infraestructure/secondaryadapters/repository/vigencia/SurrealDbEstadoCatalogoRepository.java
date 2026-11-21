package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.vigencia;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;

@Repository
public final class SurrealDbEstadoCatalogoRepository implements EstadoCatalogoRepository {
    private final SurrealDbClient client;

    public SurrealDbEstadoCatalogoRepository(final SurrealDbClient client) {
        this.client = client;
    }

    @Override
    public CatalogoVigencia consultar() {
        // Una sola llamada HTTP y solo columnas de disponibilidad: sin nombres, valores ni hashes.
        var respuesta = client.execute("""
                SELECT id FROM organizaciones;
                SELECT id, idOrganizacion, activa, fechaInicio, fechaFinal FROM aplicaciones;
                SELECT id, idAplicacion, activo, fechaInicio, fechaFinal FROM modulos;
                SELECT id, idModulo, activo, fechaInicio, fechaFinal FROM funcionalidades;
                SELECT id, idFuncionalidad, activo FROM parametros;
                SELECT id, idParametro, idAmbiente, idEstadoMetadatoAmbiente FROM metadatos_ambiente;
                """);
        if (respuesta.size() < 6) throw co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException
                .build("SurrealDB no devolvio la proyeccion completa de vigencias.");
        var primero = inicioResultados(respuesta, 6);
        var registros = new ArrayList<RegistroVigencia>();
        agregar(registros, resultado(respuesta, primero), TipoRegistroVigencia.ORGANIZACION, null, null, null, null, null, null);
        agregar(registros, resultado(respuesta, primero + 1), TipoRegistroVigencia.APLICACION, "idOrganizacion", "activa", "fechaInicio", "fechaFinal", null, null);
        agregar(registros, resultado(respuesta, primero + 2), TipoRegistroVigencia.MODULO, "idAplicacion", "activo", "fechaInicio", "fechaFinal", null, null);
        agregar(registros, resultado(respuesta, primero + 3), TipoRegistroVigencia.FUNCIONALIDAD, "idModulo", "activo", "fechaInicio", "fechaFinal", null, null);
        agregar(registros, resultado(respuesta, primero + 4), TipoRegistroVigencia.PARAMETRO, "idFuncionalidad", "activo", null, null, null, null);
        agregar(registros, resultado(respuesta, primero + 5), TipoRegistroVigencia.METADATO_AMBIENTE, "idParametro", null, null, null, "idAmbiente", "idEstadoMetadatoAmbiente");
        return new CatalogoVigencia(registros, estadosMetadatoAmbiente());
    }

    private tools.jackson.databind.JsonNode resultado(final tools.jackson.databind.JsonNode respuesta,
            final int indice) {
        return respuesta.path(indice).path("result");
    }

    private int inicioResultados(final tools.jackson.databind.JsonNode respuesta, final int cantidad) {
        // SurrealDbClient incluye las sentencias de contexto; otros adaptadores pueden devolver solo las proyecciones.
        if (respuesta.size() <= cantidad || esProyeccionDirecta(respuesta.path(0))) return 0;
        return respuesta.size() - cantidad;
    }

    private boolean esProyeccionDirecta(final tools.jackson.databind.JsonNode respuesta) {
        return respuesta.isObject() && !respuesta.has("status") && respuesta.path("result").isArray();
    }

    private java.util.Map<UUID, String> estadosMetadatoAmbiente() {
        var resultado = new java.util.HashMap<UUID, String>();
        EstadoMetadatoAmbienteCatalogoRecord.valores()
                .forEach(estado -> resultado.put(estado.id(), estado.nombre()));
        return resultado;
    }

    private void agregar(final List<RegistroVigencia> destino, final tools.jackson.databind.JsonNode filas,
            final TipoRegistroVigencia tipo, final String padre, final String activo, final String inicio,
            final String fin, final String ambiente, final String estado) {
        for (var fila : filas) {
            var id = uuid(fila.path("id"));
            if (id == null) continue;
            destino.add(new RegistroVigencia(tipo, id, padre == null ? null : uuid(fila.path(padre)),
                    ambiente == null ? null : uuid(fila.path(ambiente)), estado == null ? null : uuid(fila.path(estado)),
                    activo == null || fila.path(activo).asBoolean(), instante(fila, inicio), instante(fila, fin)));
        }
    }

    private java.time.Instant instante(final tools.jackson.databind.JsonNode fila, final String campo) {
        if (campo == null) return null;
        var fecha = co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.DateTimeHelper.parse(fila.path(campo));
        return fecha == null ? null : fecha.toInstant();
    }

    private UUID uuid(final tools.jackson.databind.JsonNode valor) {
        try {
            var texto = valor.asString().replace("`", "").replace("u'", "").replace("'", "");
            return UUID.fromString(texto.substring(texto.indexOf(':') + 1));
        } catch (final IllegalArgumentException exception) { return null; }
    }

    @Override
    public int normalizarFechasUtc() {
        var tablas = List.of("organizaciones", "aplicaciones", "modulos", "funcionalidades");
        var consulta = new StringBuilder();
        for (var tabla : tablas) {
            consulta.append("SELECT id, fechaInicio, fechaFinal FROM ").append(tabla).append(";\n");
        }
        var respuesta = client.execute(consulta.toString());
        var cambios = new StringBuilder("BEGIN TRANSACTION;\n");
        int cantidad = 0;
        var primero = inicioResultados(respuesta, tablas.size());
        for (int i = 0; i < tablas.size(); i++) {
            var tabla = tablas.get(i);
            var filas = resultado(respuesta, primero + i);
            for (var fila : filas) {
                UUID id;
                try {
                    var valor = fila.path("id").asString().replace("`", "").replace("u'", "").replace("'", "");
                    id = UUID.fromString(valor.substring(valor.indexOf(':') + 1));
                } catch (final IllegalArgumentException exception) { continue; }
                var campos = List.of("fechaInicio", "fechaFinal");
                for (var campo : campos) {
                    var fecha = co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.DateTimeHelper.parse(fila.path(campo));
                    if (fecha == null) continue;
                    var anterior = fila.path(campo).asString();
                    var canonica = java.time.format.DateTimeFormatter.ISO_INSTANT.format(fecha.toInstant());
                    if (anterior.equals(canonica)) continue;
                    cambios.append("UPDATE type::record('").append(tabla).append("', '").append(id)
                            .append("') SET ").append(campo).append(" = '").append(escape(canonica))
                            .append("' WHERE ").append(campo).append(" = '").append(escape(anterior)).append("';\n");
                    cantidad++;
                }
            }
        }
        if (cantidad > 0) client.execute(cambios.append("COMMIT TRANSACTION;").toString());
        return cantidad;
    }

    private String escape(final String valor) {
        return valor.replace("\\", "\\\\").replace("'", "\\'");
    }

    @Override
    public void desactivar(final List<RegistroVigencia> registros, final UUID estadoAsociacionInactivo) {
        if (registros.isEmpty()) return;
        var query = new StringBuilder("BEGIN TRANSACTION;\n");
        for (var registro : registros) {
            var tabla = switch (registro.tipo()) {
                case APLICACION -> "aplicaciones";
                case MODULO -> "modulos";
                case FUNCIONALIDAD -> "funcionalidades";
                case PARAMETRO -> "parametros";
                case METADATO_AMBIENTE -> "metadatos_ambiente";
                default -> throw co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException.build("El registro no tiene estado modificable.");
            };
            var campo = switch (registro.tipo()) {
                case APLICACION -> "activa";
                case METADATO_AMBIENTE -> "idEstadoMetadatoAmbiente";
                default -> "activo";
            };
            var estado = switch (registro.tipo()) {
                case METADATO_AMBIENTE -> "'" + estadoAsociacionInactivo + "'";
                default -> "false";
            };
            var anterior = registro.idEstado() == null ? "true" : "'" + registro.idEstado() + "'";
            query.append("UPDATE type::record('").append(tabla).append("', '").append(registro.id())
                    .append("') SET ").append(campo).append(" = ").append(estado)
                    .append(" WHERE ").append(campo).append(" = ").append(anterior);
            // No desactivar una fila que fue movida o cuya vigencia fue corregida concurrentemente.
            var padre = switch (registro.tipo()) {
                case APLICACION -> "idOrganizacion";
                case MODULO -> "idAplicacion";
                case FUNCIONALIDAD -> "idModulo";
                case PARAMETRO -> "idFuncionalidad";
                case METADATO_AMBIENTE -> "idParametro";
                default -> "";
            };
            query.append(" AND ").append(padre).append(" = '").append(registro.idPadre()).append("'");
            if (registro.tipo() == TipoRegistroVigencia.APLICACION || registro.tipo() == TipoRegistroVigencia.MODULO
                    || registro.tipo() == TipoRegistroVigencia.FUNCIONALIDAD) {
                if (registro.fin() == null) query.append(" AND (fechaFinal = NONE OR fechaFinal = NULL)");
                else query.append(" AND <datetime>fechaFinal = d'").append(registro.fin()).append("'");
            }
            query.append(";\n");
        }
        query.append("COMMIT TRANSACTION;");
        // Todos los valores interpolados son UUID, Instant o literales internos, nunca texto de usuario.
        client.execute(query.toString());
    }
}
