package es.udc.citytrash.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import es.udc.citytrash.controller.cuenta.CustomUserDetails;
import es.udc.citytrash.controller.util.AjaxUtils;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.anotaciones.UsuarioActual;
import es.udc.citytrash.controller.util.dtos.ruta.RutaDto;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camionService.CamionService;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.usuarioService.UsuarioService;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.util.enums.Idioma;

@Controller
public class PublicController {

	@Autowired
	TrabajadorService tservicio;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	ContenedorService contServicio;

	@Autowired
	CamionService camServicio;

	final Logger logger = LoggerFactory.getLogger(PublicController.class);

	/* HOME */
	@RequestMapping(value = { WebUtils.URL_HOME, WebUtils.URL_HOME1, WebUtils.URL_HOME2 }, method = RequestMethod.GET)
	public String index(@UsuarioActual CustomUserDetails usuario, HttpServletRequest request,
			HttpServletResponse response) {

		/* Usuario autenticado */
		if (usuario != null) {
			String lang = request.getLocale().toLanguageTag();
			Idioma idioma;
			try {
				idioma = usuarioService.obtenerIdiomaPreferencia(usuario.getPerfil().getId());
			} catch (es.udc.citytrash.model.util.excepciones.InstanceNotFoundException e) {
				idioma = Idioma.es;
			}
			lang = idioma.toString().toLowerCase();
			RequestContextUtils.getLocaleResolver(request).setLocale(request, response, new Locale(lang));
			logger.info("HOME IDIOMA lang =>" + lang);
		}
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
			tservicio.buscarTrabajadorByEmail(email);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}
	//
	// @RequestMapping(value = "/ajax/listaCamionesDisponibles", method =
	// RequestMethod.POST)
	// public String ajaxCamionesDisponibles(Model model,
	// @ModelAttribute("rutaForm") RutaDto form) {
	// logger.info("POST /ajax/listaCamionesDisponibles");
	// RutaDto rutaForm = form;
	// rutaForm.setTiposDeBasura(rutaForm.getTiposDeBasura());
	// List<Contenedor> contenedores = contServicio
	// .buscarContenedoresDiponiblesParaUnaRuta(rutaForm.getTiposDeBasura());
	// List<Camion> camiones =
	// camServicio.buscarCamionesDisponiblesParaUnaRutaByTipos(rutaForm.getTiposDeBasura());
	//
	// model.addAttribute("listaCamionesDisponibles", camiones);
	// model.addAttribute("listaContenedoresDisponibles", contenedores);
	// model.addAttribute("rutaForm", rutaForm);
	// // logger.info("html =>" +
	// // WebUtils.VISTA_RUTA_EDITAR.concat("::fragmentoCamiones"));
	// return WebUtils.VISTA_RUTA_EDITAR.concat("::fragmentoCamiones");
	// }
	//
	// @RequestMapping(value = "/ajax/listaContenedoresDisponibles", method =
	// RequestMethod.POST)
	// public String ajaxContenedoresDisponibles(Model model,
	// @ModelAttribute("rutaForm") RutaDto form) {
	// logger.info("POST /ajax/listaContenedoresDisponibles");
	// RutaDto rutaForm = form;
	// rutaForm.setTiposDeBasura(rutaForm.getTiposDeBasura());
	// List<Camion> camiones =
	// camServicio.buscarCamionesDisponiblesParaUnaRutaByTipos(rutaForm.getTiposDeBasura());
	// List<Contenedor> contenedores = contServicio
	// .buscarContenedoresDiponiblesParaUnaRuta(rutaForm.getTiposDeBasura());
	//
	// model.addAttribute("listaCamionesDisponibles", camiones);
	// model.addAttribute("listaContenedoresDisponibles", contenedores);
	// model.addAttribute("rutaForm", rutaForm);
	// // logger.info("html =>" +
	// // WebUtils.VISTA_RUTA_EDITAR.concat("::fragmentoContenedores"));
	// return WebUtils.VISTA_RUTA_EDITAR.concat(" ::fragmentoContenedores");
	// }

	@RequestMapping(value = "/ajax/listaCamionesDisponibles", method = RequestMethod.GET)
	public String ajaxCamionesDisponibles(Model model, @ModelAttribute("rutaForm") RutaDto form) {
		logger.info("POST /ajax/listaCamionesDisponibles");
		RutaDto rutaForm = form;
		rutaForm.setTiposDeBasura(rutaForm.getTiposDeBasura());
		List<Contenedor> contenedores = contServicio
				.buscarContenedoresDiponiblesParaUnaRuta(rutaForm.getTiposDeBasura());
		List<Camion> camiones = camServicio.buscarCamionesDisponiblesParaUnaRutaByTipos(rutaForm.getTiposDeBasura());

		model.addAttribute("listaCamionesDisponibles", camiones);
		model.addAttribute("listaContenedoresDisponibles", contenedores);
		model.addAttribute("rutaForm", rutaForm);
		// logger.info("html =>" +
		// WebUtils.VISTA_RUTA_EDITAR.concat("::fragmentoCamiones"));
		return WebUtils.VISTA_RUTA_EDITAR.concat("::fragmentoCamiones");
	}

	@RequestMapping(value = "/ajax/listaContenedoresDisponibles", method = RequestMethod.GET)
	public String ajaxContenedoresDisponibles(Model model, @ModelAttribute("rutaForm") RutaDto form) {
		logger.info("POST /ajax/listaContenedoresDisponibles");
		RutaDto rutaForm = form;
		rutaForm.setTiposDeBasura(rutaForm.getTiposDeBasura());
		List<Camion> camiones = camServicio.buscarCamionesDisponiblesParaUnaRutaByTipos(rutaForm.getTiposDeBasura());
		List<Contenedor> contenedores = contServicio
				.buscarContenedoresDiponiblesParaUnaRuta(rutaForm.getTiposDeBasura());

		model.addAttribute("listaCamionesDisponibles", camiones);
		model.addAttribute("listaContenedoresDisponibles", contenedores);
		model.addAttribute("rutaForm", rutaForm);
		// logger.info("html =>" +
		// WebUtils.VISTA_RUTA_EDITAR.concat("::fragmentoContenedores"));
		return WebUtils.VISTA_RUTA_EDITAR.concat(" ::fragmentoContenedores");
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
	@ExceptionHandler(DisabledException.class)
	public Model InactiveCountException(Model model, DisabledException ex) {
		logger.info("ExceptionHandler DisabledException");
		model.addAttribute("error", ex);
		model.addAttribute("type", "DisabledException");
		model.addAttribute("key", ex.getMessage());
		return model;
	}

}
