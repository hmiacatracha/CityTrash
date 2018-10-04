package es.udc.citytrash.util.enums;

public enum Prioridad {

	H("h"), M("m"), L("l");

	private final String value;

	private Prioridad(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
