package es.udc.citytrash.model.contenedorService;

import java.math.BigDecimal;
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

import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloRegistroDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorRegistroDto;
import es.udc.citytrash.controller.util.dtos.sensor.SensorDto;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedor.ContenedorDao;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorModelo.ContenedorModeloDao;
import es.udc.citytrash.model.sensor.Bateria;
import es.udc.citytrash.model.sensor.Sensor;
import es.udc.citytrash.model.sensor.SensorDao;
import es.udc.citytrash.model.sensor.Temperatura;
import es.udc.citytrash.model.sensor.Volumen;
import es.udc.citytrash.model.sensorValor.Valor;
import es.udc.citytrash.model.sensorValor.ValorDao;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;

@Service("contenedorService")
@Transactional
public class ContenedorServiceImpl implements ContenedorService {

	@Autowired
	ContenedorDao contenedorDao;

	@Autowired
	ContenedorModeloDao modeloDao;

	@Autowired
	TipoDeBasuraDao tipoDao;

	@Autowired
	SensorDao sensorDao;

	@Autowired
	ValorDao valorDao;

	final Logger logger = LoggerFactory.getLogger(ContenedorServiceImpl.class);

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

	@Transactional(readOnly = true)
	@Override
	public boolean esContenedorByNombreExistente(String nombre) {
		try {
			contenedorDao.buscarByNombre(nombre);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@Transactional(readOnly = true)
	@Override
	public boolean esModeloContenedorByNombreExistente(String nombre) {
		try {
			modeloDao.buscarModeloPorNombre(nombre);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean cambiarEstadoContenedor(long id) throws InstanceNotFoundException {
		Contenedor contenedor = contenedorDao.buscarById(id);
		contenedor.setActivo(!contenedor.getActivo());
		contenedorDao.guardar(contenedor);
		return contenedor.getActivo();
	}

	@Transactional(readOnly = true)
	@Override
	public Contenedor buscarContenedorById(long id) throws InstanceNotFoundException {
		try {
			return contenedorDao.buscarById(id);
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(id, Contenedor.class.getName());
		}
	}

	@Transactional(readOnly = true)
	@Override
	public ContenedorModelo buscarModeloById(int id) throws InstanceNotFoundException {
		ContenedorModelo modelo = modeloDao.buscarById(id);
		return modelo;
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContenedorModelo> buscarTodosLosModelosOrderByModelo() {
		List<ContenedorModelo> modelos = modeloDao.buscarTodosOrderByModelo();
		return modelos;
	}

	@Transactional(readOnly = true)
	@Override
	public List<TipoDeBasura> buscarTiposDeBasura() {
		return tipoDao.buscarTodos();
	}

	@Transactional(readOnly = true)
	@Override
	public TipoDeBasura buscarTipoDeBasuraByModelo(int id) throws InstanceNotFoundException {
		return tipoDao.buscarById(modeloDao.buscarById(id).getTipo().getId());
	}

	@Transactional(readOnly = true)
	@Override
	public TipoDeBasura buscarTiposDeBasuraByContenedor(long id) throws InstanceNotFoundException {
		return contenedorDao.buscarById(id).getModelo().getTipo();
	}

	@Override
	public Contenedor registrarContenedor(ContenedorRegistroDto form)
			throws InstanceNotFoundException, DuplicateInstanceException {
		Contenedor contenedor = null;
		ContenedorModelo modelo;
		/* Verificamos que el modelo exista */
		try {
			modelo = modeloDao.buscarById(form.getModeloContenedor());
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(form.getModeloContenedor(), ContenedorModelo.class.getName());
		}
		/* Verificamos que el nombre del contenedor no exista */
		try {
			contenedorDao.buscarByNombre(form.getNombre());
			throw new DuplicateInstanceException(form.getNombre(), Contenedor.class.getName());
		} catch (InstanceNotFoundException e) {

		}

		contenedor = new Contenedor(form.getNombre(), modelo);
		contenedor.setFechaBaja(form.getFechaBaja() != null ? dateToCalendar(form.getFechaBaja()) : null);
		contenedor.setLatitud(form.getLatitud() != null ? form.getLatitud() : null);
		contenedor.setLongitud(form.getLongitud() != null ? form.getLongitud() : null);
		contenedorDao.guardar(contenedor);
		return contenedor;
	}

	@Override
	public Contenedor modificarContenedor(ContenedorEditarDto form)
			throws InstanceNotFoundException, DuplicateInstanceException {
		Contenedor contenedor = null;
		ContenedorModelo modelo;
		/* Verificamos que exista el contenedor */
		try {
			contenedor = contenedorDao.buscarById(form.getId());
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(form.getId(), Contenedor.class.getName());
		}
		/* Verificamos que el modelo exista */
		try {
			// ContenedorModelo modelo =
			modelo = modeloDao.buscarById(form.getModeloContenedor());
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(form.getModeloContenedor(), ContenedorModelo.class.getName());
		}
		try {
			contenedor = contenedorDao.buscarByNombre(form.getNombre());
			if (contenedor.getId() != form.getId())
				throw new DuplicateInstanceException(form.getNombre(), Contenedor.class.getName());
		} catch (InstanceNotFoundException e) {
		}
		contenedor.setNombre(form.getNombre());
		contenedor.setModelo(modelo);
		contenedor.setActivo(form.isActivo());
		contenedor.setFechaBaja(form.getFechaBaja() != null ? dateToCalendar(form.getFechaBaja()) : null);
		// contenedor.setFechaBaja(form.getFechaBaja());
		contenedor.setLatitud(form.getLatitud() != null ? form.getLatitud() : null);
		contenedor.setLongitud(form.getLongitud() != null ? form.getLongitud() : null);
		contenedorDao.guardar(contenedor);
		if (form.isUpdateChildren()) {
			if (form.getSensores() != null) {
				for (SensorDto sensor : form.getSensores()) {
					Sensor s;
					try {
						s = sensorDao.buscarById(sensor.getId());
						// falta cambiar el tipo
						s.setNombre(sensor.getNombre());
						s.setActivo(sensor.isActivo());
						sensorDao.guardar(s);
					} catch (InstanceNotFoundException e) {
						switch (sensor.getSensorType()) {
						case BATERIA:
							s = new Bateria(sensor.getNombre(), contenedor, sensor.isActivo());
							break;
						case TEMPERATURA:
							s = new Temperatura(sensor.getNombre(), contenedor, sensor.isActivo());
							break;
						case VOLUMEN:
							s = new Volumen(sensor.getNombre(), contenedor, sensor.isActivo());
							break;
						default:
							s = new Volumen(sensor.getNombre(), contenedor, sensor.isActivo());
						}
						s.setActivo(sensor.isActivo());
						sensorDao.guardar(s);
					}
				}
			}
		}
		return contenedorDao.buscarById(contenedor.getId());
	}

	@Override
	public ContenedorModelo registrarModelo(ContenedorModeloRegistroDto form)
			throws DuplicateInstanceException, InvalidFieldException {
		ContenedorModelo modelo;
		TipoDeBasura tipo;
		try {
			modeloDao.buscarModeloPorNombre(form.getNombre());
			throw new DuplicateInstanceException(form.getNombre(), ContenedorModelo.class.getName());
		} catch (InstanceNotFoundException e) {
		}
		/* Verificamos el tipo de basura */
		try {
			if (form.getTipo() != null) {
				tipo = tipoDao.buscarById(form.getTipo());
			} else {
				throw new InvalidFieldException("TipoDeBasuraException", "tipo", Contenedor.class.getName());
			}
		} catch (InstanceNotFoundException e) {
			throw new InvalidFieldException("TipoDeBasuraException", "tipo", Contenedor.class.getName());
		}

		if (form.getCapacidadNominal().compareTo(new BigDecimal(0)) != 1) {
			throw new InvalidFieldException("capacidadNominalException", "", Contenedor.class.getName());
		}

		if (form.getCargaNominal().compareTo(new BigDecimal(0)) != 1) {
			throw new InvalidFieldException("cargaNominalException", "", Contenedor.class.getName());
		}

		modelo = new ContenedorModelo(form.getNombre(), form.getCapacidadNominal(), form.getCapacidadNominal());
		modelo.setAltura(form.getAltura());
		modelo.setAnchura(form.getAncho());
		modelo.setPesoVacio(form.getPesoVacio());
		modelo.setProfundidad(form.getProfundidad());
		modelo.setTipo(tipo);
		modeloDao.guardar(modelo);
		return modelo;
	}

	@Override
	public ContenedorModelo modificarModelo(ContenedorModeloEditarDto form)
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		ContenedorModelo modelo = modeloDao.buscarById(form.getId());
		TipoDeBasura tipo;
		ContenedorModelo contenedorModeloEncontrado;

		try {
			contenedorModeloEncontrado = modeloDao.buscarModeloPorNombre(form.getNombre());
			if (contenedorModeloEncontrado.getId() != form.getId())
				throw new DuplicateInstanceException(form.getNombre(), ContenedorModelo.class.getName());
		} catch (InstanceNotFoundException e) {

		}
		/* Verificamos el tipo de basura */
		try {
			if (form.getTipo() != null) {
				tipo = tipoDao.buscarById(form.getTipo());
			} else {
				throw new InvalidFieldException("TipoDeBasuraException", "tipo", Contenedor.class.getName());
			}
		} catch (InstanceNotFoundException e) {
			throw new InvalidFieldException("TipoDeBasuraException", "tipo", Contenedor.class.getName());
		}

		if (form.getCapacidadNominal().compareTo(new BigDecimal(0)) != 1)
			throw new InvalidFieldException("capacidadNominalException", "", Contenedor.class.getName());

		if (form.getCargaNominal().compareTo(new BigDecimal(0)) != 1)
			throw new InvalidFieldException("cargaNominalException", "", Contenedor.class.getName());

		modelo.setModelo(form.getNombre());
		modelo.setCapacidadNominal(form.getCapacidadNominal());
		modelo.setCargaNominal(form.getCargaNominal());
		modelo.setAltura(form.getAltura());
		modelo.setAnchura(form.getAncho());
		modelo.setPesoVacio(form.getPesoVacio());
		modelo.setProfundidad(form.getProfundidad());
		modelo.setTipo(tipo);
		modeloDao.guardar(modelo);
		return modelo;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<ContenedorModelo> buscarModelos(Pageable pageable, ContenedorModeloFormBusq formBusqueda) {
		List<ContenedorModelo> contenedoresList = new ArrayList<ContenedorModelo>();
		Page<ContenedorModelo> page = new PageImpl<ContenedorModelo>(contenedoresList, pageable,
				contenedoresList.size());
		String search = formBusqueda.getPalabrasClaveModelo() == null ? "" : formBusqueda.getPalabrasClaveModelo();
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();

		/* Agregamos los tipos de basura */
		if (formBusqueda.getTipos() != null) {
			for (Integer t : formBusqueda.getTipos()) {
				try {
					TipoDeBasura tb = tipoDao.buscarById(Integer.valueOf(t.toString()));
					tipos.add(tb);
				} catch (NumberFormatException | InstanceNotFoundException e) {

				}
			}
		}

		if (tipos.size() > 0 || search.length() > 0) {
			page = modeloDao.buscarContenedorModelo(pageable, search, tipos);
		} else {
			page = modeloDao.buscarContenedorModelo(pageable);
		}

		return page;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Contenedor> buscarContenedores(ContenedorFormBusq form) {
		ContenedorModelo modelo = null;
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();

		/* tipos de basura */
		if (form.getTiposDeBasura() != null) {
			for (Integer t : form.getTiposDeBasura()) {
				try {
					TipoDeBasura tb = tipoDao.buscarById(Integer.valueOf(t.toString()));
					tipos.add(tb);
				} catch (NumberFormatException | InstanceNotFoundException e) {

				}
			}
		}
		/* Bucamos el modelo */
		try {
			if (form.getModelo() != null) {
				modelo = modeloDao.buscarById(Integer.valueOf(form.getModelo()));
			} else {
				modelo = null;
			}
		} catch (InstanceNotFoundException e) {
			modelo = null;
		}
		return contenedorDao.buscarContenedores(form.getBuscar() != null ? form.getBuscar() : "", modelo, tipos,
				form.getMostrarSoloContenedoresActivos(), form.getMostrarSoloContenedoresDeAlta());
	}

	@Transactional(readOnly = true)
	@Override
	public List<Contenedor> buscarContenedores(List<Long> idsContenedores) {
		return contenedorDao.buscarContenedores(idsContenedores);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Contenedor> buscarContenedoresDiponiblesParaUnaRuta(List<Integer> tiposDeBasura) {
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();

		/* Agregamos los tipos de basura */
		if (tiposDeBasura != null) {
			for (Integer t : tiposDeBasura) {
				try {
					TipoDeBasura tb = tipoDao.buscarById(Integer.valueOf(t.toString()));
					tipos.add(tb);
				} catch (NumberFormatException | InstanceNotFoundException e) {

				}
			}
		}
		return contenedorDao.buscarContenedoresDisponilesParaUnaRutaByTipoDeBasura(tipos);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Contenedor> buscarContenedores(Pageable pageable, ContenedorFormBusq form) {
		ContenedorModelo modelo = null;
		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		Page<Contenedor> page = new PageImpl<Contenedor>(contenedoresList, pageable, contenedoresList.size());
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();

		/* Agregamos los tipos de basura */
		if (form.getTiposDeBasura() != null) {
			for (Integer t : form.getTiposDeBasura()) {
				try {
					TipoDeBasura tb = tipoDao.buscarById(Integer.valueOf(t.toString()));
					tipos.add(tb);
				} catch (NumberFormatException | InstanceNotFoundException e) {

				}
			}
		}
		/* Bucamos el modelo */
		try {
			if (form.getModelo() != null) {
				modelo = modeloDao.buscarById(Integer.valueOf(form.getModelo()));
			} else {
				modelo = null;
			}
		} catch (InstanceNotFoundException e) {
			modelo = null;
		}

		/* Realiamos la búsqueda */
		if (tipos.isEmpty() && modelo == null && form.getBuscar() == null) {
			page = contenedorDao.buscarContenedores(pageable, form.getMostrarSoloContenedoresActivos(),
					form.getMostrarSoloContenedoresDeAlta());
		} else {
			page = contenedorDao.buscarContenedores(pageable, form.getBuscar(), modelo, tipos,
					form.getMostrarSoloContenedoresActivos(), form.getMostrarSoloContenedoresDeAlta());
		}
		return page;
	}

	@Override
	public void eliminarSensorId(Long sensorId) throws InstanceNotFoundException {
		Sensor sensor = sensorDao.buscarById(sensorId);
		sensorDao.eliminar(sensor);
	}

	@Transactional(readOnly = true)
	@Override
	public Sensor buscarSensorById(Long sensorId) throws InstanceNotFoundException {
		return sensorDao.buscarById(sensorId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Sensor> buscarSensorsByContenedor(Long contenedorId) throws InstanceNotFoundException {
		return sensorDao.buscarSensoresByContenedor(contenedorId);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Valor> buscarValoresBySensor(Pageable pageable, Long sensorId) {
		return valorDao.buscarValoresBySensor(pageable, sensorId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Valor> buscarValoresBySensor(Long sensorId, Date fechaInicio, Date fechaFin) {
		return valorDao.buscarValoresBySensor(sensorId, fechaInicio, fechaFin);
	}

	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

}
