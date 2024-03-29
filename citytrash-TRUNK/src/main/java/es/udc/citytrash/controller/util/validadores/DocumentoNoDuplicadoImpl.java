package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.DocumentoNoDuplicado;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@Component
public class DocumentoNoDuplicadoImpl implements ConstraintValidator<DocumentoNoDuplicado, String> {

	@Autowired
	TrabajadorService tservicio;

	final Logger logger = LoggerFactory.getLogger(DocumentoNoDuplicadoImpl.class);

	@Override
	public void initialize(DocumentoNoDuplicado documento) {
	}

	@Override
	public boolean isValid(String documento, ConstraintValidatorContext context) {
		try {
			tservicio.buscarTrabajadorDocumento(documento);
			logger.info("Dni duplicado");
			return false;
		} catch (InstanceNotFoundException e) {
			logger.info("Dni no encontrado");
			return true;
		}
	}
}
