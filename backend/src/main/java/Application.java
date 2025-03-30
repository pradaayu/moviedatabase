

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Bootstraps the application: initializes & starts the application.
 * <br/>
 * This code initializes the application context, scans for components, and starts the embedded server (if applicable).
 * <br/> 
 * Bootstrapping ensures that all necessary resources and configurations are in place for the application to function properly.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
