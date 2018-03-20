package es.udc.citytrash.controller.util.anotaciones;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import es.udc.citytrash.controller.util.validadores.CamposIgualesImpl;

/**
 * Anotación de validación para validar que 2 campos tengan el mismo valor.
 *
 * Ejemplo:
 * 
 * @CamposIguales(first = "contrasena1", second = "contrasena2", message = "Las
 *                      contrasenas no coinciden")
 *
 *                      Example, Comparar más de un par
 *                      fields: @CamposIguales.List({
 * @CamposIguales(first = "contrasena", second = "contasena2", message = "Las
 *                      contrasenas no coinciden"),
 * @CamposIguales(first = "email", second = "email2", message = "Los emails no
 *                      coinciden")})
 * 
 */
@Target({ TYPE, ANNOTATION_TYPE }) //// class level constraint
@Retention(RUNTIME)
@Constraint(validatedBy = CamposIgualesImpl.class)

@Documented
public @interface CamposIguales {

	String message() default "{validador.fieldmatch}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return El primer campo
	 */
	String primerCampo();

	/**
	 * @return El segundo campo
	 */
	String segundoCampo();

	/**
	 * Define diferentes <code>@FieldMatch</code> anotaiones sobre el mismo
	 * elemento
	 * 
	 * @see CamposIguales
	 */
	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		CamposIguales[] value();
	}
}