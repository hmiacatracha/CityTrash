package es.udc.citytrash.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import es.udc.citytrash.controller.util.dtos.camion.CamionDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloTipoDeBasuraDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionRegistroDto;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;
import es.udc.citytrash.model.camionService.CamionService;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.Recolector;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajadorService.TrabajadorService;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;
import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.CampoBusqPalabrasClavesCamion;

@Controller
@PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
@RequestMapping("camiones")
public class CamionesController {

	@Autowired
	CamionService cServicio;

	@Autowired
	ContenedorService contenedorServicio;

	@Autowired
	TrabajadorService tServicio;

	final Logger logger = LoggerFactory.getLogger(CamionesController.class);

	@ModelAttribute("recolectoresActivos")
	public List<Recolector> getRecolectoresActivos() {
		List<Recolector> modelos = new ArrayList<Recolector>();
		modelos = tServicio.buscarRecolectores(true);
		return modelos;
	}

	@ModelAttribute("conductoresActivos")
	public List<Conductor> getConductoresActivos() {
		List<Conductor> modelos = new ArrayList<Conductor>();
		modelos = tServicio.buscarConductores(true);
		return modelos;
	}

	@ModelAttribute("todosLosTrabajadores")
	public List<Trabajador> getTrabajadores() {
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		trabajadores = tServicio.buscarTrabajadores(false);
		return trabajadores;
	}

	@ModelAttribute("todosLosModelos")
	public List<CamionModelo> getModelos() {
		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		modelos = cServicio.buscarTodosLosModelosOrderByModelo(null);
		return modelos;
	}

	@ModelAttribute("tiposDeBasura")
	public List<TipoDeBasura> getTiposDeBasura() {
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();
		tipos = contenedorServicio.buscarTiposDeBasura();
		return tipos;
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CAMIONES, "" }, method = RequestMethod.GET)
	public String getCamiones(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "buscar", required = false, defaultValue = "") String palabrasClaves,
			@RequestParam(value = "modelo", required = false, defaultValue = "") Integer modelo,
			@RequestParam(value = "tipos", required = false) List<Integer> types,
			@RequestParam(value = "campo", required = false, defaultValue = "matricula") CampoBusqPalabrasClavesCamion campo,
			@RequestParam(value = "mostrarSoloCamionesActivos", required = false, defaultValue = "false") Boolean activos,
			@RequestParam(value = "mostrarSoloCamionesDeAlta", required = false, defaultValue = "false") Boolean alta,
			Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		try {
			List<Camion> camionesList = new ArrayList<Camion>();
			Page<Camion> page = new PageImpl<Camion>(camionesList, pageRequest, camionesList.size());
			logger.info("GET REQUEST_MAPPING_CAMIONES");
			CamionFormBusq busquedaForm = new CamionFormBusq();
			busquedaForm.setTipos(types);
			busquedaForm.setBuscar(palabrasClaves);
			busquedaForm.setCampo(campo.toString());
			busquedaForm.setModelo(modelo);
			busquedaForm.setMostrarSoloCamionesActivos(activos);
			busquedaForm.setMostrarSoloCamionesDeAlta(alta);

			model.addAttribute("busquedaForm", busquedaForm);
			page = cServicio.buscarCamiones(pageRequest, busquedaForm);
			model.addAttribute("pageCamiones", page);

			if (page.getNumberOfElements() == 0) {
				if (!page.isFirst()) {
					throw new PageNotFoundException(
							String.format("The requested page (%s) of the camiones list was not found.",
									pageRequest.getPageNumber()));
				}
			}
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES.concat("::content");
			return WebUtils.VISTA_CAMIONES;

		} catch (FormBusquedaException e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the camiones list was not found.",
					pageRequest.getPageNumber()));
		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_REGISTRO, method = RequestMethod.GET)
	public String registroCamion(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("GET REQUEST_MAPPING_CAMIONES_REGISTRO");
		model.addAttribute("camionForm", new CamionRegistroDto());
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_CAMIONES_REGISTRO.concat(" ::content");
		return WebUtils.VISTA_CAMIONES_REGISTRO;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_EDITAR, method = RequestMethod.GET)
	public String editarCamion(@PathVariable("id") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@RequestParam(value = "msg", required = false) String msg,
			@RequestParam(value = "type", required = false) String type) throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CAMIONES_EDITAR");
		Camion camion;
		try {
			camion = cServicio.buscarCamionById(id);
			// model.addAttribute("camionForm", new CamionDto());
			CamionDto dto = new CamionDto(camion);
			model.addAttribute("camionForm", dto);
			model.addAttribute("msg", msg);
			model.addAttribute("type", type);
			model.addAttribute("key", camion.getNombre());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_EDITAR.concat(" ::content");
			return WebUtils.VISTA_CAMIONES_EDITAR;
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	/* POST CAMIONES => REGISTRAR */
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_REGISTRO, method = RequestMethod.POST)
	public String registroCamion(@ModelAttribute("camionForm") @Valid CamionRegistroDto form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, Errors errors, Model model,
			RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		model.addAttribute("camionForm", form);
		CamionDto camionDto;
		/* CHECK THE FORM */
		if (result.hasErrors()) {
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_CAMIONES_REGISTRO.concat("::content");
			}
			return WebUtils.VISTA_CAMIONES_REGISTRO;
		}
		try {
			camionDto = new CamionDto(cServicio.registrarCamion(form));
			redirectAttributes.addAttribute("msg", "ok");
			redirectAttributes.addAttribute("type", "reg_cam");
			response.setHeader("X-Requested-With", requestedWith);
			return "redirect:"
					+ WebUtils.URL_MAPPING_CAMIONES_EDITAR.replace("{id}", String.valueOf(camionDto.getId()));

		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_REGISTRO.concat("::content");
			return WebUtils.VISTA_CAMIONES_REGISTRO;
		} catch (InstanceNotFoundException e) {
			return WebUtils.VISTA_CAMIONES_MODELOS;

		} catch (InvalidFieldException e) {
			return "redirect:" + WebUtils.VISTA_CAMIONES;
		} catch (Exception e) {
			return WebUtils.VISTA_CAMIONES_REGISTRO;
		}

	}

	/* POST CAMIONES => MODIFICAR */
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_EDITAR, method = RequestMethod.POST)
	public String modificarCamion(@ModelAttribute("camionForm") @Valid CamionDto form, BindingResult result,
			HttpServletRequest request, Errors errors, Model model, RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("POST REQUEST_MAPPING_CAMIONES_EDITAR");
		// model.addAttribute("id", form.getId());
		CamionDto camion;

		if (result.hasErrors()) {
			model.addAttribute("camionForm", form);
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_CAMIONES_EDITAR.concat("::content");
			}
			return WebUtils.VISTA_CAMIONES_EDITAR;
		}
		try {
			camion = new CamionDto(cServicio.modificarCamion(form));
			model.addAttribute("camionForm", camion);
		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_EDITAR.concat("::content");
			return WebUtils.VISTA_CAMIONES_EDITAR;

		} catch (Exception e) {
			return WebUtils.VISTA_CAMIONES_EDITAR;
		}

		model.addAttribute("msg", "ok");
		model.addAttribute("type", "mod_cam");
		model.addAttribute("key", camion.getNombre());

		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return WebUtils.VISTA_CAMIONES_EDITAR.concat("::content");
		}
		return WebUtils.VISTA_CAMIONES_EDITAR;
	}

	/* Activar o desactivar camion */
	@RequestMapping(path = WebUtils.REQUEST_MAPPING_CAMIONES_ESTADO, method = RequestMethod.POST)
	@ResponseBody
	public boolean cambiarEstadoActivarODesactivar(@PathVariable(name = "id") long id)
			throws ResourceNotFoundException {
		logger.info("URL_MAPPING_CAMIONES_ESTADO");
		try {
			return cServicio.cambiarEstadoCamion(id);
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_DETALLES, method = RequestMethod.GET)
	public String camionDetalles(@PathVariable("id") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CAMIONES_DETALLES");
		try {
			cServicio.buscarCamionById(id);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_DETALLES.concat("::content");
			return WebUtils.VISTA_CAMIONES_DETALLES;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CAMIONES_DETALLES_INFO_CAMION }, method = RequestMethod.GET)
	public String camionDetallesInfo(@PathVariable("id") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CAMIONES_DETALLES_INFO_CAMION");
		try {
			Camion camion = cServicio.buscarCamionById(id);
			CamionDto camionDto = new CamionDto(camion);
			model.addAttribute("objecto", camionDto);
			model.addAttribute("id", camionDto.getId());
			model.addAttribute("nombreDelCamion", camionDto.getNombre());
			logger.info("PASO1 camionDto.getConductorPrincipal() => " + camionDto.getConductorPrincipal());

			Trabajador cp = camionDto.getConductorPrincipal() != null
					? tServicio.buscarTrabajador(camionDto.getConductorPrincipal()) : null;

			logger.info("PASO1 camionDto.getConductorPrincipal()");
			model.addAttribute("conductor", cp);

			logger.info("PASO2 camionDto.getConductorPrincipal()");
			Trabajador cs = camionDto.getConductorSuplente() != null
					? tServicio.buscarTrabajador(camionDto.getConductorSuplente()) : null;
			model.addAttribute("conductorSuplente", cs);

			logger.info("PASO3 camionDto.getConductorPrincipal()");
			Trabajador r1 = camionDto.getRecogedorUno() != null
					? tServicio.buscarTrabajador(camionDto.getRecogedorUno()) : null;
			model.addAttribute("recogedor1", r1);

			logger.info("PASO4 camionDto.getConductorPrincipal()");
			Trabajador r2 = camionDto.getRecogedorDos() != null
					? tServicio.buscarTrabajador(camionDto.getRecogedorDos()) : null;
			model.addAttribute("recogedor2", r2);
			logger.info("PASO5 camionDto.getConductorPrincipal()");

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_DETALLES_INFO.concat("::content");
			return WebUtils.VISTA_CAMIONES_DETALLES_INFO;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CAMIONES_DETALLES_INFO_MODELO }, method = RequestMethod.GET)
	public String camionDetallesModelo(@PathVariable("id") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CAMIONES_DETALLES_INFO_MODELO");
		try {
			Camion camion = cServicio.buscarCamionById(id);
			CamionModelo modelo = cServicio.buscarModeloById(camion.getModeloCamion().getId());

			model.addAttribute("objecto", modelo);
			model.addAttribute("id", camion.getId());
			model.addAttribute("nombreDelCamion", camion.getNombre());
			model.addAttribute("nombreDelModelo", getNombreDelModelo(modelo.getId()));
			model.addAttribute("modeloTiposDeBasura", getTiposDeBasuraByModelo(modelo.getId()));
			model.addAttribute("coloresTiposDeBasura", getColoresTiposDeBasuraByModel(modelo.getId()));

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_DETALLES_MODELO.concat("::content");
			return WebUtils.VISTA_CAMIONES_DETALLES_MODELO;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	/******************************** MODELOS ****************************************/

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CAMIONES_MODELOS,
			WebUtils.REQUEST_MAPPING_CAMIONES_MODELOS + "/" }, method = RequestMethod.GET)
	public String getModelos(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "palabrasClaveModelo", required = false, defaultValue = "") String palabrasClaves,
			@RequestParam(value = "tipos", required = false) List<Integer> types, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		try {
			logger.info("GET REQUEST_MAPPING_CAMIONES_MODELOS");
			List<CamionModelo> modeloList = new ArrayList<CamionModelo>();
			Page<CamionModelo> page = new PageImpl<CamionModelo>(modeloList, pageRequest, modeloList.size());
			CamionModeloFormBusq busquedaForm = new CamionModeloFormBusq();
			busquedaForm.setPalabrasClaveModelo(palabrasClaves);
			busquedaForm.setTipos(types);
			model.addAttribute("busquedaForm", busquedaForm);
			page = cServicio.buscarModelos(pageRequest, busquedaForm);
			model.addAttribute("page", page);

			if (page.getNumberOfElements() == 0) {
				if (!page.isFirst()) {
					throw new PageNotFoundException(
							String.format("The requested page (%s) of the camiones list was not found.",
									pageRequest.getPageNumber()));
				}
			}
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_MODELOS.concat("::content");
			return WebUtils.VISTA_CAMIONES_MODELOS;

		} catch (FormBusquedaException e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the camiones list was not found.",
					pageRequest.getPageNumber()));
		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_REGISTRO_MODELO, method = RequestMethod.GET)
	public String registroModelo(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("GET REQUEST_MAPPING_CAMIONES_REGISTRO_MODELOS");
		model.addAttribute("camionModeloForm", new CamionModeloDto());
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO.concat(" ::content");
		return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_EDITAR_MODELO, method = RequestMethod.GET)
	public String editarModelo(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@RequestParam(value = "msg", required = false) String msg,
			@RequestParam(value = "type", required = false) String type) throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CAMIONES_EDITAR_MODELO");
		CamionModelo modelo;

		try {
			modelo = cServicio.buscarModeloById(id);

			// model.addAttribute("camionModeloForm", new CamionModeloDto());
			CamionModeloDto dto = new CamionModeloDto(modelo);
			List<CamionModeloTipoDeBasura> tipos = cServicio.buscarTipoDeBasuraByModelo(modelo.getId());
			dto.setListaTiposDeBasura(tipos.stream().map(sensor -> convertToDto(sensor)).collect(Collectors.toList()));

			// falta añadir el id aleatorio positivos los que estan guardados en
			// la base de datos
			model.addAttribute("msg", msg);
			model.addAttribute("type", type);
			model.addAttribute("key", dto.getNombre());
			model.addAttribute("camionModeloForm", dto);

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO.concat(" ::content");
			return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO;
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private CamionModeloTipoDeBasuraDto convertToDto(CamionModeloTipoDeBasura tipo) {
		CamionModeloTipoDeBasuraDto postDto = new CamionModeloTipoDeBasuraDto();
		postDto.setNuevo(false);
		postDto.setCapacidad(tipo.getCapacidad());
		postDto.setIdTipo(tipo.getTipo().getId());
		return postDto;
	}

	/* POST MODELO CAMIONES => REGISTRAR */
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_MODELO_REGISTRO_OR_MODIFICAR, method = RequestMethod.POST, params = "registrar")
	public String registroModelo(@ModelAttribute("camionModeloForm") @Valid CamionModeloDto form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, Errors errors, Model model,
			RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("POST REQUEST_MAPPING_CAMIONES_POST_MODELO registrar");
		model.addAttribute("camionModelForm", form);
		CamionModeloDto modelo;
		/* CHECK THE FORM */
		if (result.hasErrors() || form.getId() != null) {
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO.concat("::content");
			}
			return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO;
		}

		try {
			modelo = new CamionModeloDto(cServicio.registrarModelo(form));
		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO.concat("::content");
			return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO;

		} catch (Exception e) {
			logger.debug("error register a user => " + e.toString());
			return WebUtils.VISTA_CAMIONES_MODELOS;
		}
		redirectAttributes.addAttribute("msg", "ok");
		redirectAttributes.addAttribute("type", "reg_cam_modelo");
		response.setHeader("X-Requested-With", requestedWith);
		return "redirect:"
				+ WebUtils.URL_MAPPING_CAMIONES_EDITAR_MODELO.replace("{id}", String.valueOf(modelo.getId()));

	}

	/* POST MODELO CAMIONES => MODIFICAR */
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_MODELO_REGISTRO_OR_MODIFICAR, method = RequestMethod.POST, params = "modificar")
	public String editarModelo(@ModelAttribute("camionModeloForm") @Valid CamionModeloDto form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, Errors errors, Model model,
			RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("POST REQUEST_MAPPING_CAMIONES_POST_MODELO modificar");

		CamionModeloDto modelo = null;
		model.addAttribute("camionModelForm", form);
		if (result.hasErrors() || form.getId() == null) {
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO.concat("::content");
			}
			return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO;
		}
		try {
			modelo = new CamionModeloDto(cServicio.modificarModelo(form));

		} catch (DuplicateInstanceException e) {

			model = duplicateInstanceException(model, e);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO.concat("::content");
			return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO;

		} catch (Exception e) {
			// model.addAttribute("camionModelForm", form);
			logger.debug("error register a user => " + e.toString());
			return WebUtils.VISTA_CAMIONES_MODELOS;
		}

		redirectAttributes.addAttribute("msg", "ok");
		redirectAttributes.addAttribute("type", "mod_cam_modelo");
		response.setHeader("X-Requested-With", requestedWith);
		return "redirect:"
				+ WebUtils.URL_MAPPING_CAMIONES_EDITAR_MODELO.replace("{id}", String.valueOf(modelo.getId()));
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_MODELO_REGISTRO_OR_MODIFICAR, method = RequestMethod.POST, params = "addTipoBasura")
	public String addTipoBasura(CamionModeloDto form, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {

		logger.info("POST REQUEST_MAPPING_CAMIONES_POST_MODELO add CamionModeloTipoDeBasuraDto");
		CamionModeloDto modelo = form;
		CamionModeloTipoDeBasuraDto tipo = new CamionModeloTipoDeBasuraDto();
		List<CamionModeloTipoDeBasuraDto> tipos = form.getListaTiposDeBasura() != null ? form.getListaTiposDeBasura()
				: new ArrayList<CamionModeloTipoDeBasuraDto>();
		tipos.add(tipo);
		modelo.setListaTiposDeBasura(tipos);
		model.addAttribute("camionModeloForm", modelo);

		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO.concat(" ::content");
		return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO;

	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_MODELO_REGISTRO_OR_MODIFICAR, method = RequestMethod.POST, params = {
			"eliminarTipoBasura" })
	public String eliminarTipoBasura(CamionModeloDto form, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			final HttpServletRequest req, @RequestParam("eliminarTipoBasura") int row)
			throws ResourceNotFoundException {

		logger.info("POST REQUEST_MAPPING_CAMIONES_POST_MODELO add CamionModeloTipoDeBasuraDto");

		try {

			CamionModeloTipoDeBasuraDto tipoAELiminar = form.getListaTiposDeBasura().get(row);
			form.getListaTiposDeBasura().remove(row);

			if (!tipoAELiminar.isNuevo()) {
				cServicio.eliminarModeloTipoDeBasura(form.getId(), tipoAELiminar.getIdTipo());
			}

		} catch (InstanceNotFoundException e) {

		} catch (IndexOutOfBoundsException e) {
			logger.info("row =>" + row);
			logger.info("IndexOutOfBoundsException =>");
		}

		model.addAttribute("camionModeloForm", form);
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO.concat(" ::content");
		return WebUtils.VISTA_CAMIONES_MODELOS_FORMULARIO;
	}

	/* detalles del modelo del camion */
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMIONES_DETALLES_MODELO, method = RequestMethod.GET)
	public String modeloDetalles(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CAMIONES_DETALLES_MODELO");
		try {
			// CamionModelo m =
			cServicio.buscarModeloById(id);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_MODELOS_DETALLES.concat("::content");
			return WebUtils.VISTA_CAMIONES_MODELOS_DETALLES;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@RequestMapping(value = {
			WebUtils.REQUEST_MAPPING_CAMIONES_DETALLES_MODELO_INFO_MODELO }, method = RequestMethod.GET)
	public String modelosDetallesInfo(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CAMIONES_DETALLES_MODELO_INFO");
		try {
			CamionModelo modelo = cServicio.buscarModeloById(id);
			model.addAttribute("objecto", modelo);
			model.addAttribute("id", modelo.getId());
			model.addAttribute("nombreDelModelo", getNombreDelModelo(id));
			model.addAttribute("modeloTiposDeBasura", getTiposDeBasuraByModelo(id));
			model.addAttribute("coloresTiposDeBasura", getColoresTiposDeBasuraByModel(id));

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_MODELOS_DETALLES_INFO.concat("::content");
			return WebUtils.VISTA_CAMIONES_MODELOS_DETALLES_INFO;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@RequestMapping(value = {
			WebUtils.REQUEST_MAPPING_CAMIONES_DETALLES_MODELO_INFO_CAMIONES }, method = RequestMethod.GET)
	public String modelosDetallesCamiones(@PathVariable("id") int id,
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "buscar", required = false, defaultValue = "") String palabrasClaves,
			@RequestParam(value = "campo", required = false, defaultValue = "matricula") CampoBusqPalabrasClavesCamion campo,
			@RequestParam(value = "mostrarSoloCamionesActivos", required = false, defaultValue = "false") Boolean activos,
			@RequestParam(value = "mostrarSoloCamionesDeAlta", required = false, defaultValue = "false") Boolean alta,
			Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CAMIONES_DETALLES_MODELO_CAMIONES");
		List<Camion> camionesList = new ArrayList<Camion>();
		Page<Camion> page = new PageImpl<Camion>(camionesList, pageRequest, camionesList.size());
		model.addAttribute("pageCamiones", page);

		try {

			CamionModelo modelo = cServicio.buscarModeloById(id);
			CamionFormBusq busquedaForm = new CamionFormBusq();
			busquedaForm.setBuscar(palabrasClaves);
			busquedaForm.setCampo(campo.toString());
			busquedaForm.setModelo(id);
			busquedaForm.setMostrarSoloCamionesActivos(activos);
			busquedaForm.setMostrarSoloCamionesDeAlta(alta);

			model.addAttribute("busquedaForm", busquedaForm);
			model.addAttribute("objecto", modelo);
			model.addAttribute("id", modelo.getId());
			model.addAttribute("nombreDelModelo", getNombreDelModelo(id));
			model.addAttribute("coloresTiposDeBasura", getColoresTiposDeBasuraByModel(id));

			busquedaForm.setModelo(modelo.getId());
			page = cServicio.buscarCamiones(pageRequest, busquedaForm);
			model.addAttribute("pageCamiones", page);

			if (page.getNumberOfElements() == 0) {
				if (!page.isFirst()) {
					logger.info("PageNotFoundException");
					throw new PageNotFoundException(String.format(
							"The requested page (%s) of the worker list was not found.", pageRequest.getPageNumber()));
				}
			}
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CAMIONES_MODELOS_DETALLES_CAMIONES.concat("::content");
			return WebUtils.VISTA_CAMIONES_MODELOS_DETALLES_CAMIONES;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		} catch (FormBusquedaException e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		} catch (Exception e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}
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
		model.addAttribute("type", "InstanceNotFoundException_trabajadores");
		model.addAttribute("key", ex.getKey());
		return model;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(InvalidFieldException.class)
	public Model invalidFieldException(Model model, InvalidFieldException ex) {
		model.addAttribute("error", ex);
		model.addAttribute("type", "InvalidFieldException");
		model.addAttribute("key", ex.getReason());
		return model;
	}

	public List<String> getColoresTiposDeBasuraByModel(int modeloId) {
		List<String> colores = new ArrayList<String>();
		List<CamionModeloTipoDeBasura> tipos = new ArrayList<CamionModeloTipoDeBasura>();
		try {
			tipos = cServicio.buscarTipoDeBasuraByModelo(modeloId);
			for (CamionModeloTipoDeBasura tipo : tipos) {
				colores.add(tipo.getTipo().getColor());
			}
		} catch (InstanceNotFoundException e) {
		}
		return colores;
	}

	public List<CamionModeloTipoDeBasura> getTiposDeBasuraByModelo(int modeloId) {
		List<CamionModeloTipoDeBasura> tipos = new ArrayList<CamionModeloTipoDeBasura>();
		try {
			tipos = cServicio.buscarTipoDeBasuraByModelo(modeloId);
		} catch (InstanceNotFoundException e) {
		}
		return tipos;
	}

	public String getNombreDelModelo(int modeloId) {
		String nombre = "";
		try {
			CamionModelo modelo = cServicio.buscarModeloById(modeloId);
			nombre = modelo.getModelo();
		} catch (InstanceNotFoundException e) {

		}
		return nombre;
	}
}
