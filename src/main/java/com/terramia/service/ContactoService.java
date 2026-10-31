package com.terramia.service;

public class ContactoService {

    package com.terramia.service;

import com.terramia.model.MensajeContacto;
import com.terramia.repository.MensajeRepository;


//Esta clase no pregunta por teclado ni envía correos: valida y coordina el guardado
// Revisar y guardar lo que un cliente escribe en el formulario de contacto
    public class ContactoService {

        private final MensajeRepository mensajeRepository;

        public ContactoService(MensajeRepository mensajeRepository) {
            this.mensajeRepository = mensajeRepository;
        }

        public MensajeContacto guardarMensaje(
                String nombre, String correo, String mensaje)
                throws ReservaException {

            if (nombre == null || nombre.isBlank()) {
                throw new ReservaException(
                        "Debes indicar tu nombre."
                );
            }

            if (correo == null || correo.isBlank()) {
                throw new ReservaException(
                        "Debes indicar un correo electrónico."
                );
            }

            if (mensaje == null || mensaje.isBlank()) {
                throw new ReservaException(
                        "Debes escribir un mensaje."
                );
            }

            MensajeContacto nuevoMensaje =
                    new MensajeContacto(nombre, correo, mensaje);

            mensajeRepository.guardar(nuevoMensaje);

            return nuevoMensaje;
        }
    }
}
