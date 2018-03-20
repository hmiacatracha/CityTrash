package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.business.service.trabajador.TrabajadorService;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.controller.util.anotaciones.EmailNoDuplicado;

@Component
public class EmailNoDuplicadoImpl implements ConstraintValidator<EmailNoDuplicado, String> {

	@Autowired
	TrabajadorService tservicio;

	final Logger logger = LoggerFactory.getLogger(EmailNoDuplicadoImpl.class);

	@Override
	public void initialize(EmailNoDuplicado email) {

	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		try {
			tservicio.buscarTrabajadorEmail(email);
			logger.info("email duplicado");
			return false;
		} catch (InstanceNotFoundException e) {
			logger.info("email no encontrado");
			return true;
		}
	}
}
