package es.udc.citytrash.controller.cuenta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.anotaciones.UsuarioActual;
import es.udc.citytrash.controller.util.dtos.ReiniciarPasswordFormDto;
import es.udc.citytrash.controller.util.dtos.CambiarPasswordFormDto;
import es.udc.citytrash.controller.util.dtos.IdiomaFormDto;
import es.udc.citytrash.model.emailService.EmailNotificacionesService;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.usuarioService.UsuarioService;
import es.udc.citytrash.model.util.excepciones.ExpiredTokenException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.Idioma;

@Controller
@RequestMapping("cuenta")
public class CuentaController {

	@Autowired
	UsuarioService cuentaServicio;

	@Autowired
	EmailNotificacionesService emailService;

	final Logger logger = LoggerFactory.getLogger(CuentaController.class);

	/*
	 * Retrieve User Information in Spring Security
	 * http://www.baeldung.com/get-user-in-spring-security
	 * https://docs.spring.io/spring-security/site/docs/current/reference/html/
	 * mvc.html @AuthenticationPrincipal
	 * https://docs.spring.io/spring-security/site/docs/current/reference/html/
	 * mvc.html
	 * 
	 */

	/* ACTIVAR CUENTA */
	@PreAuthorize("isAnonymous()")
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CUENTA_ACTIVAR,
			WebUtils.REQUEST_MAPPING_CUENTA_RECUPERAR }, method = RequestMethod.GET)
	public String activarCuenta(@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "token", required = true) String token, Model model,
			RedirectAttributes redirectAttributes) {
		ReiniciarPasswordFormDto activarCuentaForm = new ReiniciarPasswordFormDto();
		model.addAttribute("activarCuentaForm", activarCuentaForm);
		model.addAttribute("token", token);

		try {
			cuentaServicio.loguearsePorIdToken(id, token);
		} catch (TokenInvalidException e) {
			redirectAttributes.addFlashAttribute("msg", "TokenInvalidoException");
			return "redirect:" + WebUtils.URL_LOGIN;
		} catch (ExpiredTokenException e) {
			redirectAttributes.addFlashAttribute("msg", "ExpiredTokenException");
			return "redirect:" + WebUtils.URL_LOGIN;
		} catch (DisabledException e) {
			redirectAttributes.addFlashAttribute("msg", "DisabledException");
			return "redirect:" + WebUtils.URL_LOGIN;
		}
		logger.info("TOKEN activar cuenta DESPUES2=> " + token);
		logger.info("TOKEN activar cuenta DESPUES2 url=> " + WebUtils.URL_CUENTA_CAMBIAR_CONTRASENA);
		return "redirect:" + WebUtils.URL_CUENTA_ACTUALIZAR_CONTRASENA;
	}

	@PreAuthorize("isAnonymous()")
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CUENTA_RESET_PASSWORD, method = RequestMethod.POST)
	public String recuperarCuenta(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		String email = request.getParameter("email");
		Trabajador t = null;
		logger.info("PASO1 URL:" + WebUtils.URL_CUENTA_RESET_PASSWORD);
		try {

			/* Recuperamos la cuenta */
			cuentaServicio.recuperarCuenta(email);

			/* Enviamos un correo de recuperacion */
			t = cuentaServicio.buscarTrabajadorPorEmail(email);
			emailService.recuperarCuentaEmail(t.getId(), t.getNombre(), t.getApellidos(), t.getEmail(), t.getToken(),
					t.getFechaExpiracionToken(), t.getIdioma(), WebUtils.getUrlWithContextPath(request));

			redirectAttributes.addFlashAttribute("titulo", "title_recuperar_cuenta");
			redirectAttributes.addFlashAttribute("mensaje", "mensaje_recuperar_cuenta(" + email + ")");
			redirectAttributes.addFlashAttribute("tipoAlerta", "success");

		} catch (InstanceNotFoundException e) {
			// model.addAttribute("err", e);
			model = InstanceNotFoundException(model, e);
			return WebUtils.VISTA_RECUPERAR_CUENTA;
		} catch (DisabledException e1) {
			model = DisabledException(model, e1);
			return WebUtils.VISTA_RECUPERAR_CUENTA;
		}
		redirectAttributes.addFlashAttribute("success", "ok");
		redirectAttributes.addFlashAttribute("email", email);
		return "redirect:" + WebUtils.URL_CUENTA_RESET_PASSWORD;
	}

	@PreAuthorize("isAnonymous()")
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CUENTA_RESET_PASSWORD }, method = RequestMethod.GET)
	public String recuperarCuenta() {
		logger.info("PASO1 URL:" + WebUtils.URL_CUENTA_RESET_PASSWORD);
		return WebUtils.VISTA_RECUPERAR_CUENTA;
	}

	/* REINICIAR Y CAMBIAR CONTRASEÑA */
	@ModelAttribute("reiniciarPasswordForm")
	public ReiniciarPasswordFormDto reiniciarPasswordForm() {
		return new ReiniciarPasswordFormDto();
	}

	/* REINICIAR PASSWORD GET */
	// @PreAuthorize("hasRole('ROLE_CHANGE_PASSWORD')")
	@PreAuthorize("hasRole('" + GlobalNames.ROL_REINICIAR_PASSWORD + "')")
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CUENTA_ACTUALIZAR_CONTRASENA, method = RequestMethod.GET)
	public String reiniciarPassword(Model model) {
		return WebUtils.VISTA_REINICIAR_CONTRASENA;
	}

	/* REINICIAR PASSWORD POST */
	@PreAuthorize("hasRole('" + GlobalNames.ROL_REINICIAR_PASSWORD + "')")
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CUENTA_ACTUALIZAR_CONTRASENA }, method = RequestMethod.POST)
	public String reiniciarPassword(@UsuarioActual CustomUserDetails usuario, Model model,
			@ModelAttribute("reiniciarPasswordForm") @Valid ReiniciarPasswordFormDto form, BindingResult result,
			RedirectAttributes redir) {
		logger.info("POST ACTIVAR CUENTA1");
		if (result.hasErrors()) {
			logger.info("PAGINA vista => " + WebUtils.VISTA_REINICIAR_CONTRASENA);
			return WebUtils.VISTA_REINICIAR_CONTRASENA;
		}
		try {

			cuentaServicio.reiniciarPassword(usuario.getPerfil().getId(), form.getPassword());
			/* Eliminar el rol REINICIAR PASSWORDPASSWORD */
			List<GrantedAuthority> updatedAuthorities = new ArrayList<>(usuario.getAuthorities());
			GrantedAuthority g = new SimpleGrantedAuthority(GlobalNames.ROL_REINICIAR_PASSWORD);
			updatedAuthorities.remove(g);
			Authentication newAuth = new UsernamePasswordAuthenticationToken(
					SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					SecurityContextHolder.getContext().getAuthentication().getCredentials(), updatedAuthorities);
			SecurityContextHolder.getContext().setAuthentication(newAuth);
			redir.addFlashAttribute("cambioCredenciales", "ok");

			logger.info("PAGINA REDIRECT => " + WebUtils.URL_HOME);
			return "redirect:" + WebUtils.URL_HOME;

		} catch (InstanceNotFoundException e) {
			model = InstanceNotFoundException(model, e);
			return WebUtils.VISTA_REINICIAR_CONTRASENA;
		}
	}

	/* REINICIAR Y CAMBIAR CONTRASEÑA */
	@ModelAttribute("cambiarPasswordForm")
	public CambiarPasswordFormDto cambiarPasswordForm() {
		return new CambiarPasswordFormDto();
	}

	/* Cambiar contraseña GET */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = WebUtils.REQUEST_MAPPING_CAMBIAR_PASSWORD, method = RequestMethod.GET)
	public String cambiarPassword(Model model) {
		return WebUtils.VISTA_CAMBIAR_CONTRASENA;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CAMBIAR_PASSWORD }, method = RequestMethod.POST)
	public String cambiarPassword(@UsuarioActual CustomUserDetails usuario, Model model,
			@ModelAttribute("cambiarPasswordForm") @Valid CambiarPasswordFormDto form, BindingResult result,
			RedirectAttributes redir) {
		logger.info("POST cambiar contraseña");
		model.addAttribute("cambiarPasswordForm", form);
		if (result.hasErrors()) {
			logger.info("PAGINA vista => " + WebUtils.VISTA_REINICIAR_CONTRASENA);
			return WebUtils.VISTA_CAMBIAR_CONTRASENA;
		}
		try {

			cuentaServicio.cambiarPassword(usuario.getPerfil().getId(), form.getPasswordAntigua(), form.getPassword());
			model.addAttribute("cambiarPasswordForm", new CambiarPasswordFormDto());
			model.addAttribute("cambioCredenciales", "ok");
			return WebUtils.VISTA_CAMBIAR_CONTRASENA;

		} catch (InstanceNotFoundException e) {
			model = InstanceNotFoundException(model, e);
			return WebUtils.VISTA_CAMBIAR_CONTRASENA;
		} catch (PasswordInvalidException e) {
			model = PasswordInvalidException(model, e);
			return WebUtils.VISTA_CAMBIAR_CONTRASENA;
		}
	}

	/* CAMBIO DE IDIOMA */
	@ModelAttribute("idiomaForm")
	public IdiomaFormDto idiomaForm(@UsuarioActual CustomUserDetails perfil) {
		Idioma idioma;
		if (perfil != null) {
			try {
				idioma = cuentaServicio.obtenerIdiomaPreferencia(perfil.getPerfil().getId());
			} catch (InstanceNotFoundException e) {
				idioma = Idioma.es;
			}
		} else
			idioma = Idioma.es;
		return new IdiomaFormDto(idioma);
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CUENTA_CAMBIO_IDIOMA }, method = RequestMethod.GET)
	public String cambiarIdioma(Model model) {
		return WebUtils.VISTA_CAMBIAR_IDIOMA;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = { WebUtils.REQUEST_MAPPING_CUENTA_CAMBIO_IDIOMA }, method = RequestMethod.POST)
	public String cambiarIdioma(@UsuarioActual CustomUserDetails usuario,
			@ModelAttribute("idiomaForm") @Valid IdiomaFormDto idiomaForm, BindingResult result,
			HttpServletRequest request, Errors errors, Model model, HttpServletResponse response) {

		if (result.hasErrors()) {
			model.addAttribute("error", true);
			return WebUtils.VISTA_CAMBIAR_IDIOMA;
		}

		try {
			cuentaServicio.cambiarIdioma(usuario.getUsername(), idiomaForm.getIdioma());

		} catch (InstanceNotFoundException e) {
			model = InstanceNotFoundException(model, e);
			return WebUtils.VISTA_CAMBIAR_IDIOMA;
		}
		usuario.getPerfil().setIdioma(idiomaForm.getIdioma());
		RequestContextUtils.getLocaleResolver(request).setLocale(request, response,
				new Locale(idiomaForm.getIdioma().toString().toLowerCase()));
		model.addAttribute("success", true);
		logger.info("CAMBIO DE IDIOMA SUCCESS");
		return WebUtils.VISTA_CAMBIAR_IDIOMA;
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
	@ExceptionHandler(PasswordInvalidException.class)
	public Model PasswordInvalidException(Model model, PasswordInvalidException ex) {
		logger.info("ExceptionHandler PasswordInvalidException");
		model.addAttribute("error", ex);
		model.addAttribute("type", "PasswordInvalidException");
		model.addAttribute("key", ex.getMessage());
		return model;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(DisabledException.class)
	public Model DisabledException(Model model, DisabledException ex) {
		model.addAttribute("error", ex);
		model.addAttribute("type", "DisabledException");
		model.addAttribute("key", "");
		return model;
	}
}