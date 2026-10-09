package com.terramia.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {

    //1. ATRIBUTOS

    private static int contador = 1;// genera el ID de cada reserva

    //Identificador (final es para hacerlo inmutable)
    private final int id;

    //quien
    private final Cliente cliente;


    // Cuantos
    private int personas;
    private int ninosMenores3;
    private int tronas;


    //cuando
    private LocalDate fecha;
    private LocalTime hora;

    //estado
    private EstadoReserva estado;


    // ============= CONSTRUCTOR ===============

    public Reserva(Cliente cliente, int personas, int
            ninosMenores3, LocalDate fecha, LocalTime hora) {

        this.id = contador++;
        this.cliente = cliente;
        this.personas = personas;
        this.ninosMenores3 = ninosMenores3;
        this.tronas = 0;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = EstadoReserva.ACTIVA;

    }


    // 3. GETTERS

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public int getPersonas() {
        return personas;
    }

    public int getNinosMenores3() {
        return ninosMenores3;
    }

    public int getTronas() {
        return tronas;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    // 4. SETTERS

    public void setPersonas(int personas) {
        this.personas = personas;
    }

    public void setNinosMenores3(int ninosMenores3) {
        this.ninosMenores3 = ninosMenores3;
    }

    public void setTronas(int tronas) {
        this.tronas = tronas;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }
}



