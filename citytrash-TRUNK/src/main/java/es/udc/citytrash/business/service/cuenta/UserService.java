package es.udc.citytrash.business.service.cuenta;

import org.springframework.security.core.Authentication;

import es.udc.citytrash.business.entity.idioma.Idioma;
import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.util.excepciones.ExpiredTokenException;
import es.udc.citytrash.business.util.excepciones.InactiveCountException;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.business.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.business.util.excepciones.TokenInvalidException;

public interface UserService {

	/***
	 * Loguearse
	 * 
	 * @param email
	 * @param password
	 * @param estaEncriptada
	 *            si la contrasena pasada está encriptada o no
	 * @return
	 * @throws InstanceNotFoundException
	 *             En caso que no exista una cuenta con ese email
	 * @throws PasswordInvalidException
	 *             En caso que el password sea incorrecto
	 * @throws InactiveCountException
	 *             Cuenta no activada
	 */
	/*Trabajador loguearsePorPassword(String email, String password, boolean estaEncriptada)
			throws InstanceNotFoundException, PasswordInvalidException, InactiveCountException;
	*/
	
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
	 */

	Authentication loguearsePorIdToken(long id, String token) throws TokenInvalidException, ExpiredTokenException;

	/**
	 * Recuperar cuenta
	 * 
	 * @param email
	 * @param appUrl
	 * @throws InstanceNotFoundException
	 *             No existe ninguna cuenta con el email
	 */
	void recuperarCuenta(String email, String appUrl) throws InstanceNotFoundException;

	/***
	 * Actualizar la contrasena por token
	 * 
	 * @param email
	 * @param token
	 * @param password
	 * @throws InstanceNotFoundException
	 *             en caso que no exista una cuenta con ese email
	 */
	void cambiarPassword(String email, String password) throws InstanceNotFoundException;

	/**
	 * 
	 * @param id
	 * @param lang
	 */
	void cambiarIdioma(String email, Idioma lang) throws InstanceNotFoundException;

}
