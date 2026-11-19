package co.edu.uco.CatalogoParametrosUcoLab.application.features.token;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.usecase.domain.ActualizarEstadoTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.usecase.impl.ActualizarEstadoTokenImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.primaryports.interactor.impl.ConsultarParametrosPorTokenInteractorImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

class TokenUseCaseBranchesTest {
    @Test
    void debeRechazarAutorizacionAusenteMalformadaOVacia() {
        var consultar = mock(ConsultarParametrosPorToken.class);
        var interactor = new ConsultarParametrosPorTokenInteractorImpl(consultar);
        var token = UUID.randomUUID();

        assertFalse(interactor.execute(token, null).autorizado());
        assertFalse(interactor.execute(token, "Basic abc").autorizado());
        assertFalse(interactor.execute(token, "Bearer   ").autorizado());
        verifyNoInteractions(consultar);
    }

    @Test
    void debeDelegarTokenBearerCuandoLaAutorizacionEsValida() {
        var consultar = mock(ConsultarParametrosPorToken.class);
        var interactor = new ConsultarParametrosPorTokenInteractorImpl(consultar);
        var token = UUID.randomUUID();
        var resultado = ConsultarParametrosPorToken.Resultado.noAutorizado();
        when(consultar.execute(token, "secreto")).thenReturn(resultado);

        assertSame(resultado, interactor.execute(token, "Bearer  secreto  "));
        verify(consultar).execute(token, "secreto");
    }

    @Test
    void debeActualizarEstadoCuandoTokenYEstadoExisten() {
        var tokens = mock(TokenRepository.class);
        var estados = mock(EstadoTokenRepository.class);
        var tokenId = UUID.randomUUID();
        var estadoId = UUID.randomUUID();
        var esperado = TokenEntity.create(tokenId, "hash", estadoId, UUID.randomUUID(), UUID.randomUUID(),
                OffsetDateTime.now(), null);
        when(tokens.findById(tokenId)).thenReturn(Optional.of(esperado));
        when(estados.findById(estadoId)).thenReturn(Optional.of(EstadoTokenEntity.create(estadoId, "activo")));
        when(tokens.updateEstado(tokenId, estadoId)).thenReturn(esperado);

        assertSame(esperado, new ActualizarEstadoTokenImpl(tokens, estados)
                .execute(ActualizarEstadoTokenDomain.create(tokenId, estadoId)));
        verify(tokens).updateEstado(tokenId, estadoId);
    }

    @Test
    void debeRechazarTokenOEstadoInexistenteAlActualizar() {
        var tokens = mock(TokenRepository.class);
        var estados = mock(EstadoTokenRepository.class);
        var tokenId = UUID.randomUUID();
        var estadoId = UUID.randomUUID();
        var useCase = new ActualizarEstadoTokenImpl(tokens, estados);
        when(tokens.findById(tokenId)).thenReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(ActualizarEstadoTokenDomain.create(tokenId, estadoId)));

        when(tokens.findById(tokenId)).thenReturn(Optional.of(mock(TokenEntity.class)));
        when(estados.findById(estadoId)).thenReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(ActualizarEstadoTokenDomain.create(tokenId, estadoId)));
    }
}
