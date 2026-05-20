package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.parametro.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.parametro.CrearParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.parametro.CrearParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.domain.parametro.ParametroDomain;

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

        var entity = ParametroEntity.create(data.getId(), data.getNombre(), data.getValor(), data.getDescripcion(),
                data.isActivo());
        parametroRepository.save(entity);
    }
}
