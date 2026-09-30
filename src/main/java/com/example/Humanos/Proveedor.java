package com.example.Humanos;

import java.util.ArrayList;

import com.example.Pagos.Producto;

public class Proveedor extends Persona {
    private String razonSocial;
    private String nif;
    private ArrayList<Producto> productosSuministrados = new ArrayList<>();

    public Proveedor(String nombre, String domicilio, String dni, String telefono, String razonSocial, String nif) {
        super(nombre, domicilio, dni, telefono);
        this.razonSocial = razonSocial;
        this.nif = nif;
    }

    @Override
    public String getRol() { return "Proveedor"; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
    public ArrayList<Producto> getProductosSuministrados() { return productosSuministrados; }
    public void agregarProductoSuministrado(Producto producto) { productosSuministrados.add(producto); }
}
