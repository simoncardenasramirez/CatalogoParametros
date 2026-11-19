package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.dto.AdministrarCatalogoAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.interactor.mapper.AdministrarCatalogoAplicacionDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import static org.junit.jupiter.api.Assertions.*;

class AdministrarCatalogoAplicacionDtoMapperTest {
    private final AdministrarCatalogoAplicacionDtoMapper mapper=AdministrarCatalogoAplicacionDtoMapper.INSTANCE;
    private AdministrarCatalogoAplicacionDtoRequest datos(String inicio,String fin) {
        return new AdministrarCatalogoAplicacionDtoRequest("Modulo",null,null,null,null,null,null,null,true,inicio,fin,null);
    }
    @Test void debeFijarAplicacionDelPathCuandoCreaUnModulo() {
        var app=UUID.randomUUID();
        var dto=datos("2026-10-23T17:00:00Z","2027-10-23T17:00:00Z");
        assertEquals(app.toString(),mapper.crearModulo(app,dto).getIdAplicacion());
        assertEquals(app,mapper.toDomain(app,"modulos",null,dto,AdministrarCatalogoAplicacionDomain.Operacion.CREAR).getIdPadre());
    }
    @Test void debeRechazarRecursoGlobalCuandoNoEsAdministrableDesdeElMicrofrontend() {
        assertThrows(ValidationException.class,()->mapper.toDomain(UUID.randomUUID(),"organizaciones",null,datos(null,null),AdministrarCatalogoAplicacionDomain.Operacion.CREAR));
    }
    @Test void debeRechazarFechasAusentesCuandoElContratoExistenteLasRequiere() {
        assertThrows(ValidationException.class,()->mapper.crearModulo(UUID.randomUUID(),datos(null,null)));
    }
    @Test void debeMapearPadresYSolicitudesDeTodosLosRecursosAdministrables() {
        var app = UUID.randomUUID();
        var dto = new AdministrarCatalogoAplicacionDtoRequest("Nombre", UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                false, "2026-01-01T00:00:00Z", "2027-01-01T00:00:00Z", new tools.jackson.databind.ObjectMapper().createObjectNode());

        assertEquals(app, mapper.toDomain(app, "modulos", null, dto, AdministrarCatalogoAplicacionDomain.Operacion.CREAR).getIdPadre());
        assertEquals(UUID.fromString(dto.idModulo()), mapper.toDomain(app, "funcionalidades", null, dto,
                AdministrarCatalogoAplicacionDomain.Operacion.CREAR).getIdPadre());
        assertEquals(UUID.fromString(dto.idFuncionalidad()), mapper.toDomain(app, "parametros", null, dto,
                AdministrarCatalogoAplicacionDomain.Operacion.CREAR).getIdPadre());
        assertEquals(UUID.fromString(dto.idParametro()), mapper.toDomain(app, "metadatos", null, dto,
                AdministrarCatalogoAplicacionDomain.Operacion.CREAR).getIdPadre());
        assertEquals(UUID.fromString(dto.idParametro()), mapper.toDomain(app, "metadatos-ambiente", null, dto,
                AdministrarCatalogoAplicacionDomain.Operacion.CREAR).getIdPadre());

        mapper.crearFuncionalidad(app, dto);
        mapper.actualizarModulo(app, dto);
        mapper.actualizarFuncionalidad(app, dto);
        mapper.crearParametro(app, dto);
        mapper.actualizarParametro(app, dto);
        mapper.crearMetadato(app, dto);
        mapper.actualizarMetadato(app, dto);
        mapper.crearMetadatoAmbiente(app, dto);
        mapper.actualizarMetadatoAmbiente(app, dto);
    }

    @Test void debeRechazarDatosAusentesEnUnaOperacionDistintaDeEliminar() {
        assertThrows(ValidationException.class, () -> mapper.toDomain(UUID.randomUUID(), "modulos", null, null,
                AdministrarCatalogoAplicacionDomain.Operacion.CREAR));
        assertDoesNotThrow(() -> mapper.toDomain(UUID.randomUUID(), "modulos", UUID.randomUUID(), null,
                AdministrarCatalogoAplicacionDomain.Operacion.ELIMINAR));
    }

    @Test void debeRechazarIdentificadorPadreYFechaConFormatoInvalido() {
        var dto = new AdministrarCatalogoAplicacionDtoRequest("Nombre", "no-es-uuid", null, null, null, null,
                null, null, true, "2026-01-01T00:00:00Z", "2027-01-01T00:00:00Z", null);
        assertThrows(ValidationException.class, () -> mapper.toDomain(UUID.randomUUID(), "funcionalidades", null, dto,
                AdministrarCatalogoAplicacionDomain.Operacion.CREAR));
        assertThrows(ValidationException.class, () -> mapper.crearModulo(UUID.randomUUID(), datos("no-es-fecha", null)));
    }
}
