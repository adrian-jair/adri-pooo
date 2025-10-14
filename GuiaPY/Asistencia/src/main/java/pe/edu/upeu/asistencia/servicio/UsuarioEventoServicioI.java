package pe.edu.upeu.asistencia.servicio;


import pe.edu.upeu.asistencia.modelo.UsuarioEvento;

import java.util.List;

public interface UsuarioEventoServicioI {

    List<UsuarioEvento> findAll();
}
