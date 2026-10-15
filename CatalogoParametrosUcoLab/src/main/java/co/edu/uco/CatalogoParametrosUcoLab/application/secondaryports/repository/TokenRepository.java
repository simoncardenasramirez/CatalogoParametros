package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;

public interface TokenRepository {
    TokenEntity save(TokenEntity token);
    TokenEntity updateEstado(UUID id, UUID estadoToken);
    Optional<TokenEntity> findById(UUID id);
    List<TokenEntity> findAll();
}
