package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.udc.citytrash.controller.util.anotaciones.CamposIguales;

import java.lang.reflect.Field;

public class CamposIgualesImpl implements ConstraintValidator<CamposIguales, Object> {

	private String primerCampo;
	private String segundoCampo;

	final Logger logger = LoggerFactory.getLogger(CamposIgualesImpl.class);

	public void initialize(final CamposIguales constraintAnnotation) {
		primerCampo = constraintAnnotation.primerCampo();
		segundoCampo = constraintAnnotation.segundoCampo();
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean toReturn = false;

		try {

			final Object primerObjeto = getFieldValue(value, primerCampo);
			final Object segundoObjeto = getFieldValue(value, segundoCampo);

			logger.info("primerCampo: " + primerObjeto.toString());
			logger.info("segungiCampo: " + segundoObjeto.toString());

			toReturn = primerObjeto != null && primerObjeto.equals(segundoObjeto);

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
		Field email = clazz.getDeclaredField(fieldName);
		email.setAccessible(true);
		return email.get(object);
	}
}
