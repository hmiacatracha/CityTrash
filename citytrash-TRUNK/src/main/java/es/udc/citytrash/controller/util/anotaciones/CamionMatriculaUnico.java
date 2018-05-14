package es.udc.citytrash.controller.util.anotaciones;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import es.udc.citytrash.controller.util.validadores.CamionMatriculaUnicoImpl;

/**
 * Validar que la matricula del camion no exista
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = CamionMatriculaUnicoImpl.class)
@Documented

public @interface CamionMatriculaUnico {

	String message() default "{validador.matricula.existente}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
