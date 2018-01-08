package es.udc.citytrash.business.entity.trabajador;

import java.io.Serializable;

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
 * @param documento
 * @param nombre
 * @param apellidos
 * @param rol
 * @param email
 * @param token
 */
	public Conductor(String documento,String nombre, String apellidos, String rol, String email, String token) {
		super(documento,nombre, apellidos, rol, email, token);
	}

}