package es.udc.citytrash.model.rutaService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.controller.util.dtos.ruta.GenerarRutaFormDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutasDiariaFormBusq;
import es.udc.citytrash.controller.util.dtos.ruta.RutasFormBusq;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camion.CamionDao;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedor.ContenedorDao;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.ruta.RutaDao;
import es.udc.citytrash.model.rutaDiaria.RutaDiaria;
import es.udc.citytrash.model.rutaDiaria.RutaDiariaDao;
import es.udc.citytrash.model.rutaDiariaContenedores.RutaDiariaContenedores;
import es.udc.citytrash.model.rutaDiariaContenedores.RutaDiariaContenedoresDao;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.util.dto.Notificacion;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

@Service("rutaService")
@Transactional
public class RutaServiceImpl implements RutaService {

	@Autowired
	TrabajadorDao trabajadorDao;

	@Autowired
	CamionDao camionDao;

	@Autowired
	TipoDeBasuraDao tipoDao;

	@Autowired
	ContenedorDao contenedorDao;

	@Autowired
	RutaDao rutaDao;

	@Autowired
	RutaDiariaDao rutaDiariaDao;

	@Autowired
	RutaDiariaContenedoresDao rutaDiariaContenedoresDao;

	final Logger logger = LoggerFactory.getLogger(RutaServiceImpl.class);

	@Override
	public Ruta buscarRuta(int rutaId) throws InstanceNotFoundException {
		return rutaDao.buscarById(rutaId);
	}

	@Override
	public Ruta registrarRuta(RutaDto form) throws DuplicateInstanceException, InstanceNotFoundException {
		Ruta ruta = null;
		Camion camion = null;
		/* Verificamos que el camion exista */
		try {
			camion = camionDao.buscarById(form.getCamion());
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(form.getCamion(), Camion.class.getName());
		}
		/*
		 * Verificamos que la ruta exista (modificar), sino existe entonces es
		 * un registro de ruta
		 */
		if (form.getId() == null) {
			ruta = new Ruta(form.getPuntoInicio(), form.getPuntoFinal(), form.isActivo(), camion);
		} else {
			throw new InstanceNotFoundException(form.getId(), Ruta.class.getName());
		}

		for (Integer tipoDeBasuraId : form.getTiposDeBasura()) {
			if (tipoDeBasuraId != null)
				if (tipoDao.existe(tipoDeBasuraId)) {
					TipoDeBasura t = tipoDao.buscarById(tipoDeBasuraId);
					ruta.addTipoDeBasura(t);
				}
		}

		/* Añadimos los contenedores nuevos */
		for (Long contenedorId : form.getContenedores()) {
			if (contenedorId != null) {
				if (contenedorDao.existe(contenedorId)) {
					Contenedor contenedor = contenedorDao.buscarById(contenedorId);
					contenedor.setRuta(ruta);
					contenedorDao.guardar(contenedor);
				}
			}
		}
		rutaDao.guardar(ruta);
		return ruta;
	}

	@Override
	public Ruta actualizarRuta(RutaDto form) throws DuplicateInstanceException, InstanceNotFoundException {
		logger.info("actualizarRuta");
		Ruta ruta = null;
		Camion camion = null;
		/* Verificamos que el camion exista */
		try {
			camion = camionDao.buscarById(form.getCamion());
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(form.getCamion(), Camion.class.getName());
		}
		/*
		 * Verificamos que la ruta exista (modificar), sino existe entonces es
		 * un registro de ruta
		 */
		try {
			if (form.getId() != null) {
				ruta = rutaDao.buscarById(form.getId());
				ruta.setActivo(form.isActivo());
				ruta.setPuntoInicio(form.getPuntoInicio());
				ruta.setPuntoFinal(form.getPuntoFinal());
				ruta.setCamion(camion);
				ruta.setActivo(form.isActivo());
			} else {
				throw new InstanceNotFoundException(form.getId(), Ruta.class.getName());
			}
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(form.getId(), Ruta.class.getName());
		}

		List<TipoDeBasura> tiposDeBasura = new ArrayList<TipoDeBasura>();
		for (Integer tipoDeBasuraId : form.getTiposDeBasura()) {
			if (tipoDeBasuraId != null)
				if (tipoDao.existe(tipoDeBasuraId)) {
					TipoDeBasura t = tipoDao.buscarById(tipoDeBasuraId);
					tiposDeBasura.add(t);
				}
		}

		ruta.getTiposDeBasura().clear();
		ruta.getTiposDeBasura().addAll(tiposDeBasura);
		List<Contenedor> contenedoresGuardados = new ArrayList<Contenedor>();
		contenedoresGuardados = ruta.getContenedores();

		// List<Contenedor> contenedores = new ArrayList<Contenedor>();

		/* Eliminanos los contenedores que ya no pertenecen a esta ruta */
		for (Contenedor c : contenedoresGuardados) {
			if (!form.getContenedores().contains(c.getId())) {
				c.setRuta(null);
				contenedorDao.guardar(c);
			}
		}

		/* Añadimos los contenedores nuevos */
		for (Long contenedorId : form.getContenedores()) {
			if (contenedorId != null) {
				if (contenedorDao.existe(contenedorId)) {
					Contenedor contenedor = contenedorDao.buscarById(contenedorId);
					contenedor.setRuta(ruta);
					contenedorDao.guardar(contenedor);
				}
			}
		}
		rutaDao.guardar(ruta);
		logger.info("actualizarRuta fin");
		return ruta;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Ruta> buscarRutas(boolean mostrarSoloRutasActivas) {
		List<Ruta> rutasList = rutaDao.buscarRutas(mostrarSoloRutasActivas);
		return rutasList;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Ruta> buscarRutasSinGenerar(GenerarRutaFormDto form) {
		List<Ruta> rutaList = rutaDao.buscarRutasSinGenerar(form.getFecha(), form.getTiposDeBasura(),
				form.getCamiones());
		return rutaList;
	}

	@Override
	public void generarRutas(GenerarRutaFormDto form) {
		List<RutaDiaria> rd = new ArrayList<RutaDiaria>();
		Ruta ruta;
		Calendar calendar = form.getFecha() != null ? dateToCalendar(form.getFecha()) : Calendar.getInstance();
		Set<Integer> rutas = new HashSet<Integer>(form.getRutas());
		List<Integer> listaRutas = new ArrayList<Integer>(rutas);

		for (int id : listaRutas) {

			List<String> listaMensajes = new ArrayList<String>();

			try {
				ruta = rutaDao.buscarById(id);
				rd = rutaDiariaDao.buscarRutasDiarias(form.getFecha(), form.getFecha(), id);

				/* Verificar que la ruta no se ha generado ya */
				if (!rd.isEmpty()) {
					throw new DuplicateInstanceException(id, RutaDiaria.class.getName());
				}

				RutaDiaria rutaDiaria = new RutaDiaria(ruta, calendar);
				/*
				 * Verificar que el camion esta activo, sino esta activo no
				 * añade la ruta diaria
				 */
				if (ruta.getCamion() != null) {

					/* Añadir el camion a la ruta */
					if (!ruta.getCamion().getActivo())
						listaMensajes.add("InactiveResourceException_ruta_diaria_camion");
					else
						rutaDiaria.setCamion(ruta.getCamion());

					/* Añadir el conductor a la ruta */
					if (ruta.getCamion().getConductor() == null)
						listaMensajes.add("EmptyValueException_ruta_diaria_conductor");
					else if (ruta.getCamion().getConductor() == null && ruta.getCamion().getConductorSuplente() == null)
						listaMensajes.add("EmptyValueException_ruta_diaria_conductores");
					else {
						// Verificamos que este activo el conductor
						if (ruta.getCamion().getConductor().isActiveWorker()) {
							rutaDiaria.setConductor(ruta.getCamion().getConductor());
						} else {
							// conductor suplente
							if (ruta.getCamion().getConductorSuplente() != null) {
								if (ruta.getCamion().getConductorSuplente().isActiveWorker()) {
									rutaDiaria.setConductor(ruta.getCamion().getConductorSuplente());
								} else
									listaMensajes.add("InactiveResourceException_ruta_diaria_conductores");
							} else
								listaMensajes.add("EmptyValueException_ruta_diaria_conductor_suplente");
						}
					}

					/* Añadir conductores */
					if (ruta.getCamion().getRecogedor1() == null && ruta.getCamion().getRecogedor2() == null) {
						listaMensajes.add("EmptyValueException_ruta_diaria_recolectores");
					} else {

						if (ruta.getCamion().getRecogedor1() != null) {
							if (ruta.getCamion().getRecogedor1().isActiveWorker())
								rutaDiaria.setRecogedor1(ruta.getCamion().getRecogedor1());
							else
								listaMensajes.add("InactiveResourceException_ruta_diaria_recolector1");
						} else
							listaMensajes.add("EmptyValueException_ruta_diaria_recolector1");

						if (ruta.getCamion().getRecogedor2() != null) {
							if (ruta.getCamion().getRecogedor2().isActiveWorker())
								rutaDiaria.setRecogedor2(ruta.getCamion().getRecogedor2());
							else
								listaMensajes.add("InactiveResourceException_ruta_diaria_recolector2");
						}
					}

				} else {
					listaMensajes.add("EmptyValueException_ruta_diaria_camion");
					listaMensajes.add("EmptyValueException_ruta_diaria_conductor");
					listaMensajes.add("EmptyValueException_ruta_diaria_recolectores");
				}

				/*
				 * añadir contenedores
				 */
				List<Contenedor> contenedores = new ArrayList<Contenedor>();
				HashSet<Contenedor> rcontenedores = ruta.getContenedores() != null
						? new HashSet<Contenedor>(ruta.getContenedores()) : new HashSet<Contenedor>();

				for (Contenedor contenedor : rcontenedores) {
					if (contenedor.getActivo()) {
						RutaDiariaContenedores rdcont = new RutaDiariaContenedores(rutaDiaria, contenedor);
						rutaDiaria.addRutaDiariaContenedores(rdcont);
						contenedores.add(contenedor);
					}
				}

				if (contenedores.size() == 0) {
					listaMensajes.add("EmptyValueException_ruta_diaria_contenedores");
				}

				/* Añade la ruta, solo si no tiene ningun error */
				rutaDiaria = rutaDiariaDao.guardar(rutaDiaria);

				if (listaMensajes.size() > 0) {
					Notificacion notificacion = new Notificacion(RutaDiaria.class.getName(), ruta.getId(),
							listaMensajes);
					// guardar notificacion
				}

			} catch (InstanceNotFoundException e) {
				listaMensajes.add("InstanceNotFoundException_ruta_diaria_ruta");
				Notificacion notificacion = new Notificacion(RutaDiaria.class.getName(), id, listaMensajes);
				// guardar notificacion
			} catch (DuplicateInstanceException e) {
				listaMensajes.add("DuplicateInstanceException_ruta_diaria");
				Notificacion notificacion = new Notificacion(RutaDiaria.class.getName(), id, listaMensajes);
				// guardar notifiacion
			}
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Ruta> buscarRutas(Pageable pageable, RutasFormBusq form) {
		logger.info("buscarRutas");
		List<Ruta> rutasList = new ArrayList<Ruta>();
		Page<Ruta> page = new PageImpl<Ruta>(rutasList, pageable, rutasList.size());

		logger.info("buscarRutas paso2");
		page = rutaDao.buscarRutas(pageable, form.getTiposDeBasura(), form.getTrabajadores(), form.getContenedores(),
				form.getCamiones(), form.isMostrarSoloRutasActivas());
		logger.info("buscarRutas paso 3");
		return page;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<RutaDiaria> buscarRutasDiarias(Pageable pageable, RutasDiariaFormBusq form) {
		logger.info("buscarRutasDiarias");
		List<RutaDiaria> rutasList = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(rutasList, pageable, rutasList.size());
		logger.info("buscarRutasDiarias paso2");
		page = rutaDiariaDao.buscarRutasDiarias(pageable, form.getFrom(), form.getTo(), form.getRutas(),
				form.getTrabajadores(), form.getContenedores(), form.getCamiones());
		logger.info("buscarRutasDiarias paso 3");
		return page;
	}

	@Override
	public boolean cambiarEstadoRuta(int id) throws InstanceNotFoundException {
		logger.info("URL_MAPPING_CAMIONES_ESTADO1");
		Ruta ruta = rutaDao.buscarById(id);
		ruta.setActivo(!ruta.isActivo());
		rutaDao.guardar(ruta);
		return ruta.isActivo();
	}

	public void eliminarTipoBasuraByRuta(int rutaId, int tipoId) throws InstanceNotFoundException {
		Ruta r = rutaDao.buscarById(rutaId);
		TipoDeBasura t = tipoDao.buscarById(tipoId);
		if (r.containsTipoDeBasura(t)) {
			r.eliminarTipoDeBasura(t);
			rutaDao.guardar(r);
		} else {
			throw new InstanceNotFoundException(t, Ruta.class.getName());
		}
	}

	public boolean existeTipoBasuraByRuta(int rutaId, int tipoId) {
		Ruta r = null;
		TipoDeBasura t = null;
		try {
			r = rutaDao.buscarById(rutaId);
			t = tipoDao.buscarById(tipoId);
		} catch (InstanceNotFoundException e) {
			return false;
		}

		if (r.containsTipoDeBasura(t)) {
			return true;
		} else {
			return false;
		}
	}

	public static Calendar dateToCalendar(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

}
