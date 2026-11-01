package com.terramia.service;

import com.terramia.model.MensajeContacto;
import com.terramia.repository.MensajeRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContactoServiceTest {

    @Test
    void seGuardaUnMensajeConTodosLosDatos() throws ReservaException {

        MensajeRepository repositorio = new MensajeRepository();
        ContactoService servicio = new ContactoService(repositorio);

        servicio.guardarMensaje(
                "María",
                "maria@example.com",
                "Quiero información para un grupo de 15 personas."
        );

        assertEquals(1, repositorio.obtenerTodos().size());

        MensajeContacto guardado = repositorio.obtenerTodos().get(0);

        assertEquals("María", guardado.getNombre());
        assertEquals("maria@example.com", guardado.getCorreo());
        assertEquals(
                "Quiero información para un grupo de 15 personas.",
                guardado.getMensaje()
        );
    }

    // Comprueba que un mensaje sin nombre se rechaza y no se guarda.
    @Test
    void noSeGuardaUnMensajeSinNombre() {

        MensajeRepository repositorio = new MensajeRepository();
        ContactoService servicio = new ContactoService(repositorio);

        assertThrows(ReservaException.class,
                () -> servicio.guardarMensaje(
                        "",
                        "maria@example.com",
                        "Quiero información para un grupo."
                ));

        assertEquals(0, repositorio.obtenerTodos().size());
    }

    // Comprueba que un mensaje sin correo se rechaza y no se guarda.
    @Test
    void noSeGuardaUnMensajeSinCorreo() {

        MensajeRepository repositorio = new MensajeRepository();
        ContactoService servicio = new ContactoService(repositorio);

        assertThrows(ReservaException.class,
                () -> servicio.guardarMensaje(
                        "María",
                        "",
                        "Quiero información para un grupo."
                ));

        assertEquals(0, repositorio.obtenerTodos().size());
    }



    // Comprueba que el texto del mensaje sea obligatorio.
    @Test
    void noSeGuardaUnMensajeVacio() {

        MensajeRepository repositorio = new MensajeRepository();
        ContactoService servicio = new ContactoService(repositorio);

        assertThrows(ReservaException.class,
                () -> servicio.guardarMensaje(
                        "María",
                        "maria@example.com",
                        ""
                ));

        assertEquals(0, repositorio.obtenerTodos().size());
    }
}