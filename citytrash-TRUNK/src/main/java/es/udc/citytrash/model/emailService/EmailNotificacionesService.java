package es.udc.citytrash.model.emailService;

import java.util.Calendar;

import es.udc.citytrash.util.enums.Idioma;

//https://memorynotfound.com/spring-mail-sending-email-freemarker-html-template-example/
//http://www.technicalkeeda.com/spring-tutorials/spring-4-sending-email-with-freemarker-template

public interface EmailNotificacionesService {

	/**
	 * Enviar email para activar la cuenta
	 * 
	 * @param id
	 *            del trabajador
	 * @param nombre
	 *            del trabajador
	 * @param apellidos
	 *            del trabajador
	 * @param email
	 *            del trabajador
	 * @param token
	 *            del trabajador
	 * @param lang
	 *            idioma de preferncia
	 * @param appUrl
	 *            url de la aplicacion
	 * @param fechaExpiracionToken
	 *            fecha de expiracion del token
	 * @return
	 */
	public void activacionCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl);

	/**
	 * Enviar email para recuperar cuenta
	 * 
	 * @param id
	 *            trabajador id
	 * @param nombre
	 *            nombre del trabajador
	 * @param apellidos
	 *            apellido del trabajador
	 * @param email
	 *            email electrónico
	 * @param token
	 *            token del trabajador
	 * @param fechaExpiracionToken
	 *            fecha de expiracion
	 * @param lang
	 *            idioma de preferencia
	 * @param appUrl
	 *            url de la aplicacion
	 */

	public void recuperarCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl);
}
