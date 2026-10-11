package com.terramia.service;

import com.terramia.model.Reserva;
import com.terramia.repository.ReservaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservaService {

    // 1-. Repositorio para guardar y buscar reservas
    private final ReservaRepository reservaRepository;


    //2-.  Genero el constructor
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }


    //3-. Regla A: Se crea un metodo para la regla entre 1 y 12 personas (con un mensaje para cada caso).
    private void validarPersonas(int personas) throws ReservaException {

        if (personas < 1) {
            throw new ReservaException(
                    "La reserva debe ser para al menos 1 persona."
            );
        }

        if (personas > 12) {
            throw new ReservaException(
                    "Para grupos de 13 o más de personas, contacte con el restaurante."
            );
        }
    }

    //4-. Regla B: la fecha y la hora no pueden estar en el pasado.
    private void validarFechaHora(LocalDate fecha, LocalTime hora)
            throws ReservaException {

        //1-. Se une fecha y hora
        LocalDateTime fechaHoraReserva = LocalDateTime.of(fecha, hora);


        //2-. Se obtiene el momento actual
        LocalDateTime ahora = LocalDateTime.now();


        //3-. Se comprueba que la reserva sea para un momento futuro
        if (!fechaHoraReserva.isAfter(ahora)) {
            throw new ReservaException(
                    "No puedes reservar en una fecha u hora que ya ha pasado."
            );
        }

    }


    // 5. Busca una reserva por ID y lanza un error si no existe
    private Reserva buscarReservaExistente(int id)
            throws ReservaException {

        Reserva reserva = reservaRepository.buscarPorId(id);

        if (reserva == null) {
            throw new ReservaException("No existe ninguna reserva con el número " + id + ".");
        }

        return reserva;
    }


    //6-.  Regla C: horario de 11:30 a 23:30, cada 30 minutos
    private void validarHora(LocalTime hora) throws ReservaException {

        // a. Definimos el inicio del horario
        LocalTime apertura = LocalTime.of(11, 30);

        // b. Definimos el fin del horario
        LocalTime ultimaReserva = LocalTime.of(23, 30);


        // c. Comprobamos que la hora esté dentro del horario
        if (hora.isBefore(apertura) || hora.isAfter(ultimaReserva)) {
            throw new ReservaException(
                    "Solo puedes reservar entre las 11:30 y las 23:30");
        }

        //d. Comprobamos que la hora sea en punto o/y media (esto es una validación completa y más segura)
        if (hora.getMinute() % 30 != 0
                || hora.getSecond() != 0
                || hora.getNano() != 0) {
            throw new ReservaException(
                    "Las reservas son cada 30 minutos (por ejemplo, 19:00 o 19:30)."
            );
        }

    }

}


//7-. Regla D: Aforo de reservas: máx. 120 personas en el tramo de 2 h


//8-. Regla E: Tronas: máx. 20 en el tramo de 2 h (si no quedan puede reserva sin trona)


//9-. Regla F: Solapamiento: un cliente no puede tener dos reservas a la vez


//10-. Regla G: Aviso de reserva el mismo día


//11-. Regla H: Crear / modificar / cancelar


//12-. Regla I: Finalizada automática al terminar las 2 h de reserva


//13-. Regla J:Código de verificación (SIMULADO)




}