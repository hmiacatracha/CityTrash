package es.udc.citytrash.model.rutaService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.controller.util.dtos.ruta.RutaContenedorDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaTipoBasuraDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutasFormBusq;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camion.CamionDao;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedor.ContenedorDao;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.ruta.RutaDao;
import es.udc.citytrash.model.rutaDiaria.RutaDiariaDao;
import es.udc.citytrash.model.rutaDiariaContenedores.RutaDiariaContenedoresDao;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasuraDao;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.trabajador.TrabajadorDao;
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

		List<Contenedor> contenedores = new ArrayList<Contenedor>();

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
	public Page<Ruta> buscarRutas(Pageable pageable, RutasFormBusq form) {
		logger.info("buscarRutas");
		List<Ruta> rutasList = new ArrayList<Ruta>();
		Page<Ruta> page = new PageImpl<Ruta>(rutasList, pageable, rutasList.size());

		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		List<Contenedor> contenedores = new ArrayList<Contenedor>();
		List<TipoDeBasura> tipos = new ArrayList<TipoDeBasura>();
		List<Camion> camiones = new ArrayList<Camion>();

		logger.info("buscarRutas paso2");
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
		logger.info("buscarRutas paso2.1");
		/* Agregamos los trabajadores */
		if (form.getTrabajadores() != null) {
			for (Long t : form.getTrabajadores()) {
				try {
					Trabajador trabajador = trabajadorDao.buscarById(Long.valueOf(t.toString()));
					trabajadores.add(trabajador);
				} catch (NumberFormatException | InstanceNotFoundException e) {
				}
			}
		}
		logger.info("buscarRutas paso2.2");
		/* Agregamos los camiones */
		if (form.getCamiones() != null) {
			for (Long t : form.getCamiones()) {
				try {
					Camion camion = camionDao.buscarById(Long.valueOf(t.toString()));
					camiones.add(camion);
				} catch (NumberFormatException | InstanceNotFoundException e) {
				}
			}
		}
		logger.info("buscarRutas paso2.3");
		/* Agregamos contenedores */
		if (form.getContenedores() != null) {
			for (Long t : form.getContenedores()) {
				try {
					Contenedor contenedor = contenedorDao.buscarById(Long.valueOf(t.toString()));
					contenedores.add(contenedor);
				} catch (NumberFormatException | InstanceNotFoundException e) {
				}
			}
		}

		logger.info("buscarContenedores paso3");
		page = rutaDao.buscarRutas(pageable, tipos, trabajadores, contenedores, camiones,
				form.isMostrarSoloRutasActivas());
		logger.info("buscarContenedores paso4");
		return page;
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
}
