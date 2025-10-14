package pe.edu.upeu.asistencia.repositorio;


import pe.edu.upeu.asistencia.enums.EstadoEvento;
import pe.edu.upeu.asistencia.enums.TipoEvento;
import pe.edu.upeu.asistencia.modelo.Evento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventoRepository {
    protected  List<Evento> eventos=new ArrayList<>();

    public List<Evento> findAll(){
        eventos.add(new Evento("EV001", "Conferencia Java", LocalDate.now(), LocalTime.of(10, 0), TipoEvento.CONFERENCIA, EstadoEvento.ACTIVO, 1));
        eventos.add(new Evento("EV002", "Taller Spring", LocalDate.now(), LocalTime.of(14, 0), TipoEvento.CONFERENCIA, EstadoEvento.ACTIVO, 1));
        return eventos;
    }
}
