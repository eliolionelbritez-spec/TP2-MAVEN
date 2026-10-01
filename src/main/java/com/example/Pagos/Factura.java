package com.example.Pagos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;

import com.example.Excepciones.DatoInvalidoException;
import com.example.Excepciones.FacturaSinItemsException;
import com.example.Excepciones.LimiteCreditoExcedidoException;
import com.example.Excepciones.PagoExcedidoException;
import com.example.Humanos.Cliente;
import com.example.Humanos.Empleado;
import com.example.Interfaces.Exportable;
import com.example.Interfaces.Valorizable;

/** Implementa tres contratos independientes: Comparable, Exportable y Valorizable. */
public class Factura implements Comparable<Factura>, Exportable, Valorizable {
    private String numero;
    private String fecha; // formato dd-MM-yyyy
    private Cliente cliente;
    private Empleado empleado;
    private ArrayList<OfertaComercial> items;
    private Pago pago;

    /** Criterio alternativo (Comparator): por fecha. */
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final Comparator<Factura> POR_FECHA =
            Comparator.comparing((Factura f) -> LocalDate.parse(f.getFecha(), FORMATO));

    public Factura(String numero, String fecha, Cliente cliente, Empleado empleado,
                   ArrayList<OfertaComercial> items, Pago pago)
            throws FacturaSinItemsException, PagoExcedidoException {
        if (items == null || items.isEmpty()) throw new FacturaSinItemsException(numero);
        try { LocalDate.parse(fecha, FORMATO); }
        catch (DateTimeParseException | NullPointerException e) {
            throw new DatoInvalidoException("Fecha inválida (dd-MM-yyyy): " + fecha);
        }
        this.numero = numero;
        this.fecha = fecha;
        this.cliente = cliente;
        this.empleado = empleado;
        this.items = items;
        this.pago = pago;
        double total = calcularTotal();
        if (pago.getMonto() > total) throw new PagoExcedidoException(pago.getMonto(), total);
    }

    /** Emite la factura: registra la compra en el cliente (puede exceder su crédito) y fija el estado del pago. */
    public void emitir() throws LimiteCreditoExcedidoException {
        double total = calcularTotal();
        cliente.registrarCompra("Factura " + numero, total, pago.getMonto());
        double pagado = pago.getMonto();
        pago.setEstado(pagado >= total ? "Completo" : (pagado > 0 ? "Parcial" : "Pendiente"));
    }

    public void agregarItem(OfertaComercial item) { items.add(item); }

    public double calcularTotal() {
        double total = 0;
        for (OfertaComercial item : items) total += item.calcularPrecioFinal();
        return total;
    }

    @Override
    public double obtenerValor() { return calcularTotal(); }

    /** Orden natural (Comparable): por monto total. */
    @Override
    public int compareTo(Factura otra) {
        return Double.compare(calcularTotal(), otra.calcularTotal());
    }

    @Override
    public String aLineaTexto() {
        StringBuilder sb = new StringBuilder();
        for (OfertaComercial i : items) {
            if (sb.length() > 0) sb.append("|");
            sb.append(i.getCodigo()).append(":").append(i.getNombre());
        }
        return numero + ";" + fecha + ";" + cliente.getNombre() + ";" + empleado.getNombre() + ";"
                + calcularTotal() + ";" + pago.getFormaPago() + ";" + pago.getEstado() + ";" + sb;
    }

    public void mostrarFactura() {
        System.out.println("Factura Número: " + numero);
        System.out.println("Fecha de Emisión: " + fecha);
        System.out.println("Cliente: " + cliente.getNombre() + " (" + cliente.getCategoria() + ")");
        System.out.println("Historial de compras: " + cliente.getHistorialCompras());
        System.out.println("Empleado Gestor: " + empleado.getNombre() + " - Puesto: " + empleado.getPuesto());
        System.out.println("Departamento: " + empleado.getDepartamento().getNombre());
        System.out.println("Items Adquiridos:");
        for (OfertaComercial item : items)
            System.out.println(" - " + item.obtenerDetalle() + " - Precio: $" + item.calcularPrecioFinal());
        System.out.println("Forma de Pago: " + pago.getFormaPago());
        System.out.println("Estado del Pago: " + pago.getEstado());
        System.out.println("Total a Pagar: $" + calcularTotal());
    }

    public String getNumero() { return numero; }
    public String getFecha() { return fecha; }
    public Cliente getCliente() { return cliente; }
}
