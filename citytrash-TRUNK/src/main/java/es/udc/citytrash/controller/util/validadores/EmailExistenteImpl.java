package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.business.service.trabajador.TrabajadorService;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.controller.util.anotaciones.EmailExistente;

@Component
public class EmailExistenteImpl implements ConstraintValidator<EmailExistente, String> {

	@Autowired
	TrabajadorService tservicio;

	final Logger logger = LoggerFactory.getLogger(EmailExistenteImpl.class);

	@Override
	public void initialize(EmailExistente email) {

	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		try {
			tservicio.buscarTrabajadorEmail(email);
			logger.info("email existente");
			return true;
		} catch (InstanceNotFoundException e) {
			logger.info("email no existe");
			return false;
		}
	}
}
