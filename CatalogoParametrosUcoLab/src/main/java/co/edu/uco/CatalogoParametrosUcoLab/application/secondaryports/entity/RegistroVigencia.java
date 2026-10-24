package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.time.Instant;
import java.util.UUID;

/** Vista inmutable para evaluar disponibilidad sin depender del almacenamiento. */
public record RegistroVigencia(TipoRegistroVigencia tipo, UUID id, UUID idPadre, UUID idAmbiente,
        UUID idEstado, boolean activo, Instant inicio, Instant fin) {
}
