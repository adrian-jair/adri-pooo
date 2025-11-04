package pe.edu.cafeteria.service;

import pe.edu.cafeteria.model.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private final List<Producto> productos = new ArrayList<>();

    public List<Producto> listar() { return productos; }

    public void agregar(Producto p) { productos.add(p); }

    public void eliminar(Producto p) { productos.remove(p); }

    public void editar(int idx, Producto p) { productos.set(idx, p); }
}
