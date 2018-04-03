package es.udc.citytrash.business.repository.trabajador;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	 * Buscar trabajador por id de trabajador y Token
	 * 
	 * @param id
	 * @param token
	 * @return
	 * @throws TokenInvalidException
	 */
	Trabajador buscarTrabajadorIdToken(long id, String token) throws TokenInvalidException;

	/**
	 * Buscar trabajador por documento
	 * 
	 * @param email
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Trabajador buscarTrabajadorPorDocumentoId(String documentoId) throws InstanceNotFoundException;

	/**
	 * Busca la lista de trabajadores
	 * 
	 * @param pageable pageable
	 * @return
	 */
	Page<Trabajador> buscarTrabajadores(Pageable pageable);
}
