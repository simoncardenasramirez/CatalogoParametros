package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.modulo.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.modulo.CrearModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.modulo.CrearModuloRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.ModuloDomain;

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
    public void execute(final ModuloDomain data) {
        crearModuloRuleValidator.validate(data);
        data.generateId();

        var entity = ModuloEntity.create(data.getId(), data.getNombre(), data.getIdAplicacion(),
                data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
        moduloRepository.save(entity);
    }
}
