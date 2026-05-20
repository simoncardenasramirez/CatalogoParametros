package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.crearparametroimpl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.ParametroDomain;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametroRuleValidator;

@Service
public class CrearParametroImpl implements CrearParametro {

    private final ParametroRepository parametroRepository;
    private final CrearParametroRuleValidator crearParametroRuleValidator;

    public CrearParametroImpl(final ParametroRepository parametroRepository,
            final CrearParametroRuleValidator crearParametroRuleValidator) {
        this.parametroRepository = parametroRepository;
        this.crearParametroRuleValidator = crearParametroRuleValidator;
    }

    @Override
    public void execute(final ParametroDomain data) {
        crearParametroRuleValidator.validate(data);
        data.generateId();

        var entity = ParametroEntity.create(data.getId(), data.getNombre(), data.getIdFuncionalidad(),
                data.getIdTipoParametro(), data.isActivo());
        parametroRepository.save(entity);
    }
}
