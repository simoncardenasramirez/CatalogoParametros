package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.usecase.impl.ConsultarCambiosCatalogoAplicacionImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.CambiosCatalogoAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

class ConsultarCambiosCatalogoAplicacionTest {
    @Test void debeOmitirEventosDeOtraAplicacionCuandoEscuchaSuRama() {
        var app=UUID.randomUUID(); var otra=UUID.randomUUID(); var modulo=UUID.randomUUID(); var funcion=UUID.randomUUID(); var parametro=UUID.randomUUID();
        var aplicaciones=mock(BaseDatosAplicacionRepository.class); var modulos=mock(BaseDatosModuloRepository.class);
        var funciones=mock(BaseDatosFuncionalidadRepository.class); var parametros=mock(BaseDatosParametroRepository.class);
        var publisher=mock(CambiosCatalogoAplicacionPublisher.class);
        when(aplicaciones.findById(app)).thenReturn(Optional.of(mock(AplicacionEntity.class)));
        var m=mock(ModuloEntity.class); when(m.getIdAplicacion()).thenReturn(app); when(modulos.findById(modulo)).thenReturn(Optional.of(m));
        var f=mock(FuncionalidadEntity.class); when(f.getIdModulo()).thenReturn(modulo); when(funciones.findById(funcion)).thenReturn(Optional.of(f));
        var p=mock(ParametroEntity.class); when(p.getIdFuncionalidad()).thenReturn(funcion); when(parametros.findById(parametro)).thenReturn(Optional.of(p));
        var propio=new CambioCatalogoAplicacion("metadatos","UPDATED",UUID.randomUUID(),parametro);
        var ajeno=new CambioCatalogoAplicacion("modulos","DELETED",UUID.randomUUID(),otra);
        var ambiente=new CambioCatalogoAplicacion("ambientes","CREATED",UUID.randomUUID(),null);
        when(publisher.getStream()).thenReturn(Flux.just(ajeno, propio, ambiente));
        var interactor=new ConsultarCambiosCatalogoAplicacionImpl(publisher,aplicaciones,modulos,funciones,parametros,new co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService(new io.micrometer.core.instrument.simple.SimpleMeterRegistry()));
        assertEquals(List.of(propio, ambiente), interactor.execute(app).collectList().block());
    }
    @Test void debeRechazarLaSuscripcionCuandoLaAplicacionNoExiste() {
        var apps=mock(BaseDatosAplicacionRepository.class); var pub=mock(CambiosCatalogoAplicacionPublisher.class);
        var interactor=new ConsultarCambiosCatalogoAplicacionImpl(pub,apps,mock(BaseDatosModuloRepository.class),mock(BaseDatosFuncionalidadRepository.class),mock(BaseDatosParametroRepository.class),new co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService(new io.micrometer.core.instrument.simple.SimpleMeterRegistry()));
        assertThrows(NotFoundException.class, () -> interactor.execute(UUID.randomUUID()).collectList().block()); verifyNoInteractions(pub);
    }
}
