package com.example.Humanos;

import com.example.Excepciones.DatoInvalidoException;
import com.example.Interfaces.Valorizable;
import com.example.Pagos.Departamento;

public class Empleado extends Persona implements Valorizable {
    private double salario;
    private String puesto; // administrativo, técnico o gerente
    private String fechaIngreso;
    private Departamento departamento;

    public Empleado(String nombre, String domicilio, String dni, String telefono,
                    double salario, String puesto, String fechaIngreso, Departamento departamento) {
        super(nombre, domicilio, dni, telefono);
        setSalario(salario);
        this.puesto = puesto;
        this.fechaIngreso = fechaIngreso;
        this.departamento = departamento;
    }

    @Override
    public String getRol() { return "Empleado"; }

    @Override
    public double obtenerValor() { return salario; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) {
        if (salario < 0) throw new DatoInvalidoException("El salario no puede ser negativo");
        this.salario = salario;
    }
    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
    public String getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }
}
