package es.udc.citytrash.model.usuarioService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.controller.cuenta.CustomUserDetails;
import es.udc.citytrash.controller.util.dtos.PerfilDto;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.util.excepciones.ExpiredTokenException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.Idioma;

@Service("userService")
@Component
@Transactional
public class UsuarioServiceImpl implements UserDetailsService, UsuarioService {

	@Autowired
	private TrabajadorDao trabajadorProfileDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Override
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		/*
		 * https://www.boraji.com/spring-security-5-custom-userdetailsservice-
		 * example
		 */
		Trabajador t;

		try {
			t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);

			PerfilDto perfil = new PerfilDto(t.getId(), t.getNombre(), t.getApellidos(), t.getEmail(), t.getPassword(),
					t.getIdioma(), t.isEnabledCount(), t.isActiveWorker());

			CustomUserDetails userDetails = new CustomUserDetails(perfil, this.getAuthorities(t.getRol(), false));

			return userDetails;

		} catch (InstanceNotFoundException e) {
			throw new UsernameNotFoundException("No user found with email: " + email);
		}
	}

	@Override
	public Authentication loguearsePorIdToken(long id, String token)
			throws TokenInvalidException, ExpiredTokenException, DisabledException {
		Trabajador t = trabajadorProfileDao.buscarTrabajadorIdToken(id, token);
		Calendar ahora = Calendar.getInstance();

		logger.info("fecha de ahora=>" + ahora);
		logger.info("fecha de expiracion token=>" + t.getFechaExpiracionToken());

		if (ahora.after(t.getFechaExpiracionToken())) {
			throw new ExpiredTokenException(t.getFechaExpiracionToken());
		}

		if (!t.isActiveWorker()) {
			logger.info("pasa el if? 1 => " + ahora.after(t.getFechaExpiracionToken()));
			throw new DisabledException("User has been disabled.");
		}

		PerfilDto perfil = new PerfilDto(t.getId(), t.getNombre(), t.getApellidos(), t.getEmail(), t.getToken(),
				t.getIdioma(), t.isEnabledCount(), t.isActiveWorker());

		CustomUserDetails principal = new CustomUserDetails(perfil, getAuthorities(t.getRol(), true));
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
				principal.getAuthorities());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		logger.info("se autentica");
		return authentication;
	}

	@Override
	public void cambiarPassword(long trabajadorId, String antiguaPassword, String nuevaPassword)
			throws InstanceNotFoundException, PasswordInvalidException {

		Trabajador t = trabajadorProfileDao.buscarById(trabajadorId);

		if (!passwordEncoder.matches(antiguaPassword, t.getPassword())) {
			throw new PasswordInvalidException(t.getEmail());
		}

		t.setPassword(passwordEncoder.encode(nuevaPassword));
		trabajadorProfileDao.guardar(t);
	}

	@Override
	public void reiniciarPassword(long trabajadorId, String nuevaPassword) throws InstanceNotFoundException {
		Trabajador t = trabajadorProfileDao.buscarById(trabajadorId);
		Calendar ahora = Calendar.getInstance();

		/* Actualizamos la fecha de activacion, en caso de ser la primera vez */
		if (t.getFechaActivacion() == null) {
			logger.info("fecha activacion null = true => ");
			t.setFechaActivacion(ahora);
		}

		t.setPassword(passwordEncoder.encode(nuevaPassword));
		t.setEnabledCount(true);
		t.setFechaExpiracionToken(ahora);
		trabajadorProfileDao.guardar(t);
	}

	@Override
	public void recuperarCuenta(String email) throws InstanceNotFoundException, DisabledException {
		String token = UUID.randomUUID().toString();
		Calendar fechaExpiracion = Calendar.getInstance();
		fechaExpiracion.add(Calendar.MINUTE, 20);
		Trabajador t;

		try {

			t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);

			if (!t.isActiveWorker()) {
				throw new DisabledException("User has been disabled.");
			}
			t.setToken(token);
			t.setFechaExpiracionToken(fechaExpiracion);
			trabajadorProfileDao.guardar(t);

		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(email, Trabajador.class.getName());
		}
	}

	@Override
	public Trabajador buscarTrabajadorPorEmail(String email) throws InstanceNotFoundException {
		return trabajadorProfileDao.buscarTrabajadorPorEmail(email);
	}

	@Override
	public void cambiarIdioma(String email, Idioma lang) throws InstanceNotFoundException {
		Trabajador t;
		t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);
		t.setIdioma(lang);

	}

	@Override
	public Idioma obtenerIdiomaPreferencia(long trabajadorId) throws InstanceNotFoundException {
		Trabajador t;
		t = trabajadorProfileDao.buscarById(trabajadorId);
		return t.getIdioma();
	}

	private List<SimpleGrantedAuthority> getAuthorities(String role, Boolean loginPorToken) {
		List<SimpleGrantedAuthority> authList = new ArrayList<>();
		logger.info("getAuthorities LOGIN POR TOKEN:" + loginPorToken);

		if (role != null && role.trim().length() > 0) {
			authList.add(new SimpleGrantedAuthority(role));
		}

		if (loginPorToken == true) {
			authList.add(new SimpleGrantedAuthority(GlobalNames.ROL_REINICIAR_PASSWORD));
		}

		logger.info("getAuthorities:" + authList.toString());
		return authList;
	}

}
