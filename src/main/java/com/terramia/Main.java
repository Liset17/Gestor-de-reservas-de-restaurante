package com.terramia;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String opcion;

        do {
            System.out.println();
            System.out.println("=== TerraMía ===");
            System.out.println("1. Reservar");
            System.out.println("2. Mis reservas");
            System.out.println("3. Contacto");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.println("Has elegido Reservar.");
                    break;

                case "2":
                    System.out.println("Has elegido Mis reservas.");
                    break;

                case "3":
                    System.out.println("Has elegido Contacto.");
                    break;

                case "0":
                    System.out.println("Gracias por visitar TerraMía.");
                    break;

                default:
                    System.out.println("Opción no válida. Inténtalo otra vez.");
                    break;
            }

        } while (!opcion.equals("0"));

        scanner.close();
    }
}