package com.example.Excepciones;

/** No se puede emitir una factura sin ítems. */
public class FacturaSinItemsException extends Exception {
    public FacturaSinItemsException(String numero) {
        super("La factura " + numero + " no tiene ítems");
    }
}
