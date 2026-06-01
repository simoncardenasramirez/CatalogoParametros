package co.edu.uco.CatalogoParametrosUcoLab.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "co.edu.uco.CatalogoParametrosUcoLab")
public class CatalogoParametrosUcoLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogoParametrosUcoLabApplication.class, args);
    }
}
