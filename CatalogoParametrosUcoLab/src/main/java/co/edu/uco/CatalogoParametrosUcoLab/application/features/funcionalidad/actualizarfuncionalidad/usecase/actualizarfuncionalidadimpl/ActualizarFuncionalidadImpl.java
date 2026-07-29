package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.actualizarfuncionalidadimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.event.ActualizarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class ActualizarFuncionalidadImpl implements ActualizarFuncionalidad {

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
            throw new co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException(
                    "El id de la funcionalidad es obligatorio para actualizar.");
        }

        if (funcionalidadRepository.findById(data.getId()).isEmpty()) {
            throw new co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception.FuncionalidadException(
                    "No existe una funcionalidad con el id especificado.");
        }

        actualizarFuncionalidadRuleValidator.validate(data);

        var entity = FuncionalidadEntity.create(data.getId(), data.getNombre(), data.getIdModulo(),
                data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
        var updatedEntity = funcionalidadRepository.update(entity);
        actualizarFuncionalidadPublisher.sendEvent(ActualizarFuncionalidadEvent.updated(updatedEntity));
    }
}