package es.udc.citytrash.controller.util.anotaciones;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import es.udc.citytrash.controller.util.validadores.CamposNoIgualesImpl;

/**
 * Anotación de validación para validar que 2 campos no tengan el mismo valor
 *
 * Ejemplo:
 * 
 * @CamposNoIguales(first = "objeto1", second = "objeto2", message = "Los
 *                        objetos no deben de coincidir") Example, Comparar más
 *                        de un par fields: @CamposDiferentes.List({
 * @CamposNoIguales(first = "objeto1", second = "objeto2", message = "Los
 *                        objetos no deben coincidir"),
 * @CamposNoIguales(first = "objeto1", second = "objeto2", message = "Los
 *                        objetos no deben de coincidir")})
 * 
 */
@Target({ TYPE, ANNOTATION_TYPE }) //// class level constraint
@Retention(RUNTIME)
@Constraint(validatedBy = CamposNoIgualesImpl.class)
@Documented
public @interface CamposNoIguales {

	String message() default "{constraints.fieldNotShouldmatch}";

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
	 * @see CamposNoIguales
	 */
	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		CamposNoIguales[] value();
	}
}