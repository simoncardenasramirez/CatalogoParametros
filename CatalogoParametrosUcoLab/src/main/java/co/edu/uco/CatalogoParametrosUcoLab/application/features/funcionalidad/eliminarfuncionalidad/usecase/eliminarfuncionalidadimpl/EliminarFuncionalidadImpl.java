package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.eliminarfuncionalidadimpl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.EliminarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.event.EliminarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.publisher.EliminarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.EliminarFuncionalidadIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.usecase.domain.rules.EliminarFuncionalidadIsNotUsedByParametroRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class EliminarFuncionalidadImpl implements EliminarFuncionalidad {

    private final FuncionalidadRepository funcionalidadRepository;
    private final ParametroRepository parametroRepository;
    private final EliminarFuncionalidadPublisher eliminarFuncionalidadPublisher;
    private final EliminarFuncionalidadIdExistsRule eliminarFuncionalidadIdExistsRule;
    private final EliminarFuncionalidadIsNotUsedByParametroRule eliminarFuncionalidadIsNotUsedByParametroRule;

    public EliminarFuncionalidadImpl(final FuncionalidadRepository funcionalidadRepository,
            final ParametroRepository parametroRepository,
            final EliminarFuncionalidadPublisher eliminarFuncionalidadPublisher,
            final EliminarFuncionalidadIdExistsRule eliminarFuncionalidadIdExistsRule,
            final EliminarFuncionalidadIsNotUsedByParametroRule eliminarFuncionalidadIsNotUsedByParametroRule) {
        this.funcionalidadRepository = funcionalidadRepository;
        this.parametroRepository = parametroRepository;
        this.eliminarFuncionalidadPublisher = eliminarFuncionalidadPublisher;
        this.eliminarFuncionalidadIdExistsRule = eliminarFuncionalidadIdExistsRule;
        this.eliminarFuncionalidadIsNotUsedByParametroRule = eliminarFuncionalidadIsNotUsedByParametroRule;
    }

    @Override
    public void execute(final UUID data) {
        if (data == null || UUIDHelper.getDefault().equals(data)) {
            throw ValidationException.build("El id de la funcionalidad es obligatorio para eliminar.");
        }

        eliminarFuncionalidadIdExistsRule.execute(data);
        eliminarFuncionalidadIsNotUsedByParametroRule.execute(data);

        var funcionalidad = funcionalidadRepository.findById(data)
                .orElseThrow(() -> NotFoundException.build("No existe una funcionalidad con el id especificado."));

        funcionalidadRepository.deleteById(data);
        eliminarFuncionalidadPublisher.sendEvent(EliminarFuncionalidadEvent.deleted(funcionalidad));
    }
}