package com.terramia;

import com.terramia.model.Cliente;
import com.terramia.model.Reserva;
import com.terramia.repository.MensajeRepository;
import com.terramia.repository.ReservaRepository;
import com.terramia.service.ContactoService;
import com.terramia.service.ReservaException;
import com.terramia.service.ReservaService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // al arrancar el programa, creamos los repositorios y servicios una vez, antes del bucle del menu
        ReservaRepository reservaRepository = new ReservaRepository();
        ReservaService reservaService = new ReservaService(reservaRepository);

        MensajeRepository mensajeRepository = new MensajeRepository();
        ContactoService contactoService = new ContactoService(mensajeRepository);

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

            opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    reservar(scanner, reservaService);
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

    // Recorrido completo para crear una reserva
    private static void reservar(
            Scanner scanner, ReservaService reservaService) {

        try {
            // 1. Recoger los datos del cliente
            Cliente cliente = leerCliente(scanner);

            // 2. Pedir personas y fecha
            int personas = leerEntero(
                    scanner, "Número de personas (1–12): ", 1, 12
            );

            LocalDate fecha = leerFecha(scanner);

            // 3. Consultar horarios donde cabe el grupo
            List<LocalTime> horasDisponibles =
                    reservaService.consultarHorasDisponibles(fecha, personas);

            if (horasDisponibles.isEmpty()) {
                System.out.println(
                        "No hay horarios disponibles para ese grupo y fecha."
                );
                return;
            }

            System.out.println();
            System.out.println("Horarios disponibles:");

            for (int i = 0; i < horasDisponibles.size(); i++) {
                System.out.println(
                        (i + 1) + ". " + horasDisponibles.get(i)
                );
            }

            System.out.println("0. Volver al menú");

            int seleccion = leerEntero(
                    scanner,
                    "Elige el número del horario: ",
                    0,
                    horasDisponibles.size()
            );

            if (seleccion == 0) {
                System.out.println("No se ha creado ninguna reserva.");
                return;
            }

            LocalTime hora = horasDisponibles.get(seleccion - 1);

            // 4. Avisar si tiene otra reserva activa ese día
            if (reservaService.tieneReservaElMismoDia(
                    cliente.getMovil(), fecha)) {

                System.out.println(
                        "Ya tienes una reserva activa para ese día."
                );

                boolean continuar = confirmar(
                        scanner,
                        "¿Quieres continuar creando otra reserva?"
                );

                if (!continuar) {
                    System.out.println(
                            "No se ha creado otra reserva. "
                                    + "Tu reserva anterior sigue igual."
                    );
                    return;
                }
            }

            // 5. Pedir tronas y solicitar permiso si no hay suficientes
            int tronas;

            while (true) {
                tronas = leerEntero(
                        scanner,
                        "Número de tronas (0 si no necesitas): ",
                        0,
                        personas
                );

                int disponibles =
                        reservaService.calcularTronasDisponibles(fecha, hora);

                if (tronas <= disponibles) {
                    break;
                }

                if (disponibles == 0) {
                    System.out.println(
                            "No quedan tronas disponibles para ese horario."
                    );
                } else {
                    System.out.println(
                            "Solo quedan " + disponibles
                                    + " tronas disponibles para ese horario."
                    );
                }

                boolean aceptarCantidad = confirmar(
                        scanner,
                        "¿Quieres continuar con "
                                + disponibles + " tronas?"
                );

                if (aceptarCantidad) {
                    tronas = disponibles;
                    break;
                }

                boolean elegirOtraCantidad = confirmar(
                        scanner,
                        "¿Quieres introducir otra cantidad de tronas?"
                );

                if (!elegirOtraCantidad) {
                    System.out.println("No se ha creado ninguna reserva.");
                    return;
                }
            }

            // 6. Mostrar todos los datos antes de guardar
            System.out.println();
            System.out.println("=== Confirma tu reserva ===");
            System.out.println(
                    "Cliente: " + cliente.getNombre()
                            + " " + cliente.getApellidos()
            );
            System.out.println("Móvil: " + cliente.getMovil());
            System.out.println("Correo: " + cliente.getCorreo());
            System.out.println("Personas: " + personas);
            System.out.println("Fecha: " + fecha);
            System.out.println("Hora: " + hora);
            System.out.println("Tronas: " + tronas);

            if (!confirmar(scanner, "¿Confirmas la reserva?")) {
                System.out.println("No se ha creado ninguna reserva.");
                return;
            }

            // 7. El servicio vuelve a validar antes de guardar
            Reserva reserva = reservaService.crearReserva(
                    cliente, personas, tronas, fecha, hora
            );

            System.out.println(
                    "Reserva creada correctamente. Número de reserva: "
                            + reserva.getId()
            );

        } catch (ReservaException e) {
            System.out.println(
                    "No se ha creado la reserva: " + e.getMessage()
            );
        }
    }

    // Recoger los datos del cliente
    private static Cliente leerCliente(Scanner scanner) {

        String nombre = leerTextoObligatorio(scanner, "Nombre: ");
        String apellidos = leerTextoObligatorio(scanner, "Apellidos: ");
        String movil = leerTextoObligatorio(scanner, "Móvil: ");
        String correo = leerTextoObligatorio(
                scanner, "Correo electrónico: "
        );

        return new Cliente(nombre, apellidos, movil, correo);
    }

    // Repetir la pregunta si el usuario deja el texto vacío
    private static String leerTextoObligatorio(
            Scanner scanner, String pregunta) {

        while (true) {
            System.out.print(pregunta);
            String texto = scanner.nextLine().trim();

            if (!texto.isBlank()) {
                return texto;
            }

            System.out.println("Este dato es obligatorio.");
        }
    }

    // Leer una cantidad entera dentro del intervalo permitido
    private static int leerEntero(
            Scanner scanner, String pregunta, int minimo, int maximo) {

        while (true) {
            System.out.print(pregunta);

            try {
                int numero = Integer.parseInt(
                        scanner.nextLine().trim()
                );

                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }

                System.out.println(
                        "Introduce un número entre "
                                + minimo + " y " + maximo + "."
                );

            } catch (NumberFormatException e) {
                System.out.println(
                        "Introduce un número entero, por ejemplo 4."
                );
            }
        }
    }

    // Leer una fecha válida que no sea anterior a hoy
    private static LocalDate leerFecha(Scanner scanner) {

        while (true) {
            System.out.print(
                    "Fecha (año-mes-día, por ejemplo 2030-06-15): "
            );

            try {
                LocalDate fecha = LocalDate.parse(
                        scanner.nextLine().trim()
                );

                if (!fecha.isBefore(LocalDate.now())) {
                    return fecha;
                }

                System.out.println(
                        "La fecha no puede ser anterior a hoy."
                );

            } catch (DateTimeParseException e) {
                System.out.println(
                        "Introduce una fecha válida con formato año-mes-día."
                );
            }
        }
    }

    // Obtener una respuesta explícita: sí o no
    private static boolean confirmar(
            Scanner scanner, String pregunta) {

        while (true) {
            System.out.print(pregunta + " (s/n): ");
            String respuesta = scanner.nextLine().trim();

            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            }

            if (respuesta.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("Escribe s para sí o n para no.");
        }
    }
}