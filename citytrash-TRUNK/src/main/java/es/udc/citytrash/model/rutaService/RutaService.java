package es.udc.citytrash.model.rutaService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.ruta.RutaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutasFormBusq;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

/**
 * Servicio Rutas
 * 
 * @author hmia
 *
 */
public interface RutaService {

	/**
	 * Registrar ruta
	 * 
	 * @param form
	 * @return
	 * @throws DuplicateInstanceException
	 * @throws InstanceNotFoundException
	 */
	Ruta registrarRuta(RutaDto form) throws DuplicateInstanceException, InstanceNotFoundException;

	/**
	 * Registro o actualizacion de la ruta
	 * 
	 * @param form
	 * @return
	 * @throws DuplicateInstanceException
	 * @throws InstanceNotFoundException
	 */
	Ruta actualizarRuta(RutaDto form) throws DuplicateInstanceException, InstanceNotFoundException;

	/**
	 * Buscar ruta by id
	 * 
	 * @param rutaId
	 *            id de ruta
	 * @return Ruta
	 * @throws InstanceNotFoundException
	 */
	Ruta buscarRuta(int rutaId) throws InstanceNotFoundException;

	/**
	 * Buscar rutas
	 * 
	 * @param pageable
	 * @param form
	 * @return
	 */
	Page<Ruta> buscarRutas(Pageable pageable, RutasFormBusq form);

}
