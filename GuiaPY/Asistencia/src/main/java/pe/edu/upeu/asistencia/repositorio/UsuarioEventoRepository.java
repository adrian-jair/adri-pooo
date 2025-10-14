package pe.edu.upeu.asistencia.repositorio;

import pe.edu.upeu.asistencia.modelo.UsuarioEvento;

import java.util.ArrayList;
import java.util.List;

public class UsuarioEventoRepository {
    public List<UsuarioEvento> usuarioEventos =new ArrayList<>();

    public List<UsuarioEvento> findAll(){
        usuarioEventos.add(new UsuarioEvento("43631917", "EV001", true));
        return usuarioEventos;
    }
}
