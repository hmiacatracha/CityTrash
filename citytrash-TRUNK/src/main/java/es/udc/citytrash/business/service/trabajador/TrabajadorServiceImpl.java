package es.udc.citytrash.business.service.trabajador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.business.entity.trabajador.Administrador;
import es.udc.citytrash.business.entity.trabajador.Conductor;
import es.udc.citytrash.business.entity.trabajador.Recolector;
import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.repository.trabajador.TrabajadorDao;
import es.udc.citytrash.business.service.email.EmailNotificacionesService;
import es.udc.citytrash.business.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.controller.util.dtos.TrabajadorFormDto;

@Service("trabajadorService")
@Transactional
public class TrabajadorServiceImpl implements TrabajadorService {

	@Autowired
	EmailNotificacionesService emailService;

	@Autowired
	private TrabajadorDao trabajadorProfileDao;

	final Logger logger = LoggerFactory.getLogger(TrabajadorServiceImpl.class);

	@Override
	public Trabajador registrar(TrabajadorFormDto user, String appUrl) throws DuplicateInstanceException {
		Trabajador trabajador;
		/* the UUID class provides a simple means for generating unique ids */
		String token = UUID.randomUUID().toString();
		Calendar fechaExpiracionToken = Calendar.getInstance();
		fechaExpiracionToken.add(Calendar.HOUR, 24);

		/* Check if the email and the documentoId are not duplicate */
		try {
			trabajadorProfileDao.buscarTrabajadorPorEmail(user.getEmail());
			logger.info("Register emailDuplicado");
			throw new DuplicateInstanceException(user.getEmail(), TrabajadorFormDto.class.getName());
		} catch (InstanceNotFoundException e) {
			logger.info("Register no emailDuplicado");
		}

		try {
			trabajadorProfileDao.buscarTrabajadorPorDocumentoId(user.getDocumento());
			logger.info("documento duplicado");
			throw new DuplicateInstanceException(user.getDocumento(), TrabajadorFormDto.class.getName());
		} catch (InstanceNotFoundException e) {
			logger.info("Register documento no duplicado");
		}

		logger.info("trabajadorService paso1");
		switch (user.getTipo()) {
		/* ADMINISTRADOR */
		case ADMIN:
			Administrador admin = new Administrador(user.getDocumento(), user.getNombre(), user.getApellidos(),
					user.getEmail(), dateToCalendar(user.getFechaNacimiento()), token, fechaExpiracionToken);
			admin.setIdioma(user.getIdioma());
			admin.setNombreVia(user.getVia());
			admin.setNumero(esInteger(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null);
			admin.setPiso(esInteger(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null);
			admin.setPuerta(user.getPuerta());
			admin.setLocalidad(user.getLocalidad());
			admin.setProvincia(user.getProvincia());
			admin.setRestoDireccion(user.getRestoDireccion());
			logger.info("trabajadorService paso2");
			admin.setTelefono(esBigDecimal(user.getTelefono()) ? truncateToBigDecimaRouding(user.getTelefono()) : null);
			admin.setCp(esBigDecimal(user.getCp()) ? truncateToBigDecimaRouding(user.getCp()) : null);
			logger.info("trabajadorService paso3");
			trabajadorProfileDao.guardar(admin);
			emailService.activacionCuentaEmail(admin.getId(), admin.getNombre(), admin.getApellidos(), admin.getEmail(),
					token, fechaExpiracionToken, user.getIdioma(), appUrl);
			trabajador = (Trabajador) admin;
			break;
		/* CONDUCTOR */
		case CONDUCT:
			Conductor conductor = new Conductor(user.getDocumento(), user.getNombre(), user.getApellidos(),
					user.getEmail(), dateToCalendar(user.getFechaNacimiento()), token, fechaExpiracionToken);
			conductor.setIdioma(user.getIdioma());
			conductor.setNombreVia(user.getVia());
			conductor.setNumero(esInteger(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null);
			conductor.setPiso(esInteger(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null);
			conductor.setPuerta(user.getPuerta());
			conductor.setLocalidad(user.getLocalidad());
			conductor.setProvincia(user.getProvincia());
			conductor.setRestoDireccion(user.getRestoDireccion());
			logger.info("trabajadorService paso2");
			conductor.setTelefono(
					esBigDecimal(user.getTelefono()) ? truncateToBigDecimaRouding(user.getTelefono()) : null);
			conductor.setCp(esBigDecimal(user.getCp()) ? truncateToBigDecimaRouding(user.getCp()) : null);
			logger.info("trabajadorService paso3");
			trabajadorProfileDao.guardar(conductor);
			emailService.activacionCuentaEmail(conductor.getId(), conductor.getNombre(), conductor.getApellidos(),
					conductor.getEmail(), token, fechaExpiracionToken, conductor.getIdioma(), appUrl);
			trabajador = (Trabajador) conductor;
			break;
		/* RECOLECTOR */
		default:
			Recolector recolector = new Recolector(user.getDocumento(), user.getNombre(), user.getApellidos(),
					user.getEmail(), dateToCalendar(user.getFechaNacimiento()), token, fechaExpiracionToken);
			recolector.setIdioma(user.getIdioma());
			recolector.setNombreVia(user.getVia());
			recolector.setNumero(esInteger(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null);
			recolector.setPiso(esInteger(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null);
			recolector.setPuerta(user.getPuerta());
			recolector.setLocalidad(user.getLocalidad());
			recolector.setProvincia(user.getProvincia());
			recolector.setRestoDireccion(user.getRestoDireccion());
			logger.info("trabajadorService paso2");
			recolector.setTelefono(
					esBigDecimal(user.getTelefono()) ? truncateToBigDecimaRouding(user.getTelefono()) : null);
			recolector.setCp(esBigDecimal(user.getCp()) ? truncateToBigDecimaRouding(user.getCp()) : null);
			logger.info("trabajadorService paso3");
			trabajadorProfileDao.guardar(recolector);
			emailService.activacionCuentaEmail(recolector.getId(), user.getNombre(), user.getApellidos(),
					user.getEmail(), token, fechaExpiracionToken, user.getIdioma(), appUrl);
			trabajador = (Trabajador) recolector;
		}
		return trabajador;
	}

	@Override
	public Trabajador buscarTrabajador(long id) throws InstanceNotFoundException {
		Trabajador t = trabajadorProfileDao.buescarById(id);
		return t;
	}

	@Override
	public Trabajador buscarTrabajadorEmail(String email) throws InstanceNotFoundException {
		return trabajadorProfileDao.buscarTrabajadorPorEmail(email);
	}

	@Override
	public Trabajador buscarTrabajadorDocumento(String documento) throws InstanceNotFoundException {
		return trabajadorProfileDao.buscarTrabajadorPorDocumentoId(documento);
	}

	@Override
	public Page<Trabajador> buscarTrabajadores(Pageable pageable) {
		Page<Trabajador> trabajadores = trabajadorProfileDao.buscarTrabajadores(pageable);
		return trabajadores;
	}

	private static boolean esInteger(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private static boolean esBigDecimal(String cadena) {
		if (cadena.isEmpty())
			return false;
		try {
			Double.parseDouble(cadena);
			BigDecimal bigDecimal = new BigDecimal(cadena);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Convert Date to Calendar
	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;

	}

	// Convert Calendar to Date
	private static Date calendarToDate(Calendar calendar) {
		return calendar.getTime();
	}

	private static BigDecimal truncateToBigDecimaRouding(String text) {
		BigDecimal bigDecimal = new BigDecimal(text);
		if (bigDecimal.scale() > 2)
			bigDecimal = new BigDecimal(text).setScale(2, RoundingMode.HALF_UP);
		return bigDecimal.stripTrailingZeros();
	}
}
