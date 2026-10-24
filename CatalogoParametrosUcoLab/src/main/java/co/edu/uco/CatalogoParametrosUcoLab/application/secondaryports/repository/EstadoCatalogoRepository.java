package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CatalogoVigencia;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.RegistroVigencia;

public interface EstadoCatalogoRepository {
    CatalogoVigencia consultar();
    /** Canonicaliza fechas historicas sin alterar su instante ni agregar campos de estado. */
    int normalizarFechasUtc();
    /** Persiste todas las desactivaciones en una sola transaccion. Nunca reactiva registros. */
    void desactivar(List<RegistroVigencia> registros, UUID estadoAsociacionInactivo, UUID estadoTokenInactivo);
}
