package es.udc.citytrash.controller.rutas;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
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

import es.udc.citytrash.controller.excepciones.PageNotFoundException;
import es.udc.citytrash.controller.excepciones.ResourceNotFoundException;
import es.udc.citytrash.controller.util.AjaxUtils;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaContenedorDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutasFormBusq;
import es.udc.citytrash.controller.util.dtos.sensor.SensorDto;
import es.udc.citytrash.controller.util.dtos.tipoDeBasura.TipoDeBasuraDto;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camionService.CamionService;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.rutaService.RutaService;
import es.udc.citytrash.model.sensor.Sensor;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;

@Controller
// @PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
@RequestMapping("rutas")
public class RutasController {

	@Autowired
	ContenedorService contServicio;

	@Autowired
	CamionService camServicio;

	@Autowired
	TrabajadorService tServicio;

	@Autowired
	RutaService rServicio;

	final Logger logger = LoggerFactory.getLogger(RutasController.class);

	@ModelAttribute("rutaForm")
	public RutaDto rutaForm() {
		RutaDto ruta = new RutaDto();
		return ruta;
	}

	@ModelAttribute("listaTiposDeBasura")
	public List<TipoDeBasura> getTiposDeBasura() {
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();
		tipos = camServicio.buscarTiposDeBasura();
		return tipos;
	}

	@ModelAttribute("listaTrabajadores")
	public List<Trabajador> getTrabajadores() {
		List<Trabajador> contenedores = new ArrayList<Trabajador>();
		/* Mostrar todos los trabajadores */
		contenedores = tServicio.buscarTrabajadores(false);
		return contenedores;
	}

	@ModelAttribute("listaContenedores")
	public List<Contenedor> getContenedores() {
		/* Mostrar todos los contenedores */
		List<Contenedor> contenedores = new ArrayList<Contenedor>();
		ContenedorFormBusq form = new ContenedorFormBusq();
		form.setMostrarSoloContenedoresActivos(true);
		form.setMostrarSoloContenedoresDeAlta(false);
		contenedores = contServicio.buscarContenedores(form);
		return contenedores;
	}

	@ModelAttribute("listaCamiones")
	public List<Camion> getCamiones() {
		/* Mostrar todos los camiones */
		List<Camion> camiones = new ArrayList<Camion>();
		camiones = camServicio.buscarCamiones(false, false);
		return camiones;
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_RUTAS, "" }, method = RequestMethod.GET)
	public String getRutas(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "contenedores", required = false) List<Long> contenedores,
			@RequestParam(value = "tiposDeBasura", required = false) List<Integer> tiposDeBasura,
			@RequestParam(value = "trabajadores", required = false) List<Long> trabajadores,
			@RequestParam(value = "camiones", required = false) List<Long> camiones, Model model,
			@RequestParam(value = "mostrarSoloRutasActivas", required = false, defaultValue = "false") Boolean mostrarSoloRutasActivas,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		List<Ruta> RutasList = new ArrayList<Ruta>();
		Page<Ruta> page = new PageImpl<Ruta>(RutasList, pageRequest, RutasList.size());
		RutasFormBusq busquedaForm = new RutasFormBusq(tiposDeBasura, trabajadores, camiones, contenedores,
				mostrarSoloRutasActivas);
		model.addAttribute("busquedaForm", busquedaForm);
		try {
			page = rServicio.buscarRutas(pageRequest, busquedaForm);

		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}
		model.addAttribute("pageRutas", page);

		if (page.getNumberOfElements() == 0) {
			if (!page.isFirst()) {
				throw new PageNotFoundException(
						String.format("The requested page (%s) of the contenedores list was not found.",
								pageRequest.getPageNumber()));
			}
		}
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_RUTAS.concat("::content");
		return WebUtils.VISTA_RUTAS;

	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_RUTAS, "" }, params = {
			"filtrarBuqueda" }, method = RequestMethod.GET)
	public String buscarContenedores(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@Valid RutasFormBusq form, BindingResult result, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		logger.info("get REQUEST_MAPPING_CONTENEDORES params buscar");
		List<Ruta> rutasList = new ArrayList<Ruta>();
		Page<Ruta> page = new PageImpl<Ruta>(rutasList, pageRequest, rutasList.size());
		model.addAttribute("busquedaForm", form);
		
		if (result.hasErrors()) {
			model.addAttribute("pageRutas", page);
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_RUTAS.concat("::content");
			}
			return WebUtils.VISTA_RUTAS;
		}
		try {
			page = rServicio.buscarRutas(pageRequest, form);
			model.addAttribute("pageRutas", page);

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
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_RUTAS.concat("::content");
		return WebUtils.VISTA_RUTAS;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_RUTAS_REGISTRO, method = RequestMethod.GET)
	public String registroRuta(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("GET REQUEST_MAPPING_RUTAS_REGISTRO");

		model.addAttribute("rutaForm", new RutaDto());
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_RUTA_REGISTRO.concat(" ::content");
		return WebUtils.VISTA_RUTA_REGISTRO;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_RUTAS_EDITAR, method = RequestMethod.GET)
	public String editarRuta(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_RUTAS_EDITAR");
		Ruta ruta;

		try {
			ruta = rServicio.buscarRuta(id);
			RutaDto dto = new RutaDto(ruta);
			List<Contenedor> contenedores = contServicio
					.buscarContenedoresDiponiblesParaUnaRuta(dto.getTiposDeBasura());
			List<Camion> camiones = camServicio.buscarCamionesDisponiblesParaUnaRutaByTipos(dto.getTiposDeBasura());

			model.addAttribute("rutaForm", dto);
			model.addAttribute("listaCamionesDisponibles", camiones);
			model.addAttribute("listaContenedoresDisponibles", contenedores);

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_RUTA_EDITAR.concat(" ::content");
			return WebUtils.VISTA_RUTA_EDITAR;
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	/* POST RUTAS => REGISTRAR */
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_RUTAS_REGISTRO }, method = RequestMethod.POST)
	public String registroRuta(@ModelAttribute("rutaForm") @Valid RutaDto form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, Errors errors, Model model,
			RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("paso1 POST REQUEST_MAPPING_RUTAS_REGISTRO");
		model.addAttribute("rutaForm", form);
		/* CHECK THE FORM */
		if (result.hasErrors()) {

			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_RUTA_REGISTRO.concat("::content");
			}
			return WebUtils.VISTA_RUTA_REGISTRO;
		}
		try {
			Ruta ruta = rServicio.registrarRuta(form);
			redirectAttributes.addAttribute("msg", "ok");
			redirectAttributes.addAttribute("type", "reg_ruta");
			response.setHeader("X-Requested-With", requestedWith);
			return "redirect:" + WebUtils.URL_MAPPING_RUTAS_EDITAR.replace("{id}", String.valueOf(ruta.getId()));
		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
		} catch (InstanceNotFoundException e) {
			model.addAttribute("error", e);
			model.addAttribute("type", "InstanceNotFoundException");
			model.addAttribute("key", e.getKey());

		} catch (Exception ex) {
			logger.info("Exception =>" + ex.toString());
			model.addAttribute("error", ex);
			model.addAttribute("type", "Exception");
		}

		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return WebUtils.VISTA_RUTA_REGISTRO.concat("::content");
		}
		return WebUtils.VISTA_RUTA_REGISTRO;

	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_RUTAS_EDITAR }, method = RequestMethod.POST)
	public String actualizarRuta(@ModelAttribute("rutaForm") @Valid RutaDto form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, Errors errors, Model model,
			RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("paso1 POST REQUEST_MAPPING_RUTAS_REGISTRO");
		model.addAttribute("rutaForm", form);
		List<Contenedor> contenedores = contServicio.buscarContenedoresDiponiblesParaUnaRuta(form.getTiposDeBasura());
		List<Camion> camiones = camServicio.buscarCamionesDisponiblesParaUnaRutaByTipos(form.getTiposDeBasura());
		model.addAttribute("listaCamionesDisponibles", camiones);
		model.addAttribute("listaContenedoresDisponibles", contenedores);

		try {

			rServicio.buscarRuta(form.getId());

		} catch (InstanceNotFoundException e1) {
			throw new ResourceNotFoundException(form.getId());
		}
		/* CHECK THE FORM */
		if (result.hasErrors()) {
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_RUTA_EDITAR.concat("::content");
			}
			return WebUtils.VISTA_RUTA_EDITAR;
		}
		try {
			Ruta ruta = rServicio.actualizarRuta(form);
			redirectAttributes.addAttribute("msg", "ok");
			redirectAttributes.addAttribute("type", "mod_ruta");
			response.setHeader("X-Requested-With", requestedWith);
			return "redirect:" + WebUtils.URL_MAPPING_RUTAS_EDITAR.replace("{id}", String.valueOf(ruta.getId()));
		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
		} catch (InstanceNotFoundException e) {
			model.addAttribute("error", e);
			model.addAttribute("type", "InstanceNotFoundException");
			model.addAttribute("key", e.getKey());

		} catch (Exception ex) {
			model.addAttribute("error", ex);
			model.addAttribute("type", "Exception");
		}

		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return WebUtils.VISTA_RUTA_EDITAR.concat("::content");
		}
		return WebUtils.VISTA_RUTA_EDITAR;
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_RUTAS_REGISTRO,
			WebUtils.REQUEST_MAPPING_RUTAS_EDITAR }, params = { "addContenedor" }, method = RequestMethod.POST)

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
		model.addAttribute("type", "InstanceNotFoundException_contenedores");
		model.addAttribute("key", ex.getKey());
		return model;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(InvalidFieldException.class)
	public Model invalidFieldException(Model model, InvalidFieldException ex) {
		model.addAttribute("error", ex);
		model.addAttribute("type", ex.getReason());
		model.addAttribute("key", "");
		return model;
	}

	private TipoDeBasuraDto convertToDto(TipoDeBasura tipo) {
		// TipoDeBasuraDto postDto = modelMapper.map(tipo,
		// TipoDeBasuraDto.class);
		// postDto.setColor(tipo.getColor());
		TipoDeBasuraDto postDto = new TipoDeBasuraDto(tipo);
		return postDto;
	}

	private ContenedorModeloDto convertToDto(ContenedorModelo modelo) {
		// ContenedorModeloDto postDto = modelMapper.map(modelo,
		// ContenedorModeloDto.class);
		ContenedorModeloDto postDto = new ContenedorModeloDto(modelo);
		return postDto;
	}

	private ContenedorDto convertToDto(Contenedor contenedor) {
		// ContenedorDto postDto = modelMapper.map(contenedor,
		// ContenedorDto.class);
		ContenedorDto postDto = new ContenedorDto(contenedor);
		return postDto;
	}

	private SensorDto convertToDto(Sensor sensor) {
		SensorDto dto = new SensorDto(sensor);
		return dto;
	}

}
