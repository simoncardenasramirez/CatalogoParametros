package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.crearmetadatostodosambientesimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.publisher.ActualizarMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.publisher.CrearMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.CrearMetadatosTodosAmbientesRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.domain.CrearMetadatosTodosAmbientesDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import tools.jackson.databind.node.JsonNodeFactory;

@ExtendWith(MockitoExtension.class)
class CrearMetadatosTodosAmbientesImplTest {
    @Mock private MetadatoRepository repository;
    @Mock private AmbienteRepository ambientes;
    @Mock private CrearMetadatosTodosAmbientesRuleValidator validator;
    @Mock private CrearMetadatoPublisher crearPublisher;
    @Mock private ActualizarMetadatoPublisher actualizarPublisher;
    private CrearMetadatosTodosAmbientesImpl useCase;
    private final UUID parametro = UUID.randomUUID();
    private final UUID tipo = UUID.randomUUID();
    private final UUID produccion = UUID.randomUUID();
    private final UUID desarrollo = UUID.randomUUID();
    private final UUID metadatoExistente = UUID.randomUUID();

    @BeforeEach
    void preparar() {
        useCase = new CrearMetadatosTodosAmbientesImpl(repository, ambientes, validator, crearPublisher,
                actualizarPublisher, new TelemetryService(new SimpleMeterRegistry()));
    }

    @Test
    void debeActualizarYCrearMetadatosCuandoSeSolicitanTodosLosAmbientes() {
        var existente = MetadatoEntity.create(metadatoExistente, parametro, tipo, produccion,
                JsonNodeFactory.instance.textNode("anterior"));
        when(repository.findByIdParametro(parametro)).thenReturn(List.of(existente));
        when(ambientes.findAll()).thenReturn(List.of(AmbienteEntity.create(produccion, "Produccion"),
                AmbienteEntity.create(desarrollo, "Desarrollo")));
        when(repository.upsertAll(anyList(), any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        var resultado = useCase.execute(CrearMetadatosTodosAmbientesDomain.create(parametro, tipo,
                JsonNodeFactory.instance.textNode("nuevo")));

        assertEquals(2, resultado.size());
        verify(repository).upsertAll(anyList(), eq(Set.of(metadatoExistente)));
        verify(actualizarPublisher).sendEvent(any());
        verify(crearPublisher).sendEvent(any());
    }
}
