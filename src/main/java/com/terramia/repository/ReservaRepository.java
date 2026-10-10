package com.terramia.repository;

import com.terramia.model.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ReservaRepository {

    // 1. Lista donde se guarda las reservas
    private final List<Reserva> reservas = new ArrayList<>();


    // 2. Guardar una reserva
    public void guardar(Reserva reserva) {
        reservas.add(reserva);
    }

    // 3. Buscar una reserva por su ID

    public Reserva buscarPorId(int id) {

        for (Reserva reserva : reservas) {
            if (reserva.getId() == id) {
                return reserva;
            }
        }

        return null;
    }


    // 4. Buscar todas las reservas de un cliente por su movil

    public List<Reserva> buscarPorMovil(String movil) {
        List<Reserva> resultado = new ArrayList<>();

        for (Reserva reserva : reservas) {
            if (reserva.getCliente().getMovil().equals(movil)) {
                resultado.add(reserva);
            }
        }

        return resultado;
    }


    // 5. Devuelvo una copia de la lista de reservas
    // para que el resto de la aplicación pueda leer las reservas, pero no modificar
    // el almacén directamente
    public List<Reserva> obtenerTodas() {
        return new ArrayList<>(reservas);
    }

}
