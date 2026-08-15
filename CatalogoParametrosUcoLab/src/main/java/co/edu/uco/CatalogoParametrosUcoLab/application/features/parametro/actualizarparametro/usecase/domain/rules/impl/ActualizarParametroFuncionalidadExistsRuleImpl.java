package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.impl;

import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroFuncionalidadExistsRule;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarParametroFuncionalidadExistsRuleImpl implements ActualizarParametroFuncionalidadExistsRule {

    @Autowired
    private ConsultarMensajePort consultarMensajePort;

    private final FuncionalidadRepository funcionalidadRepository;

    public ActualizarParametroFuncionalidadExistsRuleImpl(final FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    @Override
    public void execute(final ActualizarParametroDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getIdFuncionalidad())) {
            throw ValidationException.build(consultarMensajePort.consultarMensaje("MSG-117"));
        }

        final FuncionalidadEntity funcionalidad = funcionalidadRepository.findById(data.getIdFuncionalidad())
                .orElse(null);

        if (funcionalidad == null || UUIDHelper.getDefault().equals(funcionalidad.getId())) {
            throw NotFoundException.build(consultarMensajePort.consultarMensaje("MSG-116"));
        }
    }
}
