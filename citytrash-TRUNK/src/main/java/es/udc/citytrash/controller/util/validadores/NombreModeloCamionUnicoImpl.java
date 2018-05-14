package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.NombreModeloCamionUnico;
import es.udc.citytrash.model.camionService.CamionService;

@Component
public class NombreModeloCamionUnicoImpl implements ConstraintValidator<NombreModeloCamionUnico, String> {

	@Autowired
	CamionService cServicio;

	final Logger logger = LoggerFactory.getLogger(NombreModeloCamionUnicoImpl.class);

	@Override
	public void initialize(NombreModeloCamionUnico camion) {
	}

	@Override
	public boolean isValid(String nombre, ConstraintValidatorContext context) {
		return !cServicio.esModeloCamionByNombreExistente(nombre);
	}
}
