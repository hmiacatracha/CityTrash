package es.udc.citytrash.controller.util.validadores;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validar que el dni que se está registrando no esté asignado a otro trabajador
 * 
 * @EmailNoDuplicado(message = {Email duplicado})
 * @author hmia
 *
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = es.udc.citytrash.controller.util.validadores.EmailExistenteImpl.class)
@Documented
public @interface EmailExistente {

	String message() default "{validador.email.existente}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
