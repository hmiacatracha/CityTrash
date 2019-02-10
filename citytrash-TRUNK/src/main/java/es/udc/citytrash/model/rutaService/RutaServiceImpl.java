package es.udc.citytrash.model.rutaService;

import java.math.BigDecimal;
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
import es.udc.citytrash.controller.util.dtos.ruta.RutaDiariaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutasDiariaFormBusq;
import es.udc.citytrash.controller.util.dtos.ruta.RutasFormBusq;
import es.udc.citytrash.model.alerta.Alerta;
import es.udc.citytrash.model.alerta.AlertaDao;
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
import es.udc.citytrash.model.rutaDiariaContenedores.RutaDiariaContenedoresPK;
import es.udc.citytrash.model.sensor.Sensor;
import es.udc.citytrash.model.sensor.Volumen;
import es.udc.citytrash.model.sensorValor.Valor;
import es.udc.citytrash.model.sensorValor.ValorDao;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.Recolector;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InactiveResourceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.RutaIniciadaException;
import es.udc.citytrash.util.enums.Mensaje;
import es.udc.citytrash.util.enums.Prioridad;
import es.udc.citytrash.util.enums.TipoDeAlerta;

@Service("rutaService")
@Transactional
public class RutaServiceImpl implements RutaService {

	@Autowired
	TrabajadorDao tDao;

	@Autowired
	CamionDao camionDao;

	@Autowired
	TipoDeBasuraDao tipoDao;

	@Autowired
	ContenedorDao contenedorDao;

	@Autowired
	ValorDao valorDao;

	@Autowired
	RutaDao rutaDao;

	@Autowired
	RutaDiariaDao rutaDiariaDao;

	@Autowired
	RutaDiariaContenedoresDao rutaDiariaContenedoresDao;

	@Autowired
	AlertaDao alertaDao;

	final Logger logger = LoggerFactory.getLogger(RutaServiceImpl.class);

	@Override
	public Ruta buscarRuta(int rutaId) throws InstanceNotFoundException {
		return rutaDao.buscarById(rutaId);
	}

	@Override
	public RutaDiaria buscarRutaDiaria(long rutaDiariaId) throws InstanceNotFoundException {
		return rutaDiariaDao.buscarById(rutaDiariaId);
	}

	@Override
	public RutaDiariaContenedores rutaDiariaContenedor(long rutaDiariaId, long contenedorId)
			throws InstanceNotFoundException {
		RutaDiaria rd = rutaDiariaDao.buscarById(rutaDiariaId);
		Contenedor c = contenedorDao.buscarById(contenedorId);
		RutaDiariaContenedoresPK pk = new RutaDiariaContenedoresPK(rd, c);
		return rutaDiariaContenedoresDao.buscarById(pk);
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
		calendar.set(Calendar.HOUR_OF_DAY, 0);
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

				/* Añadir tipos de basura */
				List<TipoDeBasura> tiposDeBasura = new ArrayList<TipoDeBasura>();
				for (TipoDeBasura tipo : ruta.getTiposDeBasura()) {
					tiposDeBasura.add(tipo);
					rutaDiaria.addTipoDeBasura(tipo);
				}

				/* Añade la ruta, solo si no tiene ningun error */
				rutaDiaria = rutaDiariaDao.guardar(rutaDiaria);

			} catch (InstanceNotFoundException e) {

			} catch (DuplicateInstanceException e) {

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
		List<RutaDiaria> rutasList = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(rutasList, pageable, rutasList.size());
		page = rutaDiariaDao.buscarRutasDiarias(pageable, form.getFrom(), form.getTo(), form.getRutas(),
				form.getTrabajadores(), form.getContenedores(), form.getCamiones());
		return page;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<RutaDiaria> buscarRutasDiariasByTrabajador(long trabajadorId, Pageable pageable) {
		logger.info("buscarRutasDiarias");
		List<RutaDiaria> rutasList = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(rutasList, pageable, rutasList.size());
		page = rutaDiariaDao.buscarRutasDiariasByTrabajador(trabajadorId, pageable);
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

	@Override
	public RutaDiaria actualizarRutaDiaria(RutaDiariaDto form) throws DuplicateInstanceException,
			InstanceNotFoundException, InactiveResourceException, RutaIniciadaException {
		RutaDiaria rd = null;
		Camion camion = null;
		rd = rutaDiariaDao.buscarById(form.getId());

		if (rd.getFechaHoraInicio() != null) {
			throw new RutaIniciadaException(rd.getFechaHoraInicio());
		}

		/* Modificamos el camion */
		try {
			camion = camionDao.buscarById(form.getCamion());
			if (!camion.getActivo()) {
				throw new InactiveResourceException(camion.getNombre(), Camion.class.getName());
			}
			rd.setCamion(camion);
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(form.getCamion(), Camion.class.getName());
		}

		/* Moficamos los recolectores asignados */
		if (form.getRecolectores() != null) {
			int size = form.getRecolectores().size();
			if (size == 1) {
				try {
					Trabajador r1 = tDao.buscarById(form.getRecolectores().get(0));
					if (r1 instanceof Recolector) {
						if (!r1.isActiveWorker()) {
							throw new InactiveResourceException(r1.getNombre() + " " + r1.getApellidos(),
									Recolector.class.getName());
						}
						rd.setRecogedor1(r1);
					} else {
						throw new InstanceNotFoundException(form.getRecolectores().get(0), Recolector.class.getName());
					}
				} catch (InstanceNotFoundException e) {
					throw new InstanceNotFoundException(form.getRecolectores().get(0), Recolector.class.getName());
				}
			} else if (size == 2) {
				try {

					Trabajador r1 = tDao.buscarById(form.getRecolectores().get(0));
					Trabajador r2 = tDao.buscarById(form.getRecolectores().get(1));
					if (!(r1 instanceof Recolector))
						throw new InstanceNotFoundException(form.getRecolectores().get(0), Recolector.class.getName());
					if (!(r2 instanceof Recolector))
						throw new InstanceNotFoundException(form.getRecolectores().get(1), Recolector.class.getName());

					if (!r1.isActiveWorker()) {
						throw new InactiveResourceException(r1.getNombre() + " " + r1.getApellidos(),
								Recolector.class.getName());
					}

					if (!r2.isActiveWorker()) {
						throw new InactiveResourceException(r2.getNombre() + " " + r2.getApellidos(),
								Recolector.class.getName());
					}
					rd.setRecogedor1(r1);
					rd.setRecogedor2(r2);

				} catch (InstanceNotFoundException e) {
					throw new InstanceNotFoundException(form.getRecolectores().get(0), Recolector.class.getName());
				}
			} else {
				rd.setRecogedor1(null);
				rd.setRecogedor2(null);
			}
		}
		logger.info("actualizarRutaDiaria paso8");
		/* MODIFICACAMOS EL CONDUCTOR */
		if (form.getConductor() != null) {
			try {
				Trabajador t = tDao.buscarById(form.getConductor());
				if (!(t instanceof Conductor)) {
					throw new InstanceNotFoundException(form.getConductor(), Conductor.class.getName());
				}
				rd.setConductor(t);
			} catch (InstanceNotFoundException e) {
				throw new InstanceNotFoundException(form.getConductor(), Conductor.class.getName());
			}
		}

		logger.info("actualizarRutaDiaria paso10");
		rd = rutaDiariaDao.guardar(rd);
		logger.info("actualizarRutaDiaria fin");
		return rd;
	}

	@Override
	public List<Contenedor> buscarContenedoresDeRutaDiaria(long rutaDiariaId) {
		return rutaDiariaDao.buscarContenedores(rutaDiariaId);
	}

	@Override
	public void crearAlertaTrabajadoresAsignadosAMasDeUnaRuta() {
		Calendar fechaAlerta = Calendar.getInstance();
		fechaAlerta.set(Calendar.HOUR_OF_DAY, 0);
		Prioridad prioridad = Prioridad.M;
		TipoDeAlerta tipo = TipoDeAlerta.TRABAJADORES;
		Mensaje mensaje = Mensaje.TRABAJADOR_ASIGNADO_EN_MAS_DE_UNA_RUTA;

		logger.info("alertasAsignacionRecursosAUnaRutaDiaria");
		List<Trabajador> trabajadores = tDao.buscarTrabajadoresAsignadosAVariasRutas(fechaAlerta);
		for (Trabajador t : trabajadores) {
			String recurso = t.getNombre() + " " + t.getApellidos();
			Alerta a = new Alerta(prioridad, tipo, mensaje, recurso);
			alertaDao.guardar(a);
		}
	}

	@Override
	public void crearAlertaRutaSinConductorAsignado() {
		Calendar fechaAlerta = Calendar.getInstance();
		fechaAlerta.set(Calendar.HOUR_OF_DAY, 0);
		Prioridad prioridad = Prioridad.H;
		TipoDeAlerta tipo = TipoDeAlerta.RUTA;
		Mensaje mensaje = Mensaje.RUTA_SIN_CONDUCTOR_ASIGNADO;
		logger.info("alertasAsignacionRecursosAUnaRutaDiaria");
		List<RutaDiaria> rutas = rutaDiariaDao.buscarRutasGeneradasSinConductor(fechaAlerta);
		for (RutaDiaria r : rutas) {
			String recurso = String.valueOf(r.getId());
			Alerta a = new Alerta(prioridad, tipo, mensaje, recurso);
			alertaDao.guardar(a);
		}
	}

	@Override
	public void crearAlertaRutaSinRecolectoresAsignados() {
		Calendar fechaAlerta = Calendar.getInstance();
		fechaAlerta.set(Calendar.HOUR_OF_DAY, 0);
		Prioridad prioridad = Prioridad.L;
		TipoDeAlerta tipo = TipoDeAlerta.RUTA;
		Mensaje mensaje = Mensaje.RUTA_SIN_RECOLECTORES_ASIGNADOS;
		logger.info("crearAlertaRutaSinRecolectoresAsignado");
		List<RutaDiaria> rutas = rutaDiariaDao.buscarRutasGeneradasSinRecolectores(fechaAlerta);
		for (RutaDiaria r : rutas) {
			String recurso = String.valueOf(r.getId());
			Alerta a = new Alerta(prioridad, tipo, mensaje, recurso);
			alertaDao.guardar(a);
		}
	}

	@Override
	public void crearAlertaContenedoresLlenos() {
		Calendar fechaAlerta = Calendar.getInstance();
		fechaAlerta.set(Calendar.HOUR_OF_DAY, 0);
		Prioridad prioridad = Prioridad.M;
		TipoDeAlerta tipo = TipoDeAlerta.CONTENEDORES;
		Mensaje mensaje = Mensaje.CONTENEDOR_LLENO_COMPLETO;
		logger.info("crearAlertaContenedorLleno 1");
		List<RutaDiaria> rutasGeneradas = rutaDiariaDao.buscarRutasDiariasGeneradas(fechaAlerta);

		for (RutaDiaria r : rutasGeneradas) {
			logger.info("crearAlertaContenedorLleno 2");
			List<RutaDiariaContenedores> rdc = r.getRutaDiariaContenedores();
			for (RutaDiariaContenedores rc : rdc) {
				logger.info("crearAlertaContenedorLleno 3");
				List<Sensor> sensores = rc.getContenedor().getSensores();
				logger.info("crearAlertaContenedorLleno 4");
				for (Sensor s : sensores) {
					if (s instanceof Volumen) {
						logger.info("crearAlertaContenedorLleno 5");
						Valor valor;
						try {
							logger.info("crearAlertaContenedorLleno 6");
							valor = valorDao.obtenerElUltimoValorDeUnSensor(s.getId());
							logger.info("crearAlertaContenedorLleno 7");
							if (valor.getValor().compareTo(new BigDecimal(90)) > 1) {
								logger.info("crearAlertaContenedorLleno 8");
								String recurso = rc.getContenedor().getNombre();
								Alerta a = new Alerta(prioridad, tipo, mensaje, recurso);
								alertaDao.guardar(a);
								logger.info("crearAlertaContenedorLleno 9");
							}
						} catch (InstanceNotFoundException e) {

						}
					}
				}
			}
		}
	}

	@Override
	public void crearAlertaCambioBruscoContenedor() {
		Calendar fechaAlerta = Calendar.getInstance();
		fechaAlerta.set(Calendar.HOUR_OF_DAY, 0);
		Prioridad prioridad = Prioridad.M;
		TipoDeAlerta tipo = TipoDeAlerta.CONTENEDORES;
		Mensaje mensaje = Mensaje.CAMBIO_BRUSCO_DE_VOLUMEN;
		logger.info("crearAlertaCambioBruscoContenedor fecha=>" + fechaAlerta.toString());
		List<RutaDiaria> rutasGeneradas = rutaDiariaDao.buscarRutasDiariasGeneradas(fechaAlerta);

		for (RutaDiaria r : rutasGeneradas) {
			List<RutaDiariaContenedores> rdc = r.getRutaDiariaContenedores();
			for (RutaDiariaContenedores rc : rdc) {
				List<Sensor> sensores = rc.getContenedor().getSensores();
				for (Sensor s : sensores) {
					if (s instanceof Volumen) {
						List<Valor> valores = valorDao.obtenerLosDosUltimosValoresDeUnSensor(s.getId());
						if (valores.size() == 2) {
							if (valores.get(1).getValor().compareTo(BigDecimal.ZERO) > 0) {
								BigDecimal part1 = (valores.get(0).getValor().subtract(valores.get(1).getValor()))
										.abs();
								BigDecimal part2 = (valores.get(1).getValor()).abs();
								BigDecimal porcentaje = part1.divide(part2);
								if (porcentaje.compareTo(new BigDecimal(30)) > 1) {
									String recurso = rc.getContenedor().getNombre();
									Alerta a = new Alerta(prioridad, tipo, mensaje, recurso);
									alertaDao.guardar(a);
								}
							}

						}
					}
				}
			}
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Alerta> buscarAlertas(Pageable pageable) {
		logger.info("buscarAlertas");
		List<Alerta> alertaRuta = new ArrayList<Alerta>();
		Page<Alerta> page = new PageImpl<Alerta>(alertaRuta, pageable, alertaRuta.size());
		logger.info("buscarAlertas paso2");
		page = alertaDao.buscarAlertas(pageable);
		logger.info("buscarAlertas paso 3");
		return page;
	}

	@Transactional(readOnly = true)
	@Override
	public int getNumeroAlertas() {
		int items = alertaDao.obtenerNumeroAlertas();
		return items;
	}

	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}
}
