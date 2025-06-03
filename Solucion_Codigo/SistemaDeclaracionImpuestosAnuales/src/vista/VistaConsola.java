package vista;

import java.util.Scanner;

public class VistaConsola {
    private Scanner tcl = new Scanner(System.in);

    public String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return tcl.nextLine();
    }

    public double pedirNumero(String mensaje) {
        double num;
        while (true) {
            System.out.print(mensaje);
            try {
                num = Double.parseDouble(tcl.nextLine());
                if (num >= 0) break;
                else System.out.println("El número no puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }
        return num;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public boolean deseaContinuar() {
        System.out.print("¿Desea hacer otra declaración? (si/no): ");
        return tcl.nextLine().equalsIgnoreCase("si");
    }
}
