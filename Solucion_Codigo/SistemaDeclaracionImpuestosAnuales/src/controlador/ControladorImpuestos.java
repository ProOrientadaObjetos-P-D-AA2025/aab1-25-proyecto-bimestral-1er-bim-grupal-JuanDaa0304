package controlador;

import modelo.CalculadoraImpuestos;
import vista.VistaConsola;

public class ControladorImpuestos {
    private VistaConsola vista = new VistaConsola();

    public void ejecutar() {
        boolean continuar;
        do {
            vista.mostrarMensaje("=== Declaracion de Impuestos 2023 ===");
            String nombre = vista.pedirTexto("Ingrese su nombre: ");
            String cedula = vista.pedirTexto("Ingrese su cédula: ");

            CalculadoraImpuestos calc = new CalculadoraImpuestos(nombre, cedula);

            vista.mostrarMensaje("--- Ingreso de sueldos mensuales ---");
            for (int i = 1; i <= 12; i++) {
                double sueldo = vista.pedirNumero("Mes " + i + ": $");
                calc.agregarSueldo(i, sueldo);
            }

            vista.mostrarMensaje("--- Ingreso de facturas ---");
            vista.mostrarMensaje("Opciones de categoría: Vivienda, Turismo, Salud, Alimentación, Educación");
            while (true) {
                String categoria = vista.pedirTexto("Categoría (o 'fin' para terminar): ");
                if (categoria.equalsIgnoreCase("fin")) break;
                double monto = vista.pedirNumero("Monto: $");
                calc.agregarFactura(categoria, monto);
            }

            vista.mostrarMensaje("--- Resumen de Sueldos ---");
            double[] sueldos = calc.getSueldos();
            for (int i = 0; i < sueldos.length; i++) {
                vista.mostrarMensaje("Mes " + (i + 1) + ": $" + sueldos[i]);
            }

            vista.mostrarMensaje("--- Resumen de Facturas ---");
            String[] categorias = calc.getCategorias();
            double[] montos = calc.getMontosFacturas();
            for (int i = 0; i < categorias.length; i++) {
                vista.mostrarMensaje(categorias[i] + ": $" + montos[i]);
            }

            double ingreso = calc.calcularIngresoAnual();
            double deduccion = calc.calcularDeducciones();
            double impuesto = calc.calcularImpuesto();

            vista.mostrarMensaje("--- Resultado Final ---");
            vista.mostrarMensaje("Nombre: " + calc.getNombre());
            vista.mostrarMensaje("Cédula: " + calc.getCedula());
            vista.mostrarMensaje("Ingreso Anual: $" + ingreso);
            vista.mostrarMensaje("Deducciones: $" + deduccion);
            vista.mostrarMensaje("Base Imponible: $" + (ingreso - deduccion));
            vista.mostrarMensaje("Impuesto a pagar: $" + impuesto);

            continuar = vista.deseaContinuar();

        } while (continuar);

        vista.mostrarMensaje("Gracias por usar el sistema.");
    }
}
