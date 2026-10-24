package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.usecase.impl;

import java.util.List;
import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken.MetadatoAmbienteDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken.MetadatoDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken.ParametroDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.ValidarToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.usecase.domain.ValidarTokenDomain;
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
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TokenRepository;

@Service
public final class ConsultarParametrosPorTokenImpl implements ConsultarParametrosPorToken {
    private final TokenRepository tokens;
    private final ParametroRepository parametros;
    private final ValidarToken validarToken;
    private final AplicacionRepository aplicaciones;
    private final AmbienteRepository ambientes;
    private final FuncionalidadRepository funcionalidades;
    private final ModuloRepository modulos;
    private final TipoParametroRepository tiposParametro;
    private final MetadatoRepository metadatos;
    private final TipoMetadatoRepository tiposMetadato;
    private final MetadatoAmbienteRepository metadatosAmbiente;
    private final EstadoMetadatoAmbienteRepository estadosMetadatoAmbiente;

    public ConsultarParametrosPorTokenImpl(final TokenRepository tokens, final ParametroRepository parametros,
            final ValidarToken validarToken, final AplicacionRepository aplicaciones,
            final AmbienteRepository ambientes, final FuncionalidadRepository funcionalidades,
            final ModuloRepository modulos, final TipoParametroRepository tiposParametro,
            final MetadatoRepository metadatos, final TipoMetadatoRepository tiposMetadato,
            final MetadatoAmbienteRepository metadatosAmbiente,
            final EstadoMetadatoAmbienteRepository estadosMetadatoAmbiente) {
        this.tokens = tokens;
        this.parametros = parametros;
        this.validarToken = validarToken;
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
    public Resultado execute(final UUID tokenId, final String tokenPlano) {
        if (!validarToken.execute(ValidarTokenDomain.create(tokenId, tokenPlano))) {
            return Resultado.noAutorizado();
        }
        return tokens.findById(tokenId).map(token -> {
            var detalles = parametros.findByAplicacionAndAmbiente(token.getAplicacionId(), token.getAmbienteId())
                    .stream().filter(ParametroEntity::isActivo).map(parametro -> {
                        var funcionalidad = funcionalidades.findById(parametro.getIdFuncionalidad()).orElse(null);
                        var modulo = funcionalidad == null ? null
                                : modulos.findById(funcionalidad.getIdModulo()).orElse(null);
                        var metadata = metadatos.findByIdParametro(parametro.getId()).stream()
                                .filter(metadato -> token.getAmbienteId().equals(metadato.getIdAmbiente()))
                                .map(metadato -> new MetadatoDetalle(metadato,
                                        tiposMetadato.findById(metadato.getIdTipoMetadato()).orElse(null)))
                                .toList();
                        var configuracion = metadatosAmbiente
                                .findByIdParametroAndIdAmbiente(parametro.getId(), token.getAmbienteId()).stream()
                                .findFirst().map(asociacion -> new MetadatoAmbienteDetalle(asociacion,
                                        estadosMetadatoAmbiente.findById(
                                                asociacion.getIdEstadoMetadatoAmbiente()).orElse(null)))
                                .orElse(null);
                        return new ParametroDetalle(parametro,
                                tiposParametro.findById(parametro.getIdTipoParametro()).orElse(null),
                                funcionalidad, modulo, metadata, configuracion);
                    }).toList();
            return Resultado.autorizado(aplicaciones.findById(token.getAplicacionId()).orElse(null),
                    ambientes.findById(token.getAmbienteId()).orElse(null), detalles);
        })
                .orElseGet(Resultado::noAutorizado);
    }
}
