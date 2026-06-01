package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.crearmoduloimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModuloRuleValidator;

@Service
public class CrearModuloImpl implements CrearModulo {

    private final ModuloRepository moduloRepository;
    private final CrearModuloRuleValidator crearModuloRuleValidator;

    public CrearModuloImpl(final ModuloRepository moduloRepository,
            final CrearModuloRuleValidator crearModuloRuleValidator) {
        this.moduloRepository = moduloRepository;
        this.crearModuloRuleValidator = crearModuloRuleValidator;
    }

    @Override
    public void execute(final CrearModuloDomain data) {
        crearModuloRuleValidator.validate(data);
        data.generateId();

        var entity = ModuloEntity.create(data.getId(), data.getNombre(), data.getIdAplicacion(),
                data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
        moduloRepository.save(entity);
    }
}
