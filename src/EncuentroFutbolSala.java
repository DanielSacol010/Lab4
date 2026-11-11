public class EncuentroFutbolSala extends Encuentro {
    public EncuentroFutbolSala(Equipo equipo1, Equipo equipo2) {
        super(equipo1, equipo2);
    }

    @Override
    protected void validarReglasDeporte(int marcador1, int marcador2) {
        // La validación común basta: en fútbol sala se aceptan empates.
    }

    @Override
    protected int[] calcularPuntos(int marcador1, int marcador2) {
        if (marcador1 == marcador2) return new int[] {1, 1};
        return marcador1 > marcador2 ? new int[] {3, 0} : new int[] {0, 3};
    }

    @Override
    public String getDescripcion() {
        return "Fútbol sala: " + getEquiposParticipantes().get(0).getNombre()
                + " vs " + getEquiposParticipantes().get(1).getNombre();
    }
}
