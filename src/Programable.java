import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/** Contrato común del calendario; concentra su único criterio de orden. */
public interface Programable extends Comparable<Programable> {
    void programar(LocalDate fecha, int hora, String sede);
    LocalDate getFecha();
    int getHora();
    String getSede();
    String getDescripcion();
    List<Equipo> getEquiposParticipantes();

    /** Se comparan únicamente eventos que ya tienen programación. */
    @Override
    default int compareTo(Programable otro) {
        Objects.requireNonNull(otro, "El evento no puede ser nulo.");
        if (getFecha() == null || otro.getFecha() == null) {
            throw new IllegalStateException("Solo se comparan eventos programados.");
        }
        int orden = getFecha().compareTo(otro.getFecha());
        if (orden == 0) orden = Integer.compare(getHora(), otro.getHora());
        if (orden == 0) orden = getSede().compareTo(otro.getSede());
        return orden;
    }
}
