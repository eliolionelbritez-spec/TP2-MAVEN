package com.example.Humanos;

import com.example.Excepciones.DatoInvalidoException;

/** Clase abstracta: una "persona" genérica nunca se instancia, siempre es cliente, empleado o proveedor. */
public abstract class Persona {
    private String nombre;
    private String domicilio;
    private String dni;
    private String telefono;

    public Persona(String nombre, String domicilio, String dni, String telefono) {
        setNombre(nombre);
        setDomicilio(domicilio);
        setDni(dni);
        setTelefono(telefono);
    }

    /** Método abstracto: cada subclase define su rol. */
    public abstract String getRol();

    /** Método concreto compartido. */
    public String resumen() {
        return getRol() + ": " + nombre + " (DNI " + dni + ")";
    }

    public String getNombre() { return nombre; }
    public final void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new DatoInvalidoException("El nombre no puede estar vacío");
        this.nombre = nombre.trim();
    }
    public String getDomicilio() { return domicilio; }
    public final void setDomicilio(String domicilio) {
        if (domicilio == null || domicilio.isBlank())
            throw new DatoInvalidoException("El domicilio no puede estar vacío");
        this.domicilio = domicilio.trim();
    }
    public String getDni() { return dni; }
    public final void setDni(String dni) {
        if (dni == null || !dni.matches("\\d{7,8}"))
            throw new DatoInvalidoException("El DNI debe tener 7 u 8 dígitos: " + dni);
        this.dni = dni;
    }
    public String getTelefono() { return telefono; }
    public final void setTelefono(String telefono) {
        if (telefono == null || !telefono.matches("[0-9+\\- ]{6,20}"))
            throw new DatoInvalidoException("Teléfono inválido: " + telefono);
        this.telefono = telefono;
    }
}
