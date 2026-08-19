package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Crea spans hijos del span HTTP para las capas internas de la arquitectura.
 * El span HTTP automático de WebFlux mide toda la ejecución del controlador.
 */
@Aspect
@Component
public class LayerTracingAspect {

    private static final String BASE_PACKAGE = "co.edu.uco.CatalogoParametrosUcoLab";

    private final ObservationRegistry observationRegistry;

    public LayerTracingAspect(final ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @Around("execution(public * " + BASE_PACKAGE
            + ".application.features..primaryports.interactor..*(..))")
    public Object observeInteractor(final ProceedingJoinPoint joinPoint) throws Throwable {
        return observe(joinPoint, "interactor");
    }

    @Around("execution(public * " + BASE_PACKAGE + ".application.features..usecase..*(..))")
    public Object observeUseCase(final ProceedingJoinPoint joinPoint) throws Throwable {
        return observe(joinPoint, "usecase");
    }

    @Around("execution(public * " + BASE_PACKAGE
            + ".infraestructure.secondaryadapters.repository..*(..))")
    public Object observeRepository(final ProceedingJoinPoint joinPoint) throws Throwable {
        return observe(joinPoint, "repository");
    }

    private Object observe(final ProceedingJoinPoint joinPoint, final String layer) throws Throwable {
        final Observation observation = startObservation(joinPoint, layer);
        try (Observation.Scope ignored = observation.openScope()) {
            return joinPoint.proceed();
        } catch (final Throwable exception) {
            observation.error(exception);
            throw exception;
        } finally {
            observation.stop();
        }
    }

    private Observation startObservation(final ProceedingJoinPoint joinPoint, final String layer) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String operation = layer + "." + signature.getDeclaringType().getSimpleName()
                + "." + signature.getMethod().getName();
        return Observation.start("application.layer", observationRegistry)
                .contextualName(operation)
                .lowCardinalityKeyValue("application.layer", layer)
                .lowCardinalityKeyValue("code.namespace", signature.getDeclaringTypeName())
                .lowCardinalityKeyValue("code.function", signature.getMethod().getName());
    }

}
