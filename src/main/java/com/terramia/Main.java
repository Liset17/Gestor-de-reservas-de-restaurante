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
                    misReservas(scanner, reservaService);
                    break;

                case "3":
                    contacto(scanner, contactoService);
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

    // Verifica el acceso y gestionar las reservas de ese móvil. después de verificar el código permite gestionar
    // únicamente las reservas del móvil introducido. Al modificar, recoge una propuesta sin cambiar la original
    // al confirmar, el servicio valida y actualiza. Al cancelar, también pide confirmación.
    private static void misReservas(
            Scanner scanner, ReservaService reservaService) {

        try {
            String movil = leerTextoObligatorio(scanner, "Móvil: ");
            String codigo = reservaService.generarCodigoVerificacion(movil);

            // Solo para esta versión: simular la recepción del código
            System.out.println("SIMULACIÓN. Tu código es: " + codigo);

            String codigoIntroducido = leerTextoObligatorio(
                    scanner, "Introduce el código de verificación: "
            );

            if (!reservaService.verificarCodigo(movil, codigoIntroducido)) {
                System.out.println(
                        "Código incorrecto. No se ha permitido el acceso."
                );
                return;
            }

            // Permitir varias operaciones durante este acceso verificado
            while (true) {
                reservaService.actualizarReservasFinalizadas();

                List<Reserva> reservas =
                        reservaService.consultarReservas(movil);

                if (reservas.isEmpty()) {
                    System.out.println("No tienes reservas guardadas.");
                    return;
                }

                System.out.println();
                System.out.println("=== Mis reservas ===");

                for (Reserva reserva : reservas) {
                    System.out.println(
                            "ID: " + reserva.getId()
                                    + " | Fecha: " + reserva.getFecha()
                                    + " | Hora: " + reserva.getHora()
                                    + " | Personas: " + reserva.getPersonas()
                                    + " | Tronas: " + reserva.getTronas()
                                    + " | Estado: " + reserva.getEstado()
                    );
                }

                int id = leerEntero(
                        scanner,
                        "ID de la reserva que quieres gestionar "
                                + "(0 para volver al menú): ",
                        0,
                        Integer.MAX_VALUE
                );

                if (id == 0) {
                    return;
                }

                // Buscar únicamente entre las reservas de este cliente
                Reserva seleccionada = null;

                for (Reserva reserva : reservas) {
                    if (reserva.getId() == id) {
                        seleccionada = reserva;
                        break;
                    }
                }

                if (seleccionada == null) {
                    System.out.println(
                            "Ese ID no pertenece a tus reservas."
                    );
                    continue;
                }

                System.out.println("1. Modificar");
                System.out.println("2. Cancelar");
                System.out.println("0. Volver a la lista");

                int accion = leerEntero(
                        scanner, "Elige una opción: ", 0, 2
                );

                if (accion == 1) {
                    modificarDesdeConsola(
                            scanner, reservaService, seleccionada, movil
                    );
                } else if (accion == 2) {
                    cancelarDesdeConsola(
                            scanner, reservaService, seleccionada, movil
                    );
                }
            }

        } catch (ReservaException e) {
            System.out.println(
                    "No se ha podido completar la operación: "
                            + e.getMessage()
            );
        }
    }


    // Preparar una modificación sin tocar todavía la reserva original
    private static void modificarDesdeConsola(
            Scanner scanner,
            ReservaService reservaService,
            Reserva reserva,
            String movil) throws ReservaException {

        System.out.println();
        System.out.println("=== Modificar reserva " + reserva.getId() + " ===");
        System.out.println(
                "Actual: " + reserva.getFecha()
                        + " a las " + reserva.getHora()
                        + ", " + reserva.getPersonas() + " personas"
                        + " y " + reserva.getTronas() + " tronas."
        );

        int personas = leerEntero(
                scanner, "Número de personas que quieres reservar (1–12): ",
                1, 12
        );

        LocalDate fecha = leerFecha(scanner);

        // Excluir esta reserva para no contarla dos veces
        List<LocalTime> horasDisponibles =
                reservaService.consultarHorasDisponibles(
                        fecha, personas, reserva.getId()
                );

        if (horasDisponibles.isEmpty()) {
            System.out.println(
                    "No hay horarios disponibles. "
                            + "Tu reserva original se mantiene."
            );
            return;
        }

        System.out.println("Horarios disponibles:");

        for (int i = 0; i < horasDisponibles.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + horasDisponibles.get(i)
            );
        }

        System.out.println("0. Abandonar la modificación");

        int seleccion = leerEntero(
                scanner,
                "Elige el número del horario: ",
                0,
                horasDisponibles.size()
        );

        if (seleccion == 0) {
            System.out.println("Tu reserva original se mantiene.");
            return;
        }

        LocalTime hora = horasDisponibles.get(seleccion - 1);
        int tronas;

        while (true) {
            tronas = leerEntero(
                    scanner,
                    "Número de tronas (0 si no necesitas): ",
                    0,
                    personas
            );

            int disponibles =
                    reservaService.calcularTronasDisponibles(
                            fecha, hora, reserva.getId()
                    );

            if (tronas <= disponibles) {
                break;
            }

            System.out.println(
                    "Solo quedan " + disponibles
                            + " tronas disponibles para ese horario."
            );

            if (confirmar(
                    scanner,
                    "¿Quieres continuar con " + disponibles + " tronas?")) {

                tronas = disponibles;
                break;
            }

            if (!confirmar(
                    scanner, "¿Quieres introducir otra cantidad de tronas?")) {

                System.out.println("Tu reserva original se mantiene.");
                return;
            }
        }

        System.out.println();
        System.out.println("=== Propuesta de modificación ===");
        System.out.println("Fecha: " + fecha);
        System.out.println("Hora: " + hora);
        System.out.println("Personas: " + personas);
        System.out.println("Tronas: " + tronas);

        if (!confirmar(scanner, "¿Confirmas estos cambios?")) {
            System.out.println("Tu reserva original se mantiene.");
            return;
        }

        // El servicio valida todo antes de cambiar los datos originales
        reservaService.modificarReserva(
                reserva.getId(), movil, personas, tronas, fecha, hora
        );

        System.out.println("Reserva modificada correctamente.");
    }


    // Pedir la confirmación antes de cancelar
    private static void cancelarDesdeConsola(
            Scanner scanner,
            ReservaService reservaService,
            Reserva reserva,
            String movil) throws ReservaException {

        boolean aceptar = confirmar(
                scanner,
                "¿Seguro que quieres cancelar la reserva "
                        + reserva.getId()
                        + " del " + reserva.getFecha()
                        + " a las " + reserva.getHora() + "?"
        );

        if (!aceptar) {
            System.out.println("Tu reserva se mantiene.");
            return;
        }

        reservaService.cancelarReserva(reserva.getId(), movil);

        System.out.println("Reserva cancelada correctamente.");
    }



    // Recoger y confirmar un mensaje antes de guardarlo
    private static void contacto(
            Scanner scanner, ContactoService contactoService) {

        System.out.println();
        System.out.println("=== Contacto ===");

        String nombre = leerTextoObligatorio(scanner, "Nombre: ");
        String correo = leerTextoObligatorio(
                scanner, "Correo electrónico: "
        );
        String mensaje = leerTextoObligatorio(scanner, "Mensaje: ");

        System.out.println();
        System.out.println("=== Confirma tu mensaje ===");
        System.out.println("Nombre: " + nombre);
        System.out.println("Correo: " + correo);
        System.out.println("Mensaje: " + mensaje);

        if (!confirmar(scanner, "¿Quieres guardar este mensaje?")) {
            System.out.println("No se ha guardado ningún mensaje.");
            return;
        }

        try {
            contactoService.guardarMensaje(nombre, correo, mensaje);

            System.out.println(
                    "Tu mensaje se ha guardado correctamente."
            );

        } catch (ReservaException e) {
            System.out.println(
                    "No se ha guardado el mensaje: " + e.getMessage()
            );
        }
    }
}