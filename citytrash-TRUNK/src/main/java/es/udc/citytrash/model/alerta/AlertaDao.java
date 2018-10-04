package es.udc.citytrash.model.alerta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.model.util.genericdao.GenericDAO;

/**
 * 
 * @author hmia
 * 
 */

public interface AlertaDao extends GenericDAO<Alerta, Long> {

	/**
	 * Devuelve las alertas generadas el día actual;
	 * 
	 * @param pageable
	 * @return
	 */
	Page<Alerta> buscarAlertas(Pageable pageable);

	int obtenerNumeroAlertas();

}