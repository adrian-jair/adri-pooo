package pe.edu.upeu.asistencia.servicio;

import pe.edu.upeu.asistencia.dto.ComboBoxOption;
import pe.edu.upeu.asistencia.dto.PersonaDto;
import pe.edu.upeu.asistencia.modelo.Evento;
import pe.edu.upeu.asistencia.modelo.Participante;

import java.util.List;

public interface EventoServicioI {
    void save(Evento evento);
    List<Evento> findAll();
    Evento update(Evento evento, int index); //U
    void delete(int index); //D

    Evento findById(int index); //B
    public List<ComboBoxOption> listarCombobox();
}
