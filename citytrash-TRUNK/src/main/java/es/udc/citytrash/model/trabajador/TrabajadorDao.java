package es.udc.citytrash.model.trabajador;

import java.util.Calendar;
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
	 * @param mostrarSoloActivos
	 *            mostrar solo activos
	 * @return
	 */
	List<Trabajador> buscarTrabajadoresOrderByApellidos(boolean mostrarSoloActivos);

	/**
	 * Busca la lista de trabajadores
	 * 
	 * @param pageable
	 *            pageable
	 * @param mostrarTodos
	 *            mostrar los trabajadores de baja
	 * @return
	 */
	Page<Trabajador> buscarTrabajadores(Pageable pageable, TipoTrabajador trabajadorType, Boolean mostrarTodos);

	/**
	 * Busqueda de trabajadores por nombre y/o apellidos, y tipo
	 * 
	 * @param pageable
	 *            pageable
	 * @param nombre
	 *            nombre del trabajador
	 * @param trabajadorType
	 *            tipo de trabajador
	 * @param mostrarTodos
	 *            mostrar los trabajadores de baja
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorNombreApellidosYTipo(Pageable pageable, String palabrasClaves,
			TipoTrabajador trabajadorType, Boolean mostrarTodos);

	/**
	 * Busqueda de trabajadores por documento (DNI/NIE y tipo)
	 * 
	 * @param pageable
	 *            pageable
	 * @param documento
	 *            documento (Dni/nie)
	 * @param trabajadorType
	 *            tipo de trabajador
	 * @param mostrarTodos
	 *            mostrar trabajadores de baja
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorDocumentoYTipo(Pageable pageable, String documento,
			TipoTrabajador trabajadorType, Boolean mostrarTodos);

	/**
	 * Busqueda de trabajadores por apellidos y tipo
	 * 
	 * @param pageable
	 *            pageable
	 * @param apellidos
	 *            apellidos
	 * @param trabajadorType
	 *            tipo de trabajador
	 * @param mostrarTodos
	 *            mostrar trabajadores de baja
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorApellidosYTipo(Pageable pageable, String apellidos,
			TipoTrabajador trabajadorType, Boolean mostrarTodos);

	/**
	 * Búsqueda de trabajores telefono
	 * 
	 * @param pageable
	 *            pageable
	 * @param telefono
	 *            telefono
	 * @param trabajadorType
	 *            tipo de trabajador tipo de trabajador
	 * @param mostrarTodos
	 *            mostrar trabajadores de baja
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorTelefonosYTipo(Pageable pageable, String telefono,
			TipoTrabajador trabajadorType, Boolean mostrarTodos);

	/**
	 * Buscar por email
	 * 
	 * @param pageable
	 *            pageable
	 * @param email
	 *            email
	 * @param trabajadorType
	 *            tipo de trabajador
	 * @param mostrarTodos
	 *            mostrar trabajadores de baja
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorEmailYTipo(Pageable pageable, String email, TipoTrabajador trabajadorType,
			Boolean mostrarTodos);

	/**
	 * Búsqueda de trabajores por cp
	 * 
	 * @param pageable
	 *            pageable
	 * @param cp
	 *            codigo postal
	 * @param trabajadorType
	 *            tipo de trabajador tipo de trabajador
	 * @param mostrarTodos
	 *            mostrar trabajadores de baja
	 * @return
	 */
	Page<Trabajador> buscarTrabajadoresPorCpYTipo(Pageable pageable, String cp, TipoTrabajador trabajadorType,
			Boolean mostrarTodos);

	/**
	 * Lista de trabajadores asignados a varias rutas diarias
	 * 
	 * @param fecha
	 *            fecha de la ruta
	 * @return Lista de trabajadores
	 */
	List<Trabajador> buscarTrabajadoresAsignadosAVariasRutas(Calendar fecha);
}
