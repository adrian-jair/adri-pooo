package pe.edu.upeu.asistencia.repositorio;

import pe.edu.upeu.asistencia.modelo.Asistencia;
import pe.edu.upeu.asistencia.utils.ConsultaDNI;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaRepository extends ConsultaDNI {
   protected List<Asistencia> asistencias = new ArrayList<>();

    public List<Asistencia> findAll(){
        asistencias.add(new Asistencia("43631917", "EV001", LocalDateTime.now(),
                "P"));
        return asistencias;
    }
}
