package pe.edu.upeu.asistencia.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.asistencia.modelo.UsuarioEvento;
import pe.edu.upeu.asistencia.repositorio.UsuarioEventoRepository;

import java.util.List;

@Service
public class UsuarioEventoServicioImp extends UsuarioEventoRepository implements UsuarioEventoServicioI{
    @Override
    public List<UsuarioEvento> findAll() {
        if(usuarioEventos.isEmpty()){
            return super.findAll();
        }
        return usuarioEventos;
    }
}
