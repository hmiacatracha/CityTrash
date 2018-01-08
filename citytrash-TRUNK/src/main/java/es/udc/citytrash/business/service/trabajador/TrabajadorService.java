package es.udc.citytrash.business.service.trabajador;

import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.util.excepciones.ActiveCountException;
import es.udc.citytrash.business.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.business.util.excepciones.InactiveCountException;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.business.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.business.util.excepciones.TokenInvalidException;
import es.udc.citytrash.controller.util.formularios.TrabajadorForm;

public interface TrabajadorService {

	/***
	 * Buscar trabajador por email
	 * 
	 * @param email
	 * @return
	 * @throws InstanceNotFoundException
	 *             en caso que no exista el email
	 */
	Trabajador buscarTrabajadorEmail(String email) throws InstanceNotFoundException;

	/***
	 * Buscar trabajador por email
	 * 
	 * @param documento
	 * @return
	 * @throws InstanceNotFoundException
	 *             en caso que no exista el documento
	 */
	Trabajador buscarTrabajadorDocumento(String documento) throws InstanceNotFoundException;

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
	Trabajador loguearsePorPassword(String email, String password, boolean estaEncriptada)
			throws InstanceNotFoundException, PasswordInvalidException, InactiveCountException;

	/***
	 * Loguear trabajador por token
	 * 
	 * @param token
	 * @return trabajador
	 * @throws TokenInvalidException
	 */
	Trabajador loguearsePorToken(String token) throws TokenInvalidException;

	/****
	 * Registrar a un trabajador
	 * 
	 * @param usuario
	 * @param appUrl
	 * @return
	 * @throws DuplicateInstanceException
	 *             en caso que el email o documento del trabajador que registra
	 *             ya exista en nuestra base de datos
	 */
	// @Secured("ROLE_ADMIN")
	Trabajador registrar(TrabajadorForm usuario, String appUrl) throws DuplicateInstanceException;

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
	void actualizarPasswordPorToken(String email, String token, String password)
			throws InstanceNotFoundException, TokenInvalidException;

}
