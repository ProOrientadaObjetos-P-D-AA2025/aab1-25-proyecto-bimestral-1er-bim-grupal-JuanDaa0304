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

            String cedula;
            do {
                cedula = vista.pedirTexto("Ingrese su cedula (10 digitos): ");
                if (!verificarCedula(cedula)) {
                    vista.mostrarMensaje("Cedula invalida. Intente nuevamente.");
                }
            } while (!verificarCedula(cedula));

            CalculadoraImpuestos calc = new CalculadoraImpuestos(nombre, cedula);

            vista.mostrarMensaje("--- Ingreso de sueldos mensuales ---");
            for (int i = 1; i <= 12; i++) {
                boolean valido = false;
                double sueldo = 0;
                while (!valido) {
                    try {
                        sueldo = vista.pedirNumero("Mes " + i + ": $");
                        if (sueldo < 0) {
                            vista.mostrarMensaje("El sueldo no puede ser negativo.");
                        } else {
                            valido = true;
                        }
                    } catch (Exception e) {
                        vista.mostrarMensaje("Error: " + e.getMessage());
                    }
                }
                calc.agregarSueldo(i, sueldo);
            }
            vista.mostrarMensaje("--- Ingreso de facturas ---");
            String[] categoriasValidas = {"Vivienda", "Turismo", "Salud", "Alimentacion", "Educacion", "Vestimenta"};

            vista.mostrarMensaje("Categorias disponibles:");
            int j = 0;
            while (j < categoriasValidas.length) {
                vista.mostrarMensaje("- " + categoriasValidas[j]);
                j++;
            }

            while (true) {
                String categoria = vista.pedirTexto("Ingrese una categoria (o 'fin' para terminar): ");
                if (categoria.equalsIgnoreCase("fin")) {
                    break;
                }

                boolean categoriaEsValida = false;
                int i = 0;
                while (i < categoriasValidas.length) {
                    if (categoria.equalsIgnoreCase(categoriasValidas[i])) {
                        categoriaEsValida = true;
                        categoria = categoriasValidas[i]; 
                        break;
                    }
                    i++;
                }

                if (!categoriaEsValida) {
                    vista.mostrarMensaje("Categoria inválida. Intente de nuevo.");
                    continue;
                }

                double monto = 0;
                boolean montoValido = false;
                while (!montoValido) {
                    try {
                        monto = vista.pedirNumero("Monto: $");
                        if (monto < 0) {
                            vista.mostrarMensaje("El monto no puede ser negativo.");
                        } else {
                            montoValido = true;
                        }
                    } catch (Exception e) {
                        vista.mostrarMensaje("Error al ingresar monto: " + e.getMessage());
                    }
                }

                calc.agregarFactura(categoria, monto);
            }

            vista.mostrarMensaje("--- Resumen de Sueldos ---");
            double[] sueldos = calc.getSueldos();
            int i = 0;
            while (i < sueldos.length) {
                vista.mostrarMensaje("Mes " + (i + 1) + ": $" + sueldos[i]);
                i++;
            }

            vista.mostrarMensaje("--- Resumen de Facturas ---");
            String[] categorias = calc.getCategorias();
            double[] montos = calc.getMontosFacturas();
            i = 0;
            while (i < categorias.length) {
                vista.mostrarMensaje(categorias[i] + ": $" + montos[i]);
                i++;
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
                pw.println("Cédula: " + calc.getCedula());

                pw.println("--- Sueldos Mensuales ---");
                i = 0;
                while (i < sueldos.length) {
                    pw.println("Mes " + (i + 1) + ": $" + sueldos[i]);
                    i++;
                }

                pw.println("--- Facturas por Categoria ---");
                i = 0;
                while (i < categorias.length) {
                    pw.println(categorias[i] + ": $" + montos[i]);
                    i++;
                }

                pw.println("--- Resumen ---");
                pw.println("Ingreso Anual: $" + ingreso);
                pw.println("Deducciones: $" + deduccion);
                pw.println("Base Imponible: $" + (ingreso - deduccion));
                pw.println("Impuesto a pagar: $" + impuesto);

                pw.close();
                vista.mostrarMensaje("Archivo guardado: " + nombreArchivo);
            } catch (IOException e) {
                vista.mostrarMensaje("Error al guardar el archivo: " + e.getMessage());
            }

            continuar = vista.deseaContinuar();
        } while (continuar);

        vista.mostrarMensaje("Gracias por usar el sistema.");
    }

    private boolean verificarCedula(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) {
            return false;
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            return false;
        }

        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito > 6) {
            return false;
        }

        int suma = 0;
        int i = 0;
        while (i < 9) {
            int valor = Character.getNumericValue(cedula.charAt(i));
            if (i % 2 == 0) {
                valor *= 2;
                if (valor > 9) {
                    valor -= 9;
                }
            }
            suma += valor;
            i++;
        }

        int verificador = (10 - (suma % 10)) % 10;
        return verificador == Character.getNumericValue(cedula.charAt(9));
    }
}
