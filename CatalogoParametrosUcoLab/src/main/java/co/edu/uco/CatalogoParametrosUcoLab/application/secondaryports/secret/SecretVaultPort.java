package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.secret;

public interface SecretVaultPort {

    void crearSecreto(String nombre, String valor);

    String consultarSecreto(String nombre);
}
