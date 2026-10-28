package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.vigencia;

import java.util.*;
import org.junit.jupiter.api.Test;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.publisher.ActualizarMetadatoAmbientePublisher;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class CambiosEstadoCatalogoPublisherImplTest {
    private final BaseDatosParametroRepository parametros=mock(BaseDatosParametroRepository.class);
    private final ActualizarParametroPublisher eventos=mock(ActualizarParametroPublisher.class);
    private final CambiosEstadoCatalogoPublisherImpl publisher=new CambiosEstadoCatalogoPublisherImpl(
            mock(BaseDatosAplicacionRepository.class),mock(ActualizarAplicacionPublisher.class),
            mock(BaseDatosModuloRepository.class),mock(ActualizarModuloPublisher.class),
            mock(BaseDatosFuncionalidadRepository.class),mock(ActualizarFuncionalidadPublisher.class),
            parametros,eventos,mock(BaseDatosMetadatoAmbienteRepository.class),mock(ActualizarMetadatoAmbientePublisher.class));

    private RegistroVigencia cambio(ParametroEntity entidad) {
        return new RegistroVigencia(TipoRegistroVigencia.PARAMETRO,entidad.getId(),entidad.getIdFuncionalidad(),null,null,false,null,null);
    }
    private ParametroEntity parametro() {
        return ParametroEntity.create(UUID.randomUUID(),"Parametro",UUID.randomUUID(),UUID.randomUUID(),false);
    }
    @Test void debeAgruparLecturaYExcluirOtrosRegistrosCuandoNotificaUnaCascada() {
        var primero=parametro(); var segundo=parametro(); var ajeno=parametro();
        when(parametros.findAll()).thenReturn(List.of(primero,segundo,ajeno));
        publisher.publicar(List.of(cambio(primero),cambio(segundo)));
        verify(parametros,times(1)).findAll();
        verify(parametros,never()).findById(any());
        verify(eventos).sendEvent(argThat(e->e.getParametro().getId().equals(primero.getId())));
        verify(eventos).sendEvent(argThat(e->e.getParametro().getId().equals(segundo.getId())));
        verify(eventos,never()).sendEvent(argThat(e->e.getParametro().getId().equals(ajeno.getId())));
    }
    @Test void debeLeerSoloUnRegistroCuandoLaCascadaCambiaUnParametro() {
        var entidad=parametro();
        when(parametros.findById(entidad.getId())).thenReturn(Optional.of(entidad));
        publisher.publicar(List.of(cambio(entidad)));
        verify(parametros).findById(entidad.getId());
        verify(parametros,never()).findAll();
        verify(eventos).sendEvent(any());
    }
}
