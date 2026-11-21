package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporidentidadservicio.usecase.impl;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporidentidadservicio.ConsultarParametrosPorIdentidadServicio;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.IdentidadServicioRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ForbiddenException;

@Service
public final class ConsultarParametrosPorIdentidadServicioImpl implements ConsultarParametrosPorIdentidadServicio {
    private static final Pattern CLIENT_ID = Pattern.compile("[A-Za-z0-9][A-Za-z0-9._:@-]{0,254}");
    private static final String MENSAJE_IDENTIDAD_NO_ASOCIADA =
            "La identidad de servicio no esta asociada a un ambiente autorizado.";

    private final IdentidadServicioRepository identidades;
    private final ConsultarParametrosPorAplicacionAmbiente consultarPorAplicacionAmbiente;

    public ConsultarParametrosPorIdentidadServicioImpl(final IdentidadServicioRepository identidades,
            final ConsultarParametrosPorAplicacionAmbiente consultarPorAplicacionAmbiente) {
        this.identidades = identidades;
        this.consultarPorAplicacionAmbiente = consultarPorAplicacionAmbiente;
    }

    @Override
    public ConsultarParametrosPorAplicacionAmbiente.Resultado execute(final String clientId) {
        if (clientId == null || !CLIENT_ID.matcher(clientId.trim()).matches()) {
            throw ForbiddenException.build(MENSAJE_IDENTIDAD_NO_ASOCIADA);
        }
        final var identidad = identidades.findByClientId(clientId.trim())
                .orElseThrow(() -> ForbiddenException.build(MENSAJE_IDENTIDAD_NO_ASOCIADA));
        return consultarPorAplicacionAmbiente.execute(identidad.getIdAplicacion(), identidad.getIdAmbiente());
    }

    @Override
    public ConsultarParametrosPorAplicacionAmbiente.Resultado execute(final String clientId,
            final String nombre) {
        final var resultado = execute(clientId);
        if (nombre == null || nombre.isBlank()) {
            return resultado;
        }
        final var nombreBuscado = nombre.trim();
        final var parametros = resultado.parametros().stream()
                .filter(detalle -> detalle.parametro() != null
                        && nombreBuscado.equalsIgnoreCase(detalle.parametro().getNombre()))
                .toList();
        return new ConsultarParametrosPorAplicacionAmbiente.Resultado(
                resultado.aplicacion(), resultado.ambiente(), parametros);
    }
}
