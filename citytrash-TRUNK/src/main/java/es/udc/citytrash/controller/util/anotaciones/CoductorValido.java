package es.udc.citytrash.controller.util.anotaciones;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validar si el conductor es valido
 * 
 * @RecolectorValido(message = {Recolector no valido duplicado})
 * @author hmia
 *
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = es.udc.citytrash.controller.util.validadores.ConductorValidompl.class)
@Documented
public @interface CoductorValido {

	String message() default "{validador.conductor.valido}";

	boolean allowNull() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
