package es.udc.citytrash.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.service.trabajador.TrabajadorService;
import es.udc.citytrash.business.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.controller.error.PageNotFoundException;
import es.udc.citytrash.controller.util.AjaxUtils;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.dtos.TrabajadorFormDto;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("auth/admin/")
public class RolAdminController {

	@Autowired
	TrabajadorService tservicio;

	final Logger logger = LoggerFactory.getLogger(RolAdminController.class);

	/* TRABAJADORES */
	@RequestMapping(value = WebUtils.URL_TRABAJADORES, method = RequestMethod.GET)
	public String trabajadores(
			@PageableDefault(size = WebUtils.DEFAULT_PAGE_SIZE, direction = Direction.DESC) @SortDefault("creationTimestamp") Pageable pageRequest,
			Model model) {
		logger.debug("Showing lits of workers page GET");
		int page = pageRequest.getPageNumber();
		model.addAttribute("pageRequest", pageRequest);
		Page<Trabajador> trabajadores = tservicio.buscarTrabajadores(pageRequest);
		if (trabajadores.getNumberOfElements() == 0) {
			if (!trabajadores.isFirst()) {
				throw new PageNotFoundException(
						String.format("The requested page (%s) of the worker list was not found.", page));
			}
			model.addAttribute("trabajadores", null);
		} else {
			model.addAttribute("trabajadores", trabajadores);
		}
		logger.info("Admin panel: showing page {} of the worker list.", page);
		return WebUtils.VISTA_TRABAJADORES;
	}

	/**
	 * Register form
	 */
	@ModelAttribute("registro")
	public TrabajadorFormDto registroForm() {
		return new TrabajadorFormDto();
	}

	@RequestMapping(value = WebUtils.URL_TRABAJADORES_REGISTRO, method = RequestMethod.GET)
	public String registro(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("GET REGISTRO TRABAJADORES");
		if (AjaxUtils.isAjaxRequest(requestedWith))
			return WebUtils.VISTA_TRABAJADORES_REGISTRO.concat(" :: registroForm");
		return WebUtils.VISTA_TRABAJADORES_REGISTRO;
	}

	@RequestMapping(value = WebUtils.URL_TRABAJADORES_REGISTRO, method = RequestMethod.POST)
	public String registro(@ModelAttribute("registro") @Valid TrabajadorFormDto user, BindingResult result,
			HttpServletRequest request, Errors errors, Model model, RedirectAttributes redirectAttributes,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		logger.info("POST REGISTRO TRABAJADORES");

		if (result.hasErrors()) {
			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADORES_REGISTRO.concat(" :: registroForm");
			return WebUtils.VISTA_TRABAJADORES_REGISTRO;
		}
		try {
			tservicio.registrar(user, WebUtils.getURLWithContextPath(request));
		} catch (DuplicateInstanceException e) {
			model = duplicateInstanceException(model, e);
			logger.info(model.toString());

			if (AjaxUtils.isAjaxRequest(requestedWith))
				return WebUtils.VISTA_TRABAJADORES_REGISTRO.concat(" :: registroForm");
			return WebUtils.VISTA_TRABAJADORES_REGISTRO;
		}
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			model.addAttribute("success", "ok");
			model.addAttribute("userAdd", user);
			return WebUtils.VISTA_TRABAJADORES.concat(" :: content");
		}
		redirectAttributes.addFlashAttribute("success", "ok");
		redirectAttributes.addFlashAttribute("userAdd", user);
		return "redirect:" + WebUtils.URL_TRABAJADORES_REAL;
	}

	/* FIN TRABAJADORES */

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(DuplicateInstanceException.class)
	public Model duplicateInstanceException(Model model, DuplicateInstanceException ex) {
		model.addAttribute("error", ex);
		model.addAttribute("type", "DuplicateInstanceExceptionRegister");
		model.addAttribute("key", ex.getKey());
		return model;
	}
}
