package es.udc.citytrash.model.contenedorModelo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;

/**
 * 
 * @author hmia
 * 
 */

public interface ContenedorModeloDao extends GenericDAO<ContenedorModelo, Integer> {

	/**
	 * Buscar modelo de contenedor por nombre
	 * 
	 * @param nombre
	 * @return
	 * @throws InstanceNotFoundException
	 */
	ContenedorModelo buscarModeloPorNombre(String nombre) throws InstanceNotFoundException;

	/**
	 * Buscar todos los modelos ordenador por modelo
	 * 
	 * @return
	 */
	List<ContenedorModelo> buscarTodosOrderByModelo();
	
	/**
	 * Buscar modelos by tipo de basura
	 * @param tipos
	 * @return
	 */
	List<ContenedorModelo> buscarTodosOrderByModelo(List<Integer> tipos);

	/**
	 * Buscar todos los modelos by pageable
	 * 
	 * @param pageable
	 * @return
	 */
	Page<ContenedorModelo> buscarContenedorModelo(Pageable pageable);

	/**
	 * Buscar modelo por palabras clave y tipos de basura
	 * 
	 * @param pageable
	 * @param palabrasClaveModelo
	 * @param tipos
	 * @return
	 */
	Page<ContenedorModelo> buscarContenedorModelo(Pageable pageable, String palabrasClaveModelo,
			List<TipoDeBasura> tipos);

	

}