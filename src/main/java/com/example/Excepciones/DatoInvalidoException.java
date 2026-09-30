package com.example.Excepciones;

/** Dato de entrada inválido (no checked: es un error de programación/carga). */
public class DatoInvalidoException extends IllegalArgumentException {
    public DatoInvalidoException(String mensaje) { super(mensaje); }
}
