package pe.edu.upeu.sysventas.service;

import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.dto.ModeloDataAutocomplet;
import pe.edu.upeu.sysventas.model.CompCarrito;

import java.util.List;

@Service
public interface ICompCarritoService extends ICrudGenericoService<CompCarrito, Long> {
    List<ModeloDataAutocomplet> listAutoCompletCliente();
}
