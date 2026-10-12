package com.terramia.service;

import com.terramia.model.Cliente;
import com.terramia.model.EstadoReserva;
import com.terramia.model.Reserva;
import com.terramia.repository.ReservaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservaService {

    // 1-. Repositorio para guardar y buscar reservas
    private final ReservaRepository reservaRepository;

    // 2-. Constantes de las reglas del restaurante
    // a-. Aforo máximo de personas por tramo
    private static final int AFORO_MAXIMO = 120;
    // b-. Duración de cada reserva en horas
    private static final int DURACION_HORAS = 2;


    // 3-. Constructor
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }


    // 4-. Regla 0: los datos obligatorios no deben estar vacíos
    private void validarDatosObligatorios(
            Cliente cliente,
            LocalDate fecha,
            LocalTime hora) throws ReservaException {

        // a-. El cliente es obligatorio
        if (cliente == null) {
            throw new ReservaException(
                    "Faltan los datos del cliente."
            );
        }

        // b-. La fecha es obligatoria
        if (fecha == null) {
            throw new ReservaException(
                    "Debes indicar una fecha para la reserva."
            );
        }

        // c-. La hora es obligatoria
        if (hora == null) {
            throw new ReservaException(
                    "Debes indicar una hora para la reserva."
            );
        }
    }

    // 5-. Regla A: entre 1 y 12 personas (con un mensaje para cada caso)
    private void validarPersonas(int personas) throws ReservaException {

        // a-. Mínimo 1 persona
        if (personas < 1) {
            throw new ReservaException(
                    "La reserva debe ser para al menos 1 persona."
            );
        }

        // b-. Máximo 12 personas
        if (personas > 12) {
            throw new ReservaException(
                    "Para grupos de 13 o más de personas, contacte con el restaurante."
            );
        }
    }


    // 6-. Regla B: la fecha y la hora no pueden estar en el pasado
    private void validarFechaHora(LocalDate fecha, LocalTime hora)
            throws ReservaException {

        // a-. Se une fecha y hora
        LocalDateTime fechaHoraReserva = LocalDateTime.of(fecha, hora);


        // b-. Se obtiene el momento actual
        LocalDateTime ahora = LocalDateTime.now();


        // c-. Se comprueba que la reserva sea para un momento futuro
        if (!fechaHoraReserva.isAfter(ahora)) {
            throw new ReservaException(
                    "No puedes reservar en una fecha u hora que ya ha pasado."
            );
        }

    }


    // 7-. Busca una reserva por ID y lanza un error si no existe
    private Reserva buscarReservaExistente(int id)
            throws ReservaException {

        // a-. Se busca la reserva en el repositorio
        Reserva reserva = reservaRepository.buscarPorId(id);

        // b-. Si no existe, se lanza un error
        if (reserva == null) {
            throw new ReservaException("No existe ninguna reserva con el número " + id + ".");
        }

        return reserva;
    }


    // 8-. Regla C: horario de 11:30 a 23:30, cada 30 minutos
    private void validarHora(LocalTime hora) throws ReservaException {

        // a-. Definimos el inicio del horario
        LocalTime apertura = LocalTime.of(11, 30);

        // b-. Definimos el fin del horario
        LocalTime ultimaReserva = LocalTime.of(23, 30);


        // c-. Comprobamos que la hora esté dentro del horario
        if (hora.isBefore(apertura) || hora.isAfter(ultimaReserva)) {
            throw new ReservaException(
                    "Solo puedes reservar entre las 11:30 y las 23:30.");
        }

        // d-. Comprobamos que la hora sea en punto o y media (validación completa y más segura)
        if (hora.getMinute() % 30 != 0
                || hora.getSecond() != 0
                || hora.getNano() != 0) {
            throw new ReservaException(
                    "Las reservas son cada 30 minutos (por ejemplo, 19:00 o 19:30)."
            );
        }

    }


    // 9-. Regla D: aforo máximo de 120 personas en un tramo de 2 horas
    private void validarAforo(
            LocalDate fecha,
            LocalTime hora,
            int personas) throws ReservaException {

        // a-. Tramo de la nueva reserva (inicio y fin)
        LocalDateTime inicioNuevo = LocalDateTime.of(fecha, hora);
        LocalDateTime finNuevo = inicioNuevo.plusHours(DURACION_HORAS);

        // b-. Se suman las personas de las reservas que se solapan
        int ocupadas = 0;

        for (Reserva reserva : reservaRepository.obtenerTodas()) {

            // c-. Solo contamos las reservas activas
            if (reserva.getEstado() != EstadoReserva.ACTIVA) {
                continue;
            }

            // d-. Tramo de la reserva existente (inicio y fin)
            LocalDateTime inicioExistente =
                    LocalDateTime.of(reserva.getFecha(), reserva.getHora());

            LocalDateTime finExistente =
                    inicioExistente.plusHours(DURACION_HORAS);

            // e-. Comprobamos si las reservas se solapan
            boolean seSolapan =
                    inicioNuevo.isBefore(finExistente)
                            && finNuevo.isAfter(inicioExistente);

            if (seSolapan) {
                ocupadas += reserva.getPersonas();
            }
        }

        // f-. Se comprueba si quedan plazas libres suficientes
        int libres = AFORO_MAXIMO - ocupadas;

        if (personas > libres) {
            throw new ReservaException(
                    "No hay sitio suficiente en ese horario. "
                            + "Plazas libres: " + Math.max(0, libres) + "."
            );
        }
    }


    // 10-. Regla E: tronas, máx. 20 en el tramo de 2 h (si no quedan, se puede reservar sin trona)


    // 11-. Regla F: solapamiento, un cliente no puede tener dos reservas a la vez


    // 12-. Regla G: aviso de reserva el mismo día


    // 13-. Regla H: crear / modificar / cancelar


    // 14-. Regla I: finalizada automática al terminar las 2 h de reserva


    // 15-. Regla J: código de verificación (SIMULADO)


    // 16-. Crear una reserva aplicando todas las reglas
    public Reserva crearReserva(Cliente cliente, int personas, int ninosMenores3,
                                LocalDate fecha, LocalTime hora) throws ReservaException {

        // a-. Comprobamos que todas las reglas, en orden
        validarDatosObligatorios(cliente, fecha, hora);
        validarPersonas(personas);
        validarHora(hora);
        validarFechaHora(fecha, hora);
        validarAforo(fecha, hora, personas);

        // b-. Si  todo es correcto creamos la reserva
        Reserva reserva = new Reserva(cliente, personas, ninosMenores3, fecha, hora);

        // c-. La guardamos en el repositorio
        reservaRepository.guardar(reserva);

        // d-. La devolvemos (por ejemplo, para mostrar su número al cliente)
        return reserva;
    }

}

