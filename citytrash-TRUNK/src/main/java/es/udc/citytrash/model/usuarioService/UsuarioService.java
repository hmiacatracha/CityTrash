package es.udc.citytrash.model.usuarioService;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;

import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.util.excepciones.ExpiredTokenException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.util.enums.Idioma;

public interface UsuarioService {

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
}
