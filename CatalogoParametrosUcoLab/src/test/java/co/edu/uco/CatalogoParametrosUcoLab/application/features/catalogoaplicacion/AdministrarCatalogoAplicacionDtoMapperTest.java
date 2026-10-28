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
}
