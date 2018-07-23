package es.udc.citytrash.model.rutaDiaria;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;

/**
 * 
 * @author hmia
 * 
 */

public interface RutaDiariaDao extends GenericDAO<RutaDiaria, Long> {


	/**
	 * Buscar rutas diarias by filter
	 * 
	 * @param pageable
	 * @param desde
	 * @param hasta
	 * @param rutas
	 * @param trabajadores
	 * @param contenedores
	 * @param camiones
	 * @return
	 */
	Page<RutaDiaria> buscarRutasDiarias(Pageable pageable, Date desde, Date hasta, List<Integer> rutas,
			List<Long> trabajadores, List<Long> contenedores, List<Long> camiones);

	/**
	 * Buscar rutas generadas en un rango de fechas
	 * @param fechaInicio
	 * @param fechaFin
	 * @param rutaId
	 * @return
	 */
	List<RutaDiaria> buscarRutasDiarias(Date fechaInicio, Date fechaFin, int rutaId);

}