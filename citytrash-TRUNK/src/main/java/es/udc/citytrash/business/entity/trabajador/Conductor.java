package es.udc.citytrash.business.entity.trabajador;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CONDUCTOR")
public class Conductor extends Trabajador implements Serializable {
	private static final long serialVersionUID = 1L;

	public Conductor() {
		super();
	}

	/***
	 * Conductor
	 * 
	 * @param documento
	 * @param nombre
	 * @param apellidos
	 * @param rol
	 * @param email
	 * @param token
	 * @para fechaExpiracionToken
	 */
	public Conductor(String documento, String nombre, String apellidos, String rol, String email, String token,
			Calendar fechaExpiracionToken) {
		super(documento, nombre, apellidos, rol, email, token, fechaExpiracionToken);
	}

	/**
	 * 
	 * @param documento
	 * @param nombre
	 * @param apellidos
	 * @param rol
	 * @param email
	 */
	public Conductor(String documento, String nombre, String apellidos, String rol, String email) {
		super(documento, nombre, apellidos, rol, email);
	}

}