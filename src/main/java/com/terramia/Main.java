package com.terramia;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.terramia.model.Cliente;
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
                try {
                    Cliente cliente = leerCliente(scanner);

                    System.out.print("Número de personas: ");
                    int personas = Integer.parseInt(scanner.nextLine().trim());

                    System.out.print("Fecha (año-mes-día, por ejemplo 2030-06-15): ");
                    LocalDate fecha = LocalDate.parse(scanner.nextLine().trim());

                    System.out.print("Hora (por ejemplo 19:00): ");
                    LocalTime hora = LocalTime.parse(scanner.nextLine().trim());

                    System.out.print("Número de tronas (0 si no necesitas): ");
                    int tronas = Integer.parseInt(scanner.nextLine().trim());

                    // Mostrar lo recogido; todavía no se guarda una reserva
                    System.out.println();
                    System.out.println("=== Datos de la solicitud ===");
                    System.out.println("Cliente: " + cliente.getNombre());
                    System.out.println("Personas: " + personas);
                    System.out.println("Fecha: " + fecha);
                    System.out.println("Hora: " + hora);
                    System.out.println("Tronas: " + tronas);
                    System.out.println("La reserva todavía no se ha guardado.");

                } catch (NumberFormatException | DateTimeParseException e) {
                    System.out.println(
                            "No se pudieron leer los datos. "
                                    + "Introduce cantidades enteras, fecha año-mes-día "
                                    + "y hora como 19:00."
                    );
                }
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

    // Recoge los datos del cliente para preparar su reserva.
    private static Cliente leerCliente(Scanner scanner) {

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellidos: ");
        String apellidos = scanner.nextLine();

        System.out.print("Móvil: ");
        String movil = scanner.nextLine();

        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();

        return new Cliente(nombre, apellidos, movil, correo);
    }
}