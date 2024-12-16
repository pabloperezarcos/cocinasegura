package proyecto.cocinasegura;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CocinaseguraApplicationTests {

    @Test
    void mainMethodTest() throws Exception {
        // Construye un proceso separado para ejecutar el método main
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java",
                "-cp",
                System.getProperty("java.class.path"),
                "proyecto.cocinasegura.CocinaseguraApplication");

        // Ejecuta el proceso y verifica que se ejecuta correctamente
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        assertTrue(exitCode == 0, "El proceso principal debe terminar exitosamente.");
    }
}
