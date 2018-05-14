package es.udc.citytrash.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.usuarioService.UsuarioService;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@Controller
public class PublicRestController {

	/*
	 * http://www.jtech.ua.es/j2ee/publico/spring-2012-13/sesion04-apuntes.html
	 */

	@Autowired
	TrabajadorService tservicio;

	@Autowired
	UsuarioService usuarioService;

	final Logger logger = LoggerFactory.getLogger(PublicRestController.class);

	@RequestMapping(value = "/cuenta/validarEmail", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Boolean validarEmail(HttpServletResponse response,
			@RequestParam(value = "email", required = true) String email) {
		logger.info("buscando trabajador por email");
		try {
			tservicio.buscarTrabajadorByEmail(email);
			logger.info("buscando trabajador por email => si");
			logger.info("buscando trabajador por email = " + email);
			return false;
		} catch (InstanceNotFoundException e) {
			logger.info("buscando trabajador por email => no");
			return true;
		}
	}

	@RequestMapping(value = "/cuenta/validarDocumento", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Boolean validarDocumento(HttpServletResponse response,
			@RequestParam(value = "doc", required = true) String documento) {
		try {
			tservicio.buscarTrabajadorDocumento(documento);
			return false;
		} catch (InstanceNotFoundException e) {
			return true;
		}
	}
}
