package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.actualizarmoduloimpl;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.ActualizarModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.ActualizarModuloRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.event.ActualizarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class ActualizarModuloImpl implements ActualizarModulo {

    private final ModuloRepository moduloRepository;
    private final ActualizarModuloPublisher actualizarModuloPublisher;
    private final ActualizarModuloRuleValidator actualizarModuloRuleValidator;

    public ActualizarModuloImpl(final ModuloRepository moduloRepository,
            final ActualizarModuloPublisher actualizarModuloPublisher,
            final ActualizarModuloRuleValidator actualizarModuloRuleValidator) {
        this.moduloRepository = moduloRepository;
        this.actualizarModuloPublisher = actualizarModuloPublisher;
        this.actualizarModuloRuleValidator = actualizarModuloRuleValidator;
    }

    @Override
    public void execute(final ActualizarModuloDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getId())) {
            throw ValidationException.build(
                    "El id del modulo es obligatorio para actualizar.");
        }

        if (moduloRepository.findById(data.getId()).isEmpty()) {
            throw NotFoundException.build(
                    "No existe un modulo con el id especificado.");
        }

        actualizarModuloRuleValidator.validate(data);

        var entity = ModuloEntity.create(data.getId(), data.getNombre(), data.getIdAplicacion(),
                data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
        var updatedEntity = moduloRepository.update(entity);
        actualizarModuloPublisher.sendEvent(ActualizarModuloEvent.updated(updatedEntity));
    }
}
