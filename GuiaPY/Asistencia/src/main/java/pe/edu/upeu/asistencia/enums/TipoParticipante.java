package pe.edu.upeu.asistencia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TipoParticipante {
    ASISTENTE("Asistente"), ORGANIZADOR("Organizador"), PONENTE("Ponente");

    private String descripcion;

    @Override
    public String toString() {
        return descripcion;
    }
}
