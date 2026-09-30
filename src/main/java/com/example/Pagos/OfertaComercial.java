package com.example.Pagos;

import com.example.Excepciones.DatoInvalidoException;
import com.example.Humanos.Proveedor;
import com.example.Interfaces.Valorizable;

public abstract class OfertaComercial implements Valorizable {
    private String codigo;
    private String nombre;
    private double precio;
    private String tipo;
    private Proveedor proveedor;

    public OfertaComercial(String codigo, String nombre, double precio, String tipo, Proveedor proveedor) {
        this.codigo = codigo;
        setNombre(nombre);
        setPrecio(precio);
        this.tipo = tipo;
        this.proveedor = proveedor;
    }

    public abstract double calcularPrecioFinal();

    /** Método abstracto: cada subclase describe su detalle de forma distinta. */
    public abstract String obtenerDetalle();

    /** Método concreto compartido. */
    public String resumen() { return codigo + " - " + nombre; }

    @Override
    public double obtenerValor() { return calcularPrecioFinal(); }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public final void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new DatoInvalidoException("El nombre del ítem no puede estar vacío");
        this.nombre = nombre;
    }
    public double getPrecio() { return precio; }
    public final void setPrecio(double precio) {
        if (precio < 0) throw new DatoInvalidoException("El precio no puede ser negativo");
        this.precio = precio;
    }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
}
