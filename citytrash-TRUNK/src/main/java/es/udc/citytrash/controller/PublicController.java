package es.udc.citytrash.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.citytrash.business.service.cuenta.UserService;
import es.udc.citytrash.business.service.trabajador.TrabajadorService;
import es.udc.citytrash.business.util.excepciones.InactiveCountException;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.business.util.excepciones.TokenInvalidException;
import es.udc.citytrash.controller.util.WebUtils;

@Controller
public class PublicController {

	@Autowired
	TrabajadorService tservicio;

	@Autowired
	UserService userService;

	final Logger logger = LoggerFactory.getLogger(PublicController.class);

	/* HOME */
	@RequestMapping(value = { WebUtils.URL_HOME, WebUtils.URL_HOME1, WebUtils.URL_HOME2 }, method = RequestMethod.GET)
	public String index(@RequestParam(value = "expired", required = false) String expired,
			@RequestParam(value = "invalid", required = false) String invalid) {
		return WebUtils.VISTA_HOME;
	}

	/* ABOUT US */
	@RequestMapping(value = { WebUtils.URL_ABOUT_US }, method = RequestMethod.GET)
	public String cambiarIdioma() {
		return WebUtils.VISTA_ABOUT_US;
	}

	/* LOGIN */
	@RequestMapping(value = { WebUtils.URL_LOGIN }, method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "msg", required = false) String msg, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		/* Si el usuario está logueado enviar a la pagina home */
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "forward:" + WebUtils.URL_HOME;
		}

		logger.info("login_get_paso1");
		if (error != null && !error.isEmpty()) {
			model.addAttribute("error", error);
		} else if (msg != null && !msg.isEmpty()) {
			model.addAttribute("msg", msg);
		}
		logger.info("Mostrando pagina login");
		return WebUtils.VISTA_LOGIN;
	}

	@RequestMapping(value = "/cuenta/validarEmail", method = RequestMethod.POST)
	public @ResponseBody Boolean emailExistente(HttpServletResponse response,
			@RequestParam(value = "email", required = true) String email) {
		try {
			tservicio.buscarTrabajadorEmail(email);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(InstanceNotFoundException.class)
	public Model InstanceNotFoundException(Model model, InstanceNotFoundException ex) {
		logger.info("ExceptionHandler InstanceNotFoundException");
		model.addAttribute("error", ex);
		model.addAttribute("type", "InstanceNotFoundException");
		model.addAttribute("key", ex.getKey());
		return model;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(TokenInvalidException.class)
	public Model TokenInvalidoException(Model model, TokenInvalidException ex) {
		logger.info("ExceptionHandler TokenInvalidoException");
		model.addAttribute("error", ex);
		model.addAttribute("type", "TokenInvalidoException");
		model.addAttribute("key", ex.getKey());
		return model;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(InactiveCountException.class)
	public Model InactiveCountException(Model model, InactiveCountException ex) {
		logger.info("ExceptionHandler TokenInvalidoException");
		model.addAttribute("error", ex);
		model.addAttribute("type", "TokenInvalidoException");
		model.addAttribute("key", ex.getMessage());
		return model;
	}
}
