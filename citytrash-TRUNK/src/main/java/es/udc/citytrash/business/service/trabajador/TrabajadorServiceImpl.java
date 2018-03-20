package es.udc.citytrash.business.service.trabajador;

import java.util.Calendar;
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
		String rol = "";

		logger.info("Register controller 0");
		logger.info("Register URL-> " + appUrl);
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

		switch (user.getTipo()) {
		/* ADMINISTRADOR */
		case ADMIN:
			rol = "ROLE_ADMIN";
			Administrador admin = new Administrador(user.getDocumento(), user.getNombre(), user.getApellidos(), rol,
					user.getEmail(), token, fechaExpiracionToken);
			admin.setIdioma(user.getIdioma());
			admin.setNombreVia(user.getVia());
			admin.setNumero(esNumero(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null);
			admin.setPiso(esNumero(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null);
			admin.setPuerta(user.getPuerta());
			admin.setLocalidad(user.getLocalidad());
			admin.setProvincia(user.getProvincia());
			admin.setRestoDireccion(user.getRestoDireccion());
			trabajadorProfileDao.save(admin);
			emailService.activacionCuentaEmail(admin.getId(), admin.getNombre(), admin.getApellidos(), admin.getEmail(),
					token, fechaExpiracionToken, user.getIdioma(), appUrl);
			trabajador = (Trabajador) admin;
			break;
		/* CONDUCTOR */
		case CONDUCT:
			rol = "ROLE_USER";
			Conductor conductor = new Conductor(user.getDocumento(), user.getNombre(), user.getApellidos(), rol,
					user.getEmail(), token, fechaExpiracionToken);
			conductor.setIdioma(user.getIdioma());
			conductor.setNombreVia(user.getVia());
			conductor.setNumero(esNumero(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null);
			conductor.setPiso(esNumero(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null);
			conductor.setPuerta(user.getPuerta());
			conductor.setLocalidad(user.getLocalidad());
			conductor.setProvincia(user.getProvincia());
			conductor.setRestoDireccion(user.getRestoDireccion());
			trabajadorProfileDao.save(conductor);
			emailService.activacionCuentaEmail(conductor.getId(), conductor.getNombre(), conductor.getApellidos(),
					conductor.getEmail(), token, fechaExpiracionToken, conductor.getIdioma(), appUrl);
			trabajador = (Trabajador) conductor;
			break;
		/* RECOLECTOR */
		default:
			rol = "ROLE_USER";
			Recolector recolector = new Recolector(user.getDocumento(), user.getNombre(), user.getApellidos(), rol,
					user.getEmail(), token, fechaExpiracionToken);
			recolector.setIdioma(user.getIdioma());
			recolector.setNombreVia(user.getVia());
			recolector.setNumero(esNumero(user.getNumero()) ? Integer.parseInt(user.getNumero()) : null);
			recolector.setPiso(esNumero(user.getPiso()) ? Integer.parseInt(user.getPiso()) : null);
			recolector.setPuerta(user.getPuerta());
			recolector.setLocalidad(user.getLocalidad());
			recolector.setProvincia(user.getProvincia());
			recolector.setRestoDireccion(user.getRestoDireccion());
			trabajadorProfileDao.save(recolector);
			emailService.activacionCuentaEmail(recolector.getId(), user.getNombre(), user.getApellidos(),
					user.getEmail(), token, fechaExpiracionToken, user.getIdioma(), appUrl);
			trabajador = (Trabajador) recolector;
		}
		return trabajador;
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

	private static boolean esNumero(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
