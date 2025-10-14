package pe.edu.upeu.asistencia.modelo;

import jakarta.validation.constraints.NotBlank;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import lombok.*;
import pe.edu.upeu.asistencia.componente.validacion.DNI;
import pe.edu.upeu.asistencia.enums.Carrera;
import pe.edu.upeu.asistencia.enums.TipoParticipante;

@AllArgsConstructor
@NoArgsConstructor
//@Data
@Setter
@Getter
public class Participante {
    @NotBlank(message = "El DNI no puede estar vacío.")
    @DNI
    private StringProperty dni;
    @NotBlank(message = "El nombre no puede estar vacío.")
    private StringProperty nombre;
    @NotBlank(message = "Apellidos no puede estar vacío.")
    private StringProperty apellidos;
    private Carrera carrera;
    private TipoParticipante tipoParticipante;
    private BooleanProperty estado= new SimpleBooleanProperty(true);

}
