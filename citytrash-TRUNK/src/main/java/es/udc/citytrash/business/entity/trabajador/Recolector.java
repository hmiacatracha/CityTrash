package es.udc.citytrash.business.entity.trabajador;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "1")
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
	 * @param documento coducmento id del trabajador (DNI, passaporte, etc)
	 * @param nombre nombre del trabajador
	 * @param apellidos apellido del trabajador
	 * @param email email del trabajador
	 * @param fechaNacimiento fecha_nacimiento
	 * @param token token del trabajador
	 * @param fechaExpiracionToken fecha de expiracion
	 */
	public Recolector(String documento, String nombre, String apellidos, String email,Calendar fechaNacimiento, String token,
			Calendar fechaExpiracionToken) {
		super(documento, nombre, apellidos, "ROLE_USER", email,fechaNacimiento, token, fechaExpiracionToken);
	}

	/**
	 * Recolector
	 * 
	 * @param documento documento id
	 * @param nombre nombre del trabajador
	 * @param apellidos apellidos del trabajador
	 * @param email email
	 */
	public Recolector(String documento, String nombre, String apellidos, String email) {
		super(documento, nombre, apellidos, "ROLE_USER", email);
	}

}
