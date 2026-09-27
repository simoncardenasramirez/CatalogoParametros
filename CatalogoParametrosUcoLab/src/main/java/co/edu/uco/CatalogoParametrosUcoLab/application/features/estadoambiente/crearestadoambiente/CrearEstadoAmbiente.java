package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.CrearEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

public interface CrearEstadoAmbiente {
    EstadoAmbienteEntity execute(CrearEstadoAmbienteDomain data);
}
