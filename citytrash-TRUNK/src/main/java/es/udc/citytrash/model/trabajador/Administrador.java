package es.udc.citytrash.model.trabajador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.Idioma;

@Entity
@DiscriminatorValue(value = GlobalNames.DISCRIMINADOR_ADMIN)
public class Administrador extends Trabajador implements Serializable {

	private static final long serialVersionUID = 1L;

	public Administrador() {
		super();
	}

	/**
	 * Constructor para pruebas
	 * 
	 * @param documento
	 * @param nombre
	 * @param apellidos
	 * @param email
	 */
	public Administrador(String documento, String nombre, String apellidos, String email, Idioma idioma) {
		super(documento, nombre, apellidos, email, idioma);
	}

	/**
	 * Administrador
	 * 
	 * @param docId
	 * @param nombre
	 * @param apellidos
	 * @param fechaNacimiento
	 * @param email
	 * @param token
	 * @param fechaExpiracionToken
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
	public Administrador(String docId, String nombre, String apellidos, Calendar fechaNacimiento, String email,
			String token, Calendar fechaExpiracionToken, Idioma idioma, String nombreVia, Integer numero, Integer piso,
			String puerta, String provincia, String localidad, BigDecimal cp, BigDecimal telefono,
			String restoDireccion) {

		super(docId, nombre, apellidos, fechaNacimiento, email, token, fechaExpiracionToken, idioma, nombreVia, numero,
				piso, puerta, provincia, localidad, cp, telefono, restoDireccion);
	}

	@Override
	@Column(name = GlobalNames.CAMPO_ROL_BD)
	public String getRol() {
		return GlobalNames.ROL_ADMINISTRADOR;
	}
}
