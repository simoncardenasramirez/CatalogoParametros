package co.edu.uco.CatalogoParametrosUcoLab.application.features.token;

import java.time.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.usecase.impl.CrearTokenImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.usecase.domain.CrearTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.common.TokenHashService;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CrearTokenUtcTest {
    private final TokenRepository tokens=mock(TokenRepository.class);
    private final EstadoTokenRepository estados=mock(EstadoTokenRepository.class);
    private final AplicacionRepository aplicaciones=mock(AplicacionRepository.class);
    private final AmbienteRepository ambientes=mock(AmbienteRepository.class);
    private final TokenHashService hash=mock(TokenHashService.class);
    private final Clock reloj=Clock.fixed(Instant.parse("2026-10-23T17:00:00Z"),ZoneOffset.UTC);
    private final CrearTokenImpl caso=new CrearTokenImpl(tokens,estados,aplicaciones,ambientes,hash,reloj);

    @Test void debePersistirCreacionYExpiracionUtcCuandoRecibeOtroDesfase() {
        var estado=UUID.randomUUID(); var app=UUID.randomUUID(); var ambiente=UUID.randomUUID();
        when(estados.findById(estado)).thenReturn(Optional.of(EstadoTokenEntity.create(estado,"activo")));
        when(aplicaciones.findById(app)).thenReturn(Optional.of(AplicacionEntity.create(app,"App",UUID.randomUUID(),true,null,null)));
        when(ambientes.findById(ambiente)).thenReturn(Optional.of(AmbienteEntity.create(ambiente,"Desarrollo")));
        when(hash.hash("secreto")).thenReturn("hash");
        when(tokens.save(any())).thenAnswer(invocacion->invocacion.getArgument(0));
        var resultado=caso.execute(CrearTokenDomain.create(UUID.randomUUID(),"secreto",estado,app,ambiente,
                OffsetDateTime.parse("2026-10-23T12:01:00-05:00")));
        assertEquals(OffsetDateTime.parse("2026-10-23T17:00:00Z"),resultado.getFechaCreacion());
        assertEquals(OffsetDateTime.parse("2026-10-23T17:01:00Z"),resultado.getFechaExpiracion());
    }
    @Test void debeRechazarExpiracionCuandoCoincideConElInstanteUtcActual() {
        var dato=CrearTokenDomain.create(UUID.randomUUID(),"secreto",UUID.randomUUID(),UUID.randomUUID(),
                UUID.randomUUID(),OffsetDateTime.parse("2026-10-23T12:00:00-05:00"));
        assertThrows(ValidationException.class,()->caso.execute(dato));
        verifyNoInteractions(tokens,estados,aplicaciones,ambientes,hash);
    }
}
