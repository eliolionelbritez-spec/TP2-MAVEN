package com.example.Consola;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import com.example.Excepciones.DatoInvalidoException;
import com.example.Excepciones.FacturaSinItemsException;
import com.example.Excepciones.LimiteCreditoExcedidoException;
import com.example.Excepciones.PagoExcedidoException;
import com.example.Gestion.Empresa;
import com.example.Humanos.Cliente;
import com.example.Humanos.Empleado;
import com.example.Pagos.Factura;
import com.example.Pagos.OfertaComercial;
import com.example.Pagos.Pago;

/** Interfaz de consola: solo pide datos y muestra resultados; la lógica vive en Empresa y el dominio. */
public class Menu {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final Empresa empresa;
    private final Scanner in = new Scanner(System.in);

    public Menu(Empresa empresa) { this.empresa = empresa; }

    public void iniciar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerInt("Opción: ");
            try {
                switch (opcion) {
                    case 1 -> registrarCliente();
                    case 2 -> listarClientes(empresa.getClientes());
                    case 3 -> nuevaFactura();
                    case 4 -> empresa.getFacturas().forEach(f -> { f.mostrarFactura(); System.out.println(); });
                    case 5 -> resumenFacturas("Facturas ordenadas por monto", empresa.facturasPorMonto());
                    case 6 -> resumenFacturas("Facturas ordenadas por fecha", empresa.facturasPorFecha());
                    case 7 -> listarClientes(empresa.clientesPorCompras());
                    case 8 -> listarClientes(empresa.clientesPorLimite());
                    case 9 -> { empresa.guardarFacturas(); empresa.guardarClientes(); System.out.println("Facturas y clientes guardados en sus archivos."); }
                    case 10 -> empresa.leerArchivo().forEach(l -> System.out.println("  " + l));
                    case 11 -> empresa.leerArchivoClientes().forEach(l -> System.out.println("  " + l));
                    case 0 -> System.out.println("Hasta luego.");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (DatoInvalidoException e) {
                System.out.println("[Dato inválido] " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Se esperaba un número.");
            } catch (NoSuchFileException e) {
                System.out.println("[Archivo inexistente] Todavía no se guardó " + e.getMessage() + ". Usá primero la opción 9.");
            } catch (IOException e) {
                System.out.println("[Error de archivo] " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void mostrarOpciones() {
        System.out.println("\n===== MENÚ =====");
        System.out.println(" 1) Registrar cliente");
        System.out.println(" 2) Listar clientes");
        System.out.println(" 3) Nueva factura");
        System.out.println(" 4) Listar facturas (detalle)");
        System.out.println(" 5) Facturas por monto (Comparable)");
        System.out.println(" 6) Facturas por fecha (Comparator)");
        System.out.println(" 7) Clientes por cantidad de compras (Comparable)");
        System.out.println(" 8) Clientes por límite de crédito (Comparator)");
        System.out.println(" 9) Guardar facturas y clientes en archivos");
        System.out.println("10) Leer archivo de facturas");
        System.out.println("11) Leer archivo de clientes");
        System.out.println(" 0) Salir");
    }

    private void registrarCliente() {
        String nombre = leer("Nombre: ");
        String domicilio = leer("Domicilio: ");
        String dni = leer("DNI (7 u 8 dígitos): ");
        String telefono = leer("Teléfono: ");
        double limite = Double.parseDouble(leer("Límite de crédito: "));
        String categoria = leer("Categoría (regular/premium/corporativo): ");
        empresa.agregarCliente(new Cliente(nombre, domicilio, dni, telefono, limite, categoria));
        System.out.println("Cliente registrado.");
    }

    private void nuevaFactura() {
        Cliente cliente = elegir("cliente", empresa.getClientes(), c -> c.getNombre() + " (" + c.getCategoria() + ")");
        if (cliente == null) return;
        Empleado empleado = elegir("empleado", empresa.getEmpleados(), e -> e.getNombre() + " - " + e.getPuesto());
        if (empleado == null) return;

        List<OfertaComercial> catalogo = empresa.getCatalogo();
        for (int i = 0; i < catalogo.size(); i++)
            System.out.println((i + 1) + ") " + catalogo.get(i).resumen() + " - $" + catalogo.get(i).calcularPrecioFinal());
        ArrayList<OfertaComercial> items = new ArrayList<>();
        int n;
        while ((n = leerInt("Agregar ítem (0 = terminar): ")) != 0) {
            if (n >= 1 && n <= catalogo.size()) items.add(catalogo.get(n - 1));
            else System.out.println("Ítem inexistente.");
        }

        String fecha = leer("Fecha dd-MM-yyyy (Enter = hoy): ");
        if (fecha.isEmpty()) fecha = LocalDate.now().format(FORMATO);
        double monto = Double.parseDouble(leer("Monto que paga ahora: "));
        String forma = leer("Forma de pago: ");
        Pago pago = new Pago("R" + (empresa.getFacturas().size() + 1), fecha, monto, forma, "Pendiente");

        try {
            Factura f = empresa.emitirFactura(fecha, cliente, empleado, items, pago);
            System.out.println("\nFactura emitida:");
            f.mostrarFactura();
        } catch (FacturaSinItemsException | PagoExcedidoException | LimiteCreditoExcedidoException e) {
            System.out.println("[No se pudo emitir] " + e.getMessage());
        }
    }

    private void listarClientes(List<Cliente> lista) {
        for (Cliente c : lista)
            System.out.println("  " + c.getNombre() + " | " + c.getCategoria() + " | límite $" + c.getLimiteCredito()
                    + " | deuda $" + c.getSaldoPendiente() + " | compras " + c.getHistorialCompras().size());
    }

    private void resumenFacturas(String titulo, List<Factura> lista) {
        System.out.println(titulo + ":");
        for (Factura f : lista)
            System.out.println("  " + f.getNumero() + " | " + f.getFecha() + " | " + f.getCliente().getNombre()
                    + " | $" + f.calcularTotal());
    }

    private <T> T elegir(String titulo, List<T> lista, Function<T, String> descripcion) {
        if (lista.isEmpty()) { System.out.println("No hay " + titulo + "s cargados."); return null; }
        for (int i = 0; i < lista.size(); i++) System.out.println((i + 1) + ") " + descripcion.apply(lista.get(i)));
        int n = leerInt("Elegí " + titulo + ": ");
        if (n < 1 || n > lista.size()) { System.out.println("Selección inválida."); return null; }
        return lista.get(n - 1);
    }

    private String leer(String mensaje) {
        System.out.print(mensaje);
        return in.hasNextLine() ? in.nextLine().trim() : "0"; // sin más entrada: cierra el menú
    }

    private int leerInt(String mensaje) {
        try { return Integer.parseInt(leer(mensaje)); }
        catch (NumberFormatException e) { return -1; }
    }
}
