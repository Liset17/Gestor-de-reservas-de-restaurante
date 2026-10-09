package com.terramia.model;

import java.time.LocalDateTime;

public class MensajeContacto {

    //Atributos

    private final String nombre;
    private final String correo;
    private final String mensaje;
    private final LocalDateTime fechaEnvio;


    //Constructor
    public MensajeContacto(String nombre, String correo, String mensaje) {

        this.nombre = nombre;
        this.correo = correo;
        this.mensaje = mensaje;
        this.fechaEnvio = LocalDateTime.now();
    }

    //GETTERS

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    //SETTERS no se usa porque los mensajes no se modifican

}
