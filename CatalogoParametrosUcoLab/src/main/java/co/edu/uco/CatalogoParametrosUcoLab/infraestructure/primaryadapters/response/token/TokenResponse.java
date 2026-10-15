package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.token;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class TokenResponse extends Response {
    private final List<TokenData> tokens = new ArrayList<>();
    public List<TokenData> getTokens() { return tokens; }
    public void add(final TokenEntity token) { tokens.add(TokenData.from(token)); }
    public void addAll(final List<TokenEntity> values) { values.forEach(this::add); }

    /** El hash BCrypt nunca se expone en la API. */
    public record TokenData(UUID id, UUID estadoToken, UUID aplicacionId, UUID ambienteId,
            LocalDateTime fechaCreacion, LocalDateTime fechaExpiracion) {
        public static TokenData from(final TokenEntity entity) {
            return new TokenData(entity.getId(), entity.getEstadoToken(), entity.getAplicacionId(),
                    entity.getAmbienteId(), entity.getFechaCreacion(), entity.getFechaExpiracion());
        }
    }
}
