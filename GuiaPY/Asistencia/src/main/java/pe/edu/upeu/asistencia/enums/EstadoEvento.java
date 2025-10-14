package pe.edu.upeu.asistencia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum EstadoEvento {
    ACTIVO("Activo"), INACTIVO("Inactivo");
    private String descripcion;
}
