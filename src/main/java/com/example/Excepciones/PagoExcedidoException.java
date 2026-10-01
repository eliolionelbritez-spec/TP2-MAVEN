package com.example.Excepciones;

/** El pago intenta cancelar más que el total adeudado. */
public class PagoExcedidoException extends Exception {
    private static final long serialVersionUID = 1L;

    public PagoExcedidoException(double pago, double total) {
        super("El pago ($" + pago + ") supera el total adeudado ($" + total + ")");
    }
}
