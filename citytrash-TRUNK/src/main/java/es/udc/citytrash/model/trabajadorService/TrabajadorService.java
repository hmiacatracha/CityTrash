package es.udc.citytrash.model.trabajadorService;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;

import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorBusqFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorRegistroFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorUpdateFormDto;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.Recolector;
import es.udc.citytrash.model.trabajador.Telefono;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.ExpiredTokenException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.util.enums.Idioma;

public interface TrabajadorService {

	/****
	 * Registrar a un trabajador
	 * 
	 * @param usuario
	 * @return
	 * @throws DuplicateInstanceException
	 *             en caso que el email o documento del trabajador que registra
	 *             ya exista en nuestra base de datos
	 */

	Trabajador registrar(TrabajadorRegistroFormDto usuario) throws DuplicateInstanceException;

	/***
	 * actulizar trabajadorId
	 * 
	 * @param usuario
	 * @return
	 * @throws DuplicateInstanceException
	 * @throws InstanceNotFoundException
	 */
	Trabajador modificarTrabajador(TrabajadorUpdateFormDto usuario)
			throws InstanceNotFoundException, DuplicateInstanceException;

	/**
	 * Buscar trabajador por Id
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Trabajador buscarTrabajador(long id) throws InstanceNotFoundException;

	/***
	 * Buscar trabajador por email
	 * 
	 * @param email
	 * @return
	 * @throws InstanceNotFoundException
	 *             en caso que no exista el email
	 */
	Trabajador buscarTrabajadorByEmail(String email) throws InstanceNotFoundException;

	/***
	 * Buscar trabajador por email
	 * 
	 * @param documento
	 * @return
	 * @throws InstanceNotFoundException
	 *             en caso que no exista el documento
	 */
	Trabajador buscarTrabajadorDocumento(String documento) throws InstanceNotFoundException;

	/**
	 * Busqueda de trabajador con un formulario de búsqueda
	 * 
	 * @param pageable
	 * @param formBusqueda
	 * @throws FormBusquedaException
	 *             => error en los campos de busqueda
	 * @return
	 */
	Page<Trabajador> buscarTrabajadores(Pageable pageable, TrabajadorBusqFormDto formBusqueda)
			throws FormBusquedaException;

	/**
	 * Cambia el estado de un trabajador (activo o desactiva)
	 * 
	 * @param trabajadorId
	 * @throws InstanceNotFoundException
	 */
	void cambiarEstadoTrabajador(long trabajadorId) throws InstanceNotFoundException;

	/**
	 * Obtiene la lista de recolectores ordenados por apellidos
	 * 
	 * @param mostrarSoloActivos
	 *            mostrar solo los trabajadores activos o todos
	 * @return
	 */
	List<Recolector> buscarRecolectores(Boolean mostrarSoloActivos);

	/**
	 * Obtiene la lista de conductores ordenados por apellidos
	 * 
	 * @param mostrarSoloActivos
	 *            mostrar solo los activos o todos
	 * @return
	 */
	List<Conductor> buscarConductores(Boolean mostrarSoloActivos);

	/**
	 * Buscar la lista de trabajadores ordenados por apellidos
	 * 
	 * @param mostrarSoloActivos
	 * @return
	 */
	List<Trabajador> buscarTrabajadores(Boolean mostrarSoloActivos);

	/**
	 * Elimina un telefono de la lista
	 * 
	 * @param trabajadorId
	 * @param tel
	 * @throws InstanceNotFoundException
	 */
	void eliminarTelefono(long trabajadorId, Telefono tel) throws InstanceNotFoundException;

	/**
	 * Loguear trabajador id del trabajador y por token
	 * 
	 * @param id
	 * @param token
	 * @return
	 * @throws TokenInvalidException
	 *             => token invalido
	 * @throws ExpiredTokenException
	 *             => token expirado, el token se expira a partir de cuando se
	 *             activa mas quince minutos
	 * @throws DisabledException
	 *             Cuenta desactivada
	 */

	Authentication loguearsePorIdToken(long id, String token)
			throws TokenInvalidException, ExpiredTokenException, DisabledException;

	/**
	 * Recuperar cuenta
	 * 
	 * @param email
	 *            email del trabajador
	 * @throws InstanceNotFoundException
	 *             No existe ninguna cuenta con el email
	 * @throws DisabledException
	 *             Cuenta desactivada
	 */
	void recuperarCuenta(String email) throws InstanceNotFoundException, DisabledException;

	/**
	 * Reiniciar password => Se utiliza cuando se activa o se recupera la cuenta
	 * del usuario
	 * 
	 * @param trabajadorId
	 *            id del trabajado
	 * @param nuevaPassword
	 *            password
	 * @throws InstanceNotFoundException
	 */
	void reiniciarPassword(long trabajadorId, String nuevaPassword) throws InstanceNotFoundException;

	/**
	 * Se utiliza para cambiar las credenciales del usuario autenticado
	 * 
	 * @param trabajadorId
	 *            id del trabajador
	 * @param antiguaPassword
	 *            password antigua
	 * @param nuevaPassword
	 *            nueva password
	 * @throws InstanceNotFoundException
	 * @throws PasswordInvalidException
	 */
	void cambiarPassword(long trabajadorId, String antiguaPassword, String nuevaPassword)
			throws InstanceNotFoundException, PasswordInvalidException;

	/**
	 * 
	 * @param id
	 * @param lang
	 */
	void cambiarIdioma(String email, Idioma lang) throws InstanceNotFoundException;

	/**
	 * obtener idioma de preferencia
	 * 
	 * @param trabajadorId
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Idioma obtenerIdiomaPreferencia(long trabajadorId) throws InstanceNotFoundException;

	/**
	 * Obtener trabajador por email
	 * 
	 * @param email
	 * @return
	 * @throws InstanceNotFoundExceptio
	 */
	Trabajador buscarTrabajadorPorEmail(String email) throws InstanceNotFoundException;

	/**
	 * 
	 * @param id
	 * @param nombre
	 * @param apellidos
	 * @param email
	 * @param token
	 * @param fechaExpiracionToken
	 * @param lang
	 * @param appUrl
	 */
	void activacionCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl);

	/**
	 * 
	 * @param id
	 * @param nombre
	 * @param apellidos
	 * @param email
	 * @param token
	 * @param fechaExpiracionToken
	 * @param lang
	 * @param appUrl
	 */
	void recuperarCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl);

}
