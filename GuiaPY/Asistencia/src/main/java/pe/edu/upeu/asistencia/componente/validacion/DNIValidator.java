package pe.edu.upeu.asistencia.componente.validacion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DNIValidator implements ConstraintValidator<DNI, String> {
    public DNIValidator() {
        // Un constructor público sin argumentos es requerido por el framework
    }
    @Override
    public void initialize(DNI constraintAnnotation) {
        // Opcional: para inicializar el validador con valores de la anotación
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // La validación @NotBlank se encargará de esto
        }
        return value.matches("\\d{8}");
    }
}
