package com.example.Excepciones;

/** El cliente superaría su límite de crédito con la compra. */
public class LimiteCreditoExcedidoException extends Exception {
    public LimiteCreditoExcedidoException(String cliente, double limite, double deudaResultante) {
        super("El cliente " + cliente + " superaría su límite de crédito ($" + limite
              + "). Deuda resultante: $" + deudaResultante);
    }
}
