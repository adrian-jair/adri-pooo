package pe.edu.upeu.asistencia.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.asistencia.enums.EstadoEvento;
import pe.edu.upeu.asistencia.enums.TipoEvento;

import java.time.LocalDate;
import java.time.LocalTime;




@AllArgsConstructor
@NoArgsConstructor
@Data
public class Evento {
    private String codigoEvento;
    private String nombreEvento;
    private LocalDate fecha;
    private LocalTime hora;
    private TipoEvento tipo;
    private EstadoEvento estado;
    private int cantRegistro;
}
