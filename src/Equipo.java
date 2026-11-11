/** Equipo con identidad inmutable y puntos distintos de las anotaciones. */
public class Equipo implements Comparable<Equipo> {
    private final String codigo;
    private final String nombre;
    private final String empresa;
    private int puntosTabla;

    public Equipo(String codigo, String nombre, String empresa) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del equipo no puede estar vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
        }
        if (empresa == null || empresa.trim().isEmpty()) {
            throw new IllegalArgumentException("La empresa no puede estar vacía.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.empresa = empresa.trim();
        this.puntosTabla = 0;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getEmpresa() { return empresa; }
    public int getPuntosTabla() { return puntosTabla; }

    // Solo las clases del modelo deben asignar puntos de tabla.
    void sumarPuntos(int puntos) {
        if (puntos < 0) throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        puntosTabla = Math.addExact(puntosTabla, puntos);
    }

    @Override
    public int compareTo(Equipo otro) {
        int orden = Integer.compare(otro.puntosTabla, puntosTabla);
        return orden != 0 ? orden : nombre.compareTo(otro.nombre);
    }
}
