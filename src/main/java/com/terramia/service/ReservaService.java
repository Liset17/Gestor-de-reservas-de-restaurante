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
    // c-. Tronas maximo por tramo
    private static final int TRONAS_MAXIMAS = 20;


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


    // 10-. Regla E: tronas, máx. 20 en el tramo de 2 h (si no quedan, se puede reservar sin trona).
    // 10.1-. Calcula las tronas libres en el tramo de 2 h
    public int calcularTronasDisponibles(LocalDate fecha, LocalTime hora) {

        // a-. Tramo de la nueva reserva
        LocalDateTime inicioNuevo = LocalDateTime.of(fecha, hora);
        LocalDateTime finNuevo = inicioNuevo.plusHours(DURACION_HORAS);

        // b-. Acumulador de tronas ya ocupadas
        int tronasOcupadas = 0;

        for (Reserva reserva : reservaRepository.obtenerTodas()) {

            // c-. Solo cuentan las reservas activas
            if (reserva.getEstado() != EstadoReserva.ACTIVA) {
                continue;
            }

            // d-. Tramo de la reserva existente
            LocalDateTime inicioExistente =
                    LocalDateTime.of(reserva.getFecha(), reserva.getHora());
            LocalDateTime finExistente =
                    inicioExistente.plusHours(DURACION_HORAS);

            // e-. Si se solapan, sumamos sus tronas
            boolean seSolapan =
                    inicioNuevo.isBefore(finExistente)
                            && finNuevo.isAfter(inicioExistente);

            if (seSolapan) {
                tronasOcupadas += reserva.getTronas();
            }
        }

        // f-. Devolvemos las tronas libres (nunca menos de 0)
        return Math.max(0, TRONAS_MAXIMAS - tronasOcupadas);
    }


    // 10.2-. Valida la cantidad de tronas que eligio el cliente
    private void validarTronas(int personas, int tronasSolicitadas, LocalDate fecha, LocalTime hora)
            throws ReservaException {

        // a-. No se permiten cantidades negativas
        if (tronasSolicitadas < 0) {
            throw new ReservaException("El número de tronas no puede ser negativo.");
        }

        // b-. No se pueden pedir más tronas que personas (cada bebé va con un adulto)
        if (tronasSolicitadas > personas) {
            throw new ReservaException("No puedes pedir más tronas que personas en la reserva.");
        }

        // c-. No se pueden pedir más tronas de las que quedan libres
        int tronasDisponibles = calcularTronasDisponibles(fecha, hora);

        if (tronasSolicitadas > tronasDisponibles) {
            throw new ReservaException("Solo quedan " + tronasDisponibles
                    + " tronas disponibles para esta hora.");
        }
    }


    // 11-. Regla F: solapamiento, un cliente no puede tener dos reservas a la vez


    // 12-. Regla G: aviso de reserva el mismo día


    // 13-. Regla H: crear / modificar / cancelar


    // 14-. Regla I: finalizada automática al terminar las 2 h de reserva


    // 15-. Regla J: código de verificación (SIMULADO)




    // 16-. Crear una reserva aplicando todas las reglas
    //Las validaciones son métodos privados y el metodo
    // publico crearReserva las orquesta antes de crear y guardar la reserva
    public Reserva crearReserva(Cliente cliente, int personas, int tronasSolicitadas, LocalDate fecha, LocalTime hora)
            throws ReservaException {

        validarDatosObligatorios(cliente, fecha, hora);
        validarPersonas(personas);
        validarHora(hora);
        validarFechaHora(fecha, hora);
        validarAforo(fecha, hora, personas);
        validarTronas(personas, tronasSolicitadas, fecha, hora);

        Reserva reserva = new Reserva(cliente, personas, fecha, hora);
        reserva.setTronas(tronasSolicitadas);
        reservaRepository.guardar(reserva);
        return reserva;
    }

}

