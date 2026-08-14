package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.actualizarfuncionalidadimpl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.event.ActualizarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class ActualizarFuncionalidadImpl implements ActualizarFuncionalidad {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final FuncionalidadRepository funcionalidadRepository;
    private final ActualizarFuncionalidadPublisher actualizarFuncionalidadPublisher;
    private final ActualizarFuncionalidadRuleValidator actualizarFuncionalidadRuleValidator;

    public ActualizarFuncionalidadImpl(final FuncionalidadRepository funcionalidadRepository,
            final ActualizarFuncionalidadPublisher actualizarFuncionalidadPublisher,
            final ActualizarFuncionalidadRuleValidator actualizarFuncionalidadRuleValidator) {
        this.funcionalidadRepository = funcionalidadRepository;
        this.actualizarFuncionalidadPublisher = actualizarFuncionalidadPublisher;
        this.actualizarFuncionalidadRuleValidator = actualizarFuncionalidadRuleValidator;
    }

    @Override
    public void execute(final ActualizarFuncionalidadDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getId())) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-37"));
        }

        if (funcionalidadRepository.findById(data.getId()).isEmpty()) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-36"));
        }

        actualizarFuncionalidadRuleValidator.validate(data);

        var entity = FuncionalidadEntity.create(data.getId(), data.getNombre(), data.getIdModulo(),
                data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
        var updatedEntity = funcionalidadRepository.update(entity);
        actualizarFuncionalidadPublisher.sendEvent(ActualizarFuncionalidadEvent.updated(updatedEntity));
    }
}