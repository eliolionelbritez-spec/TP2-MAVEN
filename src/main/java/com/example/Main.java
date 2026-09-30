package com.example;

import com.example.Consola.Menu;
import com.example.Gestion.Empresa;
import com.example.Humanos.Cliente;
import com.example.Humanos.Empleado;
import com.example.Humanos.Proveedor;
import com.example.Pagos.Departamento;
import com.example.Pagos.Producto;
import com.example.Pagos.Servicio;

/** Solo instancia los objetos iniciales y lanza el menú. */
public class Main {
    public static void main(String[] args) {
        Empresa empresa = new Empresa("facturas.txt", "clientes.txt");

        Departamento departamento = new Departamento("Ventas", 100000, null);
        Empleado juan = new Empleado("Juan", "Domicilio", "12345678", "1234567890",
                5000, "Administrativo", "01-01-2023", departamento);
        departamento.setResponsable(juan);
        Empleado pedro = new Empleado("Pedro", "Domicilio", "23456789", "0987654321",
                4000, "Técnico", "02-01-2023", departamento);

        Cliente laura = new Cliente("Laura", "Domicilio", "34567890", "5555555555", 10000, "Regular");
        laura.agregarHistorialCompra("Compra de laptop");
        Cliente marcos = new Cliente("Marcos", "Domicilio", "45678901", "4444444444", 1000, "Premium");

        Proveedor proveedor = new Proveedor("Proveedor X", "Domicilio", "11122233", "1111111111",
                "Proveedor de Productos", "X123");
        Producto laptop = new Producto("P001", "Laptop", 1500, "Electrónica", proveedor);
        Producto impresora = new Producto("P002", "Impresora", 300, "Electrónica", proveedor);
        Servicio soporte = new Servicio("S001", "Servicio Técnico", 500, "Servicio", proveedor);
        proveedor.agregarProductoSuministrado(laptop);
        proveedor.agregarProductoSuministrado(impresora);

        empresa.agregarEmpleado(juan);
        empresa.agregarEmpleado(pedro);
        empresa.agregarCliente(laura);
        empresa.agregarCliente(marcos);
        empresa.agregarOferta(laptop);
        empresa.agregarOferta(impresora);
        empresa.agregarOferta(soporte);

        new Menu(empresa).iniciar();
    }
}
