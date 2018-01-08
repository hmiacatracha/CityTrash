package es.udc.citytrash.controller.trabajador;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import es.udc.citytrash.business.service.trabajador.TrabajadorService;
import es.udc.citytrash.business.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.formularios.TrabajadorForm;

@Controller
@RequestMapping("auth")
public class TrabajadorController {

	private static final String VISTA_PREFIX = "trabajador/";
	private static final String VISTA_REGISTRO = "registro";
	private static final String VISTA_REGISTRO_OK = "home";

	private static final String URL_REGISTRO_ES = "/admin/trabajadores/registro";

	@Autowired
	TrabajadorService tservicio;

	final Logger logger = LoggerFactory.getLogger(TrabajadorController.class);

	/**
	 * Register page.
	 */

	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	// @Secured({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping(value = URL_REGISTRO_ES, method = RequestMethod.GET)
	public String registro(WebRequest request, Model model) {
		logger.debug("Showing register page GET");
		TrabajadorForm registro = new TrabajadorForm();
		model.addAttribute("registro", registro);
		return VISTA_PREFIX + VISTA_REGISTRO;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = URL_REGISTRO_ES, method = RequestMethod.POST)
	public String registro(@ModelAttribute("registro") @Valid TrabajadorForm user, BindingResult result,
			HttpServletRequest request, Errors errors, Model model) {
		logger.info("Showing register page POST: " + request.getContextPath());
		logger.debug("Register an user");
		logger.debug("tipo de trabajador: " + user.getTipo().toString());
		if (result.hasErrors()) {
			return VISTA_PREFIX + VISTA_REGISTRO;
		}

		try {
			tservicio.registrar(user, WebUtils.getURLWithContextPath(request));
		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			logger.info(model.toString());
			return VISTA_PREFIX + VISTA_REGISTRO;
		}
		return "redirect:/" + VISTA_REGISTRO_OK;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(DuplicateInstanceException.class)
	public Model duplicateInstanceException(Model model, DuplicateInstanceException ex) {
		model.addAttribute("error", ex);
		model.addAttribute("type", "DuplicateInstanceExceptionRegister");
		model.addAttribute("key", ex.getKey());
		return model;
	}

}
