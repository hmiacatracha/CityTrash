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
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedor.ContenedorDao;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.contenedorModelo.ContenedorModeloDao;
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

	final Logger logger = LoggerFactory.getLogger(ContenedorServiceImpl.class);

	@Override
	public boolean esModeloExistenteById(int modelo) {
		logger.info("esModeloExistenteById");
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
	public boolean esContenedorByNombreExistente(String nombre) {
		logger.info("esContenedorByNombreExistente");
		try {
			contenedorDao.buscarByNombre(nombre);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean esModeloContenedorByNombreExistente(String nombre) {
		logger.info("esModeloContenedorByNombreExistente");
		try {
			modeloDao.buscarModeloPorNombre(nombre);
			return true;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean cambiarEstadoContenedor(long id) throws InstanceNotFoundException {
		logger.info("cambiarEstadoContenedor");
		Contenedor contenedor = contenedorDao.buscarById(id);
		contenedor.setActivo(!contenedor.getActivo());
		contenedorDao.guardar(contenedor);
		return contenedor.getActivo();
	}

	@Override
	public Contenedor buscarContenedorById(long id) throws InstanceNotFoundException {
		logger.info("buscarContenedorById");
		try {
			return contenedorDao.buscarById(id);
		} catch (InstanceNotFoundException e) {
			throw new InstanceNotFoundException(id, Contenedor.class.getName());
		}
	}

	@Override
	public ContenedorModelo buscarModeloById(int id) throws InstanceNotFoundException {
		logger.info("buscarModeloById");
		ContenedorModelo modelo = modeloDao.buscarById(id);
		return modelo;
	}

	@Override
	public List<ContenedorModelo> buscarTodosLosModelosOrderByModelo() {
		logger.info("buscarTodosLosModelosOrderByModelo");
		List<ContenedorModelo> modelos = modeloDao.buscarTodosOrderByModelo();
		return modelos;
	}

	@Override
	public List<TipoDeBasura> buscarTiposDeBasura() {
		logger.info("buscarTiposDeBasura");
		return tipoDao.buscarTodos();
	}

	@Override
	public TipoDeBasura buscarTipoDeBasuraByModelo(int id) throws InstanceNotFoundException {
		logger.info("buscarTiposDeBasuraByModelo");
		return tipoDao.buscarById(modeloDao.buscarById(id).getTipo().getId());
	}

	@Override
	public TipoDeBasura buscarTiposDeBasuraByContenedor(long id) throws InstanceNotFoundException {
		logger.info("buscarTiposDeBasuraByContenedor");
		return contenedorDao.buscarById(id).getModelo().getTipo();
	}

	@Override
	public Contenedor registrarContenedor(ContenedorRegistroDto form)
			throws InstanceNotFoundException, DuplicateInstanceException {
		logger.info("registrarContenedor");
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
		logger.info("modificarContenedor");
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
		contenedor.setLatitud(form.getLatitud() != null ? form.getLatitud() : null);
		contenedor.setLongitud(form.getLongitud() != null ? form.getLongitud() : null);
		contenedorDao.guardar(contenedor);
		return contenedor;
	}

	@Override
	public ContenedorModelo registrarModelo(ContenedorModeloRegistroDto form)
			throws DuplicateInstanceException, InvalidFieldException {
		logger.info("registrarModelo => " + form.getNombre());
		ContenedorModelo modelo;
		TipoDeBasura tipo;
		try {
			logger.info("paso0");
			modeloDao.buscarModeloPorNombre(form.getNombre());
			logger.info("paso1");
			throw new DuplicateInstanceException(form.getNombre(), ContenedorModelo.class.getName());
		} catch (InstanceNotFoundException e) {
			logger.info("paso2");
		}
		/* Verificamos el tipo de basura */
		try {
			if (form.getTipo() != null) {
				logger.info("paso2.1");
				tipo = tipoDao.buscarById(form.getTipo());
				logger.info("paso3");
			} else {
				logger.info("paso4");
				throw new InvalidFieldException("TipoDeBasuraException", "tipo", Contenedor.class.getName());
			}
		} catch (InstanceNotFoundException e) {
			logger.info("paso5");
			throw new InvalidFieldException("TipoDeBasuraException", "tipo", Contenedor.class.getName());
		}

		if (form.getCapacidadNominal().compareTo(new BigDecimal(0)) != 1) {
			logger.info("paso6");
			throw new InvalidFieldException("capacidadNominalException", "", Contenedor.class.getName());
		}

		logger.info("registrarModelo paso4");
		if (form.getCargaNominal().compareTo(new BigDecimal(0)) != 1) {
			logger.info("paso7");
			throw new InvalidFieldException("cargaNominalException", "", Contenedor.class.getName());
		}

		modelo = new ContenedorModelo(form.getNombre(), form.getCapacidadNominal(), form.getCapacidadNominal());
		modelo.setAltura(form.getAltura());
		modelo.setAnchura(form.getAncho());
		modelo.setPesoVacio(form.getPesoVacio());
		modelo.setProfundidad(form.getProfundidad());
		modelo.setTipo(tipo);
		logger.info("paso8");
		modeloDao.guardar(modelo);
		logger.info("paso9");
		return modelo;
	}

	@Override
	public ContenedorModelo modificarModelo(ContenedorModeloEditarDto form)
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException {
		logger.info("modificarModelo");
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

	@Override
	public Page<ContenedorModelo> buscarModelos(Pageable pageable, ContenedorModeloFormBusq formBusqueda) {
		logger.info("IMPRIMIENDO formbusqueda buscar modelos de contenedores => " + formBusqueda.toString());
		logger.info("buscarModelos1");

		List<ContenedorModelo> contenedoresList = new ArrayList<ContenedorModelo>();
		logger.info("buscarModelos2");
		Page<ContenedorModelo> page = new PageImpl<ContenedorModelo>(contenedoresList, pageable,
				contenedoresList.size());
		logger.info("buscarModelos3");
		String search = formBusqueda.getPalabrasClaveModelo() == null ? "" : formBusqueda.getPalabrasClaveModelo();
		logger.info("buscarModelos4");
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();

		/* Agregamos los tipos de basura */
		if (formBusqueda.getTipos() != null) {
			for (Integer t : formBusqueda.getTipos()) {
				try {
					TipoDeBasura tb = tipoDao.buscarById(Integer.valueOf(t.toString()));
					logger.info("tipo encontrado =>" + tb.toString());
					tipos.add(tb);
				} catch (NumberFormatException | InstanceNotFoundException e) {

				}
			}
		}

		logger.info("buscarModelos5");
		if (tipos.size() > 0 || search.length() > 0) {
			logger.info("buscarModelos antes de buscar1");
			page = modeloDao.buscarContenedorModelo(pageable, search, tipos);
			logger.info("buscarModelos despues de buscar1");
		} else {
			logger.info("buscarModelos antes de buscar2");
			page = modeloDao.buscarContenedorModelo(pageable);
			logger.info("buscarModelos despues de buscar2");
		}

		return page;
	}

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

	@Override
	public Page<Contenedor> buscarContenedores(Pageable pageable, ContenedorFormBusq form) {
		logger.info("buscarContenedores");
		ContenedorModelo modelo = null;
		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		Page<Contenedor> page = new PageImpl<Contenedor>(contenedoresList, pageable, contenedoresList.size());
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();

		logger.info("buscarContenedores paso2");
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
		logger.info("buscarContenedores paso3");
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

		logger.info("buscarContenedores paso4");
		/* Realiamos la búsqueda */
		if (tipos.isEmpty() && modelo == null && form.getBuscar() == null) {
			logger.info("buscarContenedores paso5");
			page = contenedorDao.buscarContenedores(pageable, form.getMostrarSoloContenedoresActivos(),
					form.getMostrarSoloContenedoresDeAlta());
			logger.info("buscarContenedores paso5.1");
		} else {
			logger.info("buscarContenedores paso6");
			page = contenedorDao.buscarContenedores(pageable, form.getBuscar(), modelo, tipos,
					form.getMostrarSoloContenedoresActivos(), form.getMostrarSoloContenedoresDeAlta());
			logger.info("buscarContenedores paso6.1");
		}
		logger.info("buscarContenedores paso6");
		return page;
	}

	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

}
