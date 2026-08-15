package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.actualizarparametroimpl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.ActualizarParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.ActualizarParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.event.ActualizarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class ActualizarParametroImpl implements ActualizarParametro {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final ParametroRepository parametroRepository;
    private final ActualizarParametroPublisher actualizarParametroPublisher;
    private final ActualizarParametroRuleValidator actualizarParametroRuleValidator;

    public ActualizarParametroImpl(final ParametroRepository parametroRepository,
            final ActualizarParametroPublisher actualizarParametroPublisher,
            final ActualizarParametroRuleValidator actualizarParametroRuleValidator) {
        this.parametroRepository = parametroRepository;
        this.actualizarParametroPublisher = actualizarParametroPublisher;
        this.actualizarParametroRuleValidator = actualizarParametroRuleValidator;
    }

    @Override
    public void execute(final ActualizarParametroDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getId())) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-115"));
        }

        if (parametroRepository.findById(data.getId()).isEmpty()) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-114"));
        }

        actualizarParametroRuleValidator.validate(data);

        var entity = ParametroEntity.create(data.getId(), data.getNombre(), data.getIdFuncionalidad(),
                data.getIdTipoParametro(), data.isActivo());
        var updatedEntity = parametroRepository.update(entity);
        actualizarParametroPublisher.sendEvent(ActualizarParametroEvent.updated(updatedEntity));
    }
}
