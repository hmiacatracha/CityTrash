package es.udc.citytrash.controller.util.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.util.anotaciones.TrabajadorActivo;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@Component
public class TrabajadorActivompl implements ConstraintValidator<TrabajadorActivo, Long> {

	@Autowired
	TrabajadorService tservicio;

	final Logger logger = LoggerFactory.getLogger(TrabajadorActivompl.class);


	@Override
	public void initialize(final TrabajadorActivo trabajador) {
		
	}

	@Override
	public boolean isValid(Long id, ConstraintValidatorContext context) {
		boolean respuesta = false;
		Trabajador trabajador = null;

		try {
			if (id == null) {
				respuesta = true;
			} else {
				trabajador = tservicio.buscarTrabajador(id);
				if (trabajador.isActiveWorker())
					respuesta = true;
				logger.info("trabajadorInactivo paso3 trabajador id=> " + id);
				logger.info("trabajadorInactivo paso4 trabajador respuesta=> " + respuesta);
			}
			return respuesta;
		} catch (InstanceNotFoundException e) {
			logger.info("trabajadorInactivo paso5 trabajador => " + id);
			respuesta = false;
		}
		return respuesta;
	}
}
