package es.udc.citytrash.model.camionModelo;

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

public interface CamionModeloDao extends GenericDAO<CamionModelo, Integer> {

	/**
	 * Buscar modelo por nombre
	 * 
	 * @param nombre
	 * @return
	 * @throws InstanceNotFoundException
	 */
	CamionModelo buscarModeloPorNombre(String nombre) throws InstanceNotFoundException;

	/**
	 * Lista de modelos de camiones ordenador por nombre del modelo y por tipos
	 * de basura si el array no es nulo
	 * 
	 * @return
	 */
	List<CamionModelo> buscarTodosOrderByModelo(List<Integer> tiposDeBasura);

	/**
	 * Buscar modelo by pageable
	 * 
	 * @param pageable
	 * @return
	 */
	Page<CamionModelo> buscarCamionModelo(Pageable pageable);

	/**
	 * Buscar modelo by pageable y palabras claaves del modelo
	 * 
	 * @param pageable
	 * @param palabrasClaveModelo
	 * @param tipo
	 *            tipos de basura modelo
	 * @return
	 */
	Page<CamionModelo> buscarCamionModelo(Pageable pageable, String palabrasClaveModelo, List<TipoDeBasura> tipos);

}