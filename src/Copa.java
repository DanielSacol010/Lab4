import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Modelo de la copa. No consulta el tipo concreto de los eventos o deportes. */
public class Copa implements Iterable<Programable> {
    private final Map<String, Equipo> equipos;
    private final List<Encuentro> encuentros;
    private final List<Exhibicion> exhibiciones;

    public Copa() {
        equipos = new LinkedHashMap<>();
        encuentros = new ArrayList<>();
        exhibiciones = new ArrayList<>();
    }

    public Equipo registrarEquipo(String codigo, String nombre, String empresa) {
        Equipo equipo = new Equipo(codigo, nombre, empresa);
        if (equipos.containsKey(equipo.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un equipo con el código " + equipo.getCodigo() + ".");
        }
        equipos.put(equipo.getCodigo(), equipo);
        return equipo;
    }

    public Equipo buscarEquipo(String codigo) {
        Equipo equipo = codigo == null ? null : equipos.get(codigo.trim());
        if (equipo == null) throw new IllegalArgumentException("No existe el equipo con código " + codigo + ".");
        return equipo;
    }

    public void agregarEncuentro(Encuentro encuentro) {
        if (encuentro == null) throw new IllegalArgumentException("El encuentro es obligatorio.");
        if (encuentros.contains(encuentro)) throw new IllegalArgumentException("El encuentro ya está registrado.");
        for (Equipo equipo : encuentro.getEquiposParticipantes()) {
            if (equipos.get(equipo.getCodigo()) != equipo) {
                throw new IllegalArgumentException("Los participantes deben pertenecer a esta copa.");
            }
        }
        if (encuentro.getEstado() != EstadoEvento.SIN_PROGRAMAR) {
            throw new IllegalArgumentException("Agregue el encuentro antes de programarlo.");
        }
        encuentros.add(encuentro);
    }

    public void agregarExhibicion(Exhibicion exhibicion) {
        if (exhibicion == null) throw new IllegalArgumentException("La exhibición es obligatoria.");
        if (exhibiciones.contains(exhibicion)) throw new IllegalArgumentException("La exhibición ya está registrada.");
        if (exhibicion.getEstado() != EstadoEvento.SIN_PROGRAMAR) {
            throw new IllegalArgumentException("Agregue la exhibición antes de programarla.");
        }
        exhibiciones.add(exhibicion);
    }

    public List<Encuentro> getEncuentros() { return List.copyOf(encuentros); }

    public List<Programable> getEventos() {
        List<Programable> eventos = new ArrayList<>(encuentros);
        eventos.addAll(exhibiciones);
        return List.copyOf(eventos);
    }

    /** Único punto de entrada de la aplicación para programar o reprogramar. */
    public void programarEvento(Programable evento, LocalDate fecha, int hora, String sede) {
        validarProgramacion(evento, fecha, hora, sede);
        evento.programar(fecha, hora, sede);
    }

    private void validarProgramacion(Programable evento, LocalDate fecha, int hora, String sede) {
        if (evento == null || !getEventos().contains(evento)) {
            throw new IllegalArgumentException("El evento debe estar registrado en esta copa.");
        }
        if (fecha == null) throw new IllegalArgumentException("La fecha es obligatoria.");
        if (hora < 0 || hora > 23) throw new IllegalArgumentException("La hora debe estar entre 0 y 23.");
        if (sede == null || sede.trim().isEmpty()) {
            throw new IllegalArgumentException("La sede no puede estar vacía.");
        }
        for (Programable otro : getEventos()) {
            if (otro == evento || !fecha.equals(otro.getFecha()) || hora != otro.getHora()) continue;
            if (sede.trim().equalsIgnoreCase(otro.getSede())) {
                throw new IllegalArgumentException("La sede ya está ocupada en esa fecha y hora.");
            }
            for (Equipo equipo : evento.getEquiposParticipantes()) {
                if (otro.getEquiposParticipantes().contains(equipo)) {
                    throw new IllegalArgumentException("El equipo " + equipo.getNombre()
                            + " ya tiene otro encuentro en esa fecha y hora.");
                }
            }
        }
    }

    public List<Equipo> getTablaPosiciones() {
        List<Equipo> tabla = new ArrayList<>(equipos.values());
        Collections.sort(tabla);
        return List.copyOf(tabla);
    }

    @Override
    public Iterator<Programable> iterator() {
        List<Programable> calendario = new ArrayList<>();
        for (Programable evento : getEventos()) {
            if (evento.getFecha() != null) calendario.add(evento);
        }
        Collections.sort(calendario); // Usa Programable.compareTo, sin repetir el criterio.
        return List.copyOf(calendario).iterator();
    }
}
