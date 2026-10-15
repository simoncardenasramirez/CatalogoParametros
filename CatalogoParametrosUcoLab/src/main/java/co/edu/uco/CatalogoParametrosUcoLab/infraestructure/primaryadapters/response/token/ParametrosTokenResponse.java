package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.token;

import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken.ParametroDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class ParametrosTokenResponse extends Response {
    private final AplicacionEntity aplicacion;
    private final AmbienteEntity ambiente;
    private final List<ParametroDetalle> parametros;

    public ParametrosTokenResponse(final AplicacionEntity aplicacion, final AmbienteEntity ambiente,
            final List<ParametroDetalle> parametros) {
        this.aplicacion = aplicacion;
        this.ambiente = ambiente;
        this.parametros = List.copyOf(parametros);
    }

    public AplicacionEntity getAplicacion() { return aplicacion; }
    public AmbienteEntity getAmbiente() { return ambiente; }
    public List<ParametroDetalle> getParametros() { return parametros; }
}
