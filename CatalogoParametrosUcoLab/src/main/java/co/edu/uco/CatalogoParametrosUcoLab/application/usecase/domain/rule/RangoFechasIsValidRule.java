package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule;

import java.time.LocalDateTime;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

public final class RangoFechasIsValidRule {

    private RangoFechasIsValidRule() {
    }

    public static void execute(final LocalDateTime fechaInicio, final LocalDateTime fechaFinal) {
        ValidateHelper.validateRangoFechas(fechaInicio, fechaFinal);
    }
}
