package co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.event.ActualizarAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.interactor.CambiarEstadoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.event.ActualizarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.event.ActualizarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.event.ActualizarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CambiarEstadoInteractorImpl implements CambiarEstadoInteractor {

    private final AplicacionRepository aplicacionRepository;
    private final ModuloRepository moduloRepository;
    private final FuncionalidadRepository funcionalidadRepository;
    private final ParametroRepository parametroRepository;
    private final ActualizarAplicacionPublisher aplicacionPublisher;
    private final ActualizarModuloPublisher moduloPublisher;
    private final ActualizarFuncionalidadPublisher funcionalidadPublisher;
    private final ActualizarParametroPublisher parametroPublisher;

    public CambiarEstadoInteractorImpl(final AplicacionRepository aplicacionRepository,
            final ModuloRepository moduloRepository, final FuncionalidadRepository funcionalidadRepository,
            final ParametroRepository parametroRepository, final ActualizarAplicacionPublisher aplicacionPublisher,
            final ActualizarModuloPublisher moduloPublisher,
            final ActualizarFuncionalidadPublisher funcionalidadPublisher,
            final ActualizarParametroPublisher parametroPublisher) {
        this.aplicacionRepository = aplicacionRepository;
        this.moduloRepository = moduloRepository;
        this.funcionalidadRepository = funcionalidadRepository;
        this.parametroRepository = parametroRepository;
        this.aplicacionPublisher = aplicacionPublisher;
        this.moduloPublisher = moduloPublisher;
        this.funcionalidadPublisher = funcionalidadPublisher;
        this.parametroPublisher = parametroPublisher;
    }

    @Override
    public void execute(final TipoRecurso tipoRecurso, final UUID id, final Boolean activo) {
        if (id == null || UUIDHelper.getDefault().equals(id)) {
            throw ValidationException.build("El identificador del recurso es obligatorio para cambiar el estado.");
        }
        if (activo == null) {
            throw ValidationException.build("El campo activo es obligatorio.");
        }

        switch (tipoRecurso) {
            case APLICACION -> cambiarEstadoAplicacion(id, activo);
            case MODULO -> cambiarEstadoModulo(id, activo);
            case FUNCIONALIDAD -> cambiarEstadoFuncionalidad(id, activo);
            case PARAMETRO -> cambiarEstadoParametro(id, activo);
        }
    }

    private void cambiarEstadoAplicacion(final UUID id, final boolean activo) {
        var entity = aplicacionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.build("No existe una aplicacion con el id especificado."));
        entity.setActiva(activo);
        var updated = aplicacionRepository.update(entity);
        aplicacionPublisher.sendEvent(ActualizarAplicacionEvent.updated(updated));
    }

    private void cambiarEstadoModulo(final UUID id, final boolean activo) {
        var entity = moduloRepository.findById(id)
                .orElseThrow(() -> NotFoundException.build("No existe un modulo con el id especificado."));
        entity.setActivo(activo);
        var updated = moduloRepository.update(entity);
        moduloPublisher.sendEvent(ActualizarModuloEvent.updated(updated));
    }

    private void cambiarEstadoFuncionalidad(final UUID id, final boolean activo) {
        var entity = funcionalidadRepository.findById(id)
                .orElseThrow(() -> NotFoundException.build("No existe una funcionalidad con el id especificado."));
        entity.setActivo(activo);
        var updated = funcionalidadRepository.update(entity);
        funcionalidadPublisher.sendEvent(ActualizarFuncionalidadEvent.updated(updated));
    }

    private void cambiarEstadoParametro(final UUID id, final boolean activo) {
        var entity = parametroRepository.findById(id)
                .orElseThrow(() -> NotFoundException.build("No existe un parametro con el id especificado."));
        entity.setActivo(activo);
        var updated = parametroRepository.update(entity);
        parametroPublisher.sendEvent(ActualizarParametroEvent.updated(updated));
    }
}
