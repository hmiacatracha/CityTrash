package es.udc.citytrash.model.trabajador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import es.udc.citytrash.controller.util.WebUtils;
import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.Idioma;

@Entity
@DiscriminatorValue(value = GlobalNames.DISCRIMINADOR_RECOLECTOR)
public class Recolector extends Trabajador implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Recolector() {
		super();
	}

	/**
	 * Constructor para pruebas Recolector
	 * 
	 * @param documento
	 *            documento id
	 * @param nombre
	 *            nombre del trabajador
	 * @param apellidos
	 *            apellidos del trabajador
	 * @param email
	 *            email
	 */
	public Recolector(String documento, String nombre, String apellidos, String email) {
		super(documento, nombre, apellidos, email);
	}

	/**
	 * Constructor con todos los campos
	 * 
	 * @param docId
	 *            dni/nie
	 * @param nombre
	 *            nombre del trabajador
	 * @param apellidos
	 *            Apellidos del trabajador
	 * @param fechaNacimiento
	 *            fecha de nacimiento
	 * @param email
	 *            email (este campo debe se único)
	 * @param token
	 *            token de acceso
	 * @param fechaExpiracionToken
	 *            fecha de Expiracion
	 * @param idioma
	 * @param nombreVia
	 * @param numero
	 * @param piso
	 * @param puerta
	 * @param provincia
	 * @param localidad
	 * @param cp
	 * @param telefono
	 * @param restoDireccion
	 */
	public Recolector(String docId, String nombre, String apellidos, Calendar fechaNacimiento, String email,
			String token, Calendar fechaExpiracionToken, Idioma idioma, String nombreVia, Integer numero, Integer piso,
			String puerta, String provincia, String localidad, BigDecimal cp, BigDecimal telefono,
			String restoDireccion) {

		super(docId, nombre, apellidos, fechaNacimiento, email, token, fechaExpiracionToken, idioma, nombreVia, numero,
				piso, puerta, provincia, localidad, cp, telefono, restoDireccion);
	}

	@Override
	@Column(name = GlobalNames.CAMPO_ROL_BD)
	public String getRol() {
		return GlobalNames.ROL_RECOLECTOR;
	}

}
