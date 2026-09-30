package com.example.Pagos;

import com.example.Excepciones.DatoInvalidoException;

/** Se mantiene concreta: solo varía formaPago (dato), no hay comportamiento distinto que justifique subclases. */
public class Pago {
    private String numero;
    private String fecha;
    private double monto;
    private String formaPago;
    private String estado;

    public Pago(String numero, String fecha, double monto, String formaPago, String estado) {
        this.numero = numero;
        this.fecha = fecha;
        setMonto(monto);
        this.formaPago = formaPago;
        this.estado = estado;
    }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public double getMonto() { return monto; }
    public final void setMonto(double monto) {
        if (monto < 0) throw new DatoInvalidoException("El monto del pago no puede ser negativo");
        this.monto = monto;
    }
    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
