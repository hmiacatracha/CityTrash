package es.udc.citytrash.controller.contenedores;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloRegistroDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorRegistroDto;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorService.ContenedorService;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;

@Controller
// @PreAuthorize("hasRole('" + GlobalNames.ROL_ADMINISTRADOR + "')")
@RequestMapping("contenedores")
public class ContenedoresController {

	@Autowired
	ContenedorService cServicio;

	final Logger logger = LoggerFactory.getLogger(ContenedoresController.class);

	@ModelAttribute("todosLosModelos")
	public List<ContenedorModelo> getModelos() {
		logger.info("todosLosModelos");
		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		modelos = cServicio.buscarTodosLosModelosOrderByModelo();
		return modelos;
	}

	@ModelAttribute("tiposDeBasura")
	public List<TipoDeBasura> getTiposDeBasura() {
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();
		tipos = cServicio.buscarTiposDeBasura();
		return tipos;
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CONTENEDORES, "" }, method = RequestMethod.GET)
	public String getContenedores(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "buscar", required = false, defaultValue = "") String palabrasClaves,
			@RequestParam(value = "modelo", required = false, defaultValue = "") Integer modelo,
			@RequestParam(value = "tipos", required = false) List<Integer> types,
			@RequestParam(value = "mostrarSoloContenedoresActivos", required = false, defaultValue = "false") Boolean activos,
			@RequestParam(value = "mostrarSoloContenedoresDeAlta", required = false, defaultValue = "false") Boolean alta,
			Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {

		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		Page<Contenedor> page = new PageImpl<Contenedor>(contenedoresList, pageRequest, contenedoresList.size());
		logger.info("GET REQUEST_MAPPING_CONTENEDORES");
		ContenedorFormBusq busquedaForm = new ContenedorFormBusq();
		busquedaForm.setBuscar(palabrasClaves);
		busquedaForm.setTiposDeBasura(types);
		busquedaForm.setModelo(modelo);
		busquedaForm.setMostrarSoloContenedoresActivos(activos);
		busquedaForm.setMostrarSoloContenedoresDeAlta(alta);

		model.addAttribute("busquedaForm", busquedaForm);
		try {
			page = cServicio.buscarContenedores(pageRequest, busquedaForm);
		} catch (DataAccessException e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		} catch (Exception ex) {
			model.addAttribute("error", ex);
			model.addAttribute("type", "Exception");
		}
		model.addAttribute("pageContenedores", page);

		if (page.getNumberOfElements() == 0) {
			if (!page.isFirst()) {
				logger.info("PageNotFoundException");
				throw new PageNotFoundException(
						String.format("The requested page (%s) of the contenedores list was not found.",
								pageRequest.getPageNumber()));
			}
		}
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_CONTENEDORES.concat("::content");
		return WebUtils.VISTA_CONTENEDORES;

	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CONTENEDORES }, method = RequestMethod.POST)
	public String buscarContenedores(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@Valid ContenedorFormBusq form, BindingResult result, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		logger.info("POST REQUEST_MAPPING_CONTENEDORES");
		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		Page<Contenedor> page = new PageImpl<Contenedor>(contenedoresList, pageRequest, contenedoresList.size());
		model.addAttribute("busquedaForm", form);

		if (result.hasErrors()) {
			model.addAttribute("pageContenedores", page);
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_CONTENEDORES.concat("::content");
			}
			return WebUtils.VISTA_CONTENEDORES;
		}
		try {
			page = cServicio.buscarContenedores(pageRequest, form);
			model.addAttribute("pageContenedores", page);

			if (page.getNumberOfElements() == 0) {
				if (!page.isFirst()) {
					logger.info("PageNotFoundException");
					throw new PageNotFoundException(String.format(
							"The requested page (%s) of the worker list was not found.", pageRequest.getPageNumber()));
				}
			}

		} catch (DataAccessException e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		} catch (Exception ex) {
			model.addAttribute("pageContenedores", page);
			model.addAttribute("error", ex);
			model.addAttribute("type", "Exception");
		}
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_CONTENEDORES.concat("::content");
		return WebUtils.VISTA_CONTENEDORES;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORES_REGISTRO, method = RequestMethod.GET)
	public String registroContenedor(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("GET REQUEST_MAPPING_CONTENEDORES_REGISTRO");
		model.addAttribute("contenedorForm", new ContenedorRegistroDto());
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_CONTENEDORES_REGISTRO.concat(" ::content");
		return WebUtils.VISTA_CONTENEDORES_REGISTRO;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORES_EDITAR, method = RequestMethod.GET)
	public String editarContenedor(@PathVariable("id") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@RequestParam(value = "msg", required = false) String msg,
			@RequestParam(value = "type", required = false) String type) throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CONTENEDORES_EDITAR");
		Contenedor contenedor;
		try {

			contenedor = cServicio.buscarContenedorById(id);
			ContenedorDto dto = new ContenedorDto(contenedor);
			model.addAttribute("contenedorForm", dto);
			model.addAttribute("msg", msg);
			model.addAttribute("type", type);
			model.addAttribute("key", contenedor.getNombre());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_EDITAR.concat(" ::content");
			return WebUtils.VISTA_CONTENEDORES_EDITAR;
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		} catch (Exception ex) {
			model.addAttribute("error", ex);
			model.addAttribute("type", "Exception");
			return WebUtils.VISTA_CONTENEDORES_EDITAR;
		}
	}

	/* POST CONTENEDORES => REGISTRAR */
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORES_REGISTRO, method = RequestMethod.POST)
	public String registroContenedor(@ModelAttribute("contenedorForm") @Valid ContenedorRegistroDto form,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, Errors errors, Model model,
			RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("paso1 POST REQUEST_MAPPING_CONTENEDORES_REGISTRO");
		model.addAttribute("contenedorForm", form);
		ContenedorDto contenedorDto;
		/* CHECK THE FORM */
		if (result.hasErrors()) {
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_CONTENEDORES_REGISTRO.concat("::content");
			}
			return WebUtils.VISTA_CONTENEDORES_REGISTRO;
		}
		try {
			contenedorDto = new ContenedorDto(cServicio.registrarContenedor(form));
			redirectAttributes.addAttribute("msg", "ok");
			redirectAttributes.addAttribute("type", "reg_cont");
			response.setHeader("X-Requested-With", requestedWith);
			return "redirect:"
					+ WebUtils.URL_MAPPING_CONTENEDORES_EDITAR.replace("{id}", String.valueOf(contenedorDto.getId()));

		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_REGISTRO.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_REGISTRO;
		} catch (InstanceNotFoundException e) {
			model.addAttribute("error", e);
			model.addAttribute("type", "InstanceNotFoundException");
			model.addAttribute("key", e.getKey());
			return WebUtils.VISTA_CONTENEDORES_MODELOS;
		} catch (Exception ex) {
			model.addAttribute("error", ex);
			model.addAttribute("type", "Exception");
			return WebUtils.VISTA_CONTENEDORES_MODELOS;
		}
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORES_EDITAR, method = RequestMethod.POST)
	public String modificarContenedor(@ModelAttribute("contenedorForm") @Valid ContenedorDto form, BindingResult result,
			HttpServletRequest request, Errors errors, Model model, RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("POST REQUEST_MAPPING_CONTENEDORES_EDITAR");
		ContenedorDto contenedor;

		if (result.hasErrors()) {
			model.addAttribute("contenedorForm", form);
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_CONTENEDORES_EDITAR.concat("::content");
			}
			return WebUtils.VISTA_CONTENEDORES_EDITAR;
		}
		try {
			contenedor = new ContenedorDto(cServicio.modificarContenedor(form));
			model.addAttribute("contenedorForm", contenedor);
		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_EDITAR.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_EDITAR;

		} catch (Exception e) {
			model.addAttribute("error", e);
			model.addAttribute("type", "Exception");
			return WebUtils.VISTA_CONTENEDORES_EDITAR;
		}

		model.addAttribute("msg", "ok");
		model.addAttribute("type", "mod_cam");
		model.addAttribute("key", contenedor.getNombre());

		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return WebUtils.VISTA_CONTENEDORES_EDITAR.concat("::content");
		}
		return WebUtils.VISTA_CONTENEDORES_EDITAR;
	}

	/* Activar o desactivar contenedor */
	@RequestMapping(path = WebUtils.REQUEST_MAPPING_CONTENEDORES_ESTADO, method = RequestMethod.POST)
	@ResponseBody
	public boolean cambiarEstadoActivarODesactivar(@PathVariable(name = "id") long id)
			throws ResourceNotFoundException {
		logger.info("REQUEST_MAPPING_CONTENEDORES_ESTADO");
		try {
			return cServicio.cambiarEstadoContenedor(id);
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORES_DETALLES, method = RequestMethod.GET)
	public String contenedorDetalles(@PathVariable("id") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CONTENEDORES_DETALLES");
		try {
			cServicio.buscarContenedorById(id);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_DETALLES.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_DETALLES;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@RequestMapping(value = {
			WebUtils.REQUEST_MAPPING_CONTENEDORES_DETALLES_INFO_CONTENEDOR }, method = RequestMethod.GET)
	public String contenedorDetallesInfo(@PathVariable("id") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET URL_MAPPING_CONTENEDORES_DETALLES_INFO_CONTENEDOR");
		try {
			ContenedorDto contenedorDto = new ContenedorDto(cServicio.buscarContenedorById(id));
			model.addAttribute("objecto", contenedorDto);
			model.addAttribute("id", contenedorDto.getId());
			model.addAttribute("nombreDelContenedor", contenedorDto.getNombre());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_DETALLES_INFO_CONTENEDOR.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_DETALLES_INFO_CONTENEDOR;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CONTENEDORES_DETALLES_INFO_MODELO }, method = RequestMethod.GET)
	public String contenedorDetallesModelo(@PathVariable("id") long id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CONTENEDORES_DETALLES_INFO_MODELO");
		try {
			Contenedor contenedor = cServicio.buscarContenedorById(id);
			ContenedorModelo modelo = cServicio.buscarModeloById(contenedor.getModelo().getId());

			model.addAttribute("objecto", modelo);
			model.addAttribute("id", contenedor.getId());
			model.addAttribute("nombreDelContenedor", contenedor.getNombre());
			model.addAttribute("nombreDelModelo", modelo.getModelo() != null ? modelo.getModelo() : "_no_tiene_nombre");
			model.addAttribute("modeloTipoDeBasura", modelo.getTipo());
			model.addAttribute("colorTipoDeBasura", modelo.getTipo().getColor());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_DETALLES_MODELO.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_DETALLES_MODELO;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		} catch (Exception e) {
			model.addAttribute("error", e);
			model.addAttribute("type", "Exception");
			return WebUtils.VISTA_CONTENEDORES_DETALLES_MODELO;
		}
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CONTENEDORES_DETALLES_INFO_RUTAS }, method = RequestMethod.GET)
	public String contenedoresDetallesInfoRutas(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET URL_MAPPING_CONTENEDORES_DETALLES_INFO_RUTAS");
		try {
			ContenedorModelo modelo = cServicio.buscarModeloById(id);
			/*
			 * model.addAttribute("objecto", modelo); model.addAttribute("id",
			 * modelo.getId()); model.addAttribute("nombreDelModelo",
			 * modelo.getModelo() != null ? modelo.getModelo() :
			 * "no_tiene_nombre_"); model.addAttribute("modeloTiposDeBasura",
			 * modelo.getTipo() != null ? modelo.getTipo() : null);
			 * model.addAttribute("coloresTiposDeBasura",
			 * modelo.getTipo().getColor() != null ? modelo.getTipo().getColor()
			 * : ""); if (AjaxUtils.isAjaxRequest(requestedWith)) return
			 * WebUtils.VISTA_CONTENEDORES_MODELOS_DETALLES_INFO.concat(
			 * "::content"); return
			 * WebUtils.VISTA_CONTENEDORES_MODELOS_DETALLES_INFO;
			 */
			return WebUtils.VISTA_CONTENEDORES_DETALLES_RUTAS;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@RequestMapping(value = {
			WebUtils.REQUEST_MAPPING_CONTENEDORES_DETALLES_INFO_SENSORES }, method = RequestMethod.GET)
	public String contenedoresDetallesInfoSensores(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET URL_MAPPING_CONTENEDORES_DETALLES_INFO_RUTAS");
		try {
			ContenedorModelo modelo = cServicio.buscarModeloById(id);
			/*
			 * model.addAttribute("objecto", modelo); model.addAttribute("id",
			 * modelo.getId()); model.addAttribute("nombreDelModelo",
			 * modelo.getModelo() != null ? modelo.getModelo() :
			 * "no_tiene_nombre_"); model.addAttribute("modeloTiposDeBasura",
			 * modelo.getTipo() != null ? modelo.getTipo() : null);
			 * model.addAttribute("coloresTiposDeBasura",
			 * modelo.getTipo().getColor() != null ? modelo.getTipo().getColor()
			 * : ""); if (AjaxUtils.isAjaxRequest(requestedWith)) return
			 * WebUtils.VISTA_CONTENEDORES_MODELOS_DETALLES_INFO.concat(
			 * "::content"); return
			 * WebUtils.VISTA_CONTENEDORES_MODELOS_DETALLES_INFO;
			 */
			return WebUtils.VISTA_CONTENEDORES_DETALLES_SENSORES;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	/******************************** MODELOS ****************************************/
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CONTENEDORES_MODELOS,
			WebUtils.REQUEST_MAPPING_CONTENEDORES_MODELOS + "/" }, method = RequestMethod.GET)
	public String getModelos(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "palabrasClaveModelo", required = false, defaultValue = "") String palabrasClaves,
			@RequestParam(value = "tipos", required = false) List<Integer> types, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		try {
			logger.info("GET REQUEST_MAPPING_CONTENEDORES_MODELOS");
			List<ContenedorModelo> modeloList = new ArrayList<ContenedorModelo>();
			Page<ContenedorModelo> page = new PageImpl<ContenedorModelo>(modeloList, pageRequest, modeloList.size());
			ContenedorModeloFormBusq busquedaForm = new ContenedorModeloFormBusq();
			busquedaForm.setPalabrasClaveModelo(palabrasClaves);
			busquedaForm.setTipos(types);
			model.addAttribute("busquedaForm", busquedaForm);
			page = cServicio.buscarModelos(pageRequest, busquedaForm);
			model.addAttribute("page", page);

			if (page.getNumberOfElements() == 0) {
				if (!page.isFirst()) {
					logger.info("PageNotFoundException");
					throw new PageNotFoundException(
							String.format("The requested page (%s) of the contenedores list was not found.",
									pageRequest.getPageNumber()));
				}
			}
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_MODELOS.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_MODELOS;

		} catch (DataAccessException e) {
			throw new PageNotFoundException(String.format("The requested page (%s) of the worker list was not found.",
					pageRequest.getPageNumber()));
		}
	}

	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CONTENEDORES_MODELOS }, method = RequestMethod.POST)
	public String getModelos(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@Valid ContenedorModeloFormBusq form, BindingResult result, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		logger.info("POST REQUEST_MAPPING_CONTENEDORES_MODELOS");
		List<ContenedorModelo> modeloList = new ArrayList<ContenedorModelo>();
		Page<ContenedorModelo> page = new PageImpl<ContenedorModelo>(modeloList, pageRequest, modeloList.size());
		model.addAttribute("busquedaForm", form);

		try {
			logger.info("POST REQUEST_MAPPING_CONTENEDORES_MODELOS 2");
			if (result.hasErrors()) {
				model.addAttribute("page", page);
				if (AjaxUtils.isAjaxRequest(requestedWith)) {
					return WebUtils.VISTA_CONTENEDORES_MODELOS.concat("::content");
				}
				return WebUtils.VISTA_CONTENEDORES_MODELOS;
			}
			logger.info("POST REQUEST_MAPPING_CONTENEDORES_MODELOS 3");
			page = cServicio.buscarModelos(pageRequest, form);
			logger.info("POST REQUEST_MAPPING_CONTENEDORES_MODELOS 4");
			model.addAttribute("page", page);

			if (page.getNumberOfElements() == 0) {
				if (!page.isFirst()) {
					logger.info("PageNotFoundException");
					throw new PageNotFoundException(String.format(
							"The requested page (%s) of the worker list was not found.", pageRequest.getPageNumber()));
				}
			}

		} catch (DataAccessException e) {
			model.addAttribute("page", page);
			logger.info("formBusquedaError => " + e.getMessage());
		}

		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_CONTENEDORES_MODELOS.concat("::content");
		return WebUtils.VISTA_CONTENEDORES_MODELOS;
	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORES_REGISTRO_MODELO, method = RequestMethod.GET)
	public String registroModelo(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("GET REQUEST_MAPPING_CONTENEDORES_REGISTRO_MODELOS");
		model.addAttribute("modeloRegistroForm", new ContenedorModeloRegistroDto());
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO.concat(" ::content");
		return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO;
	}

	/* POST MODELO CONTENEDORES => REGISTRAR */
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORESS_REGISTRAR_MODELO, method = RequestMethod.POST)
	public String registroModelo(@ModelAttribute("modeloRegistroForm") @Valid ContenedorModeloRegistroDto form,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, Errors errors, Model model,
			RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("POST REQUEST_MAPPING_CONTENEDORES_POST_MODELO registrar");
		model.addAttribute("modeloRegistroForm", form);
		ContenedorModeloDto modelo;
		/* CHECK THE FORM */
		if (result.hasErrors()) {
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO.concat("::content");
			}
			return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO;
		}
		try {
			/* Registrar modelo */
			modelo = new ContenedorModeloDto(cServicio.registrarModelo(form));
			redirectAttributes.addAttribute("msg", "ok");
			redirectAttributes.addAttribute("type", "reg_cont_modelo");
			response.setHeader("X-Requested-With", requestedWith);
			return "redirect:"
					+ WebUtils.URL_MAPPING_CONTENEDORES_EDITAR_MODELO.replace("{id}", String.valueOf(modelo.getId()));

		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO;

		} catch (InvalidFieldException e) {
			model.addAttribute("error", e);
			model.addAttribute("type", e.getReason());
			model.addAttribute("key", "");
			return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO;
		} catch (Exception e) {
			model.addAttribute("error", e);
			model.addAttribute("type", "Excepcion");
			model.addAttribute("key", e.getCause());
			return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO;
		}

	}

	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORES_EDITAR_MODELO, method = RequestMethod.GET)
	public String editarModelo(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
			@RequestParam(value = "msg", required = false) String msg,
			@RequestParam(value = "type", required = false) String type) throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CONTENEDORES_EDITAR_MODELO");
		ContenedorModelo modelo;
		try {
			modelo = cServicio.buscarModeloById(id);
			ContenedorModeloDto dto = new ContenedorModeloDto(modelo);
			model.addAttribute("msg", msg);
			model.addAttribute("type", type);
			model.addAttribute("key", dto.getNombre());
			model.addAttribute("contenedorModeloForm", dto);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_MODELOS_EDITAR.concat(" ::content");
			return WebUtils.VISTA_CONTENEDORES_MODELOS_EDITAR;
		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	/* POST MODELO CONTENEDORES => MODIFICAR */
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORES_EDITAR_MODELO, method = RequestMethod.POST)
	public String editarModelo(@ModelAttribute("contenedorModeloForm") @Valid ContenedorModeloDto form,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, Errors errors, Model model,
			RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("POST REQUEST_MAPPING_CONTENEDORES_POST_MODELO modificar");

		model.addAttribute("contenedorModelForm", form);
		ContenedorModeloDto modelo;

		if (result.hasErrors()) {
			if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return WebUtils.VISTA_CONTENEDORES_MODELOS_EDITAR.concat("::content");
			}
			return WebUtils.VISTA_CONTENEDORES_MODELOS_EDITAR;
		}
		try {
			modelo = new ContenedorModeloDto(cServicio.modificarModelo(form));

			redirectAttributes.addAttribute("msg", "ok");
			redirectAttributes.addAttribute("type", "mod_cont_modelo");
			response.setHeader("X-Requested-With", requestedWith);
			return "redirect:"
					+ WebUtils.URL_MAPPING_CONTENEDORES_EDITAR_MODELO.replace("{id}", String.valueOf(modelo.getId()));

		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_MODELOS_EDITAR.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_MODELOS_EDITAR;

		} catch (InvalidFieldException e) {
			model.addAttribute("error", e);
			model.addAttribute("type", e.getReason());
			model.addAttribute("key", "");
			return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO;
		} catch (Exception e) {
			model.addAttribute("error", e);
			model.addAttribute("type", "Excepcion");
			model.addAttribute("key", e.getCause());
			return WebUtils.VISTA_CONTENEDORES_MODELOS_REGISTRO;
		}
	}

	/* detalles del modelo del contenedor */
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CONTENEDORES_DETALLES_MODELO, method = RequestMethod.GET)
	public String modeloDetalles(@PathVariable("id") int id, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CONTENEDORES_DETALLES_MODELO");
		try {
			// ContenedorModelo m =
			cServicio.buscarModeloById(id);
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_MODELOS_DETALLES.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_MODELOS_DETALLES;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@RequestMapping(value = {
			WebUtils.REQUEST_MAPPING_CCONTENEDORES_DETALLES_MODELO_INFO_COTENEDOR }, method = RequestMethod.GET)
	public String modelosDetallesContenedor(@PathVariable("id") int id,
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, page = WebUtils.DEFAULT_PAGE_NUMBER, direction = Direction.DESC) @SortDefault("id") Pageable pageRequest,
			@RequestParam(value = "buscar", required = false, defaultValue = "") String palabrasClaves,
			@RequestParam(value = "mostrarSoloContenedoresActivos", required = false, defaultValue = "false") Boolean activos,
			@RequestParam(value = "mostrarSoloContenedoresDeAlta", required = false, defaultValue = "false") Boolean alta,
			@RequestParam(value = "tipos", required = false) List<Integer> types, Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith)
			throws ResourceNotFoundException {
		logger.info("GET REQUEST_MAPPING_CCONTENEDORES_DETALLES_MODELO_INFO_COTENEDOR");
		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		Page<Contenedor> page = new PageImpl<Contenedor>(contenedoresList, pageRequest, contenedoresList.size());
		model.addAttribute("pageContenedores", page);

		try {

			ContenedorModelo modelo = cServicio.buscarModeloById(id);
			ContenedorFormBusq busquedaForm = new ContenedorFormBusq();
			busquedaForm.setBuscar(palabrasClaves);
			busquedaForm.setModelo(id);
			busquedaForm.setTiposDeBasura(types);
			busquedaForm.setMostrarSoloContenedoresActivos(activos);
			busquedaForm.setMostrarSoloContenedoresDeAlta(alta);

			model.addAttribute("busquedaForm", busquedaForm);
			model.addAttribute("objecto", modelo);
			model.addAttribute("id", modelo.getId());
			model.addAttribute("nombreDelModelo", modelo.getModelo());
			model.addAttribute("colorTiposDeBasura", modelo.getTipo().getColor());

			busquedaForm.setModelo(modelo.getId());
			page = cServicio.buscarContenedores(pageRequest, busquedaForm);
			model.addAttribute("pageContenedores", page);

			if (page.getNumberOfElements() == 0) {
				if (!page.isFirst()) {
					logger.info("PageNotFoundException");
					throw new PageNotFoundException(String.format(
							"The requested page (%s) of the worker list was not found.", pageRequest.getPageNumber()));
				}
			}
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_CONTENEDORES_DETALLES_INFO_CONTENEDOR.concat("::content");
			return WebUtils.VISTA_CONTENEDORES_DETALLES_INFO_CONTENEDOR;

		} catch (InstanceNotFoundException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataAccessException e) {
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
}
