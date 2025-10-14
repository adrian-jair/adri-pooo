package pe.edu.upeu.asistencia.componente.validacion;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

// Anotación personalizada para validar el DNI de 8 dígitos
@Documented
@Constraint(validatedBy = DNIValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DNI {
    String message() default "El DNI debe tener 8 dígitos numéricos.";
    Class<?>[] groups() default {};
    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
