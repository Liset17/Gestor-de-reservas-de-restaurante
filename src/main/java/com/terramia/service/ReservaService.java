package com.terramia.service;

import com.terramia.repository.ReservaRepository;

public class ReservaService {

    // 1-. Repositorio para guardar y buscar reservas
    private final ReservaRepository reservaRepository;


    //2-.  Genero el constructor
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }


    // 3. Se crea un metodo para la regla entre 1 y 12 personas (con un mensaje para cada caso).
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
}