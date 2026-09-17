package co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.dto;

public final class CambiarEstadoDtoRequest {

    private Boolean activo;

    public CambiarEstadoDtoRequest() {
    }

    public CambiarEstadoDtoRequest(final Boolean activo) {
        setActivo(activo);
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(final Boolean activo) {
        this.activo = activo;
    }
}
