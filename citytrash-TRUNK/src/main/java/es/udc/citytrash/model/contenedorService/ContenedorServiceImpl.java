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
import es.udc.citytrash.model.util.excepciones.InactiveResourceException;
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
	public Contenedor modificarContenedor(ContenedorEditarDto form) throws InstanceNotFoundException,
			DuplicateInstanceException, InactiveResourceException, InvalidFieldException {
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
		logger.info("registrarModelo");
		ContenedorModelo modelo;
		TipoDeBasura tipo;
		try {
			modeloDao.buscarModeloPorNombre(form.getNombre().trim());
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

		logger.info("registrarModelo paso4");
		if (form.getCargaNominal().compareTo(new BigDecimal(0)) != 1)
			throw new InvalidFieldException("cargaNominalException", "", Contenedor.class.getName());

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
		logger.info("buscarModelos");
		List<ContenedorModelo> contenedoresList = new ArrayList<ContenedorModelo>();
		Page<ContenedorModelo> page = new PageImpl<ContenedorModelo>(contenedoresList, pageable,
				contenedoresList.size());
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();
		/* Agregamos los tipos de basura */
		for (Integer t : formBusqueda.getTipos()) {
			try {
				TipoDeBasura tb = tipoDao.buscarById(Integer.valueOf(t.toString()));
				tipos.add(tb);
			} catch (NumberFormatException | InstanceNotFoundException e) {
			}
		}
		if (formBusqueda.getPalabrasClaveModelo().trim().isEmpty() && tipos.isEmpty()) {
			page = modeloDao.buscarContenedorModelo(pageable);
		} else {
			page = modeloDao.buscarContenedorModelo(pageable, formBusqueda.getPalabrasClaveModelo(), tipos);
		}
		return page;
	}

	@Override
	public Page<Contenedor> buscarContenedores(Pageable pageable, ContenedorFormBusq form) {
		logger.info("buscarContenedores");
		logger.info("IMPRIMIENDO formbusqueda buscar contenedores => " + form.toString());
		ContenedorModelo modelo = null;
		List<Contenedor> contenedoresList = new ArrayList<Contenedor>();
		Page<Contenedor> page = new PageImpl<Contenedor>(contenedoresList, pageable, contenedoresList.size());
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();
		/* Agregamos los tipos de basura */
		logger.info("antes 1");
		if (form.getTiposDeBasura() != null) {
			for (Integer t : form.getTiposDeBasura()) {
				try {
					TipoDeBasura tb = tipoDao.buscarById(Integer.valueOf(t.toString()));
					tipos.add(tb);
				} catch (NumberFormatException | InstanceNotFoundException e) {

				}
			}
		}
		logger.info("despues 1");

		logger.info("antes 2");

		try {
			if (form.getModelo() != null) {
				modelo = modeloDao.buscarById(Integer.valueOf(form.getModelo()));
			} else {
				modelo = null;
			}
		} catch (InstanceNotFoundException e) {
			modelo = null;
		}
		logger.info("despues 2");

		if (tipos.isEmpty() && modelo == null && form.getBuscar().isEmpty()) {
			logger.info("PASO1");
			page = contenedorDao.buscarContenedores(pageable, form.getMostrarSoloContenedoresActivos(),
					form.getMostrarSoloContenedoresDeAlta());
			logger.info("PASO1 PAGE =>" + page);
		} else {
			logger.info("PASO1");
			page = contenedorDao.buscarContenedores(pageable, form.getBuscar(), modelo, tipos,
					form.getMostrarSoloContenedoresActivos(), form.getMostrarSoloContenedoresDeAlta());
			logger.info("PASO1 PAGE =>" + page);
		}
		return page;
	}

	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

}