package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.impl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.CargarCatalogoMasivo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaError;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaFila;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaLibro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaLote;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaResultado;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.event.CrearFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.publisher.CrearFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.event.CrearMetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.publisher.CrearMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.event.CrearMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.publisher.CrearMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.event.CrearParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.bulk.CargaMasivaExcelPort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CargaMasivaParametrosRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.MetadatoValorTypeRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.DateTimeHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CargarCatalogoMasivoImpl implements CargarCatalogoMasivo {
    private static final Logger LOGGER = LoggerFactory.getLogger(CargarCatalogoMasivoImpl.class);
    private static final String OPERATION_NAME = "cargar-catalogo-masivo";

    private final CargaMasivaExcelPort excel;
    private final CargaMasivaParametrosRepository loteRepository;
    private final OrganizacionRepository organizaciones;
    private final AplicacionRepository aplicaciones;
    private final ModuloRepository modulos;
    private final FuncionalidadRepository funcionalidades;
    private final ParametroRepository parametros;
    private final TipoParametroRepository tiposParametro;
    private final MetadatoRepository metadatos;
    private final TipoMetadatoRepository tiposMetadato;
    private final AmbienteRepository ambientes;
    private final EstadoMetadatoAmbienteRepository estadosMetadato;
    private final MetadatoAmbienteRepository metadatosAmbiente;
    private final CrearFuncionalidadPublisher funcionalidadPublisher;
    private final CrearParametroPublisher parametroPublisher;
    private final CrearMetadatoPublisher metadatoPublisher;
    private final CrearMetadatoAmbientePublisher metadatoAmbientePublisher;
    private final TelemetryService telemetry;

    public CargarCatalogoMasivoImpl(final CargaMasivaExcelPort excel,
            final CargaMasivaParametrosRepository loteRepository, final OrganizacionRepository organizaciones,
            final AplicacionRepository aplicaciones,
            final ModuloRepository modulos, final FuncionalidadRepository funcionalidades,
            final ParametroRepository parametros, final TipoParametroRepository tiposParametro,
            final MetadatoRepository metadatos, final TipoMetadatoRepository tiposMetadato,
            final AmbienteRepository ambientes, final EstadoMetadatoAmbienteRepository estadosMetadato,
            final MetadatoAmbienteRepository metadatosAmbiente,
            final CrearFuncionalidadPublisher funcionalidadPublisher,
            final CrearParametroPublisher parametroPublisher, final CrearMetadatoPublisher metadatoPublisher,
            final CrearMetadatoAmbientePublisher metadatoAmbientePublisher,
            final TelemetryService telemetry) {
        this.excel = excel;
        this.loteRepository = loteRepository;
        this.organizaciones = organizaciones;
        this.aplicaciones = aplicaciones;
        this.modulos = modulos;
        this.funcionalidades = funcionalidades;
        this.parametros = parametros;
        this.tiposParametro = tiposParametro;
        this.metadatos = metadatos;
        this.tiposMetadato = tiposMetadato;
        this.ambientes = ambientes;
        this.estadosMetadato = estadosMetadato;
        this.metadatosAmbiente = metadatosAmbiente;
        this.funcionalidadPublisher = funcionalidadPublisher;
        this.parametroPublisher = parametroPublisher;
        this.metadatoPublisher = metadatoPublisher;
        this.metadatoAmbientePublisher = metadatoAmbientePublisher;
        this.telemetry = telemetry;
    }

    @Override public byte[] plantilla() { return excel.plantilla(); }

    @Override
    public CargaMasivaResultado validar(final byte[] archivo) {
        return telemetry.recordBusinessOperation(OPERATION_NAME + "-validar", () -> evaluar(archivo).resultado());
    }

    @Override
    public CargaMasivaResultado cargar(final byte[] archivo) {
        return telemetry.recordBusinessOperation(OPERATION_NAME, () -> {
            var evaluacion = evaluar(archivo);
            if (!evaluacion.resultado().ready()) {
                return evaluacion.resultado();
            }
            loteRepository.guardar(evaluacion.lote());
            evaluacion.lote().funcionalidades().forEach(entity -> funcionalidadPublisher.sendEvent(CrearFuncionalidadEvent.created(entity)));
            evaluacion.lote().parametros().forEach(entity -> parametroPublisher.sendEvent(CrearParametroEvent.created(entity)));
            evaluacion.lote().metadatos().forEach(entity -> metadatoPublisher.sendEvent(CrearMetadatoEvent.created(entity)));
            evaluacion.lote().metadatosAmbiente().forEach(entity -> metadatoAmbientePublisher.sendEvent(CrearMetadatoAmbienteEvent.created(entity)));
            LOGGER.info("[CARGA-MASIVA-PARAMETROS] Se guardaron {} funcionalidades, {} parametros y {} metadatos",
                    evaluacion.lote().funcionalidades().size(), evaluacion.lote().parametros().size(),
                    evaluacion.lote().metadatos().size());
            return evaluacion.resultado();
        });
    }

    @Override public byte[] reporteErrores(final byte[] archivo) {
        var evaluacion = evaluar(archivo);
        return excel.anotar(archivo, evaluacion.resultado().errores());
    }

    private Evaluacion evaluar(final byte[] archivo) {
        var libro = excel.leer(archivo);
        var errores = new ArrayList<>(libro.errores());
        var funcionalidadesNuevas = new ArrayList<FuncionalidadEntity>();
        var parametrosNuevos = new ArrayList<ParametroEntity>();
        var metadatosNuevos = new ArrayList<MetadatoEntity>();
        var metadatosAmbienteNuevos = new ArrayList<MetadatoAmbienteEntity>();
        var funcionalidadesPorClave = new HashMap<String, FuncionalidadEntity>();
        var parametrosPorClave = new HashMap<String, ParametroEntity>();
        var metadatosPorClave = new HashSet<String>();
        var referencias = cargarReferencias();

        for (var fila : libro.filas()) {
            try {
                procesarFila(fila, referencias, funcionalidadesPorClave, parametrosPorClave,
                        metadatosPorClave, funcionalidadesNuevas, parametrosNuevos, metadatosNuevos,
                        metadatosAmbienteNuevos);
            } catch (FilaInvalidaException exception) {
                errores.add(new CargaMasivaError(fila.fila(), "Catalogo", exception.campo,
                        exception.valor, exception.codigo, exception.getMessage()));
            }
        }

        var filasInvalidas = (int) errores.stream().map(CargaMasivaError::fila).filter(fila -> fila > 0).distinct().count();
        var total = libro.filas().size();
        var resultado = new CargaMasivaResultado(errores.isEmpty(), total,
                Math.max(0, total - Math.min(total, filasInvalidas)), Math.min(total, filasInvalidas), errores);
        return new Evaluacion(resultado, new CargaMasivaLote(funcionalidadesNuevas, parametrosNuevos,
                metadatosNuevos, metadatosAmbienteNuevos));
    }

    private Referencias cargarReferencias() {
        return new Referencias(organizaciones.findAll(), aplicaciones.findAll(), modulos.findAll(), funcionalidades.findAll(),
                parametros.findAll(), tiposParametro.findAll(), metadatos.findAll(), tiposMetadato.findAll(),
                ambientes.findAll(), estadosMetadato.findAll(), metadatosAmbiente.findAll());
    }

    private void procesarFila(final CargaMasivaFila fila, final Referencias refs,
            final Map<String, FuncionalidadEntity> funcionalidadesPorClave,
            final Map<String, ParametroEntity> parametrosPorClave, final Set<String> metadatosPorClave,
            final List<FuncionalidadEntity> funcionalidadesNuevas, final List<ParametroEntity> parametrosNuevos,
            final List<MetadatoEntity> metadatosNuevos, final List<MetadatoAmbienteEntity> metadatosAmbienteNuevos) {
        var organizacion = unica(refs.organizaciones, fila.organizacion(), "Organizacion", "ORGANIZATION_NOT_FOUND",
                "La organizacion no existe.", (entity, value) -> entity.getNombre().equalsIgnoreCase(value));
        var aplicacion = unica(refs.aplicaciones, fila.aplicacion(), "Aplicacion", "APPLICATION_NOT_FOUND", "La aplicacion no existe.",
                (entity, value) -> organizacion.getId().equals(entity.getIdOrganizacion()) && entity.getNombre().equalsIgnoreCase(value));
        validarVigencia(aplicacion.isActiva(), aplicacion.getFechaInicio(), aplicacion.getFechaFinal(), "Aplicacion");
        var modulo = unica(refs.modulos, fila.modulo(), "Modulo", "MODULE_NOT_FOUND", "El modulo no existe.",
                (entity, value) -> entity.getIdAplicacion().equals(aplicacion.getId()) && entity.getNombre().equalsIgnoreCase(value));
        validarVigencia(modulo.isActivo(), modulo.getFechaInicio(), modulo.getFechaFinal(), "Modulo");

        var funcionalidadClave = clave(modulo.getId(), fila.funcionalidad());
        var funcionalidad = funcionalidadesPorClave.get(funcionalidadClave);
        if (funcionalidad == null) {
            funcionalidad = refs.funcionalidades.stream()
                    .filter(entity -> entity.getIdModulo().equals(modulo.getId()) && entity.getNombre().equalsIgnoreCase(fila.funcionalidad().trim()))
                    .findFirst().orElse(null);
            if (funcionalidad == null) {
                funcionalidad = crearFuncionalidad(fila, modulo.getId());
                funcionalidadesNuevas.add(funcionalidad);
            } else {
                validarDatosFuncionalidadExistente(fila, funcionalidad);
            }
            validarVigencia(funcionalidad.isActivo(), funcionalidad.getFechaInicio(), funcionalidad.getFechaFinal(), "Funcionalidad");
            funcionalidadesPorClave.put(funcionalidadClave, funcionalidad);
        }

        var tipoParametro = unica(refs.tiposParametro, fila.tipoParametro(), "Tipo parametro", "PARAMETER_TYPE_NOT_FOUND",
                "El tipo de parametro no existe.", (entity, value) -> entity.getNombre().equalsIgnoreCase(value));
        var parametroClave = clave(funcionalidad.getId(), fila.parametro());
        var parametro = parametrosPorClave.get(parametroClave);
        if (parametro == null) {
            final var funcionalidadResuelta = funcionalidad;
            parametro = refs.parametros.stream()
                    .filter(entity -> entity.getIdFuncionalidad().equals(funcionalidadResuelta.getId()) && entity.getNombre().equalsIgnoreCase(fila.parametro().trim()))
                    .findFirst().orElse(null);
            if (parametro == null) {
                parametro = ParametroEntity.create(UUIDHelper.generate(), requerido(fila.parametro(), "Parametro"),
                        funcionalidad.getId(), tipoParametro.getId(), booleano(fila.activoParametro(), true, "Activo parametro"));
                parametrosNuevos.add(parametro);
            } else {
                if (!parametro.getIdTipoParametro().equals(tipoParametro.getId())) {
                    throw invalid("Tipo parametro", fila.tipoParametro(), "PARAMETER_TYPE_MISMATCH", "El parametro existente tiene otro tipo de parametro.");
                }
                if (!vacio(fila.activoParametro())
                        && booleano(fila.activoParametro(), true, "Activo parametro") != parametro.isActivo()) {
                    throw invalid("Activo parametro", fila.activoParametro(), "PARAMETER_DATA_MISMATCH", "El parametro existente tiene otro estado.");
                }
            }
            parametrosPorClave.put(parametroClave, parametro);
        }

        var tipoMetadato = unica(refs.tiposMetadato, fila.tipoMetadato(), "Tipo metadato", "METADATA_TYPE_NOT_FOUND",
                "El tipo de metadato no existe.", (entity, value) -> entity.getTipo().equalsIgnoreCase(value));
        var ambiente = unica(refs.ambientes, fila.ambiente(), "Ambiente", "ENVIRONMENT_NOT_FOUND", "El ambiente no existe.",
                (entity, value) -> entity.getNombre().equalsIgnoreCase(value));
        var estado = unica(refs.estadosMetadato, fila.estadoMetadatoAmbiente(), "Estado metadato ambiente",
                "METADATA_STATUS_NOT_FOUND", "El estado del metadato ambiente no existe.",
                (entity, value) -> entity.getNombre().equalsIgnoreCase(value));
        try {
            MetadatoValorTypeRule.execute(tipoMetadato.getTipo(), fila.valor());
        } catch (ValidationException exception) {
            throw invalid("Valor", valorTexto(fila), "INVALID_METADATA_VALUE", exception.getMessage());
        }

        final var parametroResuelto = parametro;
        var metadatoClave = parametro.getId() + "|" + tipoMetadato.getId() + "|" + ambiente.getId();
        if (!metadatosPorClave.add(metadatoClave)
                || refs.metadatos.stream().anyMatch(entity -> entity.getIdParametro().equals(parametroResuelto.getId())
                        && entity.getIdTipoMetadato().equals(tipoMetadato.getId())
                        && ambiente.getId().equals(entity.getIdAmbiente()))) {
            throw invalid("Metadato", metadatoClave, "METADATA_ALREADY_EXISTS", "Ya existe un metadato para el parametro, tipo y ambiente indicados.");
        }
        var asociacionClave = parametro.getId() + "|" + ambiente.getId();
        if (refs.metadatosAmbiente.stream().anyMatch(entity -> (entity.getIdParametro() + "|" + entity.getIdAmbiente()).equals(asociacionClave))
                || metadatosAmbienteNuevos.stream().anyMatch(entity -> (entity.getIdParametro() + "|" + entity.getIdAmbiente()).equals(asociacionClave))) {
            throw invalid("Ambiente", fila.ambiente(), "METADATA_ENVIRONMENT_ALREADY_EXISTS", "Ya existe la asociacion del parametro con el ambiente.");
        }
        metadatosNuevos.add(MetadatoEntity.create(UUIDHelper.generate(), parametro.getId(), tipoMetadato.getId(), ambiente.getId(), fila.valor()));
        metadatosAmbienteNuevos.add(MetadatoAmbienteEntity.create(UUIDHelper.generate(), parametro.getId(), ambiente.getId(), estado.getId()));
    }

    private FuncionalidadEntity crearFuncionalidad(final CargaMasivaFila fila, final UUID idModulo) {
        var inicio = fecha(fila.fechaInicioFuncionalidad(), "Fecha inicio funcionalidad");
        var fin = fecha(fila.fechaFinalFuncionalidad(), "Fecha final funcionalidad");
        if (fin != null && inicio != null && fin.isBefore(inicio)) {
            throw invalid("Fecha final funcionalidad", fila.fechaFinalFuncionalidad(), "INVALID_DATE_RANGE", "La fecha final no puede ser anterior a la fecha inicial.");
        }
        return FuncionalidadEntity.create(UUIDHelper.generate(), requerido(fila.funcionalidad(), "Funcionalidad"), idModulo,
                booleano(fila.activoFuncionalidad(), true, "Activo funcionalidad"), inicio, fin);
    }

    private void validarDatosFuncionalidadExistente(final CargaMasivaFila fila, final FuncionalidadEntity entity) {
        if (!vacio(fila.activoFuncionalidad()) && booleano(fila.activoFuncionalidad(), true, "Activo funcionalidad") != entity.isActivo()) {
            throw invalid("Activo funcionalidad", fila.activoFuncionalidad(), "FUNCTIONALITY_DATA_MISMATCH", "La funcionalidad existente tiene otro estado.");
        }
        var inicio = fecha(fila.fechaInicioFuncionalidad(), "Fecha inicio funcionalidad");
        var fin = fecha(fila.fechaFinalFuncionalidad(), "Fecha final funcionalidad");
        if ((inicio != null && !inicio.equals(entity.getFechaInicio())) || (fin != null && !fin.equals(entity.getFechaFinal()))) {
            throw invalid("Fechas funcionalidad", fila.fechaInicioFuncionalidad(), "FUNCTIONALITY_DATA_MISMATCH", "Las fechas no coinciden con la funcionalidad existente.");
        }
    }

    private static <T> T unica(final List<T> valores, final String texto, final String campo, final String codigo,
            final String mensaje, final Coincidencia<T> coincidencia) {
        var valor = texto == null ? "" : texto.trim();
        if (valor.isEmpty()) throw invalid(campo, valor, "REQUIRED", "El campo " + campo + " es obligatorio.");
        var encontrados = valores.stream().filter(entity -> coincidencia.coincide(entity, valor)).toList();
        if (encontrados.isEmpty()) throw invalid(campo, valor, codigo, mensaje);
        if (encontrados.size() > 1) throw invalid(campo, valor, "AMBIGUOUS_REFERENCE", "La referencia " + valor + " no es unica.");
        return encontrados.get(0);
    }

    private static void validarVigencia(final boolean activo, final OffsetDateTime inicio, final OffsetDateTime fin,
            final String campo) {
        var ahora = OffsetDateTime.now();
        if (!activo) throw invalid(campo, "false", "INACTIVE_REFERENCE", "La referencia " + campo + " esta inactiva.");
        if (inicio != null && ahora.isBefore(inicio) || fin != null && ahora.isAfter(fin)) {
            throw invalid(campo, "", "REFERENCE_OUT_OF_VALIDITY", "La referencia " + campo + " esta fuera de vigencia.");
        }
    }

    private static boolean booleano(final String texto, final boolean porDefecto, final String campo) {
        if (vacio(texto)) return porDefecto;
        if (!"true".equalsIgnoreCase(texto.trim()) && !"false".equalsIgnoreCase(texto.trim())) {
            throw invalid(campo, texto, "INVALID_BOOLEAN", "El campo " + campo + " debe ser true o false.");
        }
        return Boolean.parseBoolean(texto.trim());
    }

    private static OffsetDateTime fecha(final String texto, final String campo) {
        if (vacio(texto)) return null;
        try {
            return DateTimeHelper.toUtc(OffsetDateTime.parse(texto.trim()));
        } catch (RuntimeException exception) {
            throw invalid(campo, texto, "INVALID_DATE", "La fecha debe estar en formato ISO-8601 con zona horaria.");
        }
    }

    private static String requerido(final String texto, final String campo) {
        if (vacio(texto)) throw invalid(campo, texto, "REQUIRED", "El campo " + campo + " es obligatorio.");
        return texto.trim();
    }

    private static String clave(final UUID padre, final String nombre) {
        return padre + "|" + normalizar(nombre);
    }

    private static String normalizar(final String texto) {
        return texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
    }

    private static boolean vacio(final String texto) { return texto == null || texto.trim().isEmpty(); }
    private static String valorTexto(final CargaMasivaFila fila) { return fila.valor() == null ? "" : fila.valor().toString(); }

    private static FilaInvalidaException invalid(final String campo, final String valor, final String codigo, final String mensaje) {
        return new FilaInvalidaException(campo, valor, codigo, mensaje);
    }

    private record Evaluacion(CargaMasivaResultado resultado, CargaMasivaLote lote) { }
    private record Referencias(List<OrganizacionEntity> organizaciones, List<AplicacionEntity> aplicaciones, List<ModuloEntity> modulos,
            List<FuncionalidadEntity> funcionalidades, List<ParametroEntity> parametros,
            List<TipoParametroEntity> tiposParametro, List<MetadatoEntity> metadatos,
            List<TipoMetadatoEntity> tiposMetadato, List<AmbienteEntity> ambientes,
            List<EstadoMetadatoAmbienteEntity> estadosMetadato, List<MetadatoAmbienteEntity> metadatosAmbiente) { }
    @FunctionalInterface private interface Coincidencia<T> { boolean coincide(T entity, String value); }
    private static final class FilaInvalidaException extends RuntimeException {
        private final String campo;
        private final String valor;
        private final String codigo;
        private FilaInvalidaException(final String campo, final String valor, final String codigo, final String mensaje) {
            super(mensaje); this.campo = campo; this.valor = valor; this.codigo = codigo;
        }
    }
}
