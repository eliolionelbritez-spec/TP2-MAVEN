package com.example.Pagos;

import com.example.Humanos.Proveedor;

public class Servicio extends OfertaComercial {
    public Servicio(String codigo, String nombre, double precio, String tipo, Proveedor proveedor) {
        super(codigo, nombre, precio, tipo, proveedor);
    }

    @Override
    public double calcularPrecioFinal() { return getPrecio(); }

    @Override
    public String obtenerDetalle() {
        String prov = (getProveedor() == null) ? "propio" : getProveedor().getRazonSocial();
        return "Servicio [" + getCodigo() + "] " + getNombre() + " - prestado por " + prov;
    }
}
