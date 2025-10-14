package pe.edu.upeu.asistencia.servicio;

import pe.edu.upeu.asistencia.dto.PersonaDto;
import pe.edu.upeu.asistencia.modelo.Asistencia;
import pe.edu.upeu.asistencia.modelo.Evento;

import java.util.List;

public interface AsistenciaServicioI  {
    List<Asistencia> findAll();
    PersonaDto consultarDNI(String dni);
    List<Asistencia> buscarXCodEvent(String codEvent);
    public void save(Asistencia asistencia);
}
