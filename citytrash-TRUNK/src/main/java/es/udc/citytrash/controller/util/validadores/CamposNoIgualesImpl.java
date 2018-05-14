package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.CamposNoIguales;

import java.lang.reflect.Field;

/**
 * Los campos no deben ser iguales
 * 
 * @author hmia
 *
 */
@Component
public class CamposNoIgualesImpl implements ConstraintValidator<CamposNoIguales, Object> {

	private String primerCampo;
	private String segundoCampo;

	final Logger logger = LoggerFactory.getLogger(CamposNoIgualesImpl.class);

	public void initialize(final CamposNoIguales constraintAnnotation) {
		primerCampo = constraintAnnotation.primerCampo();
		segundoCampo = constraintAnnotation.segundoCampo();
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean toReturn = false;

		try {

			final Object primerObjeto = getFieldValue(value, primerCampo);
			final Object segundoObjeto = getFieldValue(value, segundoCampo);

			if (primerObjeto == null || segundoObjeto == null) {
				toReturn = true;
			} else if (!(primerObjeto).equals(segundoObjeto)) {
				toReturn = true;
			} else if ((primerObjeto).equals(segundoObjeto)) {
				toReturn = false;
			}

			if (!toReturn) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
						.addPropertyNode(segundoCampo).addConstraintViolation();
			}
		} catch (final Exception ignore) {
			toReturn = false;
		}
		return toReturn;
	}

	private Object getFieldValue(Object object, String fieldName) throws Exception {
		Class<?> clazz = object.getClass();
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(object);
	}
}
