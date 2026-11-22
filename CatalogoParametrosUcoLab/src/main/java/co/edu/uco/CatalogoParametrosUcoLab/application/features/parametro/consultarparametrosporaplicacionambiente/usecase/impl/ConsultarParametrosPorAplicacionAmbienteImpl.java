package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.usecase.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente.MetadatoAmbienteDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente.MetadatoDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente.ParametroDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;

@Service
public final class ConsultarParametrosPorAplicacionAmbienteImpl implements ConsultarParametrosPorAplicacionAmbiente {
    private final ParametroRepository parametros;
    private final AplicacionRepository aplicaciones;
    private final AmbienteRepository ambientes;
    private final FuncionalidadRepository funcionalidades;
    private final ModuloRepository modulos;
    private final TipoParametroRepository tiposParametro;
    private final MetadatoRepository metadatos;
    private final TipoMetadatoRepository tiposMetadato;
    private final MetadatoAmbienteRepository metadatosAmbiente;
    private final EstadoMetadatoAmbienteRepository estadosMetadatoAmbiente;

    public ConsultarParametrosPorAplicacionAmbienteImpl(final ParametroRepository parametros,
            final AplicacionRepository aplicaciones, final AmbienteRepository ambientes,
            final FuncionalidadRepository funcionalidades, final ModuloRepository modulos,
            final TipoParametroRepository tiposParametro, final MetadatoRepository metadatos,
            final TipoMetadatoRepository tiposMetadato, final MetadatoAmbienteRepository metadatosAmbiente,
            final EstadoMetadatoAmbienteRepository estadosMetadatoAmbiente) {
        this.parametros = parametros;
        this.aplicaciones = aplicaciones;
        this.ambientes = ambientes;
        this.funcionalidades = funcionalidades;
        this.modulos = modulos;
        this.tiposParametro = tiposParametro;
        this.metadatos = metadatos;
        this.tiposMetadato = tiposMetadato;
        this.metadatosAmbiente = metadatosAmbiente;
        this.estadosMetadatoAmbiente = estadosMetadatoAmbiente;
    }

    @Override
    public Resultado execute(final java.util.UUID aplicacionId, final java.util.UUID ambienteId) {
        var detalles = parametros.findByAplicacionAndAmbiente(aplicacionId, ambienteId)
                .stream().filter(ParametroEntity::isActivo).map(parametro -> {
                    var funcionalidad = funcionalidades.findById(parametro.getIdFuncionalidad()).orElse(null);
                    var modulo = funcionalidad == null ? null
                            : modulos.findById(funcionalidad.getIdModulo()).orElse(null);
                    var metadata = metadatos.findByIdParametro(parametro.getId()).stream()
                            .filter(metadato -> ambienteId.equals(metadato.getIdAmbiente()))
                            .map(metadato -> new MetadatoDetalle(metadato,
                                    tiposMetadato.findById(metadato.getIdTipoMetadato()).orElse(null)))
                            .toList();
                    var configuracion = metadatosAmbiente
                            .findByIdParametroAndIdAmbiente(parametro.getId(), ambienteId).stream()
                            .findFirst().map(asociacion -> new MetadatoAmbienteDetalle(asociacion,
                                    estadosMetadatoAmbiente.findById(
                                            asociacion.getIdEstadoMetadatoAmbiente()).orElse(null)))
                            .orElse(null);
                    return new ParametroDetalle(parametro,
                            tiposParametro.findById(parametro.getIdTipoParametro()).orElse(null),
                            funcionalidad, modulo, metadata, configuracion);
                }).toList();
        return new Resultado(aplicaciones.findById(aplicacionId).orElse(null),
                ambientes.findById(ambienteId).orElse(null), detalles);
    }
}
