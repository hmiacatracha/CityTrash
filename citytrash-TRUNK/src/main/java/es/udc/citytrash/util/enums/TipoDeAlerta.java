package es.udc.citytrash.util.enums;

public enum TipoDeAlerta {

	CONTENEDORES("contenedores"), RUTA("rutas"), TRABAJADORES("trabajadores");

	private final String value;

	private TipoDeAlerta(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
