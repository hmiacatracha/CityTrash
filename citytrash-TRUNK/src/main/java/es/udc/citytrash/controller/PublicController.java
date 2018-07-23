package es.udc.citytrash.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import es.udc.citytrash.controller.excepciones.PageNotFoundException;
import es.udc.citytrash.controller.util.AjaxUtils;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.anotaciones.UsuarioActual;
import es.udc.citytrash.controller.util.dtos.camion.CamionFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.ruta.GenerarRutaFormDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutasDiariaFormBusq;
import es.udc.citytrash.controller.util.dtos.ruta.RutasFormBusq;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionService.CamionService;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.rutaDiaria.RutaDiaria;
import es.udc.citytrash.model.rutaService.RutaService;
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

	@Autowired
	RutaService rServicio;

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

	@RequestMapping(value = "/ajax/listaCamionesDisponibles", method = RequestMethod.GET)
	public String ajaxCamionesDisponibles(Model model, @ModelAttribute("rutaForm") RutaDto form) {
		logger.info("POST /ajax/listaCamionesDisponibles");
		RutaDto rutaForm = form;
		rutaForm.setTiposDeBasura(rutaForm.getTiposDeBasura());
		List<Contenedor> contenedores;

		if (form.getId() != null)
			contenedores = contServicio.buscarContenedoresDiponiblesParaUnaRuta(form.getId(),
					rutaForm.getTiposDeBasura());
		else
			contenedores = contServicio.buscarContenedoresDiponiblesParaUnaRuta(rutaForm.getTiposDeBasura());

		List<Camion> camiones = camServicio.buscarCamionesDisponiblesParaUnaRutaByTipos(rutaForm.getTiposDeBasura());

		logger.info("listaCamionesDisponibles contenedores  => " + form.getContenedores().toString());
		logger.info("listaCamionesDisponibles camion  => " + form.getClass().toString());
		rutaForm.setContenedores(form.getContenedores());
		rutaForm.setCamion(form.getCamion());
		model.addAttribute("listaCamionesDisponibles", camiones);
		model.addAttribute("listaContenedoresDisponibles", contenedores);
		model.addAttribute("rutaForm", rutaForm);
		// logger.info("html =>" +
		// WebUtils.VISTA_RUTA_EDITAR.concat("::fragmentoCamiones"));
		return WebUtils.VISTA_RUTA_EDITAR.concat("::fragmentoCamiones");
	}

	@RequestMapping(value = "/ajax/rutas/listaCamiones", method = RequestMethod.GET)
	public String ajaxCamiones(Model model, @ModelAttribute("busquedaForm") RutasFormBusq busquedaForm) {
		logger.info("GET /ajax/listaCamiones");
		Pageable pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
		List<Camion> camionesList = new ArrayList<Camion>();
		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		List<Ruta> RutasList = new ArrayList<Ruta>();
		Page<Ruta> page = new PageImpl<Ruta>(RutasList, pageRequest, RutasList.size());
		model.addAttribute("busquedaForm", busquedaForm);

		try {
			logger.info("GET /ajax/listaCamiones paso 2");
			camionesList = camServicio.buscarCamionesByTipos(busquedaForm.getTiposDeBasura());
			logger.info("GET /ajax/listaCamiones paso 3");
			model.addAttribute("listaCamiones", camionesList);
			logger.info("GET /ajax/listaCamiones paso 4");
			// contenedoresList =
			// contServicio.buscarContenedoresByTiposDeBasura(busquedaForm.getTiposDeBasura());
			logger.info("GET /ajax/listaCamiones paso 5");
			model.addAttribute("listaContenedores", contenedoresList);
			model.addAttribute("pageRutas", page);
			logger.info("GET /ajax/listaCamiones paso 6");
		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}

		if (page.getNumberOfElements() == 0) {
			if (!page.isFirst()) {
				throw new PageNotFoundException(
						String.format("The requested page (%s) of the contenedores list was not found.",
								pageRequest.getPageNumber()));
			}
		}
		return WebUtils.VISTA_RUTAS.concat("::listaCamiones");
	}

	@RequestMapping(value = "/ajax/rutas/listaContenedores", method = RequestMethod.GET)
	public String ajaxContenedores(Model model, @ModelAttribute("busquedaForm") RutasFormBusq busquedaForm) {
		logger.info("GET /ajax/rutas/listaContenedores");

		Pageable pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
		List<Camion> camionesList = new ArrayList<Camion>();
		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		List<Ruta> RutasList = new ArrayList<Ruta>();
		Page<Ruta> page = new PageImpl<Ruta>(RutasList, pageRequest, RutasList.size());
		model.addAttribute("busquedaForm", busquedaForm);

		try {
			logger.info("GET /ajax/rutas/listaContenedores paso 1");
			contenedoresList = contServicio.buscarContenedoresByTiposDeBasura(busquedaForm.getTiposDeBasura());
			logger.info("GET /ajax/rutas/listaContenedores paso 2");
			model.addAttribute("listaContenedores", contenedoresList);
			logger.info("GET /ajax/rutas/listaContenedores paso 3");
			logger.info("GET /ajax/rutas/listaContenedores paso 4");
			model.addAttribute("listaCamiones", camionesList);
			logger.info("GET /ajax/rutas/listaContenedores paso 5");
			model.addAttribute("pageRutas", page);
			logger.info("GET /ajax/rutas/listaContenedores paso 6");
		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}

		if (page.getNumberOfElements() == 0) {
			if (!page.isFirst()) {
				throw new PageNotFoundException(
						String.format("The requested page (%s) of the contenedores list was not found.",
								pageRequest.getPageNumber()));
			}
		}
		return WebUtils.VISTA_RUTAS.concat("::listaContenedores");
	}

	@RequestMapping(value = "/ajax/listaContenedoresDisponibles", method = RequestMethod.GET)
	public String ajaxContenedoresDisponibles(Model model, @ModelAttribute("rutaForm") RutaDto form) {
		logger.info("POST /ajax/listaContenedoresDisponibles");
		RutaDto rutaForm = form;
		rutaForm.setTiposDeBasura(rutaForm.getTiposDeBasura());
		List<Camion> camiones = camServicio.buscarCamionesDisponiblesParaUnaRutaByTipos(rutaForm.getTiposDeBasura());
		List<Contenedor> contenedores;
		if (form.getId() != null)
			contenedores = contServicio.buscarContenedoresDiponiblesParaUnaRuta(form.getId(),
					rutaForm.getTiposDeBasura());
		else
			contenedores = contServicio.buscarContenedoresDiponiblesParaUnaRuta(rutaForm.getTiposDeBasura());
		logger.info("listaCamionesDisponibles contenedores  => " + form.getContenedores().toString());
		logger.info("listaCamionesDisponibles camion  => " + form.getClass().toString());
		rutaForm.setContenedores(form.getContenedores());
		rutaForm.setCamion(form.getCamion());
		model.addAttribute("listaCamionesDisponibles", camiones);
		model.addAttribute("listaContenedoresDisponibles", contenedores);
		model.addAttribute("rutaForm", rutaForm);
		// logger.info("html =>" +
		// WebUtils.VISTA_RUTA_EDITAR.concat("::fragmentoContenedores"));
		return WebUtils.VISTA_RUTA_EDITAR.concat(" ::fragmentoContenedores");
	}

	@RequestMapping(value = "/ajax/contenedores/listaModelosContenedores", method = RequestMethod.GET)
	public String ajaxModelosContenedores(Model model, @ModelAttribute("busquedaForm") ContenedorFormBusq form) {
		Pageable pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
		logger.info("GET listaModelosContenedores");
		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		Page<Contenedor> page = new PageImpl<Contenedor>(contenedoresList, pageRequest, contenedoresList.size());

		try {
			List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
			modelos = contServicio.buscarTodosLosModelosOrderByModelo(form.getTiposDeBasura());
			model.addAttribute("pageContenedores", page);
			model.addAttribute("todosLosModelos", modelos);
			model.addAttribute("busquedaForm", form);

			if (page.getNumberOfElements() == 0) {
				if (!page.isFirst()) {
					logger.info("PageNotFoundException");
					throw new PageNotFoundException(String.format(
							"The requested page (%s) of the worker list was not found.", pageRequest.getPageNumber()));
				}
			}

		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}

		return WebUtils.VISTA_CONTENEDORES.concat("::fragmentoModelos");
	}

	@RequestMapping(value = "/ajax/camiones/listaModelosCamiones", method = RequestMethod.GET)
	public String ajaxModelosCamiones(Model model, @ModelAttribute("busquedaForm") CamionFormBusq form) {
		// citytrash/ajax/camiones/listaModelosCamiones
		Pageable pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
		logger.info("GET listaModelosCamiones");
		List<Camion> camionesList = new ArrayList<Camion>();
		Page<Camion> page = new PageImpl<Camion>(camionesList, pageRequest, camionesList.size());
		try {
			List<CamionModelo> modelos = new ArrayList<CamionModelo>();
			modelos = camServicio.buscarTodosLosModelosOrderByModelo(form.getTipos());
			model.addAttribute("pageContenedores", page);
			model.addAttribute("todosLosModelos", modelos);
			model.addAttribute("busquedaForm", form);
			if (page.getNumberOfElements() == 0) {
				if (!page.isFirst()) {
					logger.info("PageNotFoundException");
					throw new PageNotFoundException(String.format(
							"The requested page (%s) of the worker list was not found.", pageRequest.getPageNumber()));
				}
			}
		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}
		return WebUtils.VISTA_CONTENEDORES.concat("::fragmentoModelos");
	}

	/**
	 * Lista de camiones a partir del filtro de tipos de basura, sino devuelve
	 * todos los camiones.
	 * 
	 * @param model
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/ajax/rutas/generar/listaCamiones", method = RequestMethod.GET)
	public String ajaxGenerarRUtasListaDeCamiones(Model model,
			@ModelAttribute("generarRutaForm") GenerarRutaFormDto form) {
		logger.info("POST /ajax/listaCamionesDisponibles");
		Pageable pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
		List<RutaDiaria> RutasDiariaList = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(RutasDiariaList, pageRequest, RutasDiariaList.size());
		List<Camion> listaTodosLosCamiones = camServicio.buscarCamionesByTipos(form.getTiposDeBasura());
		List<Ruta> listaRutasAGenerar = new ArrayList<Ruta>();
		model.addAttribute("generarRutaForm", form);
		model.addAttribute("listaTodosLosCamiones", listaTodosLosCamiones);
		model.addAttribute("listaRutasAGenerar", listaRutasAGenerar);
		model.addAttribute("pageRutas", page);
		return WebUtils.VISTA_RUTA_GENERAR.concat("::fragmentoCamiones");
	}

	@RequestMapping(value = "/ajax/rutas/generar/listaRutas", method = RequestMethod.GET)
	public String ajaxGenerarRutasListadoDeRutas(Model model,
			@ModelAttribute("generarRutaForm") GenerarRutaFormDto formulario) {
		logger.info("POST /ajax/listaCamionesDisponibles");
		Pageable pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "id");
		List<RutaDiaria> RutasDiariaList = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(RutasDiariaList, pageRequest, RutasDiariaList.size());

		GenerarRutaFormDto form = formulario;
		List<Ruta> listaRutasAGenerar = rServicio.buscarRutasSinGenerar(form);
		RutasDiariaFormBusq busquedaForm = new RutasDiariaFormBusq(form.getFecha(), form.getFecha(), null, null, null,
				null);
		page = rServicio.buscarRutasDiarias(pageRequest, busquedaForm);
		model.addAttribute("generarRutaForm", form);
		model.addAttribute("listaRutasAGenerar", listaRutasAGenerar);
		model.addAttribute("pageRutas", page);
		return WebUtils.VISTA_RUTA_GENERAR.concat("::fragmentoRutas");
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
