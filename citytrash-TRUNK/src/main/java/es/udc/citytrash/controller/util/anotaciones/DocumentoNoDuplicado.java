package es.udc.citytrash.controller.util.anotaciones;

import static java.lang.annotation.ElementType.*;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import es.udc.citytrash.controller.util.validadores.DocumentoNoDuplicadoImpl;

/**
 * Validar que el email que se está registrando no esté asignado a otro
 * trabajador
 * 
 * @DocumentoNoDuplicado(message = {DNI/NIE Duplicado})
 * @author hmia
 *
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = es.udc.citytrash.controller.util.validadores.DocumentoNoDuplicadoImpl.class)
@Documented
public @interface DocumentoNoDuplicado {

	String message() default "{constraints.duplicated.documentoId}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
