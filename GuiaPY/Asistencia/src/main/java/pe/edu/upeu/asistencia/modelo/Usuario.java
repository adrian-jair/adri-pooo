package pe.edu.upeu.asistencia.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.asistencia.enums.Perfil;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario {
    private String dni;
    private String clave;
    private Perfil perfil;
}
