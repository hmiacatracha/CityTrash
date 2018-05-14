package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.CamionVinUnico;
import es.udc.citytrash.model.camionService.CamionService;

@Component
public class CamionVinUnicoImpl implements ConstraintValidator<CamionVinUnico, String> {

	@Autowired
	CamionService cServicio;

	final Logger logger = LoggerFactory.getLogger(CamionVinUnicoImpl.class);

	@Override
	public void initialize(CamionVinUnico camion) {
	}

	@Override
	public boolean isValid(String vin, ConstraintValidatorContext context) {
		return !cServicio.esCamionByVinExistente(vin);
	}
}
