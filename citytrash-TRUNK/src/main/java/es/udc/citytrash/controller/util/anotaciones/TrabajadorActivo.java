package es.udc.citytrash.controller.util.anotaciones;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import es.udc.citytrash.controller.util.validadores.EmailExistenteImpl;

/**
 * validar si el trabajador esta activo
 * 
 * @author hmia
 *
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = es.udc.citytrash.controller.util.validadores.TrabajadorActivompl.class)
@Documented
public @interface TrabajadorActivo {

	String message() default "{validador.trabajador.inactivo}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
