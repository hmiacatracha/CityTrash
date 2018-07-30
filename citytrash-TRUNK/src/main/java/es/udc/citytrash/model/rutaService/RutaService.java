package es.udc.citytrash.model.rutaService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.ruta.GenerarRutaFormDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaDiariaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutaDto;
import es.udc.citytrash.controller.util.dtos.ruta.RutasDiariaFormBusq;
import es.udc.citytrash.controller.util.dtos.ruta.RutasFormBusq;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.rutaDiaria.RutaDiaria;
import es.udc.citytrash.model.rutaDiariaContenedores.RutaDiariaContenedores;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InactiveResourceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.RutaIniciadaException;

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

	/**
	 * Listar todas las rutas
	 * 
	 * @param mostrarSoloRutasActivas
	 * @return
	 */
	List<Ruta> buscarRutas(boolean mostrarSoloRutasActivas);

	/**
	 * Cambiar el estado de una ruta
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	boolean cambiarEstadoRuta(int id) throws InstanceNotFoundException;

	/**
	 * Buscar historial de rutas
	 * 
	 * @param trabajadorId
	 * @param pageable
	 * @return
	 */
	Page<RutaDiaria> buscarRutasDiariasByTrabajador(long trabajadorId, Pageable pageable);

	/**
	 * Busqueda de rutas diarias
	 * 
	 * @param pageable
	 * @param form
	 * @return
	 */
	Page<RutaDiaria> buscarRutasDiarias(Pageable pageable, RutasDiariaFormBusq form);

	/**
	 * Lista de rutas a generar
	 * 
	 * @param form
	 * @return
	 */
	List<Ruta> buscarRutasSinGenerar(GenerarRutaFormDto form);

	/**
	 * Generar rutas
	 * 
	 * @param form
	 * @throws GenerarRutasException
	 */
	void generarRutas(GenerarRutaFormDto form);

	/**
	 * Buscar ruta diaria
	 * 
	 * @param rutaDiaria
	 * @return
	 * @throws InstanceNotFoundException
	 */
	RutaDiaria buscarRutaDiaria(long rutaDiaria) throws InstanceNotFoundException;

	/**
	 * 
	 * @param form
	 * @return Actualizar Ruta
	 * @throws DuplicateInstanceException
	 * @throws InstanceNotFoundException
	 * @throws RutaIniciadaException
	 * @throws InactiveResourceException
	 */
	RutaDiaria actualizarRutaDiaria(RutaDiariaDto form) throws DuplicateInstanceException, InstanceNotFoundException,
			RutaIniciadaException, InactiveResourceException;


	/**
	 * Buscar los contenedores que pertenecen a una ruta diaria
	 * @param rutaDiariaId
	 * @return
	 */
	List<Contenedor> buscarContenedoresDeRutaDiaria(long rutaDiariaId);

}
