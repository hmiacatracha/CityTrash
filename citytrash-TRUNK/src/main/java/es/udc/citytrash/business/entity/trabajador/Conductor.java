package es.udc.citytrash.business.entity.trabajador;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
// @DiscriminatorValue(value = "CONDUCTOR")
@DiscriminatorValue(value = "2")
public class Conductor extends Trabajador implements Serializable {
	private static final long serialVersionUID = 1L;

	public Conductor() {
		super();
	}

	/***
	 * Conductor
	 * 
	 * @param documento
	 *            id del trabajador
	 * @param nombre
	 *            del trabajador
	 * @param apellidos
	 *            del trabajador
	 * @param email
	 *            email del trabajador
	 * @param fechaNacimiento
	 *            fecha de nacimiento
	 * @param token
	 *            token del trabajador
	 * @para fechaExpiracionToken fecha de expiracion del token
	 */
	public Conductor(String documento, String nombre, String apellidos, String email, Calendar fechaNacimiento,
			String token, Calendar fechaExpiracionToken) {
		super(documento, nombre, apellidos, "ROLE_USER", email, fechaNacimiento, token, fechaExpiracionToken);
	}

	/**
	 * 
	 * @param documento
	 *            id del trabajador
	 * @param nombre
	 *            bombre del trabajador
	 * @param apellidos
	 *            apellidos
	 * @param email
	 *            email
	 */
	public Conductor(String documento, String nombre, String apellidos, String rol, String email) {
		super(documento, nombre, apellidos, "ROLE_USER", email);
	}

}