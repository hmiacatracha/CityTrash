package es.udc.citytrash.business.service.cuenta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.business.entity.idioma.Idioma;
import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.repository.trabajador.TrabajadorDao;
import es.udc.citytrash.business.service.email.EmailNotificacionesService;
import es.udc.citytrash.business.util.excepciones.ExpiredTokenException;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.business.util.excepciones.TokenInvalidException;
import es.udc.citytrash.controller.util.CustomUserDetails;
import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.controller.util.dtos.TrabajadoDto;

@Service("userService")
@Component
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private TrabajadorDao trabajadorProfileDao;

	@Autowired
	EmailNotificacionesService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		/*
		 * https://www.boraji.com/spring-security-5-custom-userdetailsservice-
		 * example
		 */
		logger.debug("PASA POR CustomUserDetailsService metodo loadUserByUsername");
		Trabajador t;

		try {
			t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);
			TrabajadoDto perfil = new TrabajadoDto(t.getId(), t.getNombre(), t.getApellidos(), t.getEmail(),
					t.getPassword(), t.getIdioma(), t.isEnabledCount(), t.isActiveWorker());

			CustomUserDetails userDetails = new CustomUserDetails(perfil, this.getAuthorities(t.getRol(), false));

			return userDetails;

		} catch (InstanceNotFoundException e) {
			throw new UsernameNotFoundException("No user found with email: " + email);
		}
	}

	@Override
	public Authentication loguearsePorIdToken(long id, String token)
			throws TokenInvalidException, ExpiredTokenException {
		Trabajador t = trabajadorProfileDao.buscarTrabajadorIdToken(id, token);
		Calendar fechaVencimientoToken = t.getFechaExpiracionToken();
		Calendar ahora = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

		logger.info("fecha de vencimiento=>" + sdf.format(fechaVencimientoToken.getTime()));
		logger.info("fecha de ahora=>" + sdf.format(ahora.getTime()));
		logger.info("pasa el if? => " + ahora.after(fechaVencimientoToken));

		if (ahora.after(fechaVencimientoToken)) {
			throw new ExpiredTokenException(fechaVencimientoToken);
		}

		TrabajadoDto perfil = new TrabajadoDto(t.getId(), t.getNombre(), t.getApellidos(), t.getEmail(), t.getToken(),
				t.getIdioma(), t.isEnabledCount(), t.isActiveWorker());

		CustomUserDetails principal = new CustomUserDetails(perfil, getAuthorities(t.getRol(), true));
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
				principal.getAuthorities());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		return authentication;
	}

	@Override
	public void cambiarPassword(String email, String password) throws InstanceNotFoundException {
		Trabajador t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);
		Calendar ahora = Calendar.getInstance();

		logger.info("password => " + password);
		t.setPassword(passwordEncoder.encode(password));
		t.setEnabledCount(true);
		logger.info("password encode => " + t.getPassword());
		t.setFechaExpiracionToken(ahora);
		logger.info("password encode => " + t.getPassword());

		if (t.getFechaActivacion() == null) {
			logger.info("fecha activacion null = true => ");
			Calendar calendar = Calendar.getInstance();
			t.setFechaActivacion(calendar);
		}
		trabajadorProfileDao.save(t);
	}

	@Override
	public void recuperarCuenta(String email, String appUrl) throws InstanceNotFoundException {
		String token = UUID.randomUUID().toString();
		Calendar fechaExpiracion = Calendar.getInstance();
		fechaExpiracion.add(Calendar.MINUTE, 20);
		Trabajador t;

		try {

			t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);
			t.setToken(token);
			t.setFechaExpiracionToken(fechaExpiracion);
			trabajadorProfileDao.save(t);
			emailService.recuperarCuentaEmail(t.getId(), t.getNombre(), t.getApellidos(), t.getEmail(), token,
					fechaExpiracion, t.getIdioma(), appUrl);

		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(email, Trabajador.class.getName());
		}
	}

	@Override
	public void cambiarIdioma(String email, Idioma lang) throws InstanceNotFoundException {
		Trabajador t;
		t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);
		t.setIdioma(lang);

	}

	private List<SimpleGrantedAuthority> getAuthorities(String role, Boolean loginPorToken) {
		List<SimpleGrantedAuthority> authList = new ArrayList<>();

		authList.add(new SimpleGrantedAuthority("ROLE_USER"));

		if (loginPorToken == true) {
			authList.add(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE"));
		}

		if (role != null && role.trim().length() > 0) {
			if (!role.equalsIgnoreCase("ROLE_USER")) {
				authList.add(new SimpleGrantedAuthority(role));
			}
		}
		return authList;
	}
}
