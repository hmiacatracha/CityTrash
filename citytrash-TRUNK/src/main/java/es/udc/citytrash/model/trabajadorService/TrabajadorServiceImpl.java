package es.udc.citytrash.model.trabajadorService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.DisabledException;
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
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.util.UriComponentsBuilder;

import es.udc.citytrash.controller.util.dtos.cuenta.CustomUserDetails;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorBusqFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorRegistroFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorUpdateFormDto;
import es.udc.citytrash.model.trabajador.Administrador;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.ConductorDao;
import es.udc.citytrash.model.trabajador.Recolector;
import es.udc.citytrash.model.trabajador.RecolectorDao;
import es.udc.citytrash.model.trabajador.Telefono;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.ExpiredTokenException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.dtos.email.Mail;
import es.udc.citytrash.util.dtos.usuario.Usuario;
import es.udc.citytrash.util.enums.CampoBusqTrabajador;
import es.udc.citytrash.util.enums.Idioma;
import es.udc.citytrash.util.enums.TipoTelefono;
import es.udc.citytrash.util.enums.TipoTrabajador;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Service("trabajadorService")
@Component
@Transactional
@PropertySources(value = { @PropertySource("classpath:config/mail.properties"),
		@PropertySource("classpath:config/urls.properties") })
public class TrabajadorServiceImpl implements TrabajadorService, UserDetailsService {

	@Value("${mail.from}")
	private String from;
	@Value("${mail.firma}")
	private String firma;
	@Value("${link.cuenta.activar}")
	private String urlActivar;
	@Value("${link.cuenta.recuperar}")
	private String urlRecuperar;
	@Value("${mail.plantilla.activar}")
	private String plantillaActivarCuenta;
	@Value("${mail.plantilla.recuperar}")
	private String plantillaRecuperarCuenta;
	@Value("${mail.asunto.activar.en}")
	private String activiarAsuntoEn;
	@Value("${mail.asunto.activar.es}")
	private String activiarAsuntoEs;
	@Value("${mail.asunto.activar.gal}")
	private String activarAsuntoGal;
	@Value("${mail.asunto.recuperar.es}")
	private String recuperarAsuntoEs;
	@Value("${mail.asunto.recuperar.en}")
	private String recuperarAsuntoEn;
	@Value("${mail.asunto.recuperar.gal}")
	private String recuperarAsuntoGal;

	@Autowired
	private TrabajadorDao trabajadorProfileDao;

	@Autowired
	private ConductorDao conductorDao;

	@Autowired
	private RecolectorDao recolectorDao;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	Configuration fmConfiguration;

	final Logger logger = LoggerFactory.getLogger(TrabajadorServiceImpl.class);

	@Override
	public Trabajador registrar(TrabajadorRegistroFormDto user) throws DuplicateInstanceException {
		Trabajador trabajador;
		/* the UUID class provides a simple means for generating unique ids */
		String token = UUID.randomUUID().toString();
		Calendar fechaExpiracionToken = Calendar.getInstance();
		fechaExpiracionToken.set(Calendar.HOUR_OF_DAY, 0);

		logger.info("fecha de expiracion (registrar usuario" + fechaExpiracionToken.toString());

		fechaExpiracionToken.add(Calendar.HOUR, 24);
		logger.info("fecha de expiracion (registrar usuario" + fechaExpiracionToken.toString());
		switch (user.getTipo()) {
		/* ADMINISTRADOR */
		case ADMIN:
			trabajador = new Administrador(user.getDocumento(), user.getNombre(), user.getApellidos(),
					dateToCalendar(user.getFechaNacimiento()), user.getEmail(), token, fechaExpiracionToken,
					user.getIdioma(), user.getVia(),
					esInteger(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null,
					esInteger(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null, user.getPuerta(),
					user.getProvincia(), user.getLocalidad(),
					esBigDecimal(user.getCp()) ? truncateToBigDecimaRouding(user.getCp()) : null,
					user.getRestoDireccion());
			break;
		/* CONDUCTOR */
		case CONDUCT:
			trabajador = new Conductor(user.getDocumento(), user.getNombre(), user.getApellidos(),
					dateToCalendar(user.getFechaNacimiento()), user.getEmail(), token, fechaExpiracionToken,
					user.getIdioma(), user.getVia(),
					esInteger(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null,
					esInteger(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null, user.getPuerta(),
					user.getProvincia(), user.getLocalidad(),
					esBigDecimal(user.getCp()) ? truncateToBigDecimaRouding(user.getCp()) : null,
					user.getRestoDireccion());
			break;
		/* default es RECOLECTOR */
		default:
			trabajador = new Recolector(user.getDocumento(), user.getNombre(), user.getApellidos(),
					dateToCalendar(user.getFechaNacimiento()), user.getEmail(), token, fechaExpiracionToken,
					user.getIdioma(), user.getVia(),
					esInteger(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null,
					esInteger(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null, user.getPuerta(),
					user.getProvincia(), user.getLocalidad(),
					esBigDecimal(user.getCp()) ? truncateToBigDecimaRouding(user.getCp()) : null,
					user.getRestoDireccion());
		}
		/* By default you can only add a telephone */
		if (user.getTelefono().trim().length() > 0) {
			Telefono telefono = new Telefono();
			telefono.setTipo(TipoTelefono.HOME);
			telefono.setNumero(user.getTelefono());
			trabajador.addTelefono(telefono);
		}

		/* Check if the email and the documentoId are not duplicate */
		try {
			trabajadorProfileDao.buscarTrabajadorPorEmail(user.getEmail());
			logger.info("Register emailDuplicado");
			throw new DuplicateInstanceException(user.getEmail(), TrabajadorRegistroFormDto.class.getName());
		} catch (InstanceNotFoundException e) {
			logger.debug("Register no emailDuplicado");
		}
		try {
			trabajadorProfileDao.buscarTrabajadorPorDocumentoId(user.getDocumento());
			logger.info("documento duplicado");
			throw new DuplicateInstanceException(user.getDocumento(), TrabajadorRegistroFormDto.class.getName());
		} catch (InstanceNotFoundException e) {
			logger.debug("Register documento no duplicado");
		}

		trabajadorProfileDao.guardar(trabajador);
		return trabajador;
	}

	@Override
	public Trabajador modificarTrabajador(TrabajadorUpdateFormDto user)
			throws InstanceNotFoundException, DuplicateInstanceException {

		Trabajador t = trabajadorProfileDao.buscarById(user.getId());
		/* Verificamos que no hay otro trabajador con el mismo documento */
		try {
			Trabajador aux = trabajadorProfileDao.buscarTrabajadorPorDocumentoId(user.getDocumento());
			if (t.getId() != aux.getId())
				throw new DuplicateInstanceException(user.getDocumento(), Trabajador.class.getName());
		} catch (InstanceNotFoundException e) {

		}

		logger.info("Verificamos que no hay otro trabajador con el mismo email");
		/* Verificamos que no hay otro trabajador con el mismo email */
		try {
			Trabajador aux = trabajadorProfileDao.buscarTrabajadorPorEmail(user.getEmail());
			if (t.getId() != aux.getId())
				throw new DuplicateInstanceException(user.getEmail(), Trabajador.class.getName());
		} catch (InstanceNotFoundException e) {

		}

		logger.info("pasa por aqui pruebas antes");
		/* Verificamos que no tenga numeros de telefonos duplicados */
		Map<String, Telefono> map = new HashMap<String, Telefono>();

		for (Telefono telefono : user.getTelefonos()) {
			// logger.info("contains " + telefono.getNumero() + " =>" +
			// map.containsKey(telefono.getNumero().trim()));
			if (map.containsKey(telefono.getNumero().trim())) {
				throw new DuplicateInstanceException(telefono.getNumero(), Telefono.class.getName());
			} else {
				map.put(telefono.getNumero().trim(), telefono);
			}
		}

		t.setDocId(user.getDocumento());
		t.setEmail(user.getEmail());
		t.setNombre(user.getNombre());
		t.setApellidos(user.getApellidos());
		t.setFecNac(dateToCalendar(user.getFechaNacimiento()));
		t.setIdioma(user.getIdioma());
		t.setNombreVia(user.getVia());
		t.setNumero(esInteger(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null);
		t.setPiso(esInteger(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null);
		t.setPuerta(user.getPuerta());
		t.setLocalidad(user.getLocalidad());
		t.setProvincia(user.getProvincia());
		t.setRestoDireccion(user.getRestoDireccion());
		t.setCp(esBigDecimal(user.getCp()) ? truncateToBigDecimaRouding(user.getCp()) : null);
		t.setActiveWorker(!user.isEstaDeBaja());
		t.setTelefonos(user.getTelefonos());
		/*
		 * for (Telefono tel : user.getTelefonos()) { if
		 * (tel.getNumero().trim().length() > 0) t.addTelefono(tel); }
		 */

		String tipo1 = t.getTrabajadorType();
		String tipo2 = user.getTipo().toString();

		if (tipo1 == null)
			tipo1 = "NONE";

		if (tipo2 == null)
			tipo2 = "NONE";

		if (!tipo1.toUpperCase().equals(tipo2.toUpperCase())) {
			logger.info("ANTES DE CAMBIAR TIPO");
			TipoTrabajador tipo = user.getTipo();
			String rol = "";

			switch (user.getTipo()) {
			case CONDUCT:
				rol = GlobalNames.ROL_CONDUCTOR;
				tipo = TipoTrabajador.CONDUCT;
				break;
			case ADMIN:
				rol = GlobalNames.ROL_ADMINISTRADOR;
				tipo = TipoTrabajador.ADMIN;
				break;
			default:
				rol = GlobalNames.ROL_RECOLECTOR;
				tipo = TipoTrabajador.RECOLEC;
			}

			String update = "UPDATE " + GlobalNames.TBL_TRABAJ + " SET " + GlobalNames.CAMPO_TRABAJADOR_DISCRIMINADOR
					+ " = '" + tipo.toString().toUpperCase() + "' , " + GlobalNames.CAMPO_ROL_BD + " = '" + rol
					+ "' WHERE TRABAJADOR_ID = " + t.getId();
			try {
				trabajadorProfileDao.createNativeQuery(update);
			} catch (Exception e) {
				logger.info("EXCEPTION AL INTENTAR CAMBIAR DE TIPO EN ACTUALIZAR TRABAJADOR => no guardamos cambios");
				logger.info("ERROR  =>" + e.getMessage());
				return t;
			}
			// t.setTrabajadorType(tipo.toString());
			// t.setRol(rol);
		}

		trabajadorProfileDao.guardar(t);
		return trabajadorProfileDao.buscarById(t.getId());
	}

	@Override
	public Trabajador buscarTrabajador(long id) throws InstanceNotFoundException {
		Trabajador t = trabajadorProfileDao.buscarById(id);
		logger.info("trabajador =>" + t.toString());
		
		return t;
	}

	@Override
	public Trabajador buscarTrabajadorByEmail(String email) throws InstanceNotFoundException {
		return trabajadorProfileDao.buscarTrabajadorPorEmail(email);
	}

	@Override
	public Trabajador buscarTrabajadorDocumento(String documento) throws InstanceNotFoundException {
		return trabajadorProfileDao.buscarTrabajadorPorDocumentoId(documento);
	}

	@Override
	public List<Recolector> buscarRecolectores(Boolean mostrarSoloActivos) {
		List<Recolector> lista = new ArrayList<Recolector>();
		lista = recolectorDao.buscarTrabajadoresOrderByApellidos(mostrarSoloActivos);
		return lista;
	}

	@Override
	public List<Conductor> buscarConductores(Boolean mostrarSoloActivos) {
		List<Conductor> lista = new ArrayList<Conductor>();
		lista = conductorDao.buscarTrabajadoresOrderByApellidos(mostrarSoloActivos);
		return lista;
	}

	@Override
	public List<Trabajador> buscarTrabajadores(Boolean mostrarSoloActivos) {
		List<Trabajador> lista = new ArrayList<Trabajador>();
		lista = trabajadorProfileDao.buscarTrabajadoresOrderByApellidos(mostrarSoloActivos);
		return lista;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Trabajador> buscarTrabajadores(Pageable pageable, TrabajadorBusqFormDto formBusqueda)
			throws FormBusquedaException {
		TipoTrabajador tipo = TipoTrabajador.NONE;
		CampoBusqTrabajador campo = CampoBusqTrabajador.nombre;

		Page<Trabajador> trabajadores = null;

		/* Convertimos el tipo de trabajador para hacer el filtro */
		try {
			tipo = TipoTrabajador.valueOf(formBusqueda.getTipo().toString());
			logger.info("TipoTrabajador ok " + tipo.getValue());
		} catch (IllegalArgumentException e) {
			throw new FormBusquedaException(e);
		} catch (NullPointerException e) {
			throw new FormBusquedaException(e);
		}

		/* Convertimos el campo por el que buscar las palabras claves */
		try {
			campo = CampoBusqTrabajador.valueOf(formBusqueda.getCampo());
			logger.info("CampoBusqTrabajador ok " + campo.getValue());
		} catch (IllegalArgumentException e) {
			throw new FormBusquedaException(e);
		} catch (NullPointerException e) {
			throw new FormBusquedaException(e);
		}

		if ((formBusqueda.getBuscar().trim()).length() > 0) {
			logger.error("form de busqueda campo=>" + formBusqueda.getCampo());
			logger.error("form de busqueda campo=>" + formBusqueda.getCampo());

			switch (campo) {
			case documento:
				trabajadores = trabajadorProfileDao.buscarTrabajadoresPorDocumentoYTipo(pageable,
						formBusqueda.getBuscar(), tipo, formBusqueda.getMostrarTodosLosTrabajadores());
				return trabajadores;
			case nombre:
				trabajadores = trabajadorProfileDao.buscarTrabajadoresPorNombreApellidosYTipo(pageable,
						formBusqueda.getBuscar(), tipo, formBusqueda.getMostrarTodosLosTrabajadores());
				return trabajadores;
			case cp:
				trabajadores = trabajadorProfileDao.buscarTrabajadoresPorCpYTipo(pageable, formBusqueda.getBuscar(),
						tipo, formBusqueda.getMostrarTodosLosTrabajadores());
				return trabajadores;
			case telefono:
				trabajadores = trabajadorProfileDao.buscarTrabajadoresPorTelefonosYTipo(pageable,
						formBusqueda.getBuscar(), tipo, formBusqueda.getMostrarTodosLosTrabajadores());
				return trabajadores;
			case email:
				trabajadores = trabajadorProfileDao.buscarTrabajadoresPorEmailYTipo(pageable, formBusqueda.getBuscar(),
						tipo, formBusqueda.getMostrarTodosLosTrabajadores());
				return trabajadores;
			default:
				trabajadores = trabajadorProfileDao.buscarTrabajadores(pageable, tipo,
						formBusqueda.getMostrarTodosLosTrabajadores());
				return trabajadores;
			}
		}
		trabajadores = trabajadorProfileDao.buscarTrabajadores(pageable, tipo,
				formBusqueda.getMostrarTodosLosTrabajadores());
		return trabajadores;
	}

	@Override
	public void eliminarTelefono(long trabajadorId, Telefono tel) throws InstanceNotFoundException {
		Trabajador t = trabajadorProfileDao.buscarById(trabajadorId);

		if (tel != null) {
			if (!t.getTelefonos().contains(tel))
				throw new InstanceNotFoundException(tel.getNumero().toString(), Telefono.class.getName());
			else
				t.removeTelefono(tel);
		} else
			throw new InstanceNotFoundException(tel.getNumero().toString(), Telefono.class.getName());
	}

	@Override
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		/*
		 * https://www.boraji.com/spring-security-5-custom-userdetailsservice-
		 * example
		 */
		Trabajador t;

		try {
			t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);

			Usuario perfil = new Usuario(t.getId(), t.getNombre(), t.getApellidos(), t.getEmail(), t.getPassword(),
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
		ahora.set(Calendar.HOUR_OF_DAY, 0);
		logger.info("fecha de ahora=>" + ahora);
		logger.info("fecha de expiracion token=>" + t.getFechaExpiracionToken());

		if (ahora.after(t.getFechaExpiracionToken())) {
			throw new ExpiredTokenException(t.getFechaExpiracionToken());
		}

		if (!t.isActiveWorker()) {
			logger.info("pasa el if? 1 => " + ahora.after(t.getFechaExpiracionToken()));
			throw new DisabledException("User has been disabled.");
		}

		Usuario perfil = new Usuario(t.getId(), t.getNombre(), t.getApellidos(), t.getEmail(), t.getToken(),
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
		ahora.set(Calendar.HOUR_OF_DAY, 0);
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
		fechaExpiracion.set(Calendar.HOUR_OF_DAY, 0);
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

	@Override
	public void activacionCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl) {
		Map<String, Object> model = new HashMap<String, Object>();
		SimpleDateFormat sdf;
		Mail mail = new Mail();
		logger.info("enviar email  registro " + email);
		try {
			model.put("nombre", nombre);
			model.put("email", email);
			model.put("apellidos", apellidos);
			model.put("firma", firma);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appUrl + urlActivar).queryParam("id", id)
					.queryParam("token", token).queryParam("lang", lang.name());
			String uri = builder.build().encode().toUriString();
			model.put("activar_url", uri);
			mail.setMailTo(email);
			mail.setMailFrom("no-reply@citytrash.com");
			mail.setMailCc(from);

			switch (lang) {
			case es:
				sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", new Locale("es_ES"));
				mail.setMailSubject(activiarAsuntoEs);
				model.put("titulo", activiarAsuntoEs);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				break;
			/*
			 * case gal: sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss", new
			 * Locale("gal_ES")); mail.setMailSubject(activarAsuntoGal);
			 * model.put("titulo", activarAsuntoGal);
			 * model.put("fechaExpiracion",
			 * sdf.format(fechaExpiracionToken.getTime())); break;
			 */
			default:
				sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
				mail.setMailSubject(activiarAsuntoEn);
				model.put("titulo", activiarAsuntoEn);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				lang = Idioma.en;
			}
			mail.setMailContent(getContenidoPlantilla(this.plantillaActivarCuenta + "_" + lang.name() + ".ftl", model));

			enviarEmail(mail);
		} catch (TemplateNotFoundException e) {
			try {
				model.put("motivo", "Activar Cuenta de " + email);
				mail.setMailSubject("Error al intentar activar la cuenta");
				mail.setMailTo(from);
				mail.setMailFrom("no-reply@citytrash.com");
				mail.setMailCc(from);
				mail.setMailContent(getContenidoPlantilla("error.ftl", model));
				enviarEmail(mail);
			} catch (IOException | TemplateException | MessagingException e1) {
			}
		} catch (MessagingException | IOException | TemplateException e) {

		}
	}

	@Override
	public void recuperarCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl) {
		Map<String, Object> model = new HashMap<String, Object>();
		SimpleDateFormat sdf;// new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		Mail mail = new Mail();

		logger.info("enviar email  registro " + email);
		try {
			model.put("nombre", nombre);
			model.put("email", email);
			model.put("apellidos", apellidos);
			model.put("firma", firma);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appUrl + urlRecuperar)
					.queryParam("id", id).queryParam("token", token).queryParam("lang", lang.name());
			String uri = builder.build().encode().toUriString();
			model.put("recuperar_url", uri);
			mail.setMailTo(email);
			mail.setMailFrom("no-reply@citytrash.com");
			mail.setMailCc(from);

			switch (lang) {
			case es:
				sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", new Locale("es_ES"));
				mail.setMailSubject(recuperarAsuntoEs);
				model.put("titulo", recuperarAsuntoEs);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				break;
			/*
			 * case gal: sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", new
			 * Locale("gal_ES")); mail.setMailSubject(recuperarAsuntoGal);
			 * model.put("titulo", recuperarAsuntoEs);
			 * model.put("fechaExpiracion",
			 * sdf.format(fechaExpiracionToken.getTime())); break;
			 */
			default:
				sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
				mail.setMailSubject(recuperarAsuntoEn);
				model.put("titulo", recuperarAsuntoEn);
				model.put("fechaExpiracion", sdf.format(fechaExpiracionToken.getTime()));
				lang = Idioma.en;
			}

			mail.setMailContent(
					getContenidoPlantilla(this.plantillaRecuperarCuenta + "_" + lang.name() + ".ftl", model));
			enviarEmail(mail);

		} catch (TemplateNotFoundException e) {
			try {
				model.put("motivo", "Recuperar Cuenta de " + email);
				mail.setMailSubject("Error al intentar recuperar la cuenta");
				mail.setMailTo(from);
				mail.setMailFrom("no-reply@citytrash.com");
				mail.setMailContent(getContenidoPlantilla("error.ftl", model));
				enviarEmail(mail);
			} catch (IOException | TemplateException | MessagingException e1) {
			}
		} catch (MessagingException | IOException | TemplateException e) {
			e.printStackTrace();
		}
	}

	private void enviarEmail(Mail mail) throws MessagingException {
		logger.info("Enviar email");
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setSubject(mail.getMailSubject());
		mimeMessageHelper.setFrom(mail.getMailFrom());
		mimeMessageHelper.setTo(mail.getMailTo());
		mimeMessageHelper.setText(mail.getMailContent(), true);
		mailSender.send(mimeMessageHelper.getMimeMessage());
	}

	private String getContenidoPlantilla(String templateName, Map<String, Object> model)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
			TemplateException {

		logger.info("Coger contenio de la plantilla ");
		StringBuffer content = new StringBuffer();
		content.append(
				FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateName), model));
		return content.toString();
	}

	private static boolean esInteger(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}

	private static boolean esBigDecimal(String cadena) {
		try {
			Double.parseDouble(cadena);
			BigDecimal bigDecimal = new BigDecimal(cadena);
			return true;
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}

	// Convert Date to Calendar
	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.setTime(date);
		return calendar;
	}

	private static BigDecimal truncateToBigDecimaRouding(String text) {
		try {
			BigDecimal bigDecimal = new BigDecimal(text);
			if (bigDecimal.scale() > 2)
				bigDecimal = new BigDecimal(text).setScale(2, RoundingMode.HALF_UP);
			return bigDecimal.stripTrailingZeros();
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}

	@Override
	public void cambiarEstadoTrabajador(long trabajadorId) throws InstanceNotFoundException {
		Trabajador t = trabajadorProfileDao.buscarById(trabajadorId);
		t.setActiveWorker(!t.isActiveWorker());
		trabajadorProfileDao.guardar(t);
	}
}
