package es.udc.citytrash.model.camionModeloTipoDeBasura;

import java.util.List;

import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasuraPK;
import es.udc.citytrash.model.util.genericdao.GenericDAO;

/**
 * 
 * @author hmia
 * 
 */

public interface CamionModeloTipoDeBasuraDao extends GenericDAO<CamionModeloTipoDeBasura, CamionModeloTipoDeBasuraPK> {

	/**
	 * Buscar tipos de basura by modelo
	 * @param modeloId
	 * @return
	 */
	List<CamionModeloTipoDeBasura> buscarTiposDeBasuraByModelo(int modeloId);
	
	

}