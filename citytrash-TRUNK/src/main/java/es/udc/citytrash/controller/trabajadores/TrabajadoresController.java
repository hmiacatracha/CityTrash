package es.udc.citytrash.controller.trabajadores;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.udc.citytrash.controller.excepciones.EmployeeNotFoundException;
import es.udc.citytrash.controller.excepciones.PageNotFoundException;
import es.udc.citytrash.controller.excepciones.ResourceNotFoundException;
import es.udc.citytrash.controller.util.AjaxUtils;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.anotaciones.UsuarioActual;
import es.udc.citytrash.controller.util.dtos.cuenta.PerfilDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorBusqFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorRegistroFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorUpdateFormDto;
import es.udc.citytrash.model.emailService.EmailNotificacionesService;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.util.enums.CampoBusqTrabajador;
import es.udc.citytrash.util.enums.Idioma;
import es.udc.citytrash.util.enums.TipoTrabajador;

import es.udc.citytrash.util.GlobalNames;

@Controller
// @PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
@RequestMapping("trabajadores")
public class TrabajadoresController {

	@Autowired
	TrabajadorService tservicio;

	@Autowired
	EmailNotificacionesService emailService;

	final Logger logger = LoggerFactory.getLogger(TrabajadoresController.class);

	/* TRABAJADORES */

	/* Formulario de búsqueda trabajadores */
	@ModelAttribute("busquedaTrabajadoresForm")
	public TrabajadorBusqFormDto busquedaTrabajadoresForm() {
		return new TrabajadorBusqFormDto();
	}

	/* Lista de trabajadores por defecto */
	@ModelAttribute("listaTrabajadores")
	public Page<TrabajadorDto> listaTrabajadoresPage(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest) {
		List<TrabajadorDto> trabajadoresPage = new ArrayList<TrabajadorDto>();
		Page<TrabajadorDto> page = new PageImpl<TrabajadorDto>(trabajadoresPage, pageRequest, trabajadoresPage.size());
		return page;
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_TRABAJADORES, "" }, method = RequestMethod.GET)
	public String listaTrabajadoresGet(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "buscar", required = false, defaultValue = "") String search,
			@RequestParam(value = "campo", required = false, defaultValue = "documento") CampoBusqTrabajador campo,
			@RequestParam(value = "tipo", required = false, defaultValue = "NONE") TipoTrabajador tipo, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		TrabajadorBusqFormDto busquedaForm = new TrabajadorBusqFormDto();
		busquedaForm.setBuscar(search);
		busquedaForm.setCampo(campo.toString());
		busquedaForm.setTipo(tipo.toString());
		model.addAttribute("busquedaTrabajadoresForm", busquedaForm);

		Page<Trabajador> trabajadoresPage;
		Page<TrabajadorDto> page;
		try {
			trabajadoresPage = tservicio.buscarTrabajadores(pageRequest, busquedaForm);
		} catch (FormBusquedaException e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}

		page = trabajadoresPage.map(new Converter<Trabajador, TrabajadorDto>() {
			@Override
			public TrabajadorDto convert(Trabajador t) {
				TrabajadorDto dto = new TrabajadorDto(t);
				return dto;
			}
		});

		model.addAttribute("listaTrabajadores", page);
		if (page.getNumberOfElements() == 0) {
			if (!page.isFirst()) {
				throw new PageNotFoundException(String.format(
						"The requested page (%s) of the worker list was not found.", pageRequest.getPageNumber()));
			}
		}

		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return WebUtils.VISTA_TRABAJADORES.concat("::content");
		}
		return WebUtils.VISTA_TRABAJADORES;
	}

	@RequestMapping(value = { "", WebUtils.REQUEST_MAPPING_TRABAJADORES }, method = RequestMethod.POST)
	public String listaTrabajadoresPost(@RequestParam(value = "page", required = false, defaultValue = "0") Integer p,
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			Model model, @Valid TrabajadorBusqFormDto busquedaForm, BindingResult result,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		logger.info("POST FROM TRABAJADORES");
		model.addAttribute("busquedaTrabajadoresForm", busquedaForm);

		if (result.hasErrors()) {
			logger.info("ERRORS: => " + result.getAllErrors().toString());
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_TRABAJADORES.concat("::content");
			}
			logger.info("POST LISTA_TRABAJADORES has errores no ajax");
			return WebUtils.VISTA_TRABAJADORES;
		}

		Page<Trabajador> trabajadoresPage;

		try {
			trabajadoresPage = tservicio.buscarTrabajadores(pageRequest, busquedaForm);

		} catch (FormBusquedaException e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}

		Page<TrabajadorDto> page = trabajadoresPage.map(new Converter<Trabajador, TrabajadorDto>() {
			@Override
			public TrabajadorDto convert(Trabajador t) {
				// return ConvertidorTrabajador.trabajadorToDto(t);
				TrabajadorDto dto = new TrabajadorDto(t);
				return dto;
			}
		});

		if (page.getNumberOfElements() == 0) {
			if (!page.isFirst()) {
				logger.info("PageNotFoundException");
				throw new PageNotFoundException(String.format(
						"The requested page (%s) of the worker list was not found.", pageRequest.getPageNumber()));
			}
		}

		model.addAttribute("listaTrabajadores", page);
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			logger.info("resultado con ajax");
			return WebUtils.VISTA_TRABAJADORES.concat("::listaTrabajadoresContent");
		}
		logger.info("sin resultado");
		return WebUtils.VISTA_TRABAJADORES;
	}

	/* Detalles del trabajador */

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_TRABAJADOR_DETALLES,
			WebUtils.REQUEST_MAPPING_TRABAJADOR_DETALLES + "/" }, method = RequestMethod.GET)
	public String trabajadorDetalles(@PathVariable("trabajadorId") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws EmployeeNotFoundException {
		String nombre = "";

		try {
			logger.info("Showing details of a worker GET");
			TrabajadorDto t = new TrabajadorDto(tservicio.buscarTrabajador(id));
			logger.info("Showing details of a worker trabajador =>" + t.toString());
			nombre = t.getNombre() + " " + t.getApellidos();
			model.addAttribute("trabajador_nombre_completo", nombre);
			model.addAttribute("trabajadorId", t.getId());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADOR_DETALLES.concat("::content");
			return WebUtils.VISTA_TRABAJADOR_DETALLES;

		} catch (InstanceNotFoundException e) {
			throw new EmployeeNotFoundException(id);
		}
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_TRABAJADOR_INFO_PERSONAL }, method = RequestMethod.GET)
	// @ResponseBody
	public String trabajadorInformacionPersonal(@PathVariable("trabajadorId") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws EmployeeNotFoundException {
		try {
			TrabajadorDto t = new TrabajadorDto(tservicio.buscarTrabajador(id));
			model.addAttribute("trabajador", t);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADOR_INFORMACION_PERSONAL.concat("::content");
			return WebUtils.VISTA_TRABAJADOR_INFORMACION_PERSONAL;

		} catch (InstanceNotFoundException e) {
			throw new EmployeeNotFoundException(id);
		}
	}

	/* detalles del trabajador */
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_TRABAJADOR_INFO_CUENTA }, method = RequestMethod.GET)
	public String trabajadorInformacionCuenta(@PathVariable("trabajadorId") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws EmployeeNotFoundException {
		try {
			TrabajadorDto t = new TrabajadorDto(tservicio.buscarTrabajador(id));
			model.addAttribute("trabajador", t);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADOR_INFORMACION_CUENTA.concat("::content");
			return WebUtils.VISTA_TRABAJADOR_INFORMACION_CUENTA;
		} catch (InstanceNotFoundException e) {
			throw new EmployeeNotFoundException(id);
		}
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_TRABAJADOR_RUTAS }, method = RequestMethod.GET)
	public String trabajadorRutas(@PathVariable("trabajadorId") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws EmployeeNotFoundException {
		Trabajador t;
		try {
			t = tservicio.buscarTrabajador(id);
			model.addAttribute("trabajador", t);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADOR_RUTAS.concat("::content");
			return WebUtils.VISTA_TRABAJADOR_RUTAS;

		} catch (InstanceNotFoundException e) {
			throw new EmployeeNotFoundException(id);
		}
	}

	/**
	 * Register form
	 */
	@ModelAttribute("registro")
	public TrabajadorRegistroFormDto registroForm() {
		return new TrabajadorRegistroFormDto();
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_TRABAJADORES_REGISTRO, method = RequestMethod.GET)
	public String registro(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_TRABAJADORES_REGISTRO.concat(" :: registroForm");
		return WebUtils.VISTA_TRABAJADORES_REGISTRO;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_TRABAJADORES_REGISTRO, method = RequestMethod.POST)
	public String registro(@ModelAttribute("registro") @Valid TrabajadorRegistroFormDto user, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, Errors errors, Model model,
			RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		Trabajador t;
		if (result.hasErrors()) {
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_TRABAJADORES_REGISTRO.concat("::registroForm");
			}
			return WebUtils.VISTA_TRABAJADORES_REGISTRO;
		}
		try {

			/* Registramos al usuario */
			t = tservicio.registrar(user);

			/* Enviamos el correo electronico sobre el registro del usuario */
			if (t != null)
				emailService.activacionCuentaEmail(t.getId(), t.getNombre(), t.getApellidos(), t.getEmail(),
						t.getToken(), t.getFechaExpiracionToken(), user.getIdioma(),
						WebUtils.getUrlWithContextPath(request));

		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			logger.info(model.toString());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADORES_REGISTRO.concat("::registroForm");
			return WebUtils.VISTA_TRABAJADORES_REGISTRO;
		} catch (Exception e) {
			logger.debug("error register a user => " + e.toString());
			return WebUtils.VISTA_TRABAJADORES;
		}

		redirectAttributes.addAttribute("msg", "ok");
		redirectAttributes.addAttribute("type", "frm_registro_ok");
		response.setHeader("X-Requested-With", requestedWith);

		/*
		 * if (AjaxUtils.isAjaxRequest(requestedWith)) {
		 * model.addAttribute("msg", "ok"); model.addAttribute("type",
		 * "frm_registro_ok"); model.addAttribute("key",
		 * UriComponentsBuilder.fromUriString(WebUtils.URL_TRABAJADOR_DETALLES)
		 * .queryParam("trabajadorId", t.getId())); return
		 * WebUtils.VISTA_TRABAJADORES.concat("::content"); }
		 */
		return "redirect:" + WebUtils.URL_TRABAJADOR_UPDATE.replace("{trabajadorId}", String.valueOf(t.getId()));
	}

	/* UPDATE A WORKER */
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_TRABAJADOR_UPDATE }, method = RequestMethod.GET)
	public String updateTrabajador(@PathVariable("trabajadorId") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@RequestParam(value = "msg", required = false) String msg,
			@RequestParam(value = "type", required = false) String type) throws EmployeeNotFoundException {
		model.addAttribute("id", id);
		try {
			logger.info("UPDATE WORKER GET");
			Trabajador t = tservicio.buscarTrabajador(id);
			TrabajadorUpdateFormDto updateForm = new TrabajadorUpdateFormDto(t.getId(), t.getDocId(), t.getNombre(),
					t.getApellidos(), t.getFecNac(), t.getEmail(), t.getToken(), t.getFechaExpiracionToken(),
					t.getIdioma(), t.getNombreVia(), t.getNumero(), t.getPiso(), t.getPuerta(), t.getProvincia(),
					t.getLocalidad(), t.getCp(), t.getTelefono(), t.getRestoDireccion(), t.isActiveWorker(),
					t.getTrabajadorType());
			logger.info("UPDATE WORKER GET updateForm => " + updateForm.toString());
			model.addAttribute("updateForm", updateForm);
			model.addAttribute("msg", msg);
			model.addAttribute("type", type);
			model.addAttribute("key", updateForm.getEmail());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADORES_UPDATE.concat(" :: content");
			return WebUtils.VISTA_TRABAJADORES_UPDATE;

		} catch (InstanceNotFoundException e) {
			throw new EmployeeNotFoundException(id);
		}
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_TRABAJADOR_UPDATE, method = RequestMethod.POST)
	public String updateTrabajador(@PathVariable("trabajadorId") long id,
			@ModelAttribute("updateForm") @Valid TrabajadorUpdateFormDto updateForm, BindingResult result,
			HttpServletRequest request, Errors errors, Model model, RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws EmployeeNotFoundException {
		logger.info("POST REGISTRO TRABAJADORES");
		model.addAttribute("updateForm", updateForm);
		model.addAttribute("id", id);
		Trabajador t;

		try {
			t = tservicio.buscarTrabajador(id);
		} catch (InstanceNotFoundException e1) {
			throw new EmployeeNotFoundException(id);
		}

		if (result.hasErrors()) {
			logger.info("POST UPDATE TRABAJADORES has errores => " + result.getAllErrors().toString());
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADORES_UPDATE.concat(" :: content");
			return WebUtils.VISTA_TRABAJADORES_UPDATE;
		}
		try {
			t = tservicio.modificarTrabajador(updateForm);
			model.addAttribute("msg", "ok");
			model.addAttribute("type", "cambios_realizados_ok");
			model.addAttribute("key", t.getEmail());
			updateForm = new TrabajadorUpdateFormDto(t.getId(), t.getDocId(), t.getNombre(), t.getApellidos(),
					t.getFecNac(), t.getEmail(), t.getToken(), t.getFechaExpiracionToken(), t.getIdioma(),
					t.getNombreVia(), t.getNumero(), t.getPiso(), t.getPuerta(), t.getProvincia(), t.getLocalidad(),
					t.getCp(), t.getTelefono(), t.getRestoDireccion(), t.isActiveWorker(), t.getTrabajadorType());
			model.addAttribute("updateForm", updateForm);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADORES_UPDATE.concat(" :: content");
			return WebUtils.VISTA_TRABAJADORES_UPDATE;

		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADORES_UPDATE.concat(" :: content");
			return WebUtils.VISTA_TRABAJADORES_UPDATE;
		} catch (InstanceNotFoundException e) {
			model = instanceNotFoundException(model, e);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADORES_UPDATE.concat(" :: content");
			return WebUtils.VISTA_TRABAJADORES_UPDATE;
		}
	}

	@RequestMapping(path = WebUtils.REQUEST_MAPPING_CAMBIAR_ESTADO, method = RequestMethod.POST)
	@ResponseBody
	public boolean cambiarEstadoActivarODesactivar(@PathVariable(name = "trabajadorId") long id)
			throws ResourceNotFoundException {
		boolean activo = false;
		try {
			activo = tservicio.esUnTrabajadorActivo(id);
			if (activo) {
				tservicio.desactivarTrabajador(id);
			} else {
				tservicio.activarTrabajador(id);
			}
			activo = tservicio.esUnTrabajadorActivo(id);
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
		return activo;
	}

	/* CAMBIO DE IDIOMA */
	@ModelAttribute("idiomaPreferencia")
	public String getIdiomaPreferencia(@UsuarioActual PerfilDto perfil) {
		Idioma idioma;
		logger.info("idiomaPreferencia del usuario");
		if (perfil != null) {
			try {
				Trabajador t = tservicio.buscarTrabajador(perfil.getId());
				idioma = t.getIdioma();
				logger.info("idiomaPreferencia del usuario idioma elegido es => " + idioma.name());
				return idioma.name();
			} catch (InstanceNotFoundException e) {

			}
		}
		idioma = Idioma.en;
		return idioma.name();
	}

	/* FIN TRABAJADORES */

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(DuplicateInstanceException.class)
	public Model duplicateInstanceException(Model model, DuplicateInstanceException ex) {
		model.addAttribute("error", ex);
		model.addAttribute("type", "DuplicateInstanceException");
		model.addAttribute("key", ex.getKey());
		return model;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(InstanceNotFoundException.class)
	public Model instanceNotFoundException(Model model, InstanceNotFoundException ex) {
		model.addAttribute("error", ex);
		model.addAttribute("type", "InstanceNotFoundException_trabajadores");
		model.addAttribute("key", ex.getKey());
		return model;
	}
}
