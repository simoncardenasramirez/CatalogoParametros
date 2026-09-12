package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule;

import java.time.OffsetDateTime;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper;

public final class RangoFechasIsValidRule {

    private RangoFechasIsValidRule() {
    }

    public static void execute(final OffsetDateTime fechaInicio, final OffsetDateTime fechaFinal) {
        ValidateHelper.validateRangoFechas(fechaInicio, fechaFinal);
    }
}
