package pe.edu.upeu.asistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.asistencia.enums.Carrera;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AsistenciaDetalleDto {
    private String dni;
    private String nombreParticipante;
    private String apellidosParticipante;
    private Carrera carrera;
    private String codigoEvento;
    private String nombreEvento;
    private LocalDateTime fechaHora;
    private boolean presente;

    /*@Override
    public String toString(){

        return nombreEstudiante;
    }*/
}
