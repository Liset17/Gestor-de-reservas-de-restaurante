package com.terramia.service;

import com.terramia.repository.ReservaRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.terramia.model.Cliente;
import com.terramia.model.Reserva;
import com.terramia.model.EstadoReserva;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservaServiceTest {


    //Prueba con el restaurante vacio. No guardamos ninguna reserva en el repositorio.
    //Consultamos las tronas disponibles y esperamos que estén libres las 20
    @Test
    void sinReservasHayVeinteTronasDisponibles() {

        // 1. Preparar: repositorio vacío y servicio que lo utiliza
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.of(2027, 6, 15);
        LocalTime hora = LocalTime.of(19, 0);

        // 2. Actuar: consultar las tronas disponibles
        int disponibles =
                servicio.calcularTronasDisponibles(fecha, hora);

        // 3. Comprobar: esperamos que estén libres las 20
        assertEquals(20, disponibles);
    }


    //Se hace una reserva con 3 tronas
    @Test
    void reservaActivaConTresTronasDejaDiecisieteDisponibles() {

        // 1. Preparar un repositorio vacío y su servicio
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.of(2027, 6, 15);
        LocalTime hora = LocalTime.of(19, 0);

        // Crear un cliente con datos ficticios
        Cliente cliente = new Cliente(
                "Juana",
                "Benítez",
                "600000001",
                "juana.beni@example.com"
        );

        // La reserva empieza ACTIVA y dura de 19:00 a 21:00
        Reserva reserva = new Reserva(cliente, 4, fecha, hora);
        reserva.setTronas(3);

        // Guarda para que el servicio pueda encontrarlo
        repositorio.guardar(reserva);

        // 2. Consultar las tronas disponibles para ese horario
        int disponibles =
                servicio.calcularTronasDisponibles(fecha, hora);

        // 3. Comprobar el resultado: 20 - 3 = 17
        assertEquals(17, disponibles);
    }


    //Comprobar que las tronas se liberan justo cuando termina una reserva.
    @Test
    void lasTronasSeLiberanJustoAlTerminarLaReserva() {

        // 1. Preparar
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.of(2027, 6, 15);

        Cliente cliente = new Cliente(
                "Juana",
                "Benítez",
                "600000001",
                "juana.beni@example.com"
        );

        // Empieza a las 19:00 y termina a las 21:00
        Reserva reserva = new Reserva(
                cliente, 4, fecha, LocalTime.of(19, 0)
        );
        reserva.setTronas(3);
        repositorio.guardar(reserva);

        // 2. Consultar justo cuando termina la reserva anterior
        int disponibles = servicio.calcularTronasDisponibles(
                fecha, LocalTime.of(21, 0)
        );

        // 3. Comprobar que las 20 tronas están disponibles
        assertEquals(20, disponibles);
    }


    //Ahora verificaremos que una reserva cancelada no ocupa tronas, aunque su horario coincida con
    // la nueva consulta.
    @Test
    void unaReservaCanceladaNoOcupaTronas() {

        // 1. Preparar
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.of(2027, 6, 15);
        LocalTime hora = LocalTime.of(19, 0);

        Cliente cliente = new Cliente(
                "Juana",
                "Benítez",
                "600000001",
                "juana@example.com"
        );

        Reserva reserva = new Reserva(cliente, 4, fecha, hora);
        reserva.setTronas(3);
        reserva.setEstado(EstadoReserva.CANCELADA);

        repositorio.guardar(reserva);

        // 2. Consultar el mismo horario
        int disponibles =
                servicio.calcularTronasDisponibles(fecha, hora);

        // 3. La reserva cancelada no debe consumir sus 3 tronas
        assertEquals(20, disponibles);
    }

    //Confirmamos que el sistema reutiliza las tronas de grupos que están en momentos distintos, sin sumarlos como si coincidieran.
    // Osea, dos grupos ocupan tronas en momentos distintos y el sistema debe permitir reutilizarlas.
    @Test
    void gruposEnMomentosDistintosPermitenReutilizarTronas() {

        // 1. Preparar
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.of(2027, 6, 15);

        Cliente clienteA = new Cliente(
                "Ana", "García", "600000001", "ana@example.com"
        );

        Cliente clienteB = new Cliente(
                "Luis", "Pérez", "600000002", "luis@example.com"
        );

        // Grupo A: ocupa 10 tronas hasta las 19:30
        Reserva reservaA = new Reserva(
                clienteA, 10, fecha, LocalTime.of(17, 30)
        );
        reservaA.setTronas(10);
        repositorio.guardar(reservaA);

        // Grupo B: ocupa 10 tronas desde las 20:30
        Reserva reservaB = new Reserva(
                clienteB, 10, fecha, LocalTime.of(20, 30)
        );
        reservaB.setTronas(10);
        repositorio.guardar(reservaB);

        // 2. Consultar disponibilidad de 19:00 a 21:00
        int disponibles = servicio.calcularTronasDisponibles(
                fecha, LocalTime.of(19, 0)
        );

        // 3. Quedan 10 disponibles durante toda la estancia
        assertEquals(10, disponibles);
    }

    //Prueba de nombre obligatoria, queremos comprobar que, si el nombre está vacío,
    // crearReserva() lanza una ReservaException y no guarda la reserva
    @Test
    void noSePuedeCrearUnaReservaSinNombre() {

        // 1. Preparar
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        Cliente cliente = new Cliente(
                "",
                "Benítez",
                "600000001",
                "juana@example.com"
        );

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(19, 0);

        // 2. Intentar crearla y comprobar que lanza la excepción
        assertThrows(ReservaException.class, () -> {
            servicio.crearReserva(cliente, 4, 0, fecha, hora);
        });

        // 3. Comprobar que no se ha guardado ninguna reserva
        assertEquals(0, repositorio.obtenerTodas().size());
    }


    //Apellidos obligatorios
    @Test
    void noSePuedeCrearUnaReservaSinApellidos() {

        // 1. Preparar: solo faltan los apellidos
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        Cliente cliente = new Cliente(
                "Juana",
                "",
                "600000001",
                "juana@example.com"
        );

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(19, 0);

        // 2. Comprobar que se rechaza la reserva
        assertThrows(ReservaException.class, () -> {
            servicio.crearReserva(cliente, 4, 0, fecha, hora);
        });

        // 3. Comprobar que no se guardó
        assertEquals(0, repositorio.obtenerTodas().size());
    }


    //Correo electronico obligatorio

    @Test
    void noSePuedeCrearUnaReservaSinCorreo() {

        // 1. Preparar: solo falta el correo
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        Cliente cliente = new Cliente(
                "Juana",
                "Benítez",
                "600000001",
                ""
        );

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(19, 0);

        // 2. Comprobar que se rechaza la reserva
        assertThrows(ReservaException.class, () -> {
            servicio.crearReserva(cliente, 4, 0, fecha, hora);
        });

        // 3. Comprobar que no se guardó
        assertEquals(0, repositorio.obtenerTodas().size());
    }



    //Móvil obligatorio
@Test
void noSePuedeCrearUnaReservaSinMovil() {

    // 1. Preparar: solo falta el móvil
    ReservaRepository repositorio = new ReservaRepository();
    ReservaService servicio = new ReservaService(repositorio);

    Cliente cliente = new Cliente(
            "Juana",
            "Benítez",
            "",
            "juana@example.com"
    );

    LocalDate fecha = LocalDate.now().plusDays(1);
    LocalTime hora = LocalTime.of(19, 0);

    // 2. Comprobar que se rechaza la reserva
    assertThrows(ReservaException.class, () -> {
        servicio.crearReserva(cliente, 4, 0, fecha, hora);
    });

    // 3. Comprobar que no se guardó
    assertEquals(0, repositorio.obtenerTodas().size());
}




//Comprobar que llegar exactamente a 120 personas está permitido
    @Test
    void sePuedeCrearUnaReservaQueCompleteElAforoDe120Personas()
            throws ReservaException {

        // 1. Preparar el repositorio y el servicio
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(19, 0);

        // Guardar 9 reservas de 12 personas: 108 en total
        for (int i = 1; i <= 9; i++) {

            Cliente cliente = new Cliente(
                    "Cliente",
                    "Prueba",
                    "60000000" + i,
                    "cliente" + i + "@example.com"
            );

            Reserva reserva = new Reserva(cliente, 12, fecha, hora);
            repositorio.guardar(reserva);
        }

        Cliente nuevoCliente = new Cliente(
                "Juana",
                "Benítez",
                "600000010",
                "juana@example.com"
        );

        // 2. Crear otra reserva de 12: llegamos a 120 personas
        Reserva nuevaReserva =
                servicio.crearReserva(nuevoCliente, 12, 0, fecha, hora);

        // 3. Comprobar que se creó y se guardó
        assertEquals(12, nuevaReserva.getPersonas());
        assertEquals(10, repositorio.obtenerTodas().size());
    }


    @Test
    void noSePuedeCrearUnaReservaQueSupereElAforoDe120Personas() {

        // 1. Preparar: 10 reservas de 12 personas = 120
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(19, 0);

        for (int i = 1; i <= 10; i++) {

            Cliente cliente = new Cliente(
                    "Cliente",
                    "Prueba",
                    "60000000" + i,
                    "cliente" + i + "@example.com"
            );

            Reserva reserva = new Reserva(cliente, 12, fecha, hora);
            repositorio.guardar(reserva);
        }

        Cliente nuevoCliente = new Cliente(
                "Juana",
                "Benítez",
                "600000099",
                "juana@example.com"
        );

        // 2. Intentar añadir una persona: serían 121
        assertThrows(ReservaException.class, () -> {
            servicio.crearReserva(nuevoCliente, 1, 0, fecha, hora);
        });

        // 3. Comprobar que siguen guardadas solo las 10 iniciales
        assertEquals(10, repositorio.obtenerTodas().size());
    }




//Compruebo  que el servicio revisa también lo que ocurrirá más tarde durante la estancia.
    @Test
    void noSePuedeReservarSiElAforoSeSuperaMasTarde() {

        // 1. Preparar
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.now().plusDays(1);

        // 9 reservas de 12 personas desde las 20:00: total 108
        for (int i = 1; i <= 9; i++) {

            Cliente cliente = new Cliente(
                    "Cliente",
                    "Prueba",
                    "60000000" + i,
                    "cliente" + i + "@example.com"
            );

            Reserva reserva = new Reserva(
                    cliente, 12, fecha, LocalTime.of(20, 0)
            );
            repositorio.guardar(reserva);
        }

        // Otras 12 personas desde las 20:30: total 120
        Cliente otroCliente = new Cliente(
                "Luis", "Pérez", "600000050", "luis@example.com"
        );

        Reserva otraReserva = new Reserva(
                otroCliente, 12, fecha, LocalTime.of(20, 30)
        );
        repositorio.guardar(otraReserva);

        Cliente nuevoCliente = new Cliente(
                "Juana", "Benítez", "600000099", "juana@example.com"
        );

        // 2. Intentar reservar de 19:00 a 21:00
        assertThrows(ReservaException.class, () -> {
            servicio.crearReserva(
                    nuevoCliente, 1, 0, fecha, LocalTime.of(19, 0)
            );
        });

        // 3. La nueva reserva no debe guardarse
        assertEquals(10, repositorio.obtenerTodas().size());
    }



//Probaremos que cancelarReserva() cambia el estado y libera las tronas.
    @Test
    void cancelarUnaReservaCambiaSuEstadoYLiberaSusTronas()
            throws ReservaException {

        // 1. Preparar una reserva futura con 3 tronas
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(19, 0);

        Cliente cliente = new Cliente(
                "Juana",
                "Benítez",
                "600000001",
                "juana@example.com"
        );

        Reserva reserva = servicio.crearReserva(
                cliente, 4, 3, fecha, hora
        );

        // Antes de cancelar, sus 3 tronas están ocupadas
        assertEquals(
                17, servicio.calcularTronasDisponibles(fecha, hora)
        );

        // 2. Cancelar indicando la reserva y el móvil de su cliente
        servicio.cancelarReserva(
                reserva.getId(), cliente.getMovil()
        );

        // 3. Comprobar el resultado
        Reserva reservaGuardada =
                repositorio.buscarPorId(reserva.getId());

        assertEquals(EstadoReserva.CANCELADA, reservaGuardada.getEstado());
        assertEquals(20, servicio.calcularTronasDisponibles(fecha, hora));
        assertEquals(1, repositorio.obtenerTodas().size());
    }



//Comprobamos que otro cliente no pueda cancelarla
    @Test
    void noSePuedeCancelarUnaReservaDeOtroCliente()
            throws ReservaException {

        // 1. Preparar una reserva de Juana
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(19, 0);

        Cliente cliente = new Cliente(
                "Juana",
                "Benítez",
                "600000001",
                "juana@example.com"
        );

        Reserva reserva = servicio.crearReserva(
                cliente, 4, 3, fecha, hora
        );

        // 2. Intentar cancelarla con un móvil distinto
        assertThrows(ReservaException.class, () -> {
            servicio.cancelarReserva(
                    reserva.getId(), "600000099"
            );
        });

        // 3. Comprobar que sigue activa y mantiene sus tronas ocupadas
        assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());
        assertEquals(
                17, servicio.calcularTronasDisponibles(fecha, hora)
        );
    }



    //Comprobamos que no se pueda cancelar una reserva que ya empezo
    @Test
    void noSePuedeCancelarUnaReservaQueYaEmpezo() {

        // 1. Preparar una reserva de ayer
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        Cliente cliente = new Cliente(
                "Juana",
                "Benítez",
                "600000001",
                "juana@example.com"
        );

        Reserva reserva = new Reserva(
                cliente,
                4,
                LocalDate.now().minusDays(1),
                LocalTime.of(19, 0)
        );

        repositorio.guardar(reserva);

        // 2. Intentar cancelarla con el movil correcto
        assertThrows(ReservaException.class, () -> {
            servicio.cancelarReserva(
                    reserva.getId(), cliente.getMovil()
            );
        });

        // 3. Comprobar que no se cambio su estado
        assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());
    }



//Comprobamos que una reserva cancelada no se pueda cancelar otra vez
    @Test
    void noSePuedeCancelarDosVecesLaMismaReserva()
            throws ReservaException {

        // 1. Preparar una reserva futura
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        Cliente cliente = new Cliente(
                "Juana",
                "Benítez",
                "600000001",
                "juana@example.com"
        );

        Reserva reserva = servicio.crearReserva(
                cliente,
                4,
                3,
                LocalDate.now().plusDays(1),
                LocalTime.of(19, 0)
        );

        // La primera cancelación debe funcionar
        servicio.cancelarReserva(
                reserva.getId(), cliente.getMovil()
        );

        // 2. La segunda debe rechazarse
        assertThrows(ReservaException.class, () -> {
            servicio.cancelarReserva(
                    reserva.getId(), cliente.getMovil()
            );
        });

        // 3. La reserva sigue cancelada y no se ha duplicado
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertEquals(1, repositorio.obtenerTodas().size());
    }



    //Intentar cancelar una reserva que no existe.

    @Test
    void noSePuedeCancelarUnaReservaQueNoExiste() {

        // 1. Preparar un repositorio vacío
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        // 2. Intentar cancelar un ID que no está guardado
        ReservaException error = assertThrows(
                ReservaException.class,
                () -> servicio.cancelarReserva(999, "600000001")
        );

        // 3. Comprobar el motivo del rechazo
        assertEquals(
                "No existe ninguna reserva con el número 999.",
                error.getMessage()
        );

        assertEquals(0, repositorio.obtenerTodas().size());
    }



    // Comprueba que la reserva cambia de 4 personas y 1 trona a
// 6 personas y 2 tronas, sigue activa y no se duplica.
    @Test
    void sePuedeModificarUnaReservaSinDuplicarla() throws ReservaException {

        // 1. Preparar la reserva original
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        Cliente cliente = new Cliente(
                "María",
                "García",
                "600000001",
                "maria@example.com"
        );

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(19, 0);

        Reserva reserva = servicio.crearReserva(
                cliente, 4, 1, fecha, hora
        );

        int idOriginal = reserva.getId();

        // 2. Modificar la misma reserva
        servicio.modificarReserva(
                idOriginal, cliente.getMovil(), 6, 2, fecha, hora
        );

        // 3. Consultar lo que quedó guardado
        Reserva guardada = repositorio.buscarPorId(idOriginal);

        assertEquals(6, guardada.getPersonas());
        assertEquals(2, guardada.getTronas());
        assertEquals(EstadoReserva.ACTIVA, guardada.getEstado());
        assertEquals(1, repositorio.obtenerTodas().size());
        assertEquals(18, servicio.calcularTronasDisponibles(fecha, hora));
    }



    // Comprueba si el cambio supera el aforo de 120 personas,
// se rechaza y la reserva conserva todos sus datos originales.
    @Test
    void modificarSinAforoConservaLaReservaOriginal()
            throws ReservaException {

        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(19, 0);

        // Otros clientes ocupan 114 plazas: 9 × 12 + 6
        for (int i = 1; i <= 10; i++) {
            Cliente cliente = new Cliente(
                    "Cliente", "Prueba",
                    "60000000" + i,
                    "cliente" + i + "@example.com"
            );

            int personas = (i <= 9) ? 12 : 6;

            repositorio.guardar(
                    new Reserva(cliente, personas, fecha, hora)
            );
        }

        Cliente maria = new Cliente(
                "María", "García",
                "600000099", "maria@example.com"
        );

        // Total ocupado: 114 + 4 = 118
        Reserva reserva = servicio.crearReserva(
                maria, 4, 1, fecha, hora
        );

        // Pasar a 7 supondría 121 personas: debe rechazarse
        assertThrows(ReservaException.class, () -> {
            servicio.modificarReserva(
                    reserva.getId(), maria.getMovil(),
                    7, 2, fecha, hora
            );
        });

        // Conserva todos sus datos originales
        Reserva guardada = repositorio.buscarPorId(reserva.getId());

        assertEquals(4, guardada.getPersonas());
        assertEquals(1, guardada.getTronas());
        assertEquals(fecha, guardada.getFecha());
        assertEquals(hora, guardada.getHora());
        assertEquals(EstadoReserva.ACTIVA, guardada.getEstado());
        assertEquals(11, repositorio.obtenerTodas().size());
    }
}