package es.udc.citytrash.util.enums;

public enum Idioma {
	en(0), es(1);

	private final int value;

	private Idioma(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
