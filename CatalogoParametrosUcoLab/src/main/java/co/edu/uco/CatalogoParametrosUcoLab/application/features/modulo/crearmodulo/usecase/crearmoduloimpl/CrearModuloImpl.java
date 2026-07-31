package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.crearmoduloimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModuloRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.event.CrearModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.publisher.CrearModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import org.springframework.stereotype.Service;

@Service
public class CrearModuloImpl implements CrearModulo {

    private final ModuloRepository moduloRepository;
    private final CrearModuloPublisher crearModuloPublisher;
    private final CrearModuloRuleValidator crearModuloRuleValidator;

    public CrearModuloImpl(final ModuloRepository moduloRepository,
            final CrearModuloPublisher crearModuloPublisher,
            final CrearModuloRuleValidator crearModuloRuleValidator) {
        this.moduloRepository = moduloRepository;
        this.crearModuloPublisher = crearModuloPublisher;
        this.crearModuloRuleValidator = crearModuloRuleValidator;
    }

    @Override
    public void execute(final CrearModuloDomain data) {
        crearModuloRuleValidator.validate(data);
        data.generateId();

        var entity = ModuloEntity.create(data.getId(), data.getNombre(), data.getIdAplicacion(),
                data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
        var savedEntity = moduloRepository.save(entity);
        crearModuloPublisher.sendEvent(CrearModuloEvent.created(savedEntity));
    }
}
