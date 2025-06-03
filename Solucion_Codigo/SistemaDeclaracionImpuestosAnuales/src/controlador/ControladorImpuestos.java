package controlador;

import modelo.CalculadoraImpuestos;
import vista.VistaConsola;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

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
            vista.mostrarMensaje("Opciones de categoria: Vivienda, Turismo, Salud, Alimentacion, Educacion, Vestimenta");
            while (true) {
                String categoria = vista.pedirTexto("Categoria (o 'fin' para terminar): ");
                if (categoria.equalsIgnoreCase("fin"))
                    break;
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
            vista.mostrarMensaje("Cedula: " + calc.getCedula());
            vista.mostrarMensaje("Ingreso Anual: $" + ingreso);
            vista.mostrarMensaje("Deducciones: $" + deduccion);
            vista.mostrarMensaje("Base Imponible: $" + (ingreso - deduccion));
            vista.mostrarMensaje("Impuesto a pagar: $" + impuesto);

            try {
                String nombreArchivo = "declaracion_" + calc.getCedula() + ".txt";
                FileWriter fw = new FileWriter(nombreArchivo);
                PrintWriter pw = new PrintWriter(fw);

                pw.println("=== Declaracion de Impuestos 2023 ===");
                pw.println("Nombre: " + calc.getNombre());
                pw.println("Cedula: " + calc.getCedula());

                pw.println("--- Sueldos Mensuales ---");
                for (int i = 0; i < sueldos.length; i++) {
                    pw.println("Mes " + (i + 1) + ": $" + sueldos[i]);
                }

                pw.println("--- Facturas por Categoría ---");
                for (int i = 0; i < categorias.length; i++) {
                    pw.println(categorias[i] + ": $" + montos[i]);
                }

                pw.println("--- Resumen ---");
                pw.println("Ingreso Anual: $" + ingreso);
                pw.println("Deducciones: $" + deduccion);
                pw.println("Base Imponible: $" + (ingreso - deduccion));
                pw.println("Impuesto a pagar: $" + impuesto);

                pw.close();
                vista.mostrarMensaje("Resultados guardados en el archivo: " + nombreArchivo);
            } catch (IOException e) {
                vista.mostrarMensaje("Error al guardar el archivo: " + e.getMessage());
            }

            continuar = vista.deseaContinuar();

        } while (continuar);

        vista.mostrarMensaje("Gracias por usar el sistema.");
    }
}
