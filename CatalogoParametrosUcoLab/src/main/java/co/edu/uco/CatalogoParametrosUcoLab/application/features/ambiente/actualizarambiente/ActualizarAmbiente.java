package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.ActualizarAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

public interface ActualizarAmbiente {
    AmbienteEntity execute(ActualizarAmbienteDomain data);
}
