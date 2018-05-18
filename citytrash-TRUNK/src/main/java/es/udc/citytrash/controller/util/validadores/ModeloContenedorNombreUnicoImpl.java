package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.ModeloContenedorNombreUnico;
import es.udc.citytrash.model.contenedorService.ContenedorService;

@Component
public class ModeloContenedorNombreUnicoImpl implements ConstraintValidator<ModeloContenedorNombreUnico, String> {

	@Autowired
	ContenedorService cServicio;

	final Logger logger = LoggerFactory.getLogger(ModeloContenedorNombreUnicoImpl.class);

	@Override
	public void initialize(ModeloContenedorNombreUnico contenedor) {
	}

	@Override
	public boolean isValid(String nombre, ConstraintValidatorContext context) {
		return !cServicio.esModeloContenedorByNombreExistente(nombre);
	}
}
