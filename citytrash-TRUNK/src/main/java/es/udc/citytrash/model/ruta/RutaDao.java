package es.udc.citytrash.model.ruta;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.model.util.genericdao.GenericDAO;

/**
 * 
 * @author hmia
 * 
 */

public interface RutaDao extends GenericDAO<Ruta, Integer> {

	/**
	 * 
	 * @param pageable
	 * @param tiposDeBasura
	 * @param trabajadores
	 * @param contenedores
	 * @param camiones
	 * @param mostrarSoloRutasActivas
	 * @return
	 */
	Page<Ruta> buscarRutas(Pageable pageable, List<Integer> tiposDeBasura, List<Long> trabajadores,
			List<Long> contenedores, List<Long> camiones, boolean mostrarSoloRutasActivas);

	/**
	 * Buscar la lista de rutas que no se han generado. Tiene en cuenta la fecha
	 * de la ruta, el tipo de basura y los camiones. Ademas tambien verifica que
	 * la ruta este activa.
	 * 
	 * @param fecha
	 * @param tiposDeBasura
	 * @param camiones
	 * @return
	 */
	List<Ruta> buscarRutasSinGenerar(Date fecha, List<Integer> tiposDeBasura, List<Long> camiones);

	/**
	 * Devuelve la lista de rutas activas
	 * 
	 * @param mostrarSoloRutasActivas
	 * @return
	 */
	List<Ruta> buscarRutas(boolean mostrarSoloRutasActivas);

}