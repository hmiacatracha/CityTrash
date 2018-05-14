package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.CoductorValido;
import es.udc.citytrash.model.trabajador.ConductorDao;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@Component
public class ConductorValidompl implements ConstraintValidator<CoductorValido, Long> {

	@Autowired
	TrabajadorService tServicio;

	private boolean allowNull;

	final Logger logger = LoggerFactory.getLogger(RecolectorValidompl.class);

	@Override
	public void initialize(final CoductorValido recolector) {
		this.allowNull = recolector.allowNull();
	}

	@Override
	public boolean isValid(Long id, ConstraintValidatorContext context) {
		boolean valid = false;

		if (allowNull == true && id == null) {
			valid = true;
			logger.info("paso1");
		} else if (allowNull == false && id == null) {
			valid = false;
			logger.info("paso2");
		} else {
			valid = tServicio.esTrabajadorConductor(id);
		}
		return valid;
	}
}
