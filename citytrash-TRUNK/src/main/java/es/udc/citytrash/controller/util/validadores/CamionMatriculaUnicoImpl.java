package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.CamionMatriculaUnico;
import es.udc.citytrash.model.camionService.CamionService;

@Component
public class CamionMatriculaUnicoImpl implements ConstraintValidator<CamionMatriculaUnico, String> {

	@Autowired
	CamionService cServicio;

	final Logger logger = LoggerFactory.getLogger(CamionMatriculaUnicoImpl.class);

	@Override
	public void initialize(final CamionMatriculaUnico camion) {

	}

	@Override
	public boolean isValid(String matricula, ConstraintValidatorContext context) {
		logger.info("PASA POR AQUI => CamionMatriculaUnicoImpl PASO1");
		if (matricula.trim().length() > 0) {
			logger.info("PASA POR AQUI => CamionMatriculaUnicoImpl PASO2");
			return !cServicio.esCamionByMatriculaExistente(matricula);
		} else {
			logger.info("PASA POR AQUI => CamionMatriculaUnicoImpl PASO2");
			return true;
		}
	}
}
