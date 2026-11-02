package com.terramia;

import java.util.Scanner;

import com.terramia.repository.ReservaRepository;
import com.terramia.repository.MensajeRepository;
import com.terramia.service.ReservaService;
import com.terramia.service.ContactoService;

public class Main {

    public static void main(String[] args) {

        // Crear el almacén de reservas y el servicio que aplica sus reglas
        ReservaRepository reservaRepository = new ReservaRepository();
        ReservaService reservaService = new ReservaService(reservaRepository);

        // Crear el almacén de mensajes y el servicio que los valida
        MensajeRepository mensajeRepository = new MensajeRepository();
        ContactoService contactoService = new ContactoService(mensajeRepository);

        // Preparar la lectura del teclado y la variable para la opción elegida
        Scanner scanner = new Scanner(System.in);
        String opcion;


        // Mostrar el menú al menos una vez
        do {
            System.out.println();
            System.out.println("=== TerraMía ===");
            System.out.println("1. Reservar");
            System.out.println("2. Mis reservas");
            System.out.println("3. Contacto");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            opcion = scanner.nextLine();

            // Ejecutar el bloque correspondiente a la opción elegida por el usuario
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

            // Repetir mientras el usuario no elija salir
        } while (!opcion.equals("0"));

        scanner.close();
    }
}