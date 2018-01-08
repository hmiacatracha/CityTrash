package es.udc.citytrash.business.service.trabajador;

import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.udc.citytrash.business.entity.trabajador.Administrador;
import es.udc.citytrash.business.entity.trabajador.Conductor;
import es.udc.citytrash.business.entity.trabajador.Recolector;
import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.repository.trabajador.TrabajadorDao;
import es.udc.citytrash.business.service.email.EmailNotificacionesService;
import es.udc.citytrash.business.util.excepciones.ActiveCountException;
import es.udc.citytrash.business.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.business.util.excepciones.InactiveCountException;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.business.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.business.util.excepciones.TokenInvalidException;
import es.udc.citytrash.controller.util.formularios.ActivarCuentaForm;
import es.udc.citytrash.controller.util.formularios.TrabajadorForm;

@Service("trabajadorService")
@Transactional
public class TrabajadorServiceImpl implements TrabajadorService {

	@Autowired
	EmailNotificacionesService emailService;

	@Autowired
	private TrabajadorDao trabajadorProfileDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	final Logger logger = LoggerFactory.getLogger(TrabajadorServiceImpl.class);

	@Override
	public Trabajador buscarTrabajadorEmail(String email) throws InstanceNotFoundException {
		return trabajadorProfileDao.buscarTrabajadorPorEmail(email);
	}

	@Override
	public Trabajador buscarTrabajadorDocumento(String documento) throws InstanceNotFoundException {
		return trabajadorProfileDao.buscarTrabajadorPorDocumentoId(documento);
	}

	@Override
	public void recuperarCuenta(String email, String appUrl) throws InstanceNotFoundException {
		String token = UUID.randomUUID().toString();
		Trabajador t;

		try {
			t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);

			if (emailService.recuperarCuentaEmail(t.getNombre(), t.getApellidos(), t.getEmail(), token, appUrl)) {
				t.setToken(token);
				trabajadorProfileDao.save(t);
			}

		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(email, Trabajador.class.getName());
		}
	}

	public Trabajador registrar(TrabajadorForm user, String appUrl) throws DuplicateInstanceException {
		Trabajador trabajador;
		/* the UUID class provides a simple means for generating unique ids */
		String token = UUID.randomUUID().toString();
		String lang = Locale.getDefault().getLanguage();
		String rol = "";

		logger.info("Register controller 0");
		logger.info("Register URL-> " + appUrl);
		/* Check if the email and the documentoId are not duplicate */
		try {
			trabajadorProfileDao.buscarTrabajadorPorEmail(user.getEmail());
			logger.info("Register emailDuplicado");
			throw new DuplicateInstanceException(user.getEmail(), TrabajadorForm.class.getName());
		} catch (InstanceNotFoundException e) {
			logger.info("Register no emailDuplicado");
		}

		try {
			trabajadorProfileDao.buscarTrabajadorPorDocumentoId(user.getDocumento());
			logger.info("documento duplicado");
			throw new DuplicateInstanceException(user.getDocumento(), TrabajadorForm.class.getName());
		} catch (InstanceNotFoundException e) {
			logger.info("Register documento no duplicado");
		}

		switch (user.getTipo()) {
		/* ADMINISTRADOR */
		case ADMIN:
			rol = "ROLE_ADMIN";
			Administrador admin = new Administrador(user.getDocumento(), user.getNombre(), user.getApellidos(), rol,
					user.getEmail(), token);
			admin.setIdioma(lang);
			admin.setNumero(Integer.parseInt(user.getNumero()));
			admin.setPiso(user.getPiso());
			admin.setPuerta(user.getPuerta());
			admin.setLocalidad(user.getLocalidad());
			admin.setProvincia(user.getProvincia());
			
			if (emailService.activacionCuentaEmail(user.getNombre(), user.getApellidos(), user.getEmail(), token,
					appUrl)) {
				trabajadorProfileDao.save(admin);
			}
			trabajador = (Trabajador) admin;
			break;
		/* CONDUCTOR */
		case CONDUCT:
			rol = "ROLE_USER";
			Conductor conductor = new Conductor(user.getDocumento(), user.getNombre(), user.getApellidos(), rol,
					user.getEmail(), token);
			if (emailService.activacionCuentaEmail(user.getNombre(), user.getApellidos(), user.getEmail(), token,
					appUrl)) {
				trabajadorProfileDao.save(conductor);
			}
			trabajador = (Trabajador) conductor;
			break;
		/* RECOLECTOR */
		default:
			rol = "ROLE_USER";
			Recolector recolector = new Recolector(user.getDocumento(), user.getNombre(), user.getApellidos(), rol,
					user.getEmail(), token);
			if (emailService.activacionCuentaEmail(user.getNombre(), user.getApellidos(), user.getEmail(), token,
					appUrl)) {
				trabajadorProfileDao.save(recolector);
			}
			trabajador = (Trabajador) recolector;
			break;
		}
		return trabajador;
	}

	@Override
	public Trabajador loguearsePorPassword(String email, String password, boolean estaEncriptada)
			throws InstanceNotFoundException, PasswordInvalidException, InactiveCountException {

		logger.info("loguearse por password email=>" + email);
		logger.info("loguearse por password contrasena=>" + password);

		Trabajador t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);
		String password_guardada = t.getPassword();

		if (estaEncriptada) {
			if (!password.equals(password_guardada)) {
				throw new PasswordInvalidException(email);
			}
		} else if (!passwordEncoder.matches(password, password_guardada)) {
			logger.info("loguearse por password contrasena guardada=>" + password_guardada);
			logger.info("loguearse por password contrasena guardada=>" + password);
			throw new PasswordInvalidException(email);
		}

		if (!t.isEnabledCount()) {
			throw new InactiveCountException(email);
		}
		return t;
	}

	@Override
	public Trabajador loguearsePorToken(String token) throws TokenInvalidException {

		Trabajador t = trabajadorProfileDao.buscarTrabajadorPorToken(token);
		return t;
	}

	@Override
	public void actualizarPasswordPorToken(String email, String token, String password)
			throws InstanceNotFoundException, TokenInvalidException {

		Trabajador t = trabajadorProfileDao.buscarTrabajadorPorEmail(email);
		if (!StringUtils.hasText(token) || !token.equals(t.getToken())) {
			throw new TokenInvalidException(token, ActivarCuentaForm.class.getName());
		}

		logger.info("password => " + password);
		t.setPassword(passwordEncoder.encode(password));
		t.setEnabledCount(true);
		logger.info("password encode => " + t.getPassword());
		t.setToken(null);
		trabajadorProfileDao.save(t);
	}
}
