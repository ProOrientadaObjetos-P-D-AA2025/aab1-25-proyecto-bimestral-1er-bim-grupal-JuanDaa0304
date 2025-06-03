package modelo;

public class CalculadoraImpuestos {
    private String nombre;
    private String cedula;
    private double[] sueldos = new double[12];
    private String[] categorias = {"Vivienda", "Educacion", "Alimentacion", "Vestimenta", "Salud", "Turismo"};
    private double[] montosFacturas = new double[categorias.length];

    private static final double[] DEDUCCION_MAX_CATEGORIA = {3697.0, 3697.0, 3697.0, 3697.0, 15616.0, 3697.0};
    private static final double DEDUCCION_MAX_TOTAL = 15294.0;

    public CalculadoraImpuestos(String nombre, String cedula) {
        this.nombre = nombre;
        this.cedula = cedula;
    }

    public void agregarSueldo(int mes, double monto) {
        if (mes >= 1 && mes <= 12 && monto >= 0) {
            sueldos[mes - 1] = monto;
        }
    }

    public void agregarFactura(String categoria, double monto) {
        int index = buscarCategoria(categoria);
        if (index != -1 && monto >= 0) {
            montosFacturas[index] += monto;
        }
    }

    private int buscarCategoria(String categoria) {
        for (int i = 0; i < categorias.length; i++) {
            if (categorias[i].equalsIgnoreCase(categoria)) return i;
        }
        return -1;
    }

    public double calcularIngresoAnual() {
        double total = 0;
        for (int i = 0; i < sueldos.length; i++) {
            total += sueldos[i];
        }
        return total;
    }

    public double calcularDeducciones() {
        double total = 0;
        for (int i = 0; i < categorias.length; i++) {
            total += Math.min(montosFacturas[i], DEDUCCION_MAX_CATEGORIA[i]);
        }
        return Math.min(total, DEDUCCION_MAX_TOTAL);
    }

    public double calcularImpuesto() {
        double base = calcularIngresoAnual() - calcularDeducciones();
        double[][] tabla = {
            {0, 11722, 0, 0},
            {11722.01, 14935, 0, 0.05},
            {14935.01, 18666, 160.65, 0.10},
            {18666.01, 22418, 606.39, 0.12},
            {22418.01, 32783, 1142.63, 0.15},
            {32783.01, 43147, 2872.88, 0.20},
            {43147.01, 56095, 5645.76, 0.25},
            {56095.01, 75833, 9703.66, 0.30},
            {75833.01, Double.MAX_VALUE, 15798.76, 0.37}
        };
        for (int i = 0; i < tabla.length; i++) {
            if (base >= tabla[i][0] && base <= tabla[i][1]) {
                return tabla[i][2] + (base - tabla[i][0]) * tabla[i][3];
            }
        }
        return 0.0;
    }

    public String[] getCategorias() {
        return categorias;
    }

    public double[] getMontosFacturas() {
        return montosFacturas;
    }

    public double[] getSueldos() {
        return sueldos;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }
}
