package es.udc.citytrash.model.trabajador;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;
import es.udc.citytrash.util.enums.TipoTrabajador;

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
	 * Actualizar el tipo de trabajador
	 * 
	 * @param t
	 * @param tipoToChange
	 * @return
	 * @throws InstanceNotFoundException
	 */
	void cambiarTipoDeTrabajador(long trabajadorId, TipoTrabajador tipo) throws InstanceNotFoundException;

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
	 * Lista de todos los trabajadores
	 * 
	 * @param List
	 * @return
	 */
	List<Trabajador> buscarTrabajadores();

	/**
	 * Busca la lista de trabajadores
	 * 
	 * @param pageable
	 *            pageable
	 * @return
	 */
	Page<Trabajador> buscarTrabajadores(Pageable pageable, TipoTrabajador trabajadorType);

	/**
	 * Busqueda de trabajadores por nombre y/o apellidos, y tipo
	 * 
	 * @param pageable
	 * @param nombre
	 * @param trabajadorType
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorNombreApellidosYTipo(Pageable pageable, String palabrasClaves,
			TipoTrabajador trabajadorType);

	/**
	 * Busqueda de trabajadores por documento (DNI/NIE y tipo)
	 * 
	 * @param pageable
	 * @param documento
	 * @param trabajadorType
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorDocumentoYTipo(Pageable pageable, String documento,
			TipoTrabajador trabajadorType);

	/**
	 * Busqueda de trabajadores por apellidos y tipo
	 * 
	 * @param pageable
	 * @param apellidos
	 * @param trabajadorType
	 *            tipo de trabajador
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorApellidosYTipo(Pageable pageable, String apellidos,
			TipoTrabajador trabajadorType);

	/**
	 * Búsqueda de trabajores telefono
	 * 
	 * @param pageable
	 * @param telefono
	 * @param trabajadorType
	 *            tipo de trabajador
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorTelefonoYTipo(Pageable pageable, String telefono,
			TipoTrabajador trabajadorType);

	/**
	 * Buscar por email
	 * 
	 * @param pageable
	 * @param email
	 * @param trabajadorType
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorEmailYTipo(Pageable pageable, String email, TipoTrabajador trabajadorType);

	/**
	 * Búsqueda de trabajores por cp
	 * 
	 * @param pageable
	 * @param cp
	 * @param trabajadorType
	 *            tipo de trabajador
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorCpYTipo(Pageable pageable, String cp, TipoTrabajador trabajadorType);
}
