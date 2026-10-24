package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.ControlEstadoCatalogo;
import co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.vigencia.RegistroVigenciaMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class TokenCatalogoStrategy implements TokenRepository {
    private final BaseDatosTokenRepository baseDatos;
    private final ControlEstadoCatalogo controlEstado;
    public TokenCatalogoStrategy(final BaseDatosTokenRepository baseDatos, final ControlEstadoCatalogo controlEstado) {
        this.baseDatos = baseDatos;
        this.controlEstado = controlEstado;
    }
    @Override public TokenEntity save(final TokenEntity token) {
        return controlEstado.guardar(RegistroVigenciaMapper.INSTANCE.toRegistro(token), true,
                () -> baseDatos.save(token));
    }
    @Override public TokenEntity updateEstado(final UUID id, final UUID estadoToken) {
        var actual = baseDatos.findById(id).orElseThrow(() -> NotFoundException.build("El token no existe."));
        var nuevo = TokenEntity.create(actual.getId(), actual.getTokenHash(), estadoToken,
                actual.getAplicacionId(), actual.getAmbienteId(), actual.getFechaCreacion(), actual.getFechaExpiracion());
        return controlEstado.guardar(RegistroVigenciaMapper.INSTANCE.toRegistro(nuevo), false,
                () -> baseDatos.updateEstado(id, estadoToken));
    }
    @Override public Optional<TokenEntity> findById(final UUID id) { return baseDatos.findById(id); }
    @Override public List<TokenEntity> findAll() { return baseDatos.findAll(); }
}
