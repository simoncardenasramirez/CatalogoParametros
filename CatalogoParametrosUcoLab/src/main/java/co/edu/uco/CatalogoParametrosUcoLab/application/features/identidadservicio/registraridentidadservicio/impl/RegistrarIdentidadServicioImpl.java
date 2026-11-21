package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.RegistrarIdentidadServicio;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto.RegistrarIdentidadServicioDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.IdentidadServicioRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class RegistrarIdentidadServicioImpl implements RegistrarIdentidadServicio {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrarIdentidadServicioImpl.class);
    private static final String OPERATION_NAME = "registrar-identidad-servicio";
    private final IdentidadServicioRepository repository;
    private final AplicacionRepository aplicaciones;
    private final AmbienteRepository ambientes;
    private final TelemetryService telemetryService;

    public RegistrarIdentidadServicioImpl(final IdentidadServicioRepository repository,
            final AplicacionRepository aplicaciones, final AmbienteRepository ambientes,
            final TelemetryService telemetryService) {
        this.repository = repository;
        this.aplicaciones = aplicaciones;
        this.ambientes = ambientes;
        this.telemetryService = telemetryService;
    }

    @Override
    public IdentidadServicioEntity execute(final UUID idAplicacion, final RegistrarIdentidadServicioDtoInput input) {
        return telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            if (idAplicacion == null || UUIDHelper.getDefault().equals(idAplicacion)) {
                throw ValidationException.build("El identificador de la aplicacion es obligatorio.");
            }
            if (input == null || UUIDHelper.getDefault().equals(input.getIdAmbiente())
                    || input.getClientId().isBlank()) {
                throw ValidationException.build("El ambiente y el client_id son obligatorios.");
            }
            if (!input.getClientId().matches("[A-Za-z0-9][A-Za-z0-9._:@-]{0,254}")) {
                throw ValidationException.build("El client_id solo admite letras, numeros y los caracteres . _ : @ -, con un maximo de 255 caracteres.");
            }
            aplicaciones.findById(idAplicacion).orElseThrow(
                    () -> NotFoundException.build("No se encontro la aplicacion indicada."));
            ambientes.findById(input.getIdAmbiente()).orElseThrow(
                    () -> NotFoundException.build("No se encontro el ambiente indicado."));
            if (repository.findByClientId(input.getClientId()).isPresent()) {
                throw ConflictException.build("La identidad de servicio ya esta asociada a un ambiente.");
            }
            final var saved = repository.save(IdentidadServicioEntity.create(UUIDHelper.generate(), idAplicacion,
                    input.getIdAmbiente(), input.getClientId()));
            LOGGER.info("[REGISTRAR-IDENTIDAD-SERVICIO] Identidad asociada a aplicacion {} y ambiente {}",
                    idAplicacion, input.getIdAmbiente());
            return saved;
        });
    }
}
