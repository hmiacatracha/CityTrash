package es.udc.citytrash.model.contenedor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;

/**
 * 
 * @author hmia
 * 
 */

public interface ContenedorDao extends GenericDAO<Contenedor, Long> {

	/**
	 * Buscar contenedor por nombre
	 * 
	 * @param nombre
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Contenedor buscarByNombre(String nombre) throws InstanceNotFoundException;

	/**
	 * Buscar contenedores
	 * 
	 * @param mostrarSoloActivos
	 *            mostrar solo contenedores activos
	 * @param mostrarSoloContenedoresDeAlta
	 *            mostrar solo contenedores de alta
	 * @return
	 */
	List<Contenedor> buscarContenedores(boolean mostrarSoloActivos, boolean mostrarSoloContenedoresDeAlta);

	/**
	 * buscar contenedor by pageable
	 * 
	 * @param pageable
	 * @param mostrarSoloActivos
	 *            mostrar solo contenedores activos
	 * @param mostrarSoloContenedoresDeAlta
	 *            mostrar solo contenedores de alta
	 * @return
	 */
	Page<Contenedor> buscarContenedores(Pageable pageable, boolean mostrarSoloActivos,
			boolean mostrarSoloContenedoresDeAlta);

	/**
	 * Buscar contenedores by modelo y/0 nombre y/o tipo
	 * 
	 * @param pageable
	 * @param palabrasClaves
	 * @param modelo
	 * @param tipos
	 * @param mostrarSoloActivos
	 * @param mostrarSoloContenedoresDeAlta
	 * @return Pageable
	 */
	Page<Contenedor> buscarContenedores(Pageable pageable, String palabrasClaves, ContenedorModelo modelo,
			List<TipoDeBasura> tipos, boolean mostrarSoloActivos, boolean mostrarSoloContenedoresDeAlta);

	/**
	 * Buscar by modelo y/0 nombre y/o tipo
	 * 
	 * @param palabrasClaves
	 * @param modelo
	 * @param tiposDeBasura
	 * @param mostrarSoloActivos
	 * @param mostrarSoloContenedoresDeAlta
	 * @return List
	 */
	List<Contenedor> buscarContenedores(String palabrasClaves, ContenedorModelo modelo,
			List<TipoDeBasura> tiposDeBasura, boolean mostrarSoloActivos, boolean mostrarSoloContenedoresDeAlta);

	/**
	 * Lista de contenedores disponibles en una ruta by id
	 * 
	 * @param tiposDeBasura
	 * @return
	 */
	List<Contenedor> buscarContenedoresDisponilesParaUnaRutaByTipoDeBasura(Integer rutaId,
			List<TipoDeBasura> tiposDeBasura);

	/**
	 * Buscar contenedores by list ids
	 * 
	 * @param ids
	 * @return
	 */
	List<Contenedor> buscarContenedores(List<Long> ids);

	/**
	 * Buscar contenedores by tipos de basura
	 * @param tiposDeBasura
	 * @return
	 */
	List<Contenedor> buscarContenedoresByTiposDeBasura(List<Integer> tiposDeBasura);

}