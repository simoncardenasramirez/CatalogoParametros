package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosporidentidadservicio.usecase.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.IdentidadServicioRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ForbiddenException;

@ExtendWith(MockitoExtension.class)
class ConsultarParametrosPorIdentidadServicioImplTest {

    @Mock
    private IdentidadServicioRepository identidades;

    @Mock
    private ConsultarParametrosPorToken consultarPorAplicacionAmbiente;

    @Test
    void debeConsultarElAmbienteAsociadoCuandoElClientIdEstaRegistrado() {
        final UUID aplicacionId = UUID.randomUUID();
        final UUID ambienteId = UUID.randomUUID();
        final var identidad = IdentidadServicioEntity.create(UUID.randomUUID(), aplicacionId, ambienteId,
                "svc-mensajes");
        final var esperado = ConsultarParametrosPorToken.Resultado.noAutorizado();
        final var useCase = new ConsultarParametrosPorIdentidadServicioImpl(identidades,
                consultarPorAplicacionAmbiente);
        when(identidades.findByClientId("svc-mensajes")).thenReturn(Optional.of(identidad));
        when(consultarPorAplicacionAmbiente.execute(aplicacionId, ambienteId)).thenReturn(esperado);

        final var resultado = useCase.execute(" svc-mensajes ");

        assertSame(esperado, resultado);
        verify(consultarPorAplicacionAmbiente).execute(aplicacionId, ambienteId);
    }

    @Test
    void debeProhibirElConsumoCuandoElClientIdNoEstaAsociado() {
        final var useCase = new ConsultarParametrosPorIdentidadServicioImpl(identidades,
                consultarPorAplicacionAmbiente);
        when(identidades.findByClientId("svc-desconocido")).thenReturn(Optional.empty());

        assertThrows(ForbiddenException.class, () -> useCase.execute("svc-desconocido"));
    }
}
