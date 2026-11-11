import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    private Main() { }

    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in, StandardCharsets.UTF_8)) {
            Copa copa = new Copa();
            VistaConsola vista = new VistaConsola(entrada);
            new ControladorCopa(copa, vista).iniciar();
        }
    }
}
