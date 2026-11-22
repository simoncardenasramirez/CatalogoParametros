package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro;

import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente.ParametroDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class ParametrosConsumidorResponse extends Response {
    private final AplicacionEntity aplicacion;
    private final AmbienteEntity ambiente;
    private final List<ParametroDetalle> parametros;

    public ParametrosConsumidorResponse(final AplicacionEntity aplicacion, final AmbienteEntity ambiente,
            final List<ParametroDetalle> parametros) {
        this.aplicacion = aplicacion;
        this.ambiente = ambiente;
        this.parametros = List.copyOf(parametros);
    }

    public AplicacionEntity getAplicacion() { return aplicacion; }
    public AmbienteEntity getAmbiente() { return ambiente; }
    public List<ParametroDetalle> getParametros() { return parametros; }
}
