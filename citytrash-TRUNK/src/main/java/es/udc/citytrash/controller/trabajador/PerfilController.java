package es.udc.citytrash.controller.trabajador;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.service.trabajador.TrabajadorService;
import es.udc.citytrash.business.util.excepciones.InactiveCountException;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.business.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.business.util.excepciones.TokenInvalidException;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.formularios.ActivarCuentaForm;
import es.udc.citytrash.controller.util.formularios.LoginForm;

@Controller
public class PerfilController {

	private static final String PREFIX_VISTA = "trabajador/";
	private static final String VISTA_ACTIVAR = "activar";
	private static final String VISTA_LOGIN = "login";
	private static final String VISTA_RECUPERAR_CUENTA = "recuperarCuenta";
	private static final String VISTA_RESULTADO = "resultado/resultado";

	/* urls */
	private static final String URL_LOGIN_LOGOUT = "login?logout";
	private static final String URL_ACTIVAR = "/cuenta/activar/{token}";
	private static final String URL_ACTIVAR_SUBMIT = "/cuenta/activar-submit/{token}";
	private static final String URL_RECUPERAR_CUENTA_SUBMIT = "/cuenta/recuperar/{token}";
	private static final String URL_RECUPERAR_CUENTA_RESULT = "/cuenta/recuperar/result";
	private static final String URL_LOGIN = "/login";
	private static final String URL_LOGIN_SUBMIT = "/login-submit";
	private static final String URL_LOGOUT = "/logout";
	private static final String URL_RECUPERAR_CUENTA = "/cuenta/recuperar";

	@Autowired
	TrabajadorService tservicio;

	// @Autowired
	// private PasswordEncoder passwordEncoder;

	final Logger logger = LoggerFactory.getLogger(PerfilController.class);

	/**
	 * 
	 */
	@RequestMapping(value = { URL_ACTIVAR, URL_ACTIVAR_SUBMIT }, method = RequestMethod.GET)
	public String activarCuenta(@PathVariable("token") String token, Model model,
			RedirectAttributes redirectAttributes) {
		logger.info("activar cuenta controler");
		Trabajador t;
		ActivarCuentaForm activarForm = new ActivarCuentaForm();
		model.addAttribute("token", token);
		model.addAttribute("activarCuentaForm", activarForm);
		try {
			t = tservicio.loguearsePorToken(token);
		} catch (TokenInvalidException e) {
			logger.info("tokenINvalidoException ERROR ENTRA");
			redirectAttributes.addFlashAttribute("msg", "TokenInvalidoException");
			return "redirect:/" + URL_ACTIVAR;
		}
		logger.info("PREFIX_VISTA + VISTA_ACTIVAR");
		return PREFIX_VISTA + VISTA_ACTIVAR;
	}

	/**
	 * 
	 */
	@RequestMapping(value = URL_ACTIVAR_SUBMIT, method = RequestMethod.POST)
	public String activarCuenta(@PathVariable("token") String token,
			@ModelAttribute("activarCuentaForm") @Valid ActivarCuentaForm form, HttpServletRequest request, Model model,
			BindingResult result, Errors errors) {

		Trabajador t;

		logger.info("POST ACTIVAR CUENTA");
		logger.info("token=>" + token);
		logger.info("model=>" + model.asMap());

		if (result.hasErrors()) {
			logger.info("url:" + request.getContextPath());
			logger.info("coger todos los errore: " + result.getAllErrors());
			return PREFIX_VISTA + VISTA_ACTIVAR;
		}
		try {

			t = tservicio.loguearsePorToken(token);
			tservicio.actualizarPasswordPorToken(t.getEmail(), token, form.getPassword());
			User user = new User(t.getEmail(), t.getPassword(), !t.isEnabledCount(), true, true, true,
					Collections.singleton(new SimpleGrantedAuthority(t.getRol())));
			Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authentication);
			HttpSession session = request.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
			logger.debug("Logging in with {}", authentication.getPrincipal());
			logger.info("2 Session value for SPRING_SECURITY_CONTEXT: {}",
					session.getAttribute("SPRING_SECURITY_CONTEXT"));
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (TokenInvalidException e) {
			model = TokenInvalidoException(model, e);
			return PREFIX_VISTA + VISTA_ACTIVAR;
		} catch (InstanceNotFoundException e) {
			model = InstanceNotFoundException(model, e);
			return PREFIX_VISTA + VISTA_ACTIVAR;
		}
		return "redirect:/";
	}

	@RequestMapping(value = { URL_RECUPERAR_CUENTA }, method = RequestMethod.GET)
	public String recuperarCuenta() {
		logger.info("PASO1 URL:" + URL_RECUPERAR_CUENTA);
		return PREFIX_VISTA + VISTA_RECUPERAR_CUENTA;
	}

	@RequestMapping(value = URL_RECUPERAR_CUENTA, method = RequestMethod.POST)
	public String recuperarCuenta(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		String email = request.getParameter("email");
		logger.info("PASO1 URL:" + URL_RECUPERAR_CUENTA);

		try {
			tservicio.recuperarCuenta(email, WebUtils.getURLWithContextPath(request));
			redirectAttributes.addFlashAttribute("titulo", "title_recuperar_cuenta");
			redirectAttributes.addFlashAttribute("mensaje", "mensaje_recuperar_cuenta(" + email + ")");
			redirectAttributes.addFlashAttribute("tipoAlerta", "success");

		} catch (InstanceNotFoundException e) {
			model.addAttribute("err", e);
			return PREFIX_VISTA + VISTA_RECUPERAR_CUENTA;
		}
		return "redirect:" + URL_RECUPERAR_CUENTA_RESULT;
	}

	@RequestMapping(value = { URL_RECUPERAR_CUENTA_RESULT }, method = RequestMethod.GET)
	public String recuperarCuentaResultado(@ModelAttribute(value = "titulo") String title,
			@ModelAttribute(value = "mensaje") String mensaje,
			@ModelAttribute(value = "tipoAlerta") String tipoAlerta) {
		logger.info("PASO1 URL:" + URL_RECUPERAR_CUENTA_RESULT);
		return VISTA_RESULTADO;
	}

	@RequestMapping(value = { URL_LOGIN, URL_LOGIN_SUBMIT }, method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "msg", required = false) String msg, Model model) {
		logger.info("login_get_paso1");
		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		logger.info("login_get_paso2 loginForm:" + loginForm);
		if (error != null) {
			model.addAttribute("error", error);
		} else if (logout != null) {
			model.addAttribute("logout", logout);
		} else if (msg != null) {
			model.addAttribute("msg", msg);
		}
		logger.info("Mostrando pagina login");
		return PREFIX_VISTA + VISTA_LOGIN;
	}

	@RequestMapping(value = URL_LOGIN_SUBMIT, method = RequestMethod.POST)
	public String login(@ModelAttribute("loginForm") @Valid LoginForm form, BindingResult result,
			HttpServletRequest request, Errors errors, Model model) {
		logger.info("login_post_paso1");

		Trabajador t = null;
		if (result.hasErrors()) {
			logger.info("model=>" + model.asMap());
			return PREFIX_VISTA + VISTA_LOGIN;
		}
		try {
			logger.info("model=>" + model.asMap());
			t = tservicio.loguearsePorPassword(form.getEmail(), form.getPassword(), false);
			logger.info("model=>" + model.asMap());
			User user = new User(t.getEmail(), t.getPassword(), !t.isEnabledCount(), true, true, true,
					Collections.singleton(new SimpleGrantedAuthority(t.getRol())));
			Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authentication);
			HttpSession session = request.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
			logger.debug("Logging in with {}", authentication.getPrincipal());
			logger.info("2 Session value for SPRING_SECURITY_CONTEXT: {}",
					session.getAttribute("SPRING_SECURITY_CONTEXT"));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (InstanceNotFoundException | PasswordInvalidException | InactiveCountException e) {
			model.addAttribute("error", e);
			return PREFIX_VISTA + VISTA_LOGIN;
		}
		return "redirect:/";
	}

	@RequestMapping(value = URL_LOGOUT, method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/" + URL_LOGIN_LOGOUT;
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
	@ExceptionHandler(InactiveCountException.class)
	public Model InactiveCountException(Model model, InactiveCountException ex) {
		logger.info("ExceptionHandler TokenInvalidoException");
		model.addAttribute("error", ex);
		model.addAttribute("type", "TokenInvalidoException");
		model.addAttribute("key", ex.getMessage());
		return model;
	}
}
