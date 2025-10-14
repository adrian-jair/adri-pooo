package pe.edu.upeu.asistencia.repositorio;

import pe.edu.upeu.asistencia.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    List<Usuario> usuarios=new ArrayList<>();

    List<Usuario> findAll(){


        return usuarios;
    }

}
