package com.example.Gestion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.Excepciones.FacturaSinItemsException;
import com.example.Excepciones.LimiteCreditoExcedidoException;
import com.example.Excepciones.PagoExcedidoException;
import com.example.Humanos.Cliente;
import com.example.Humanos.Empleado;
import com.example.Pagos.Factura;
import com.example.Pagos.OfertaComercial;
import com.example.Pagos.Pago;
import com.example.Persistencia.GestorArchivo;

/** Reúne los datos del negocio y las operaciones sobre ellos. No hace entrada/salida por consola. */
public class Empresa {
    private final ArrayList<Cliente> clientes = new ArrayList<>();
    private final ArrayList<Empleado> empleados = new ArrayList<>();
    private final ArrayList<OfertaComercial> catalogo = new ArrayList<>();
    private final ArrayList<Factura> facturas = new ArrayList<>();
    private final GestorArchivo archivo;          // facturas
    private final GestorArchivo archivoClientes;  // clientes

    public Empresa(String rutaFacturas, String rutaClientes) {
        this.archivo = new GestorArchivo(rutaFacturas);
        this.archivoClientes = new GestorArchivo(rutaClientes);
    }

    public void agregarCliente(Cliente c) { clientes.add(c); }
    public void agregarEmpleado(Empleado e) { empleados.add(e); }
    public void agregarOferta(OfertaComercial o) { catalogo.add(o); }

    /** Crea, emite y registra una factura. Propaga los errores de dominio a quien la invoque. */
    public Factura emitirFactura(String fecha, Cliente c, Empleado e, ArrayList<OfertaComercial> items, Pago pago)
            throws FacturaSinItemsException, PagoExcedidoException, LimiteCreditoExcedidoException {
        String numero = String.format("F%03d", facturas.size() + 1);
        Factura f = new Factura(numero, fecha, c, e, items, pago);
        f.emitir();
        facturas.add(f);
        return f;
    }

    public List<Factura> facturasPorMonto() {
        List<Factura> l = new ArrayList<>(facturas);
        Collections.sort(l);
        return l;
    }
    public List<Factura> facturasPorFecha() {
        List<Factura> l = new ArrayList<>(facturas);
        Collections.sort(l, Factura.POR_FECHA);
        return l;
    }
    public List<Cliente> clientesPorCompras() {
        List<Cliente> l = new ArrayList<>(clientes);
        Collections.sort(l);
        return l;
    }
    public List<Cliente> clientesPorLimite() {
        List<Cliente> l = new ArrayList<>(clientes);
        Collections.sort(l, Cliente.POR_LIMITE_CREDITO);
        return l;
    }

    public void guardarFacturas() throws IOException { archivo.guardar(facturas); }
    public List<String> leerArchivo() throws IOException { return archivo.leer(); }

    public void guardarClientes() throws IOException { archivoClientes.guardar(clientes); }
    public List<String> leerArchivoClientes() throws IOException { return archivoClientes.leer(); }

    public List<Cliente> getClientes() { return Collections.unmodifiableList(clientes); }
    public List<Empleado> getEmpleados() { return Collections.unmodifiableList(empleados); }
    public List<OfertaComercial> getCatalogo() { return Collections.unmodifiableList(catalogo); }
    public List<Factura> getFacturas() { return Collections.unmodifiableList(facturas); }
}
