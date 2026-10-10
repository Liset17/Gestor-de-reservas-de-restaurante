package com.terramia.repository;

import com.terramia.model.MensajeContacto;

import java.util.ArrayList;
import java.util.List;

public class MensajeRepository {

    // 1. Lista donde se guarda los mensajes
    private final List<MensajeContacto> mensajes = new ArrayList<>();

    // 2. Guardar mensaje
    public void guardar(MensajeContacto mensaje) {
        mensajes.add(mensaje);
    }


    // 3. Devuelvo una copia de la lista de mensajes
    // para que el resto de la aplicación pueda leer los mensajes, pero no modificar
    // el almacén directamente
    public List<MensajeContacto> obtenerTodos() {
        return new ArrayList<>(mensajes);
    }

}
