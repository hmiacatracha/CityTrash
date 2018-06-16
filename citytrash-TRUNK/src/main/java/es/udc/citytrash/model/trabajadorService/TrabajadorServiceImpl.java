package es.udc.citytrash.model.trabajadorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorBusqFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorRegistroFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorUpdateFormDto;
import es.udc.citytrash.model.telefono.Telefono;
import es.udc.citytrash.model.trabajador.Administrador;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.ConductorDao;
import es.udc.citytrash.model.trabajador.Recolector;
import es.udc.citytrash.model.trabajador.RecolectorDao;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.CampoBusqTrabajador;
import es.udc.citytrash.util.enums.TipoTelefono;
import es.udc.citytrash.util.enums.TipoTrabajador;

@Service("trabajadorService")
@Transactional
public class TrabajadorServiceImpl implements TrabajadorService {

	@Autowired
	private TrabajadorDao trabajadorProfileDao;

	@Autowired
	private ConductorDao conductorDao;

	@Autowired
	private RecolectorDao recolectorDao;

	final Logger logger = LoggerFactory.getLogger(TrabajadorServiceImpl.class);

	@Override
	public Trabajador registrar(TrabajadorRegistroFormDto user) throws DuplicateInstanceException {
		Trabajador trabajador;
		/* the UUID class provides a simple means for generating unique ids */
		String token = UUID.randomUUID().toString();
		Calendar fechaExpiracionToken = Calendar.getInstance();

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

			String update = "UPDATE " + GlobalNames.TBL_TRABAJ + " SET " + GlobalNames.CAMPO_TRABAJADOR_DISCRIMINADOR + " = '"
					+ tipo.toString().toUpperCase() + "' , " + GlobalNames.CAMPO_ROL_BD + " = '" + rol
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
	public boolean esTrabajadorRecolector(long id) {
		try {
			recolectorDao.buscarById(id);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean esTrabajadorConductor(long id) {
		try {
			conductorDao.buscarById(id);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
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
	public Page<Trabajador> buscarTrabajadores(Pageable pageable, Boolean mostrarTodos) {
		Page<Trabajador> trabajadores = trabajadorProfileDao.buscarTrabajadores(pageable, null, mostrarTodos);
		return trabajadores;
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
	public void desactivarTrabajador(long trabajadorId) throws InstanceNotFoundException {
		Trabajador t = trabajadorProfileDao.buscarById(trabajadorId);
		t.setActiveWorker(false);
		trabajadorProfileDao.guardar(t);
	}

	@Override
	public void activarTrabajador(long trabajadorId) throws InstanceNotFoundException {
		Trabajador t = trabajadorProfileDao.buscarById(trabajadorId);
		t.setActiveWorker(true);
		trabajadorProfileDao.guardar(t);
	}

	@Override
	public boolean esUnTrabajadorActivo(long trabajadorId) throws InstanceNotFoundException {
		return trabajadorProfileDao.buscarById(trabajadorId).isActiveWorker();
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
}
