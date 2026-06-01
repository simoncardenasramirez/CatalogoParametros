package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.usecase.eliminarparametroimpl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.EliminarParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.event.EliminarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.publisher.EliminarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public class EliminarParametroImpl implements EliminarParametro {

    private final ParametroRepository parametroRepository;
    private final EliminarParametroPublisher eliminarParametroPublisher;

    public EliminarParametroImpl(final ParametroRepository parametroRepository,
            final EliminarParametroPublisher eliminarParametroPublisher) {
        this.parametroRepository = parametroRepository;
        this.eliminarParametroPublisher = eliminarParametroPublisher;
    }

    @Override
    public void execute(final UUID data) {
        if (data == null || UUIDHelper.getDefault().equals(data)) {
            throw new ParametroException("El id del parametro es obligatorio para eliminar.");
        }

        var parametro = parametroRepository.findById(data)
                .orElseThrow(() -> new ParametroException("No existe un parametro con el id especificado."));

        parametroRepository.deleteById(data);
        eliminarParametroPublisher.sendEvent(EliminarParametroEvent.deleted(parametro));
    }
}
