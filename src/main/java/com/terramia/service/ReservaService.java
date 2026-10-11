package com.terramia.service;

import com.terramia.repository.ReservaRepository;

public class ReservaService {

    // 1-. Repositorio para guardar y buscar reservas
    private final ReservaRepository reservaRepository;



    //2-.  Genero el constructor
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }




    // 3. Se crea un metodo para la regla entre 1 y 12 personas
    private void validarPersonas(int personas) throws ReservaException {

        if (personas < 1 || personas > 12) {
            throw new ReservaException("El número de personas debe estar entre 1 y 12. " +
                    "Para grupos de 13 o más, por favor contacta con el restaurante.");
        }


    }
}