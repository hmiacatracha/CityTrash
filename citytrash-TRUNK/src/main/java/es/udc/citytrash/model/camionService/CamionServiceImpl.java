package es.udc.citytrash.model.camionService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.controller.camiones.CamionesController;
import es.udc.citytrash.controller.util.dtos.camion.CamionDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionRegistroDto;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camion.CamionDao;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModelo.CamionModeloDao;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasuraDao;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.trabajador.AdministradorDao;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.ConductorDao;
import es.udc.citytrash.model.trabajador.Recolector;
import es.udc.citytrash.model.trabajador.RecolectorDao;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.model.util.excepciones.InactiveResourceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;
import es.udc.citytrash.util.enums.CampoBusqPalabrasClavesCamion;

@Service("camionService")
@Transactional
public class CamionServiceImpl implements CamionService {

	@Autowired
	AdministradorDao administradorDao;

	@Autowired
	RecolectorDao recolectorDao;

	@Autowired
	ConductorDao conductorDao;

	@Autowired
	TrabajadorDao trabajadorDao;

	@Autowired
	CamionDao camionDao;

	@Autowired
	CamionModeloDao modeloDao;

	@Autowired
	TipoDeBasuraDao tipoDao;

	@Autowired
	CamionModeloTipoDeBasuraDao cmtbDao;

	final Logger logger = LoggerFactory.getLogger(CamionServiceImpl.class);

	@Override
	public boolean esModeloExistenteById(int modelo) {
		boolean existe = false;

		try {
			modeloDao.buscarById(modelo);
			existe = true;
		} catch (InstanceNotFoundException i) {
			existe = false;
		}
		return existe;
	}

	@Override
	public boolean esCamionByMatriculaExistente(String matricula) {

		try {
			camionDao.buscarByMatricula(matricula);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean esCamionByNombreExistente(String nombre) {
		try {
			camionDao.buscarByNombre(nombre);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean esModeloCamionByNombreExistente(String nombre) {
		try {
			modeloDao.buscarModeloPorNombre(nombre);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean esCamionByVinExistente(String vin) {
		try {
			camionDao.buscarByVin(vin);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean cambiarEstadoCamion(long id) throws InstanceNotFoundException {
		logger.info("URL_MAPPING_CAMIONES_ESTADO1");
		Camion camion = camionDao.buscarById(id);
		logger.info("URL_MAPPING_CAMIONES_ESTADO2 => " + camion.getActivo());
		camion.setActivo(!camion.getActivo());
		logger.info("URL_MAPPING_CAMIONES_ESTADO3 => " + camion.getActivo());
		camionDao.guardar(camion);
		logger.info("URL_MAPPING_CAMIONES_ESTADO4 => " + camion.getActivo());
		return camion.getActivo();
	}

	@Override
	public Camion buscarCamionById(long id) throws InstanceNotFoundException {
		try {
			return camionDao.buscarById(id);
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(id, Camion.class.getName());
		}
	}

	@Override
	public CamionModelo buscarModeloById(int id) throws InstanceNotFoundException {
		CamionModelo modelo = modeloDao.buscarById(id);
		return modelo;
	}

	@Override
	public List<CamionModelo> buscarTodosLosModelosOrderByModelo() {
		List<CamionModelo> modelos = modeloDao.buscarTodosOrderByModelo();
		return modelos;
	}

	@Override
	public List<TipoDeBasura> buscarTiposDeBasura() {
		return tipoDao.buscarTodos();
	}

	@Override
	public List<CamionModeloTipoDeBasura> buscarTipoDeBasuraByModelo(int idModelo) throws InstanceNotFoundException {
		List<CamionModeloTipoDeBasura> list = new ArrayList<CamionModeloTipoDeBasura>(
				modeloDao.buscarById(idModelo).getTiposDeBasura());
		return list;
	}

	@Override
	public Camion registrarCamion(CamionRegistroDto form) throws InstanceNotFoundException, DuplicateInstanceException,
			InactiveResourceException, InvalidFieldException {
		CamionModelo modelo = null;
		Conductor conductor = null;
		Conductor conductorSuplente = null;
		Recolector recolector1 = null;
		Recolector recolector2 = null;
		Camion camion = null;
		/* Verificamos que el modelo exista */

		logger.info("paso1 registrarCamion");
		try {
			modelo = modeloDao.buscarById(form.getModeloCamion());
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(form.getModeloCamion(), CamionModelo.class.getName());
		}

		/* Verificamos que el nombre del camion no exista */

		logger.info("paso2 registrarCamion");
		if (form.getNombre().length() > 0) {
			try {
				camionDao.buscarByNombre(form.getNombre());
				throw new DuplicateInstanceException(form.getNombre(), Camion.class.getName());
			} catch (InstanceNotFoundException e) {

			}
		} else {
			throw new InvalidFieldException("campoObligatorio", "nombre", Camion.class.getName());
		}
		logger.info("paso3 registrarCamion");
		camion = new Camion(form.getNombre(), modelo);

		logger.info("paso4 registrarCamion");
		/* Verificamos que la matricula no exista */
		if (form.getMatricula().length() > 0) {
			if (this.esCamionByMatriculaExistente(form.getMatricula())) {
				throw new DuplicateInstanceException(form.getMatricula(), Camion.class.getName());
			}
			camion.setMatricula(form.getMatricula());
		}

		logger.info("paso4.1 registrarCamion");
		/* Verificamos que el vin del camion no exista */
		if (form.getVin().trim().length() > 0) {
			try {
				camionDao.buscarByVin(form.getVin());
				throw new DuplicateInstanceException(form.getVin(), Camion.class.getName());
			} catch (InstanceNotFoundException e) {
			}
			camion.setVin(form.getVin());
		}
		/*
		 * Verificamos que los trabajadores asignados al camion son existentes
		 */
		logger.info("paso5 registrarCamion");
		if (form.getConductorPrincipal() != null) {
			try {
				logger.info("paso51 registrarCamion");
				conductor = conductorDao.buscarById(form.getConductorPrincipal());
				logger.info("paso52 registrarCamion");
				if (!conductor.isActiveWorker()) {
					logger.info("paso53 registrarCamion");
					throw new InactiveResourceException(conductor.getNombre() + " " + conductor.getApellidos(),
							Conductor.class.getName());
				}
				logger.info("paso54 registrarCamion");
				camion.setConductor(conductor);
			} catch (InstanceNotFoundException e) {
				logger.info("paso55 registrarCamion");
				throw new InstanceNotFoundException(form.getConductorPrincipal(), Conductor.class.getName());
			}
		}
		logger.info("paso6 registrarCamion");
		if (form.getConductorSuplente() != null) {
			try {
				conductorSuplente = conductorDao.buscarById(form.getConductorSuplente());
				if (!conductorSuplente.isActiveWorker())
					throw new InactiveResourceException(
							conductorSuplente.getNombre() + " " + conductorSuplente.getApellidos(),
							Conductor.class.getName());
				camion.setConductorSuplente(conductorSuplente);
			} catch (InstanceNotFoundException e) {
				throw new InstanceNotFoundException(form.getConductorSuplente(), Conductor.class.getName());
			}
		}
		logger.info("paso7 registrarCamion");
		if (form.getRecogedorUno() != null) {
			try {
				recolector1 = recolectorDao.buscarById(form.getRecogedorUno());
				camion.setRecogedor1(recolector1);
				if (!recolector1.isActiveWorker())
					throw new InactiveResourceException(recolector1.getNombre() + " " + recolector1.getApellidos(),
							Recolector.class.getName());
			} catch (InstanceNotFoundException e) {
				throw new InstanceNotFoundException(form.getRecogedorUno(), Recolector.class.getName());
			}
		}

		logger.info("paso8 registrarCamion");
		if (form.getRecogedorDos() != null) {
			try {
				recolector2 = recolectorDao.buscarById(form.getRecogedorDos());
				camion.setRecogedor2(recolector2);
				if (!recolector2.isActiveWorker())
					throw new InactiveResourceException(recolector2.getNombre() + " " + recolector2.getApellidos(),
							Recolector.class.getName());
			} catch (InstanceNotFoundException e) {
				throw new InstanceNotFoundException(form.getRecogedorDos(), Recolector.class.getName());
			}
		}
		logger.info("paso9 registrarCamion");
		camionDao.guardar(camion);
		return camion;
	}

	@Override
	public Camion modificarCamion(CamionDto camionForm) throws InstanceNotFoundException, DuplicateInstanceException,
			InactiveResourceException, InvalidFieldException {
		CamionModelo modelo = null;
		Trabajador conductor = null;
		Trabajador conductorSuplente = null;
		Trabajador recolector1 = null;
		Trabajador recolector2 = null;
		Camion camion = null;
		/* Verificamos que exista el camion */
		try {
			camion = camionDao.buscarById(camionForm.getId());
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(camionForm.getId(), Camion.class.getName());
		}
		/* Verificamos que el modelo exista */
		try {
			modelo = modeloDao.buscarById(camionForm.getModeloCamion());
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(camionForm.getModeloCamion(), CamionModelo.class.getName());
		}

		/* Verificamos que el nombre del camion no exista */
		if (camionForm.getNombre().length() > 0) {
			try {
				camion = camionDao.buscarByNombre(camionForm.getNombre());
				if (camion.getId() != camionForm.getId())
					throw new DuplicateInstanceException(camionForm.getNombre(), Camion.class.getName());
			} catch (InstanceNotFoundException e) {
			}
			camion.setNombre(camionForm.getNombre());
		} else {
			throw new InvalidFieldException("InvalidNameException", camionForm.getNombre(), Camion.class.getName());
		}

		/* Verificamos que la matricula del camion no exista */
		if (camionForm.getMatricula().trim().length() > 0) {
			try {
				camion = camionDao.buscarByMatricula(camionForm.getMatricula());
				if (camion.getId() != camionForm.getId())
					throw new DuplicateInstanceException(camionForm.getMatricula(), Camion.class.getName());
			} catch (InstanceNotFoundException e) {
			}
			camion.setMatricula(camionForm.getMatricula());
		}

		/* Verificamos que el vin del camion no exista */
		if (camionForm.getVin().trim().length() > 0) {
			try {
				camion = camionDao.buscarByVin(camionForm.getVin());
				if (camion.getId() != camionForm.getId())
					throw new DuplicateInstanceException(camionForm.getVin(), Camion.class.getName());
			} catch (InstanceNotFoundException e) {
			}
			camion.setVin(camionForm.getVin());
		}

		camion.setModeloCamion(modelo);
		/*
		 * Verificamos que los trabajadores asignados al camion son existentes
		 */
		if (camionForm.getConductorPrincipal() != null) {
			try {
				conductor = conductorDao.buscarById(camionForm.getConductorPrincipal());
				if (!conductor.isActiveWorker())
					throw new InactiveResourceException(conductor.getNombre() + " " + conductor.getApellidos(),
							Conductor.class.getName());
				camion.setConductor(conductor);
			} catch (InstanceNotFoundException e) {
				throw new InstanceNotFoundException(camionForm.getConductorPrincipal(), Conductor.class.getName());
			}
		}

		if (camionForm.getConductorSuplente() != null) {
			try {
				conductorSuplente = conductorDao.buscarById(camionForm.getConductorSuplente());
				if (!conductorSuplente.isActiveWorker())
					throw new InactiveResourceException(
							conductorSuplente.getNombre() + " " + conductorSuplente.getApellidos(),
							Conductor.class.getName());

				camion.setConductorSuplente(conductorSuplente);
			} catch (InstanceNotFoundException e) {
				throw new InstanceNotFoundException(camionForm.getConductorSuplente(), Conductor.class.getName());
			}
		}

		if (camionForm.getRecogedorUno() != null) {
			try {
				recolector1 = recolectorDao.buscarById(camionForm.getRecogedorUno());
				camion.setRecogedor1(recolector1);
				if (!recolector1.isActiveWorker())
					throw new InactiveResourceException(recolector1.getNombre() + " " + recolector1.getApellidos(),
							Recolector.class.getName());
			} catch (InstanceNotFoundException e) {
				throw new InstanceNotFoundException(camionForm.getRecogedorUno(), Recolector.class.getName());
			}
		}

		if (camionForm.getRecogedorDos() != null) {
			try {
				recolector2 = recolectorDao.buscarById(camionForm.getRecogedorDos());
				camion.setRecogedor2(recolector2);
				if (!recolector2.isActiveWorker())
					throw new InactiveResourceException(recolector2.getNombre() + " " + recolector2.getApellidos(),
							Recolector.class.getName());
			} catch (InstanceNotFoundException e) {
				throw new InstanceNotFoundException(camionForm.getRecogedorDos(), Recolector.class.getName());
			}
		}
		camion.setActivo(camionForm.isActivo());
		camion.setFechaBaja(camionForm.getFechaBaja() != null ? dateToCalendar(camionForm.getFechaBaja()) : null);
		camionDao.guardar(camion);
		return camion;
	}

	@Override
	public CamionModelo registrarModelo(CamionModeloDto form) throws DuplicateInstanceException {
		CamionModelo camionModelo;
		try {
			logger.info("paso1 registrarModelo paso inicial");
			modeloDao.buscarModeloPorNombre(form.getNombre().trim());
			logger.info("paso2 registrarModelo paso ");
			throw new DuplicateInstanceException(form.getNombre(), CamionModelo.class.getName());

		} catch (InstanceNotFoundException e) {

		}
		logger.info("paso3 registrarModelo");
		camionModelo = new CamionModelo(form.getNombre(), form.getAncho(), form.getAltura(), form.getLongitud());
		camionModelo.setDistancia(form.getDistancia() != null ? form.getDistancia() : null);
		camionModelo.setPma(form.getPma());
		camionModelo.setVolumenTolva(form.getVolumenTolva() != null ? form.getVolumenTolva() : null);
		modeloDao.guardar(camionModelo);
		return camionModelo;
	}

	@Override
	public CamionModelo modificarModelo(CamionModeloDto form)
			throws InstanceNotFoundException, DuplicateInstanceException {

		CamionModelo camionModelo = modeloDao.buscarById(form.getId());
		CamionModelo camionModeloEncontrado;

		try {
			camionModeloEncontrado = modeloDao.buscarModeloPorNombre(form.getNombre());
			if (camionModeloEncontrado.getId() != form.getId())
				throw new DuplicateInstanceException(form.getNombre(), CamionModelo.class.getName());
		} catch (InstanceNotFoundException e) {

		}
		camionModelo.setModelo(form.getNombre());
		camionModelo.setAncho(form.getAncho());
		camionModelo.setAltura(form.getAltura());
		camionModelo.setLongitud(form.getLongitud());
		camionModelo.setDistancia(form.getDistancia() != null ? form.getDistancia() : null);
		camionModelo.setPma(form.getPma());
		camionModelo.setVolumenTolva(form.getVolumenTolva() != null ? form.getVolumenTolva() : null);
		modeloDao.guardar(camionModelo);
		return camionModelo;
	}

	@Override
	public Page<CamionModelo> buscarModelos(Pageable pageable, CamionModeloFormBusq formBusqueda) {
		logger.info("IMPRIMIENDO formbusqueda buscar modelos de camiones => " + formBusqueda.toString());

		TipoDeBasura tipo = null;
		List<CamionModelo> camionesList = new ArrayList<CamionModelo>();
		Page<CamionModelo> page = new PageImpl<CamionModelo>(camionesList, pageable, camionesList.size());

		if (formBusqueda.getPalabrasClaveModelo().length() > 0) {
			page = modeloDao.buscarCamionModelo(pageable, formBusqueda.getPalabrasClaveModelo());
		} else {
			page = modeloDao.buscarCamionModelo(pageable);
		}
		return page;
	}

	@Override
	public Page<Camion> buscarCamiones(Pageable pageable, CamionFormBusq formBusqueda) throws FormBusquedaException {

		logger.info("IMPRIMIENDO formbusqueda buscar camiones => " + formBusqueda.toString());
		CampoBusqPalabrasClavesCamion campo = CampoBusqPalabrasClavesCamion.matricula;
		CamionModelo modelo = null;
		List<Camion> camionesList = new ArrayList<Camion>();
		Page<Camion> page = new PageImpl<Camion>(camionesList, pageable, camionesList.size());

		try {
			if (formBusqueda.getModelo() != null)
				modelo = modeloDao.buscarById(formBusqueda.getModelo());
		} catch (InstanceNotFoundException e) {
		}

		/* Convertimos el campo por el que buscar las palabras claves */
		try {
			campo = CampoBusqPalabrasClavesCamion.valueOf(formBusqueda.getCampo());
		} catch (IllegalArgumentException e) {
			throw new FormBusquedaException(e);
		} catch (NullPointerException e) {
			throw new FormBusquedaException(e);
		}

		if (formBusqueda.getBuscar().length() > 0)
			switch (campo) {
			case matricula:
				logger.info("CAMPO => matricula buscarCamionesByModeloYMatricula");
				page = camionDao.buscarCamionesByModeloYMatricula(pageable, formBusqueda.getBuscar(), modelo,
						formBusqueda.getMostrarSoloCamionesActivos(), formBusqueda.getMostrarSoloCamionesDeAlta());
				break;
			case nombre:
				logger.info("CAMPO => nombre buscarCamionesByModeloYNombre");
				page = camionDao.buscarCamionesByModeloYNombre(pageable, formBusqueda.getBuscar(), modelo,
						formBusqueda.getMostrarSoloCamionesActivos(), formBusqueda.getMostrarSoloCamionesDeAlta());
				break;
			case vin:
				logger.info("CAMPO => vin buscarCamionesByModeloYNombre");
				page = camionDao.buscarCamionesByModeloYVin(pageable, formBusqueda.getBuscar(), modelo,
						formBusqueda.getMostrarSoloCamionesActivos(), formBusqueda.getMostrarSoloCamionesDeAlta());
				break;
			default:
				logger.info("CAMPO => default buscarCamionesByModeloYMatricula");
				page = camionDao.buscarCamionesByModeloYMatricula(pageable, formBusqueda.getBuscar(), modelo,
						formBusqueda.getMostrarSoloCamionesActivos(), formBusqueda.getMostrarSoloCamionesDeAlta());
			}
		else {
			logger.info("CAMPO => NO_CAMPO buscarCamionesByModelo");
			page = camionDao.buscarCamionesByModelo(pageable, modelo, formBusqueda.getMostrarSoloCamionesActivos(),
					formBusqueda.getMostrarSoloCamionesDeAlta());
		}
		return page;
	}

	/**
	 * metodo privado => cambia de fecha a calendar
	 * 
	 * @param date
	 * @return
	 */
	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

}
