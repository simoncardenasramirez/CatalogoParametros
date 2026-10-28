package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion;

import java.util.*;
import org.junit.jupiter.api.Test;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.usecase.impl.ConsultarCatalogoAplicacionImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ConsultarCatalogoAplicacionImplTest {
    private final BaseDatosAplicacionRepository apps=mock(BaseDatosAplicacionRepository.class);
    private final BaseDatosModuloRepository modulos=mock(BaseDatosModuloRepository.class);
    private final BaseDatosFuncionalidadRepository funciones=mock(BaseDatosFuncionalidadRepository.class);
    private final BaseDatosParametroRepository parametros=mock(BaseDatosParametroRepository.class);
    private final BaseDatosMetadatoRepository metadatos=mock(BaseDatosMetadatoRepository.class);
    private final BaseDatosMetadatoAmbienteRepository asociaciones=mock(BaseDatosMetadatoAmbienteRepository.class);
    private final ConsultarCatalogoAplicacionImpl caso=new ConsultarCatalogoAplicacionImpl(apps,modulos,funciones,parametros,
            metadatos,asociaciones,mock(BaseDatosAmbienteRepository.class),mock(BaseDatosTipoParametroRepository.class),
            mock(BaseDatosTipoMetadatoRepository.class),mock(BaseDatosEstadoMetadatoAmbienteRepository.class),
            new TelemetryService(new SimpleMeterRegistry()));

    @Test void debeExcluirTodaLaOtraRamaCuandoConsultaUnaAplicacion() {
        var app=UUID.randomUUID(); var otra=UUID.randomUUID();
        var m=ModuloEntity.create(UUID.randomUUID(),"Propio",app,false,null,null);
        var ajeno=ModuloEntity.create(UUID.randomUUID(),"Ajeno",otra,true,null,null);
        var f=FuncionalidadEntity.create(UUID.randomUUID(),"Propia",m.getId(),false,null,null);
        var fAjena=FuncionalidadEntity.create(UUID.randomUUID(),"Ajena",ajeno.getId(),true,null,null);
        var p=ParametroEntity.create(UUID.randomUUID(),"Propio",f.getId(),UUID.randomUUID(),false);
        var pAjeno=ParametroEntity.create(UUID.randomUUID(),"Ajeno",fAjena.getId(),UUID.randomUUID(),true);
        var meta=MetadatoEntity.create(UUID.randomUUID(),p.getId(),UUID.randomUUID(),null);
        var metaAjeno=MetadatoEntity.create(UUID.randomUUID(),pAjeno.getId(),UUID.randomUUID(),null);
        var a=MetadatoAmbienteEntity.create(UUID.randomUUID(),p.getId(),UUID.randomUUID(),UUID.randomUUID());
        var aAjena=MetadatoAmbienteEntity.create(UUID.randomUUID(),pAjeno.getId(),a.getIdAmbiente(),UUID.randomUUID());
        when(apps.findById(app)).thenReturn(Optional.of(AplicacionEntity.create(app,"App",UUID.randomUUID(),false,null,null)));
        when(modulos.findAll()).thenReturn(List.of(m,ajeno));
        when(funciones.findAll()).thenReturn(List.of(f,fAjena));
        when(parametros.findAll()).thenReturn(List.of(p,pAjeno));
        when(metadatos.findAll()).thenReturn(List.of(meta,metaAjeno));
        when(asociaciones.findAll()).thenReturn(List.of(a,aAjena));
        var resultado=caso.execute(app);
        assertEquals(List.of(m),resultado.modulos()); assertEquals(List.of(f),resultado.funcionalidades());
        assertEquals(List.of(p),resultado.parametros()); assertEquals(List.of(meta),resultado.metadatos());
        assertEquals(List.of(a),resultado.metadatosAmbiente());
        assertFalse(resultado.aplicacion().isActiva(),"La administracion puede consultar sus inactivos.");
    }
    @Test void debeRechazarConsultaCuandoNoExisteLaAplicacion() {
        assertThrows(NotFoundException.class,()->caso.execute(UUID.randomUUID()));
        verifyNoInteractions(modulos,funciones,parametros,metadatos,asociaciones);
    }
}
