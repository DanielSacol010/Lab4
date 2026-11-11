import java.time.LocalDate;
import java.util.List;

/** Evento sin equipos ni resultados; no implementa Puntuable. */
public class Exhibicion implements Programable {
    private final String titulo;
    private EstadoEvento estado;
    private LocalDate fecha;
    private int hora;
    private String sede;

    public Exhibicion(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la exhibición no puede estar vacío.");
        }
        this.titulo = titulo.trim();
        estado = EstadoEvento.SIN_PROGRAMAR;
        hora = -1;
    }

    @Override
    public void programar(LocalDate fecha, int hora, String sede) {
        if (fecha == null) throw new IllegalArgumentException("La fecha es obligatoria.");
        if (hora < 0 || hora > 23) throw new IllegalArgumentException("La hora debe estar entre 0 y 23.");
        if (sede == null || sede.trim().isEmpty()) {
            throw new IllegalArgumentException("La sede no puede estar vacía.");
        }
        this.fecha = fecha;
        this.hora = hora;
        this.sede = sede.trim();
        estado = EstadoEvento.PROGRAMADO;
    }

    public EstadoEvento getEstado() { return estado; }
    @Override public LocalDate getFecha() { return fecha; }
    @Override public int getHora() { return hora; }
    @Override public String getSede() { return sede; }
    @Override public String getDescripcion() { return "Exhibición: " + titulo; }
    @Override public List<Equipo> getEquiposParticipantes() { return List.of(); }
}
