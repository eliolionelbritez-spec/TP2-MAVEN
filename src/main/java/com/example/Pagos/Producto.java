package com.example.Pagos;

import com.example.Humanos.Proveedor;

public class Producto extends OfertaComercial {
    public Producto(String codigo, String nombre, double precio, String tipo, Proveedor proveedor) {
        super(codigo, nombre, precio, tipo, proveedor);
    }

    @Override
    public double calcularPrecioFinal() { return getPrecio(); }

    @Override
    public String obtenerDetalle() {
        return "Producto [" + getCodigo() + "] " + getNombre() + " - categoría " + getTipo();
    }
}
