package es.udc.citytrash.util.enums;

/**
 * Campos por lo que se puede realizar una busqueda
 * 
 * @author hmia
 *
 */

public enum CampoBusqPalabrasClavesCamion {

	matricula("matricula"), nombre("nombre"), vin("vin");

	private final String value;

	private CampoBusqPalabrasClavesCamion(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
