package com.terramia.service;

// Tarjeta para un error propio del proyecto cuando se rompe una regla personalizada.
public class ReservaException extends Exception {

    public ReservaException(String mensaje) {
        super(mensaje);


    }
}