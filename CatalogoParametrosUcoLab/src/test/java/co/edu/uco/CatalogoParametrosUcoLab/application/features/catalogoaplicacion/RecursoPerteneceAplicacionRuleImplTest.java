package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.rules.impl.RecursoPerteneceAplicacionRuleImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RecursoPerteneceAplicacionRuleImplTest {
    private final BaseDatosAplicacionRepository apps=mock(BaseDatosAplicacionRepository.class);
    private final BaseDatosModuloRepository modulos=mock(BaseDatosModuloRepository.class);
    private final BaseDatosFuncionalidadRepository funciones=mock(BaseDatosFuncionalidadRepository.class);
    private final BaseDatosParametroRepository parametros=mock(BaseDatosParametroRepository.class);
    private final BaseDatosMetadatoRepository metadatos=mock(BaseDatosMetadatoRepository.class);
    private final BaseDatosMetadatoAmbienteRepository asociaciones=mock(BaseDatosMetadatoAmbienteRepository.class);
    private final RecursoPerteneceAplicacionRuleImpl regla=new RecursoPerteneceAplicacionRuleImpl(apps,modulos,funciones,parametros,metadatos,asociaciones);
    private final UUID app=UUID.randomUUID(), otra=UUID.randomUUID(), modulo=UUID.randomUUID(),
            funcion=UUID.randomUUID(), parametro=UUID.randomUUID(), metadato=UUID.randomUUID(), asociacion=UUID.randomUUID();

    private void preparar(UUID propietaria) {
        lenient().when(apps.findById(app)).thenReturn(Optional.of(AplicacionEntity.create(app,"App",UUID.randomUUID(),true,null,null)));
        lenient().when(modulos.findById(modulo)).thenReturn(Optional.of(ModuloEntity.create(modulo,"Modulo",propietaria,true,null,null)));
        lenient().when(funciones.findById(funcion)).thenReturn(Optional.of(FuncionalidadEntity.create(funcion,"Funcion",modulo,true,null,null)));
        lenient().when(parametros.findById(parametro)).thenReturn(Optional.of(ParametroEntity.create(parametro,"Parametro",funcion,UUID.randomUUID(),true)));
        lenient().when(metadatos.findById(metadato)).thenReturn(Optional.of(MetadatoEntity.create(metadato,parametro,UUID.randomUUID(),new tools.jackson.databind.ObjectMapper().readTree("\"valor\""))));
        lenient().when(asociaciones.findById(asociacion)).thenReturn(Optional.of(MetadatoAmbienteEntity.create(asociacion,parametro,UUID.randomUUID(),UUID.randomUUID())));
    }
    private UUID id(RecursoCatalogoAplicacion tipo) {
        return switch(tipo) { case MODULO->modulo; case FUNCIONALIDAD->funcion; case PARAMETRO->parametro; case METADATO->metadato; case METADATO_AMBIENTE->asociacion; };
    }
    private UUID padre(RecursoCatalogoAplicacion tipo) {
        return switch(tipo) { case MODULO->app; case FUNCIONALIDAD->modulo; case PARAMETRO->funcion; case METADATO,METADATO_AMBIENTE->parametro; };
    }
    @ParameterizedTest @EnumSource(RecursoCatalogoAplicacion.class)
    void debePermitirOperacionCuandoRecursoYPadrePertenecenALaAplicacion(RecursoCatalogoAplicacion tipo) {
        preparar(app);
        assertDoesNotThrow(()->regla.execute(AdministrarCatalogoAplicacionDomain.create(id(tipo),app,padre(tipo),tipo,AdministrarCatalogoAplicacionDomain.Operacion.ACTUALIZAR)));
    }
    @ParameterizedTest @EnumSource(RecursoCatalogoAplicacion.class)
    void debeRechazarEliminacionCuandoElRecursoPerteneceAOtraAplicacion(RecursoCatalogoAplicacion tipo) {
        preparar(otra);
        assertThrows(NotFoundException.class,()->regla.execute(AdministrarCatalogoAplicacionDomain.create(id(tipo),app,null,tipo,AdministrarCatalogoAplicacionDomain.Operacion.ELIMINAR)));
    }
    @Test void debeRechazarCreacionCuandoElPadrePerteneceAOtraAplicacion() {
        preparar(otra);
        assertThrows(NotFoundException.class,()->regla.execute(AdministrarCatalogoAplicacionDomain.create(null,app,funcion,RecursoCatalogoAplicacion.PARAMETRO,AdministrarCatalogoAplicacionDomain.Operacion.CREAR)));
    }
    @Test void debeRechazarCuandoLaAplicacionNoExiste() {
        assertThrows(NotFoundException.class,()->regla.execute(AdministrarCatalogoAplicacionDomain.create(null,app,app,RecursoCatalogoAplicacion.MODULO,AdministrarCatalogoAplicacionDomain.Operacion.CREAR)));
        verifyNoInteractions(modulos,funciones,parametros,metadatos,asociaciones);
    }
}
