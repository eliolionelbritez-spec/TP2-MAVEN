package com.example.Humanos;

import java.util.ArrayList;
import java.util.Comparator;

import com.example.Excepciones.DatoInvalidoException;
import com.example.Excepciones.LimiteCreditoExcedidoException;
import com.example.Interfaces.Exportable;

public class Cliente extends Persona implements Comparable<Cliente>, Exportable {
    private double limiteCredito;
    private String categoria; // regular, premium o corporativo
    private double saldoPendiente = 0;
    private ArrayList<String> historialCompras = new ArrayList<>();

    /** Criterio alternativo (Comparator): por límite de crédito. */
    public static final Comparator<Cliente> POR_LIMITE_CREDITO =
            Comparator.comparingDouble(Cliente::getLimiteCredito);

    public Cliente(String nombre, String domicilio, String dni, String telefono,
                   double limiteCredito, String categoria) {
        super(nombre, domicilio, dni, telefono);
        setLimiteCredito(limiteCredito);
        setCategoria(categoria);
    }

    @Override
    public String getRol() { return "Cliente"; }

    /** Registra una compra; falla si la deuda resultante supera el límite de crédito. */
    public void registrarCompra(String descripcion, double total, double pagado)
            throws LimiteCreditoExcedidoException {
        double deudaResultante = saldoPendiente + (total - pagado);
        if (deudaResultante > limiteCredito)
            throw new LimiteCreditoExcedidoException(getNombre(), limiteCredito, deudaResultante);
        saldoPendiente = deudaResultante;
        historialCompras.add(descripcion);
    }

    /** Orden natural (Comparable): por cantidad de compras. */
    @Override
    public int compareTo(Cliente otro) {
        return Integer.compare(historialCompras.size(), otro.historialCompras.size());
    }

    @Override
    public String aLineaTexto() {
        return getDni() + ";" + getNombre() + ";" + categoria + ";" + limiteCredito + ";" + historialCompras.size();
    }

    public double getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(double limiteCredito) {
        if (limiteCredito < 0) throw new DatoInvalidoException("El límite de crédito no puede ser negativo");
        this.limiteCredito = limiteCredito;
    }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) {
        if (categoria == null || !categoria.toLowerCase().matches("regular|premium|corporativo"))
            throw new DatoInvalidoException("Categoría inválida (regular, premium o corporativo): " + categoria);
        this.categoria = categoria.toLowerCase();
    }
    public double getSaldoPendiente() { return saldoPendiente; }
    public ArrayList<String> getHistorialCompras() { return historialCompras; }
    public void agregarHistorialCompra(String compra) { historialCompras.add(compra); }
}
