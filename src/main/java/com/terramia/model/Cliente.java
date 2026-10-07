package com.terramia.model;

public class Cliente {


    // 1. ATRIBUTOS privados: datos escondidos
    private String nombre;
    private String apellidos;
    private String movil;
    private String correo;




    // 2. CONSTRUCTOR: es para crear el objeto, para darle sus datos
    public Cliente(String nombre, String apellidos, String movil, String correo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.movil = movil;
        this.correo = correo;
    }





    // 3. GETTER: para LEER un dato
    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getMovil() {
        return movil;
    }

    public String getCorreo() {
        return correo;
    }





    // 4. SETTER: para CAMBIAR un dato
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }


    public void setMovil(String movil) {
        this.movil = movil;
    }


    public void setCorreo(String correo) {
        this.correo = correo;
    }


}
