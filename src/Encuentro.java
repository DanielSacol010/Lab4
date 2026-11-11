import java.time.LocalDate;
import java.util.List;

/** Datos comunes y secuencia de registro compartida por todos los deportes. */
public abstract class Encuentro implements Programable, Puntuable {
    private final Equipo equipo1;
    private final Equipo equipo2;
    private Integer marcador1;
    private Integer marcador2;
    private EstadoEvento estado;
    private LocalDate fecha;
    private int hora;
    private String sede;

    protected Encuentro(Equipo equipo1, Equipo equipo2) {
        if (equipo1 == null || equipo2 == null) {
            throw new IllegalArgumentException("Se requieren dos equipos registrados.");
        }
        if (equipo1.getCodigo().equals(equipo2.getCodigo())) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo.");
        }
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        estado = EstadoEvento.SIN_PROGRAMAR;
        hora = -1;
    }

    /** La disponibilidad global se valida previamente en Copa.programarEvento. */
    @Override
    public void programar(LocalDate fecha, int hora, String sede) {
        if (estado == EstadoEvento.FINALIZADO) {
            throw new IllegalStateException("No se puede reprogramar un encuentro finalizado.");
        }
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

    /** Valida todo antes de modificar marcador, puntos o estado. */
    @Override
    public final void registrarResultado(int marcador1, int marcador2) {
        if (estado != EstadoEvento.PROGRAMADO) {
            throw new IllegalStateException("Solo se registran resultados de encuentros programados sin resultado.");
        }
        if (marcador1 < 0 || marcador2 < 0) {
            throw new IllegalArgumentException("Los marcadores deben ser enteros no negativos.");
        }
        validarReglasDeporte(marcador1, marcador2);
        int[] puntos = calcularPuntos(marcador1, marcador2);
        if (puntos == null || puntos.length != 2 || puntos[0] < 0 || puntos[1] < 0) {
            throw new IllegalArgumentException("El deporte debe devolver dos puntos de tabla no negativos.");
        }
        // Evita una actualización parcial incluso si se agotara el rango de int.
        Math.addExact(equipo1.getPuntosTabla(), puntos[0]);
        Math.addExact(equipo2.getPuntosTabla(), puntos[1]);
        equipo1.sumarPuntos(puntos[0]);
        equipo2.sumarPuntos(puntos[1]);
        this.marcador1 = marcador1;
        this.marcador2 = marcador2;
        estado = EstadoEvento.FINALIZADO;
    }

    protected abstract void validarReglasDeporte(int marcador1, int marcador2);
    protected abstract int[] calcularPuntos(int marcador1, int marcador2);
    @Override public abstract String getDescripcion();
    public EstadoEvento getEstado() { return estado; }
    public Integer getMarcador1() { return marcador1; }
    public Integer getMarcador2() { return marcador2; }
    @Override public LocalDate getFecha() { return fecha; }
    @Override public int getHora() { return hora; }
    @Override public String getSede() { return sede; }
    @Override public List<Equipo> getEquiposParticipantes() { return List.of(equipo1, equipo2); }
}
