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


    // 3. Regla A : Se crea un metodo para la regla entre 1 y 12 personas (con un mensaje para cada caso).
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
        LocalDateTime ahora= LocalDateTime.now();


        //3-. Se comprueba si la reserva es para el pasado o actual
        if (!fechaHoraReserva.isAfter(ahora)){
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

}