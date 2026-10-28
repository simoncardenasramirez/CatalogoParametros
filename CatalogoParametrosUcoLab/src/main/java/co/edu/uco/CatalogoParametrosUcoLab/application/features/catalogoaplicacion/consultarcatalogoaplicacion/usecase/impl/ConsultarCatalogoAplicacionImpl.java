package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.usecase.impl;

import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.ConsultarCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ConsultarCatalogoAplicacionImpl implements ConsultarCatalogoAplicacion {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsultarCatalogoAplicacionImpl.class);
    private final BaseDatosAplicacionRepository aplicaciones;
    private final BaseDatosModuloRepository modulos;
    private final BaseDatosFuncionalidadRepository funcionalidades;
    private final BaseDatosParametroRepository parametros;
    private final BaseDatosMetadatoRepository metadatos;
    private final BaseDatosMetadatoAmbienteRepository asociaciones;
    private final BaseDatosAmbienteRepository ambientes;
    private final BaseDatosTipoParametroRepository tiposParametro;
    private final BaseDatosTipoMetadatoRepository tiposMetadato;
    private final BaseDatosEstadoMetadatoAmbienteRepository estados;
    private final TelemetryService telemetry;
    public ConsultarCatalogoAplicacionImpl(final BaseDatosAplicacionRepository aplicaciones,
            final BaseDatosModuloRepository modulos,
            final BaseDatosFuncionalidadRepository funcionalidades,
            final BaseDatosParametroRepository parametros,
            final BaseDatosMetadatoRepository metadatos,
            final BaseDatosMetadatoAmbienteRepository asociaciones,
            final BaseDatosAmbienteRepository ambientes,
            final BaseDatosTipoParametroRepository tiposParametro,
            final BaseDatosTipoMetadatoRepository tiposMetadato,
            final BaseDatosEstadoMetadatoAmbienteRepository estados,
            final TelemetryService telemetry) {
        this.aplicaciones = aplicaciones;
        this.modulos = modulos;
        this.funcionalidades = funcionalidades;
        this.parametros = parametros;
        this.metadatos = metadatos;
        this.asociaciones = asociaciones;
        this.ambientes = ambientes;
        this.tiposParametro = tiposParametro;
        this.tiposMetadato = tiposMetadato;
        this.estados = estados;
        this.telemetry = telemetry;
    }
    @Override public CatalogoAplicacionEntity execute(final UUID idAplicacion) {
        return telemetry.recordBusinessOperation("consultar-catalogo-aplicacion", () -> {
            var app = aplicaciones.findById(idAplicacion)
                    .orElseThrow(() -> NotFoundException.build("La aplicacion no existe."));
            var ms = modulos.findAll().stream().filter(e -> idAplicacion.equals(e.getIdAplicacion())).toList();
            var idsModulo = ms.stream().map(ModuloEntity::getId).collect(Collectors.toSet());
            var fs = funcionalidades.findAll().stream().filter(e -> idsModulo.contains(e.getIdModulo())).toList();
            var idsFuncion = fs.stream().map(FuncionalidadEntity::getId).collect(Collectors.toSet());
            var ps = parametros.findAll().stream().filter(e -> idsFuncion.contains(e.getIdFuncionalidad())).toList();
            var idsParametro = ps.stream().map(ParametroEntity::getId).collect(Collectors.toSet());
            var meta = metadatos.findAll().stream().filter(e -> idsParametro.contains(e.getIdParametro())).toList();
            var porAmbiente = asociaciones.findAll().stream().filter(e -> idsParametro.contains(e.getIdParametro())).toList();
            LOGGER.debug("[CONSULTAR-CATALOGO-APLICACION] Aplicacion {}, parametros {}", idAplicacion, ps.size());
            return new CatalogoAplicacionEntity(app, ms, fs, ps, meta, porAmbiente, ambientes.findAll(),
                    tiposParametro.findAll(), tiposMetadato.findAll(), estados.findAll());
        });
    }
}
