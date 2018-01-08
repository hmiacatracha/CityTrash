package es.udc.citytrash.business.repository.trabajador;

import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.repository.genericdao.GenericDAO;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.business.util.excepciones.TokenInvalidException;

public interface TrabajadorDao extends GenericDAO<Trabajador, Long> {

	/**
	 * Buscar trabajador por Email
	 * 
	 * @param email
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Trabajador buscarTrabajadorPorEmail(String email) throws InstanceNotFoundException;
	
	
	/**
	 * Buscar trabajador por TOken
	 * 
	 * @param token
	 * @return
	 * @throws TokenInvalidException
	 */
	Trabajador buscarTrabajadorPorToken(String token) throws TokenInvalidException;

	/**
	 * Buscar trabajador por documento
	 * 
	 * @param email
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Trabajador buscarTrabajadorPorDocumentoId(String documentoId) throws InstanceNotFoundException;
}
