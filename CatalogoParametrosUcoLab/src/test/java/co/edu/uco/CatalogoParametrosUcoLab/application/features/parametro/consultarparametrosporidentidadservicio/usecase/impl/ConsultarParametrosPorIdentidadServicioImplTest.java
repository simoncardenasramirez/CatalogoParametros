package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporidentidadservicio.usecase.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.IdentidadServicioRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ForbiddenException;

class ConsultarParametrosPorIdentidadServicioImplTest {
    @Test void debeResolverElAlcanceDesdeElClientIdAsociado() {
        var repository = mock(IdentidadServicioRepository.class);
        var consulta = mock(ConsultarParametrosPorAplicacionAmbiente.class);
        var app = UUID.randomUUID();
        var ambiente = UUID.randomUUID();
        var identidad = IdentidadServicioEntity.create(UUID.randomUUID(), app, ambiente, "svc-consumidor");
        var resultado = new ConsultarParametrosPorAplicacionAmbiente.Resultado(null, null, java.util.List.of());
        when(repository.findByClientId("svc-consumidor")).thenReturn(Optional.of(identidad));
        when(consulta.execute(app, ambiente)).thenReturn(resultado);

        assertSame(resultado, new ConsultarParametrosPorIdentidadServicioImpl(repository, consulta)
                .execute(" svc-consumidor "));
        verify(consulta).execute(app, ambiente);
    }

    @Test void debeRechazarClientIdNoAsociado() {
        var repository = mock(IdentidadServicioRepository.class);
        when(repository.findByClientId("svc-desconocido")).thenReturn(Optional.empty());
        var caso = new ConsultarParametrosPorIdentidadServicioImpl(repository,
                mock(ConsultarParametrosPorAplicacionAmbiente.class));
        assertThrows(ForbiddenException.class, () -> caso.execute("svc-desconocido"));
    }

    @Test void debeFiltrarPorNombreSinPerderLaInformacionDelParametro() {
        var repository = mock(IdentidadServicioRepository.class);
        var consulta = mock(ConsultarParametrosPorAplicacionAmbiente.class);
        var app = UUID.randomUUID();
        var ambiente = UUID.randomUUID();
        var identidad = IdentidadServicioEntity.create(UUID.randomUUID(), app, ambiente, "svc-consumidor");
        var parametro = co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity.create(
                UUID.randomUUID(), "correo", UUID.randomUUID(), UUID.randomUUID(), true);
        var otro = co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity.create(
                UUID.randomUUID(), "endpoint", UUID.randomUUID(), UUID.randomUUID(), true);
        var detalle = new ConsultarParametrosPorAplicacionAmbiente.ParametroDetalle(parametro, null, null, null,
                List.of(), null);
        var otroDetalle = new ConsultarParametrosPorAplicacionAmbiente.ParametroDetalle(otro, null, null, null,
                List.of(), null);
        var resultado = new ConsultarParametrosPorAplicacionAmbiente.Resultado(null, null,
                List.of(detalle, otroDetalle));
        when(repository.findByClientId("svc-consumidor")).thenReturn(Optional.of(identidad));
        when(consulta.execute(app, ambiente)).thenReturn(resultado);

        var filtrado = new ConsultarParametrosPorIdentidadServicioImpl(repository, consulta)
                .execute("svc-consumidor", " CORREO ");

        assertEquals(1, filtrado.parametros().size());
        assertSame(parametro, filtrado.parametros().get(0).parametro());
        verify(consulta).execute(app, ambiente);
    }
}
