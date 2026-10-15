package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.usecase.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.common.TokenHashService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.CrearToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.usecase.domain.CrearTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

@Service
public final class CrearTokenImpl implements CrearToken {
    private final TokenRepository repository;
    private final EstadoTokenRepository estados;
    private final AplicacionRepository aplicaciones;
    private final AmbienteRepository ambientes;
    private final TokenHashService hashService;

    public CrearTokenImpl(final TokenRepository repository, final EstadoTokenRepository estados,
            final AplicacionRepository aplicaciones, final AmbienteRepository ambientes,
            final TokenHashService hashService) {
        this.repository = repository;
        this.estados = estados;
        this.aplicaciones = aplicaciones;
        this.ambientes = ambientes;
        this.hashService = hashService;
    }

    @Override
    public TokenEntity execute(final CrearTokenDomain data) {
        if (TextHelper.isBlank(data.getToken())) throw ValidationException.build("El token es obligatorio.");
        if (data.getFechaExpiracion() == null || !data.getFechaExpiracion().isAfter(LocalDateTime.now())) {
            throw ValidationException.build("La fecha de expiracion debe ser posterior a la fecha actual.");
        }
        if (estados.findById(data.getEstadoToken()).isEmpty()) throw NotFoundException.build("El estado de token no existe.");
        if (aplicaciones.findById(data.getAplicacionId()).isEmpty()) throw NotFoundException.build("La aplicacion no existe.");
        if (ambientes.findById(data.getAmbienteId()).isEmpty()) throw NotFoundException.build("El ambiente no existe.");
        data.generateId();
        return repository.save(TokenEntity.create(data.getId(), hashService.hash(data.getToken()),
                data.getEstadoToken(), data.getAplicacionId(), data.getAmbienteId(), LocalDateTime.now(),
                data.getFechaExpiracion()));
    }
}
