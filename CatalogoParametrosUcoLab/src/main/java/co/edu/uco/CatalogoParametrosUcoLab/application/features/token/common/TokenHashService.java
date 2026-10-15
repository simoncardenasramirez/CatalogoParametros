package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.common;

public interface TokenHashService {
    String hash(String token);
    boolean matches(String token, String tokenHash);
}
