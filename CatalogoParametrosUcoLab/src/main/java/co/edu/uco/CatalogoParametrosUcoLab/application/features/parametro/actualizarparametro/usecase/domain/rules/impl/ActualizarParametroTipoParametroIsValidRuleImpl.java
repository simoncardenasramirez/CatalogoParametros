package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroTipoParametroIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarParametroTipoParametroIsValidRuleImpl implements ActualizarParametroTipoParametroIsValidRule {

    private final ConsultarMensajePort consultarMensajePort;
    private final TipoParametroRepository tipoParametroRepository;

    public ActualizarParametroTipoParametroIsValidRuleImpl(final ConsultarMensajePort consultarMensajePort,
            final TipoParametroRepository tipoParametroRepository) {
        this.consultarMensajePort = consultarMensajePort;
        this.tipoParametroRepository = tipoParametroRepository;
    }

    @Override
    public void execute(final ActualizarParametroDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getIdTipoParametro())) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-121"));
        }
        if (tipoParametroRepository.findById(data.getIdTipoParametro()).isEmpty()) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-145"));
        }
    }
}
