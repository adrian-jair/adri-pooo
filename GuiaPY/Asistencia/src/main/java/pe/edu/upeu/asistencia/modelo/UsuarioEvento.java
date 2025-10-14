package pe.edu.upeu.asistencia.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioEvento {
    private String dni;
    private String codigoEvento;
    private boolean estado;
}
