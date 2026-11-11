import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/** Coordina el menú y comunica los errores del modelo. */
public class ControladorCopa {
    private final Copa copa;
    private final VistaConsola vista;

    public ControladorCopa(Copa copa, VistaConsola vista) {
        if (copa == null || vista == null) throw new IllegalArgumentException("Modelo y vista son obligatorios.");
        this.copa = copa;
        this.vista = vista;
    }

    public void iniciar() {
        cargarDatosIniciales();
        vista.mostrarMensaje("Datos de ejemplo cargados: 4 equipos, 4 encuentros y 1 exhibición.");
        while (true) {
            try {
                vista.mostrarMenu();
                int opcion = vista.leerEntero("Opción: ");
                if (opcion == 0) {
                    vista.mostrarMensaje("Hasta pronto.");
                    return;
                }
                ejecutarOpcion(opcion);
            } catch (NoSuchElementException error) {
                vista.mostrarMensaje("\nFin de la entrada. Programa terminado.");
                return;
            } catch (IllegalArgumentException | IllegalStateException | ArithmeticException error) {
                vista.mostrarMensaje("Error: " + error.getMessage());
            }
        }
    }

    private void cargarDatosIniciales() {
        Equipo faro = copa.registrarEquipo("FAR", "Faro", "Empresa Norte");
        Equipo sol = copa.registrarEquipo("SOL", "Sol", "Empresa Sur");
        Equipo luna = copa.registrarEquipo("LUN", "Luna", "Empresa Norte");
        Equipo rio = copa.registrarEquipo("RIO", "Río", "Empresa Sur");
        Encuentro primero = new EncuentroFutbolSala(faro, sol);
        Encuentro segundo = new EncuentroBaloncesto(sol, faro);
        Encuentro tercero = new EncuentroFutbolSala(luna, rio);
        Encuentro cuarto = new EncuentroBaloncesto(luna, sol);
        copa.agregarEncuentro(primero);
        copa.agregarEncuentro(segundo);
        copa.agregarEncuentro(tercero);
        copa.agregarEncuentro(cuarto);
        LocalDate fecha = LocalDate.of(2026, 11, 12);
        copa.programarEvento(primero, fecha, 9, "Cancha Central");
        copa.programarEvento(segundo, fecha, 11, "Gimnasio");
        copa.programarEvento(tercero, fecha, 14, "Cancha Central");
        Puntuable resultado1 = primero;
        Puntuable resultado2 = segundo;
        Puntuable resultado3 = tercero;
        resultado1.registrarResultado(3, 1);
        resultado2.registrarResultado(78, 74);
        resultado3.registrarResultado(2, 2);
        Exhibicion clinica = new Exhibicion("Clínica de baloncesto");
        copa.agregarExhibicion(clinica);
        copa.programarEvento(clinica, fecha, 10, "Gimnasio");
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1: registrarEquipo(); break;
            case 2: crearEncuentro(); break;
            case 3: crearExhibicion(); break;
            case 4: programarEvento(); break;
            case 5: registrarResultado(); break;
            case 6: mostrarTabla(); break;
            case 7: mostrarCalendario(); break;
            default: vista.mostrarMensaje("Error: opción no válida. Seleccione de 0 a 7.");
        }
    }

    private void registrarEquipo() {
        String codigo = vista.leerTexto("Código: ");
        String nombre = vista.leerTexto("Nombre: ");
        String empresa = vista.leerTexto("Empresa: ");
        Equipo equipo = copa.registrarEquipo(codigo, nombre, empresa);
        vista.mostrarMensaje("Equipo registrado: " + equipo.getNombre());
    }

    private void crearEncuentro() {
        vista.mostrarTabla(copa.getTablaPosiciones());
        int deporte = vista.leerEntero("Deporte (1 = fútbol sala, 2 = baloncesto): ");
        if (deporte != 1 && deporte != 2) throw new IllegalArgumentException("Deporte no válido.");
        Equipo equipo1 = copa.buscarEquipo(vista.leerTexto("Código del primer equipo: "));
        Equipo equipo2 = copa.buscarEquipo(vista.leerTexto("Código del segundo equipo: "));
        Encuentro encuentro = deporte == 1 ? new EncuentroFutbolSala(equipo1, equipo2)
                : new EncuentroBaloncesto(equipo1, equipo2);
        copa.agregarEncuentro(encuentro);
        vista.mostrarMensaje("Encuentro creado sin programar.");
    }

    private void crearExhibicion() {
        copa.agregarExhibicion(new Exhibicion(vista.leerTexto("Título: ")));
        vista.mostrarMensaje("Exhibición creada sin programar.");
    }

    private void programarEvento() {
        List<Programable> eventos = copa.getEventos();
        if (eventos.isEmpty()) throw new IllegalArgumentException("No hay eventos registrados.");
        vista.mostrarEventos(eventos);
        int indice = vista.leerEntero("Número del evento: ") - 1;
        if (indice < 0 || indice >= eventos.size()) throw new IllegalArgumentException("Evento no válido.");
        LocalDate fecha = vista.leerFecha("Fecha (AAAA-MM-DD): ");
        int hora = vista.leerEntero("Hora completa (0 a 23): ");
        String sede = vista.leerTexto("Sede: ");
        copa.programarEvento(eventos.get(indice), fecha, hora, sede);
        vista.mostrarMensaje("Evento programado correctamente.");
    }

    private void registrarResultado() {
        List<Encuentro> encuentros = copa.getEncuentros();
        if (encuentros.isEmpty()) throw new IllegalArgumentException("No hay encuentros registrados.");
        vista.mostrarEncuentros(encuentros);
        int indice = vista.leerEntero("Número del encuentro: ") - 1;
        if (indice < 0 || indice >= encuentros.size()) throw new IllegalArgumentException("Encuentro no válido.");
        int marcador1 = vista.leerEntero("Marcador del primer equipo: ");
        int marcador2 = vista.leerEntero("Marcador del segundo equipo: ");
        Puntuable encuentro = encuentros.get(indice);
        encuentro.registrarResultado(marcador1, marcador2);
        vista.mostrarMensaje("Resultado registrado y puntos de tabla actualizados.");
    }

    private void mostrarTabla() { vista.mostrarTabla(copa.getTablaPosiciones()); }
    private void mostrarCalendario() { vista.mostrarCalendario(copa); }
}
