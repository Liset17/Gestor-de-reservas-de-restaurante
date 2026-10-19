package com.terramia.service;

import com.terramia.repository.ReservaRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.terramia.model.Cliente;
import com.terramia.model.Reserva;

class ReservaServiceTest {


    //Prueba con el restaurante vacio. No guardamos ninguna reserva en el repositorio.
    //Consultamos las tronas disponibles y esperamos que estén libres las 20
    @Test
    void sinReservasHayVeinteTronasDisponibles() {

        // 1. Preparar: repositorio vacío y servicio que lo utiliza
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.of(2027, 6, 15);
        LocalTime hora = LocalTime.of(19, 0);

        // 2. Actuar: consultar las tronas disponibles
        int disponibles =
                servicio.calcularTronasDisponibles(fecha, hora);

        // 3. Comprobar: esperamos que estén libres las 20
        assertEquals(20, disponibles);
    }





    //Se hace una reserva con 3 tronas
    @Test
    void reservaActivaConTresTronasDejaDiecisieteDisponibles() {

        // 1. Preparar un repositorio vacío y su servicio
        ReservaRepository repositorio = new ReservaRepository();
        ReservaService servicio = new ReservaService(repositorio);

        LocalDate fecha = LocalDate.of(2027, 6, 15);
        LocalTime hora = LocalTime.of(19, 0);

        // Crear un cliente con datos ficticios
        Cliente cliente = new Cliente(
                "Juana",
                "Benítez",
                "600000001",
                "juana.Beni@example.com"
        );

        // La reserva empieza ACTIVA y dura de 19:00 a 21:00
        Reserva reserva = new Reserva(cliente, 4, fecha, hora);
        reserva.setTronas(3);

        // Guarda para que el servicio pueda encontrarlo
        repositorio.guardar(reserva);

        // 2. Consultar las tronas disponibles para ese horario
        int disponibles =
                servicio.calcularTronasDisponibles(fecha, hora);

        // 3. Comprobar el resultado: 20 - 3 = 17
        assertEquals(17, disponibles);
    }
}