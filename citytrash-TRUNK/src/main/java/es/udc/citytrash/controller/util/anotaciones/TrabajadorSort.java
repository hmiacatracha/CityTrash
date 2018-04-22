package es.udc.citytrash.controller.util.anotaciones;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import es.udc.citytrash.controller.util.sort.TrabajadorFieldSort;

/**
 * 
 * @author hmia
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface TrabajadorSort {

	/**
	 * Alias for {@link #sort()} to make a declaration configuring fields only
	 * more concise.
	 * 
	 * @return
	 */
	TrabajadorFieldSort[] value() default {};

	/**
	 * The properties to sort by by default. If unset, no sorting will be
	 * applied at all.
	 * 
	 * @return
	 */
	TrabajadorFieldSort[] sort() default {};

	/**
	 * The direction to sort by. Defaults to {@link Direction#ASC}.
	 * 
	 * @return
	 */
	Direction direction() default Direction.ASC;

	/**
	 * 
	 * @author hmia
	 *
	 */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface SortDefaults {

		/**
		 * The individual {@link SortDefault} declarations to be sorted by.
		 * 
		 * @return
		 */
		TrabajadorFieldSort[] value();
	}
}
