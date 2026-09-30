package com.example.Persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.Interfaces.Exportable;

/** Única responsabilidad: leer y escribir archivos de texto. No conoce reglas del dominio. */
public class GestorArchivo {
    private final Path ruta;

    public GestorArchivo(String ruta) { this.ruta = Paths.get(ruta); }

    public void guardar(List<? extends Exportable> datos) throws IOException {
        List<String> lineas = new ArrayList<>();
        for (Exportable e : datos) lineas.add(e.aLineaTexto());
        Files.write(ruta, lineas, StandardCharsets.UTF_8);
    }

    public List<String> leer() throws IOException {
        return Files.readAllLines(ruta, StandardCharsets.UTF_8);
    }
}
