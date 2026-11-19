package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.crearmetadatostodosambientesimpl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.event.ActualizarMetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.publisher.ActualizarMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.CrearMetadatosTodosAmbientes;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.CrearMetadatosTodosAmbientesRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.domain.CrearMetadatosTodosAmbientesDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.event.CrearMetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.publisher.CrearMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearMetadatosTodosAmbientesImpl implements CrearMetadatosTodosAmbientes {
    private final MetadatoRepository repository;
    private final AmbienteRepository ambientes;
    private final CrearMetadatosTodosAmbientesRuleValidator validator;
    private final CrearMetadatoPublisher crearPublisher;
    private final ActualizarMetadatoPublisher actualizarPublisher;
    private final TelemetryService telemetry;

    public CrearMetadatosTodosAmbientesImpl(final MetadatoRepository repository, final AmbienteRepository ambientes,
            final CrearMetadatosTodosAmbientesRuleValidator validator, final CrearMetadatoPublisher crearPublisher,
            final ActualizarMetadatoPublisher actualizarPublisher, final TelemetryService telemetry) {
        this.repository = repository; this.ambientes = ambientes; this.validator = validator;
        this.crearPublisher = crearPublisher; this.actualizarPublisher = actualizarPublisher; this.telemetry = telemetry;
    }

    @Override public List<MetadatoEntity> execute(final CrearMetadatosTodosAmbientesDomain data) {
        return telemetry.recordBusinessOperation("crear-metadato-todos-ambientes", () -> {
            validator.validate(data);
            var existentes = repository.findByIdParametro(data.getIdParametro()).stream()
                    .filter(item -> data.getIdTipoMetadato().equals(item.getIdTipoMetadato()) && item.getIdAmbiente() != null)
                    .collect(Collectors.toMap(MetadatoEntity::getIdAmbiente, Function.identity(), (primero, ignorado) -> primero));
            var entidades = ambientes.findAll().stream().map(ambiente -> {
                var actual = existentes.get(ambiente.getId());
                return MetadatoEntity.create(actual == null ? UUIDHelper.generate() : actual.getId(), data.getIdParametro(),
                        data.getIdTipoMetadato(), ambiente.getId(), data.getValor());
            }).toList();
            Set<UUID> idsExistentes = existentes.values().stream().map(MetadatoEntity::getId).collect(Collectors.toUnmodifiableSet());
            var guardadas = repository.upsertAll(entidades, idsExistentes);
            guardadas.forEach(item -> {
                if (idsExistentes.contains(item.getId())) actualizarPublisher.sendEvent(ActualizarMetadatoEvent.updated(item));
                else crearPublisher.sendEvent(CrearMetadatoEvent.created(item));
            });
            return guardadas;
        });
    }
}
