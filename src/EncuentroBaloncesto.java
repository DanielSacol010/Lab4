public class EncuentroBaloncesto extends Encuentro {
    public EncuentroBaloncesto(Equipo equipo1, Equipo equipo2) {
        super(equipo1, equipo2);
    }

    @Override
    protected void validarReglasDeporte(int marcador1, int marcador2) {
        if (marcador1 == marcador2) {
            throw new IllegalArgumentException("El baloncesto no permite un marcador empatado.");
        }
    }

    @Override
    protected int[] calcularPuntos(int marcador1, int marcador2) {
        return marcador1 > marcador2 ? new int[] {2, 1} : new int[] {1, 2};
    }

    @Override
    public String getDescripcion() {
        return "Baloncesto: " + getEquiposParticipantes().get(0).getNombre()
                + " vs " + getEquiposParticipantes().get(1).getNombre();
    }
}
