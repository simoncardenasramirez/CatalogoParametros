package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.validator;

public interface RuleValidator<T> {

    void validate(T data);
}
