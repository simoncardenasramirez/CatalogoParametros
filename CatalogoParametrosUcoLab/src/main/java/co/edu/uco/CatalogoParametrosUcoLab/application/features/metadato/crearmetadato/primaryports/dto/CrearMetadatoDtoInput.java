package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto;

import java.util.UUID;

public final class CrearMetadatoDtoInput {
    private final UUID idParametro;
    private final UUID idTipoMetadato;
    private final String valor;
    private CrearMetadatoDtoInput(final UUID idParametro, final UUID idTipoMetadato, final String valor) {
        this.idParametro = idParametro;
        this.idTipoMetadato = idTipoMetadato;
        this.valor = valor;
    }
    public static CrearMetadatoDtoInput create(final UUID idParametro, final UUID idTipoMetadato, final String valor) {
        return new CrearMetadatoDtoInput(idParametro, idTipoMetadato, valor);
    }
    public UUID getIdParametro() { return idParametro; }
    public UUID getIdTipoMetadato() { return idTipoMetadato; }
    public String getValor() { return valor; }
}
