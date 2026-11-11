import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Entrada y salida de consola, sin reglas de puntuación ni de orden. */
public class VistaConsola {
    private final Scanner entrada;

    public VistaConsola(Scanner entrada) {
        if (entrada == null) throw new IllegalArgumentException("El lector de entradas es obligatorio.");
        this.entrada = entrada;
    }

    public void mostrarMenu() {
        System.out.println("\n=== ArenaGT Lite ===");
        System.out.println("1. Registrar equipo\n2. Crear encuentro\n3. Crear exhibición");
        System.out.println("4. Programar o reprogramar evento\n5. Registrar resultado");
        System.out.println("6. Mostrar tabla de posiciones\n7. Mostrar calendario\n0. Salir");
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        if (!entrada.hasNextLine()) throw new NoSuchElementException("Fin de la entrada.");
        return entrada.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException error) {
                mostrarMensaje("Error: ingrese un número entero dentro del rango de int.");
            }
        }
    }

    public LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje));
            } catch (DateTimeParseException error) {
                mostrarMensaje("Error: ingrese una fecha válida con formato AAAA-MM-DD.");
            }
        }
    }

    public void mostrarMensaje(String mensaje) { System.out.println(mensaje); }

    public void mostrarEncuentros(List<Encuentro> encuentros) {
        for (int i = 0; i < encuentros.size(); i++) {
            Encuentro encuentro = encuentros.get(i);
            String resultado = encuentro.getMarcador1() == null ? ""
                    : " | Marcador: " + encuentro.getMarcador1() + "-" + encuentro.getMarcador2();
            System.out.println((i + 1) + ". " + encuentro.getDescripcion()
                    + " | " + encuentro.getEstado() + resultado);
        }
    }

    public void mostrarEventos(List<Programable> eventos) {
        for (int i = 0; i < eventos.size(); i++) {
            Programable evento = eventos.get(i);
            String programacion = evento.getFecha() == null ? "Sin programar"
                    : String.format("%s %02d:00 | %s", evento.getFecha(), evento.getHora(), evento.getSede());
            System.out.println((i + 1) + ". " + evento.getDescripcion() + " | " + programacion);
        }
    }

    public void mostrarTabla(List<Equipo> equiposOrdenados) {
        System.out.println("\nTABLA DE POSICIONES");
        System.out.printf("%-9s %-18s %-22s %s%n", "Código", "Equipo", "Empresa", "Puntos");
        for (Equipo equipo : equiposOrdenados) {
            System.out.printf("%-9s %-18s %-22s %d%n", equipo.getCodigo(), equipo.getNombre(),
                    equipo.getEmpresa(), equipo.getPuntosTabla());
        }
        if (equiposOrdenados.isEmpty()) System.out.println("No hay equipos registrados.");
    }

    public void mostrarCalendario(Iterable<Programable> calendario) {
        System.out.println("\nCALENDARIO");
        boolean vacio = true;
        for (Programable evento : calendario) {
            vacio = false;
            System.out.printf("%s %02d:00 | %s | %s%n", evento.getFecha(), evento.getHora(),
                    evento.getSede(), evento.getDescripcion());
        }
        if (vacio) System.out.println("No hay eventos programados.");
    }
}
