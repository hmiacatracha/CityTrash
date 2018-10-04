package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.ContenedorNombreUnico;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@Component
public class ContenedorNombreUnicoImpl implements ConstraintValidator<ContenedorNombreUnico, String> {

	@Autowired
	ContenedorService cServicio;

	final Logger logger = LoggerFactory.getLogger(ContenedorNombreUnicoImpl.class);

	@Override
	public void initialize(ContenedorNombreUnico contenedor) {
	}

	@Override
	public boolean isValid(String nombre, ConstraintValidatorContext context) {
		try {
			cServicio.buscarContenedorByNombre(nombre);
			return false;
		} catch (InstanceNotFoundException e) {
			return true;
		}
	}
}
