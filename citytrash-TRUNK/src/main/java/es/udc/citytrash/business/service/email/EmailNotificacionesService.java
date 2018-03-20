package es.udc.citytrash.business.service.email;

import java.util.Calendar;

import es.udc.citytrash.business.entity.idioma.Idioma;

//https://memorynotfound.com/spring-mail-sending-email-freemarker-html-template-example/
//http://www.technicalkeeda.com/spring-tutorials/spring-4-sending-email-with-freemarker-template

public interface EmailNotificacionesService {

	/**
	 * Enviar email para activar la cuenta
	 * 
	 * @param id
	 * @param nombre
	 * @param apellidos
	 * @param email
	 * @param token
	 * @param lang
	 * @param appUrl
	 * @param fechaExpiracionToken
	 * @return
	 */
	public void activacionCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl);

	/**
	 * Enviar email para recuperar cuenta
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

	public void recuperarCuentaEmail(long id, String nombre, String apellidos, String email, String token,
			Calendar fechaExpiracionToken, Idioma lang, String appUrl);
}
