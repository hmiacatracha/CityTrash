package es.udc.citytrash.util.enums;

/**
 * Campos por lo que se puede realizar una busqueda
 * 
 * @author hmia
 *
 */

public enum CampoBusqTrabajador {
	nombre("nombre"), documento("documento"), email("email"), telefono("telefono"), cp("cp");

	private final String value;

	private CampoBusqTrabajador(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
