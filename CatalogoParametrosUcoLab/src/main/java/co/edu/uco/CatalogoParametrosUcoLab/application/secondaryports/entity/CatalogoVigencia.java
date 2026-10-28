package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CatalogoVigencia(List<RegistroVigencia> registros, Map<UUID, String> estadosAsociacion,
        Map<UUID, String> estadosToken) {
    public CatalogoVigencia {
        registros = List.copyOf(registros);
        estadosAsociacion = Map.copyOf(estadosAsociacion);
        estadosToken = Map.copyOf(estadosToken);
    }
}
