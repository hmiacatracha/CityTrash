package es.udc.citytrash.controller.rutas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.format.annotation.DateTimeFormat;
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

import es.udc.citytrash.controller.excepciones.PageNotFoundException;
import es.udc.citytrash.controller.excepciones.ResourceNotFoundException;
import es.udc.citytrash.controller.util.AjaxUtils;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloDto;
import es.udc.citytrash.controller.util.dtos.ruta.GenerarRutaFormDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutasDiariaFormBusq;
import es.udc.citytrash.controller.util.dtos.ruta.RutasFormBusq;
import es.udc.citytrash.controller.util.dtos.sensor.SensorDto;
import es.udc.citytrash.controller.util.dtos.tipoDeBasura.TipoDeBasuraDto;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camionService.CamionService;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.rutaDiaria.RutaDiaria;
import es.udc.citytrash.model.rutaService.RutaService;
import es.udc.citytrash.model.sensor.Sensor;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;
import es.udc.citytrash.util.GlobalNames;

@Controller
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
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		trabajadores = tServicio.buscarTrabajadores(false);
		return trabajadores;
	}

	@ModelAttribute("listaTodosLosContenedores")
	public List<Contenedor> getContenedores() {
		/* Mostrar todos los contenedores */
		List<Contenedor> contenedores = new ArrayList<Contenedor>();
		ContenedorFormBusq form = new ContenedorFormBusq();
		form.setMostrarSoloContenedoresActivos(false);
		form.setMostrarSoloContenedoresDeAlta(false);
		contenedores = contServicio.buscarContenedores(form);
		return contenedores;
	}

	@ModelAttribute("listaTodosLosCamiones")
	public List<Camion> getCamiones() {
		/* Mostrar todos los camiones */
		List<Camion> camiones = new ArrayList<Camion>();
		camiones = camServicio.buscarCamiones(false, false);
		return camiones;
	}

	@ModelAttribute("listarTodasLasRutas")
	public List<Ruta> getRutas() {
		/* Mostrar todos las rutas */
		List<Ruta> rutas = new ArrayList<Ruta>();
		rutas = rServicio.buscarRutas(false);
		return rutas;
	}

	@ModelAttribute("busquedaForm")
	public RutasDiariaFormBusq getFormBusqueda() {
		RutasDiariaFormBusq form = new RutasDiariaFormBusq();
		return form;
	}

	@PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
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
		List<Camion> camionesList = new ArrayList<Camion>();
		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		RutasFormBusq busquedaForm = new RutasFormBusq(tiposDeBasura, trabajadores, camiones, contenedores,
				mostrarSoloRutasActivas);
		model.addAttribute("busquedaForm", busquedaForm);
		try {
			page = rServicio.buscarRutas(pageRequest, busquedaForm);
			camionesList = camServicio.buscarCamionesByTipos(busquedaForm.getTiposDeBasura());
			contenedoresList = contServicio.buscarContenedoresByTiposDeBasura(busquedaForm.getTiposDeBasura());
			logger.info("get rutas despues de buscar rutas 1");
			model.addAttribute("listaCamiones", camionesList);
			model.addAttribute("listaContenedores", contenedoresList);
			model.addAttribute("pageRutas", page);
			logger.info("get rutas despues de buscar rutas 2");
		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the routes list was not found.",
					pageRequest.getPageNumber()));
		}

		if (page.getNumberOfElements() == 0) {
			if (!page.isFirst()) {
				throw new PageNotFoundException(String.format(
						"The requested page (%s) of the routes list was not found.", pageRequest.getPageNumber()));
			}
		}
		logger.info("get rutas despues de buscar rutas fin");
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_RUTAS.concat("::content");
		return WebUtils.VISTA_RUTAS;

	}

	@PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_RUTAS_REGISTRO, method = RequestMethod.GET)
	public String registroRuta(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@RequestParam(value = "msg", required = false) String msg,
			@RequestParam(value = "type", required = false) String type) {

		logger.info("GET REQUEST_MAPPING_RUTAS_REGISTRO");
		model.addAttribute("msg", msg);
		model.addAttribute("type", type);
		model.addAttribute("rutaForm", new RutaDto());
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_RUTA_REGISTRO.concat(" ::content");
		return WebUtils.VISTA_RUTA_REGISTRO;
	}

	@PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_RUTAS_EDITAR, method = RequestMethod.GET)
	public String editarRuta(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@RequestParam(value = "msg", required = false) String msg,
			@RequestParam(value = "type", required = false) String type) throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_RUTAS_EDITAR");
		Ruta ruta;
		try {
			ruta = rServicio.buscarRuta(id);
			RutaDto dto = new RutaDto(ruta);
			List<Contenedor> contenedores = contServicio.buscarContenedoresDiponiblesParaUnaRuta(id,
					dto.getTiposDeBasura());
			List<Camion> camiones = camServicio.buscarCamionesDisponiblesParaUnaRutaByTipos(dto.getTiposDeBasura());

			model.addAttribute("rutaForm", dto);
			model.addAttribute("listaCamionesDisponibles", camiones);
			model.addAttribute("listaContenedoresDisponibles", contenedores);
			model.addAttribute("msg", msg);
			model.addAttribute("type", type);
			model.addAttribute("key", ruta.getId());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_RUTA_EDITAR.concat(" ::content");
			return WebUtils.VISTA_RUTA_EDITAR;
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	/* POST RUTAS => REGISTRAR */
	@PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
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

	@PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
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
			logger.info("fin actualizar ruta controller");
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

	@PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_RUTAS_DETALLES, method = RequestMethod.GET)
	public String detallesRuta(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_RUTAS_DETALLES");
		Ruta ruta;
		try {

			ruta = rServicio.buscarRuta(id);
			RutaDto dto = new RutaDto(ruta);
			model.addAttribute("rutaForm", dto);
			model.addAttribute("camionAsignado", camServicio.buscarCamionById(dto.getCamion()));
			model.addAttribute("tiposDeBasuraAsignadas", ruta.getTiposDeBasura());
			model.addAttribute("contenedoresAsignados", ruta.getContenedores());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_RUTA_DETALLES.concat(" ::content");
			return WebUtils.VISTA_RUTA_DETALLES;
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	/* Activar o desactivar camion */
	@PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
	@RequestMapping(path = WebUtils.REQUEST_MAPPING_RUTAS_ESTADO, method = RequestMethod.POST)
	@ResponseBody
	public boolean cambiarEstadoActivarODesactivar(@PathVariable(name = "id") int id) throws ResourceNotFoundException {
		logger.info("URL_MAPPING_CAMIONES_ESTADO");
		try {
			return rServicio.cambiarEstadoRuta(id);
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	/* Activar o desactivar camion */

	// @PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")

	@RequestMapping(path = { WebUtils.REQUEST_MAPPING_RUTAS_GENERAR,
			WebUtils.REQUEST_MAPPING_RUTAS_GENERAR + "/" }, method = RequestMethod.GET)
	public String getGenerarRutas(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "rutas", required = false) List<Integer> rutas,
			@RequestParam(value = "tiposDeBasura", required = false) List<Integer> tiposDeBasura,
			@RequestParam(value = "camiones", required = false) List<Long> camiones, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		List<RutaDiaria> RutasDiariaList = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(RutasDiariaList, pageRequest, RutasDiariaList.size());
		Date fecha = Calendar.getInstance().getTime();
		GenerarRutaFormDto form = new GenerarRutaFormDto(fecha, tiposDeBasura, camiones, rutas);
		List<Camion> listaTodosLosCamiones = camServicio.buscarCamionesByTipos(form.getTiposDeBasura());
		List<Ruta> listaRutasAGenerar = rServicio.buscarRutasSinGenerar(form);
		RutasDiariaFormBusq busquedaForm = new RutasDiariaFormBusq(fecha, fecha, null, null, null, null);
		page = rServicio.buscarRutasDiarias(pageRequest, busquedaForm);
		model.addAttribute("generarRutaForm", form);
		model.addAttribute("listaTodosLosCamiones", listaTodosLosCamiones);
		model.addAttribute("listaRutasAGenerar", listaRutasAGenerar);
		model.addAttribute("pageRutas", page);

		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_RUTA_GENERAR.concat("::content");
		return WebUtils.VISTA_RUTA_GENERAR;

	}

	// @PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
	@RequestMapping(path = { WebUtils.REQUEST_MAPPING_RUTAS_GENERAR,
			WebUtils.REQUEST_MAPPING_RUTAS_GENERAR + "/" }, method = RequestMethod.POST)
	public String postGenerarRutas(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@ModelAttribute("generarRutaForm") @Valid GenerarRutaFormDto form, BindingResult result, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		List<RutaDiaria> RutasDiariaList = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(RutasDiariaList, pageRequest, RutasDiariaList.size());
		RutasDiariaFormBusq busquedaForm = new RutasDiariaFormBusq(form.getFecha(), form.getFecha(), null, null, null,
				null);
		List<Ruta> listaRutasAGenerar = rServicio.buscarRutasSinGenerar(form);

		if (result.hasErrors()) {
			page = rServicio.buscarRutasDiarias(pageRequest, busquedaForm);
			model.addAttribute("generarRutaForm", form);
			model.addAttribute("listaRutasAGenerar", listaRutasAGenerar);
			model.addAttribute("pageRutas", page);
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_RUTA_GENERAR.concat("::content");
			}
			return WebUtils.VISTA_RUTA_GENERAR;
		}

		try {
			rServicio.generarRutas(form);
			page = rServicio.buscarRutasDiarias(pageRequest, busquedaForm);
			listaRutasAGenerar = rServicio.buscarRutasSinGenerar(form);
			model.addAttribute("msg", "ok");
			model.addAttribute("type", "generarRutasCorrectamente");
			// model.addAttribute("key", form.getRutas());

		} catch (Exception e) {
			model.addAttribute("error", e);
			model.addAttribute("type", e.getMessage());
		}

		logger.info("lista de rutas a generar ok");
		model.addAttribute("listaRutasAGenerar", listaRutasAGenerar);
		model.addAttribute("generarRutaForm", form);
		model.addAttribute("pageRutas", page);

		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_RUTA_GENERAR.concat("::content");
		return WebUtils.VISTA_RUTA_GENERAR;

	}

	/*************************** RUTAS DIARIAS *************************/
	// @PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_RUTAS_HISTORIAL,
			WebUtils.REQUEST_MAPPING_RUTAS_HISTORIAL + "/" }, method = RequestMethod.GET)
	public String getRutasDiarias(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date to,
			@RequestParam(value = "contenedores", required = false) List<Long> contenedores,
			@RequestParam(value = "rutas", required = false) List<Integer> rutas,
			@RequestParam(value = "trabajadores", required = false) List<Long> trabajadores,
			@RequestParam(value = "camiones", required = false) List<Long> camiones, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {

		List<RutaDiaria> RutasDiariaList = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(RutasDiariaList, pageRequest, RutasDiariaList.size());
		logger.info("GET RUTAS DIARIAS 1=>");
		if (from == null || to == null) {
			from = Calendar.getInstance().getTime();
			to = Calendar.getInstance().getTime();
		}

		RutasDiariaFormBusq busquedaForm = new RutasDiariaFormBusq(from, to, rutas, trabajadores, camiones,
				contenedores);
		model.addAttribute("busquedaForm", busquedaForm);
		logger.info("imprimir form de busqueda =>" + busquedaForm.toString());
		try {
			page = rServicio.buscarRutasDiarias(pageRequest, busquedaForm);
			logger.info("GET RUTAS DIARIAS 4");
			model.addAttribute("pageRutas", page);
			logger.info("GET RUTAS DIARIAS 5");
		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the routes list was not found.",
					pageRequest.getPageNumber()));
		}

		if (page.getNumberOfElements() == 0) {
			if (!page.isFirst()) {
				throw new PageNotFoundException(String.format(
						"The requested page (%s) of the routes list was not found.", pageRequest.getPageNumber()));
			}
		}
		logger.info("GET RUTAS DIARIAS FIN");
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_RUTAS_HISTORIAL.concat("::content");
		return WebUtils.VISTA_RUTAS_HISTORIAL;
	}

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
		model.addAttribute("type", "InstanceNotFoundException_routes");
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
