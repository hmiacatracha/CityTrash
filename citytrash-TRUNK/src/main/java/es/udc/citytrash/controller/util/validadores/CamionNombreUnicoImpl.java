package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.CamionNombreUnico;
import es.udc.citytrash.model.camionService.CamionService;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@Component
public class CamionNombreUnicoImpl implements ConstraintValidator<CamionNombreUnico, String> {

	@Autowired
	CamionService cServicio;

	final Logger logger = LoggerFactory.getLogger(CamionNombreUnicoImpl.class);

	@Override
	public void initialize(CamionNombreUnico camion) {
	}

	@Override
	public boolean isValid(String nombre, ConstraintValidatorContext context) {
		try {
			cServicio.buscarCamionByNombre(nombre);
			return false;
		} catch (InstanceNotFoundException e) {
			return true;
		}
	}
}
