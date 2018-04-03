package es.udc.citytrash.business.entity.trabajador;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "3")
public class Administrador extends Trabajador implements Serializable {

	private static final long serialVersionUID = 1L;

	public Administrador() {
		super();
	}

	/***
	 * Administrador
	 * 
	 * @param documento
	 *            documento id
	 * @param nombre
	 *            nombre del trabajador
	 * @param apellidos
	 *            apellidos del trabajador
	 * @param email
	 *            email del trabajador
	 * @param fechaNacimiento
	 *            fecha con formato dd/mm/yyyy
	 * @param token
	 *            token para activar la cuenta
	 * @param fechaExpiracionToken
	 *            fecha de expiracion del token
	 */
	public Administrador(String documento, String nombre, String apellidos, String email, Calendar fechaNacimiento,
			String token, Calendar fechaExpiracionToken) {
		super(documento, nombre, apellidos, "ROLE_ADMIN", email, fechaNacimiento, token, fechaExpiracionToken);
	}

	public Administrador(String documento, String nombre, String apellidos, String email) {
		super(documento, nombre, apellidos, "ROLE_ADMIN", email);
	}
}
