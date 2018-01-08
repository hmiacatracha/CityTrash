package es.udc.citytrash.business.service.email;

import java.util.Locale;

//https://memorynotfound.com/spring-mail-sending-email-freemarker-html-template-example/
//http://www.technicalkeeda.com/spring-tutorials/spring-4-sending-email-with-freemarker-template

public interface EmailNotificacionesService {

	/**
	 * Enviar email para activar la cuenta
	 * 
	 * @param nombre
	 * @param apellidos
	 * @param email
	 * @param token
	 * @param appUrl
	 * @return
	 */
	boolean activacionCuentaEmail(String nombre, String apellidos, String email, String token, String appUrl);

	/**
	 * Enviar email para recuperar cuenta
	 * 
	 * @param nombre
	 * @param apellidos
	 * @param email
	 * @param token
	 * @param appUrl
	 * @return
	 */

	boolean recuperarCuentaEmail(String nombre, String apellidos, String email, String token, String appUrl);
}
