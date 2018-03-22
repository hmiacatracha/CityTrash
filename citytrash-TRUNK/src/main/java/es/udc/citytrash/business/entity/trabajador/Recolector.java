package es.udc.citytrash.business.entity.trabajador;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "RECOLECTOR")
public class Recolector extends Trabajador implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Recolector() {
		super();
	}

	/***
	 * Recolector
	 * 
	 * @param documento
	 * @param nombre
	 * @param apellidos
	 * @param rol
	 * @param email
	 * @param fechaNacimiento
	 * @param token
	 * @param fechaExpiracionToken
	 */
	public Recolector(String documento, String nombre, String apellidos, String rol, String email,Calendar fechaNacimiento, String token,
			Calendar fechaExpiracionToken) {
		super(documento, nombre, apellidos, rol, email,fechaNacimiento, token, fechaExpiracionToken);
	}

	/**
	 * Recolector
	 * 
	 * @param documento
	 * @param nombre
	 * @param apellidos
	 * @param rol
	 * @param email
	 */
	public Recolector(String documento, String nombre, String apellidos, String rol, String email) {
		super(documento, nombre, apellidos, rol, email);
	}

}
