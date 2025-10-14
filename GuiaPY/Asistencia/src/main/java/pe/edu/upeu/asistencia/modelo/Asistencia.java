package pe.edu.upeu.asistencia.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Asistencia {
    private String dni;
    private String codigoEvento;
    private LocalDateTime fechaHora;
    private String presente;
}
