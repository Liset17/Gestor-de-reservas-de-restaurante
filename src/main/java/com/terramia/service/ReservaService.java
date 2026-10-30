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
    private void validarDatosObligatorios(Cliente cliente, LocalDate fecha,
                                          LocalTime hora) throws ReservaException {

        // a-. El cliente es obligatorio
        if (cliente == null) {
            throw new ReservaException(
                    "Faltan los datos del cliente."
            );
        }
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new ReservaException("Es obligatorio indicar tu nombre para continuar tu reserva.");
        }

        if (cliente.getApellidos() == null || cliente.getApellidos().isBlank()) {
            throw new ReservaException("debes indicar tus apellidos.");
        }

        if (cliente.getCorreo() == null || cliente.getCorreo().isBlank()) {
            throw new ReservaException("Debes indicar un correo electrónico.");
        }


        // b-. Comprobar que el móvil está informado
        if (cliente.getMovil() == null || cliente.getMovil().isBlank()) {
            throw new ReservaException(
                    "Debes indicar un número móvil."
            );
        }

        // c-. Comprobar que existe una fecha
        if (fecha == null) {
            throw new ReservaException(
                    "Debes indicar una fecha para la reserva."
            );
        }

        // d-. La hora es obligatoria
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


    // 8.1-. Auxiliar: comprueba si una reserva existente se solapa con un tramo nuevo de 2 h
    private boolean seSolapa(Reserva reserva, LocalDate fecha, LocalTime hora) {

        // a-. Tramo de la nueva reserva
        LocalDateTime inicioNuevo = LocalDateTime.of(fecha, hora);
        LocalDateTime finNuevo = inicioNuevo.plusHours(DURACION_HORAS);

        // b-. Tramo de la reserva existente
        LocalDateTime inicioExistente =
                LocalDateTime.of(reserva.getFecha(), reserva.getHora());
        LocalDateTime finExistente =
                inicioExistente.plusHours(DURACION_HORAS);

        // c-. Se solapan si la nueva empieza antes de que acabe la existente
        //     y termina después de que empiece la existente
        return inicioNuevo.isBefore(finExistente)
                && finNuevo.isAfter(inicioExistente);
    }

// 9-. Regla D: aforo máximo de 120 personas durante las 2 horas

    // Al crear, no excluimos ninguna reserva
    private void validarAforo(LocalDate fecha, LocalTime hora, int personas)
            throws ReservaException {
        validarAforo(fecha, hora, personas, null);
    }

    // Al modificar, podemos excluir la reserva original del cálculo
    private void validarAforo(LocalDate fecha, LocalTime hora, int personas, Integer idReservaExcluir)
            throws ReservaException {

        LocalDateTime inicioNuevo = LocalDateTime.of(fecha, hora);

        for (int minutos = 0;
             minutos < DURACION_HORAS * 60;
             minutos += 30) {
            LocalDateTime momento = inicioNuevo.plusMinutes(minutos);
            int ocupadas = 0;

            for (Reserva reserva : reservaRepository.obtenerTodas()) {

                // Solo se omite del cálculo; no se borra ni se cancela
                if (idReservaExcluir != null
                        && reserva.getId() == idReservaExcluir.intValue()) {
                    continue;
                }

                if (reserva.getEstado() != EstadoReserva.ACTIVA) {
                    continue;
                }

                LocalDateTime inicioExistente =
                        LocalDateTime.of(reserva.getFecha(), reserva.getHora());

                LocalDateTime finExistente =
                        inicioExistente.plusHours(DURACION_HORAS);

                boolean estaDentro = !momento.isBefore(inicioExistente)
                        && momento.isBefore(finExistente);

                if (estaDentro) {
                    ocupadas += reserva.getPersonas();
                }
            }

            int libres = AFORO_MAXIMO - ocupadas;

            if (personas > libres) {
                throw new ReservaException(
                        "Lo sentimos, no hay disponibilidad en el horario escogido. "
                                + "A las " + momento.toLocalTime()
                                + " solo quedan " + Math.max(0, libres)
                                + " plazas libres."
                );
            }
        }
    }


    // 10-. Regla E: tronas, máx. 20 en el tramo de 2 h (si no quedan, se puede reservar sin trona).
    // 10.1-. Calcula las tronas libres en el tramo de 2 h
    public int calcularTronasDisponibles(LocalDate fecha, LocalTime hora) {
        return calcularTronasDisponibles(fecha, hora, null);
    }

    // Consulta que permite excluir la reserva que se modifica
    private int calcularTronasDisponibles(
            LocalDate fecha,
            LocalTime hora,
            Integer idReservaExcluir) {

        LocalDateTime inicioNuevo = LocalDateTime.of(fecha, hora);
        int tronasDisponibles = TRONAS_MAXIMAS;

        for (int minutos = 0;
             minutos < DURACION_HORAS * 60;
             minutos += 30) {

            LocalDateTime momento = inicioNuevo.plusMinutes(minutos);
            int tronasOcupadas = 0;

            for (Reserva reserva : reservaRepository.obtenerTodas()) {

                if (idReservaExcluir != null
                        && reserva.getId() == idReservaExcluir.intValue()) {
                    continue;
                }

                if (reserva.getEstado() != EstadoReserva.ACTIVA) {
                    continue;
                }

                LocalDateTime inicioExistente =
                        LocalDateTime.of(reserva.getFecha(), reserva.getHora());

                LocalDateTime finExistente =
                        inicioExistente.plusHours(DURACION_HORAS);

                boolean estaDentro = !momento.isBefore(inicioExistente)
                        && momento.isBefore(finExistente);

                if (estaDentro) {
                    tronasOcupadas += reserva.getTronas();
                }
            }

            int libres = Math.max(0, TRONAS_MAXIMAS - tronasOcupadas);
            tronasDisponibles = Math.min(tronasDisponibles, libres);
        }

        return tronasDisponibles;
    }

    // 10.2-. Validar las tronas solicitadas
    private void validarTronas(int personas, int tronasSolicitadas, LocalDate fecha,
                               LocalTime hora) throws ReservaException {

        validarTronas(personas, tronasSolicitadas, fecha, hora, null);
    }

    // permite recibir el ID de la reserva que se modifica
    private void validarTronas(
            int personas,
            int tronasSolicitadas,
            LocalDate fecha,
            LocalTime hora,
            Integer idReservaExcluir) throws ReservaException {

        if (tronasSolicitadas < 0) {
            throw new ReservaException(
                    "El número de tronas no puede ser negativo."
            );
        }

        if (tronasSolicitadas > personas) {
            throw new ReservaException(
                    "No puedes pedir más tronas que personas en la reserva."
            );
        }

        //Reserva modificarReserva pasamos también el ID que debe excluirse
        int tronasDisponibles =
                calcularTronasDisponibles(fecha, hora, idReservaExcluir);

        if (tronasSolicitadas > tronasDisponibles) {
            throw new ReservaException(
                    "Solo quedan " + tronasDisponibles
                            + " tronas disponibles para esta hora."
            );
        }
    }


    // 11-. Regla F: solapamiento, un cliente no puede tener dos reservas a la vez
    private void validarSolapamientoCliente(Cliente cliente, LocalDate fecha, LocalTime hora)
            throws ReservaException {

        validarSolapamientoCliente(cliente, fecha, hora, null);
    }

    // Permite excluir la reserva que estamos modificando
    private void validarSolapamientoCliente(
            Cliente cliente,
            LocalDate fecha,
            LocalTime hora,
            Integer idReservaExcluir) throws ReservaException {

        for (Reserva reserva :
                reservaRepository.buscarPorMovil(cliente.getMovil())) {

            // Saltamos la propia reserva que se está modificando
            if (idReservaExcluir != null
                    && reserva.getId() == idReservaExcluir.intValue()) {
                continue;
            }

            if (reserva.getEstado() != EstadoReserva.ACTIVA) {
                continue;
            }

            if (seSolapa(reserva, fecha, hora)) {
                throw new ReservaException(
                        "Ya tienes una reserva el "
                                + reserva.getFecha()
                                + " a las "
                                + reserva.getHora()
                                + " que coincide con ese horario."
                );
            }
        }
    }


    // 12-. Regla G: aviso si el cliente ya tiene una reserva ese mismo día
    public boolean tieneReservaElMismoDia(String movil, LocalDate fecha) {

        // a-. Recorremos las reservas de este cliente
        for (Reserva reserva : reservaRepository.buscarPorMovil(movil)) {

            // b-. Solo cuentan las reservas activas
            if (reserva.getEstado() != EstadoReserva.ACTIVA) {
                continue;
            }

            // c-. Si la fecha coincide, ya tiene reserva ese día
            if (reserva.getFecha().equals(fecha)) {
                return true;
            }
        }
        //d-. Si recorremos todas y ninguna coincide
        return false;
    }


    // 13-. Regla H: crear / modificar / cancelar
    public void cancelarReserva(int id, String movil)
            throws ReservaException {

        // 1. El sistema busca la reserva si no existe, lanza una excepcion
        Reserva reserva = buscarReservaExistente(id);

        // 2. Comprobar que pertenece al cliente
        if (!reserva.getCliente().getMovil().equals(movil)) {
            throw new ReservaException(
                    "No puedes cancelar una reserva de otro cliente."
            );
        }

        // 3. Comprobar que sigue activa
        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new ReservaException(
                    "Solo puedes cancelar una reserva activa."
            );
        }

        // 4. Comprobar que todavia no ha empezado
        validarFechaHora(reserva.getFecha(), reserva.getHora());

        // 5. Cancelar después de superar todas las comprobaciones
        reserva.setEstado(EstadoReserva.CANCELADA);
    }

    public Reserva modificarReserva(int id, String movil, int personas,
                                    int tronasSolicitadas, LocalDate fecha, LocalTime hora)
            throws ReservaException {

        Reserva reserva = buscarReservaExistente(id);

        if (!reserva.getCliente().getMovil().equals(movil)) {
            throw new ReservaException(
                    "No puedes modificar una reserva de otro cliente."
            );
        }

        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new ReservaException(
                    "Solo puedes modificar una reserva activa."
            );
        }


        validarFechaHora(reserva.getFecha(), reserva.getHora());

        Cliente cliente = reserva.getCliente();

        validarDatosObligatorios(cliente, fecha, hora);
        validarPersonas(personas);
        validarHora(hora);
        validarFechaHora(fecha, hora);

        validarSolapamientoCliente(cliente, fecha, hora, id);
        validarAforo(fecha, hora, personas, id);
        validarTronas(personas, tronasSolicitadas, fecha, hora, id);

        reserva.setPersonas(personas);
        reserva.setTronas(tronasSolicitadas);
        reserva.setFecha(fecha);
        reserva.setHora(hora);

        return reserva;
    }


    // 14-. Regla I: finalizada automática al terminar las 2 h de reserva
    public void actualizarReservasFinalizadas() {

        // Obtener el momento actual una sola vez
        LocalDateTime ahora = LocalDateTime.now();

        for (Reserva reserva : reservaRepository.obtenerTodas()) {

            // Las canceladas o finalizadas se mantienen como están
            if (reserva.getEstado() != EstadoReserva.ACTIVA) {
                continue;
            }

            LocalDateTime inicio =
                    LocalDateTime.of(reserva.getFecha(), reserva.getHora());

            LocalDateTime fin = inicio.plusHours(DURACION_HORAS);

            // Finalizar si ya llegó o pasó su hora de fin
            if (!ahora.isBefore(fin)) {
                reserva.setEstado(EstadoReserva.FINALIZADA);
            }
        }
    }


    // 15-. Regla J: código de verificación (SIMULADO)


    // 16-. Crear una reserva aplicando todas las reglas
    //Las validaciones son métodos privados y el metodo
    // publico crearReserva las orquesta antes de crear y guardar la reserva
    public Reserva crearReserva(Cliente cliente, int personas, int tronasSolicitadas, LocalDate fecha, LocalTime
            hora)
            throws ReservaException {

        validarDatosObligatorios(cliente, fecha, hora);
        validarPersonas(personas);
        validarHora(hora);
        validarFechaHora(fecha, hora);
        validarSolapamientoCliente(cliente, fecha, hora);
        validarAforo(fecha, hora, personas);
        validarTronas(personas, tronasSolicitadas, fecha, hora);

        Reserva reserva = new Reserva(cliente, personas, fecha, hora);
        reserva.setTronas(tronasSolicitadas);
        reservaRepository.guardar(reserva);
        return reserva;
    }

}

