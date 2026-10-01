package com.example.Excepciones;

/** No se puede emitir una factura sin ítems. */
public class FacturaSinItemsException extends Exception {
    private static final long serialVersionUID = 1L;

    public FacturaSinItemsException(String numero) {
        super("La factura " + numero + " no tiene ítems");
    }
}
