package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.crearfuncionalidadimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.CrearFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.CrearFuncionalidadRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;

@Service
public class CrearFuncionalidadImpl implements CrearFuncionalidad {

    private final FuncionalidadRepository funcionalidadRepository;
    private final CrearFuncionalidadRuleValidator crearFuncionalidadRuleValidator;

    public CrearFuncionalidadImpl(final FuncionalidadRepository funcionalidadRepository,
            final CrearFuncionalidadRuleValidator crearFuncionalidadRuleValidator) {
        this.funcionalidadRepository = funcionalidadRepository;
        this.crearFuncionalidadRuleValidator = crearFuncionalidadRuleValidator;
    }

    @Override
    public void execute(final CrearFuncionalidadDomain data) {
        crearFuncionalidadRuleValidator.validate(data);
        data.generateId();

        var entity = FuncionalidadEntity.create(data.getId(), data.getNombre(), data.getIdModulo(),
                data.isActivo(), data.getFechaInicio(), data.getFechaFinal());
        funcionalidadRepository.save(entity);
    }
}