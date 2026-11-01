package com.terramia.service;

import com.terramia.model.MensajeContacto;
import com.terramia.repository.MensajeRepository;

public class ContactoService {



    //Se comprueba que un mensaje tenga sus datos obligatorios y lo guarda.
    // No pide datos por teclado ni envía correos, si falta algún dato, rechaza la operación y explica qué falta.
    private final MensajeRepository mensajeRepository;

    public ContactoService(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    public MensajeContacto guardarMensaje(
            String nombre, String correo, String mensaje)
            throws ReservaException {

        if (nombre == null || nombre.isBlank()) {
            throw new ReservaException("Debes indicar tu nombre.");
        }

        if (correo == null || correo.isBlank()) {
            throw new ReservaException(
                    "Debes indicar un correo electrónico."
            );
        }

        if (mensaje == null || mensaje.isBlank()) {
            throw new ReservaException("Debes escribir un mensaje.");
        }

        MensajeContacto nuevoMensaje =
                new MensajeContacto(nombre, correo, mensaje);

        mensajeRepository.guardar(nuevoMensaje);

        return nuevoMensaje;
    }
}