package co.edu.uco.CatalogoParametrosUcoLab.application.usecase;

public interface RuleValidator<T> {

    void validate(T data);
}
