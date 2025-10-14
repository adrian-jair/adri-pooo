package pe.edu.upeu.asistencia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum TipoEvento {
    CONFERENCIA("Conferencia"), TALLER("Taller"), PONENCIA("Ponencia");
    private String descripcion;
}
