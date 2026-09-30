package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.Excepciones.DatoInvalidoException;
import com.example.Excepciones.FacturaSinItemsException;
import com.example.Excepciones.LimiteCreditoExcedidoException;
import com.example.Excepciones.PagoExcedidoException;
import com.example.Humanos.Cliente;
import com.example.Humanos.Empleado;
import com.example.Humanos.Persona;
import com.example.Humanos.Proveedor;
import com.example.Interfaces.Valorizable;
import com.example.Pagos.Departamento;
import com.example.Pagos.Factura;
import com.example.Pagos.OfertaComercial;
import com.example.Pagos.Pago;
import com.example.Pagos.Producto;
import com.example.Pagos.Servicio;
import com.example.Persistencia.GestorArchivo;

public class Demo {
    public static void main(String[] args) {
        // Departamento (todavía sin responsable asignado)
        Departamento departamento = new Departamento("Ventas", 100000, null);

        // Empleados
        Empleado juan = new Empleado("Juan", "Domicilio", "12345678", "1234567890",
                5000, "Administrativo", "01-01-2023", departamento);
        departamento.setResponsable(juan);
        Empleado pedro = new Empleado("Pedro", "Domicilio", "23456789", "0987654321",
                4000, "Técnico", "02-01-2023", departamento);

        // Clientes
        Cliente laura = new Cliente("Laura", "Domicilio", "34567890", "5555555555", 10000, "Regular");
        laura.agregarHistorialCompra("Compra de laptop");
        Cliente marcos = new Cliente("Marcos", "Domicilio", "45678901", "4444444444", 1000, "Premium");

        // Proveedor y sus productos/servicios
        Proveedor proveedor = new Proveedor("Proveedor X", "Domicilio", "11122233", "1111111111",
                "Proveedor de Productos", "X123");
        Producto producto1 = new Producto("P001", "Laptop", 1500, "Electrónica", proveedor);
        Producto producto2 = new Producto("P002", "Impresora", 300, "Electrónica", proveedor);
        Servicio servicio1 = new Servicio("S001", "Servicio Técnico", 500, "Servicio", proveedor);
        proveedor.agregarProductoSuministrado(producto1);
        proveedor.agregarProductoSuministrado(producto2);

        // Validación de datos
        try {
            new Cliente("Ana", "Domicilio", "DNI123", "5555555555", 1000, "Regular");
        } catch (DatoInvalidoException e) {
            System.out.println("[Dato inválido] " + e.getMessage());
        }

        // Facturas (válidas y con errores de dominio)
        List<Factura> facturas = new ArrayList<>();
        emitir(facturas, "F001", "01-09-2026", laura, pedro, items(producto1, producto2, servicio1),
                new Pago("R001", "03-01-2023", 2300, "Efectivo", "Pendiente"));
        emitir(facturas, "F002", "15-08-2026", laura, juan, items(servicio1),
                new Pago("R002", "15-08-2026", 200, "Tarjeta", "Pendiente"));
        emitir(facturas, "F003", "20-09-2026", marcos, juan, items(producto1),
                new Pago("R003", "20-09-2026", 0, "Crédito", "Pendiente"));      // supera límite de crédito
        emitir(facturas, "F004", "21-09-2026", laura, juan, items(servicio1),
                new Pago("R004", "21-09-2026", 9999, "Efectivo", "Pendiente")); // pago mayor al total
        emitir(facturas, "F005", "22-09-2026", laura, juan, new ArrayList<>(),
                new Pago("R005", "22-09-2026", 0, "Efectivo", "Pendiente"));    // sin ítems

        System.out.println("\n--- Facturas emitidas ---");
        for (Factura f : facturas) { f.mostrarFactura(); System.out.println(); }

        // Orden natural (Comparable) y alternativo (Comparator)
        Collections.sort(facturas);
        System.out.println("Facturas por monto (Comparable):");
        facturas.forEach(f -> System.out.println("  " + f.getNumero() + " $" + f.calcularTotal()));
        Collections.sort(facturas, Factura.POR_FECHA);
        System.out.println("Facturas por fecha (Comparator):");
        facturas.forEach(f -> System.out.println("  " + f.getNumero() + " " + f.getFecha()));

        List<Cliente> clientes = new ArrayList<>(List.of(laura, marcos));
        Collections.sort(clientes);
        System.out.println("Clientes por cantidad de compras (Comparable):");
        clientes.forEach(c -> System.out.println("  " + c.getNombre() + " " + c.getHistorialCompras().size()));
        Collections.sort(clientes, Cliente.POR_LIMITE_CREDITO);
        System.out.println("Clientes por límite de crédito (Comparator):");
        clientes.forEach(c -> System.out.println("  " + c.getNombre() + " $" + c.getLimiteCredito()));

        // Polimorfismo: clase abstracta Persona e interfaz Valorizable
        List<Persona> personas = List.of(juan, laura, proveedor);
        personas.forEach(p -> System.out.println("\n" + p.resumen()));
        List<Valorizable> valorizables = new ArrayList<>(facturas);
        valorizables.add(juan);
        valorizables.add(producto1);
        System.out.println("Valores (Valorizable):");
        valorizables.forEach(v -> System.out.println("  " + v.getClass().getSimpleName() + " -> $" + v.obtenerValor()));

        // Persistencia
        GestorArchivo archivo = new GestorArchivo("facturas.txt");
        try {
            archivo.guardar(facturas);
            System.out.println("\nContenido leído de facturas.txt:");
            archivo.leer().forEach(l -> System.out.println("  " + l));
        } catch (IOException e) {
            System.out.println("[Error de archivo] " + e.getMessage());
        }
    }

    private static ArrayList<OfertaComercial> items(OfertaComercial... oc) {
        ArrayList<OfertaComercial> l = new ArrayList<>();
        Collections.addAll(l, oc);
        return l;
    }

    private static void emitir(List<Factura> lista, String num, String fecha, Cliente c, Empleado e,
                               ArrayList<OfertaComercial> items, Pago p) {
        try {
            Factura f = new Factura(num, fecha, c, e, items, p);
            f.emitir();
            lista.add(f);
        } catch (FacturaSinItemsException | PagoExcedidoException | LimiteCreditoExcedidoException ex) {
            System.out.println("[Rechazada " + num + "] " + ex.getMessage());
        }
    }
}
