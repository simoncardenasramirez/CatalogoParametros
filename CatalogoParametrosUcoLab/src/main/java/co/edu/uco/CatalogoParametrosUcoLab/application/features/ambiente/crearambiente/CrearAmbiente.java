package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.CrearAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

public interface CrearAmbiente {
    AmbienteEntity execute(CrearAmbienteDomain data);
}
