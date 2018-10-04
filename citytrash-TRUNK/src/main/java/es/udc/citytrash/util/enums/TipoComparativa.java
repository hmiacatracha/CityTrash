package es.udc.citytrash.util.enums;

public enum TipoComparativa {

	YEAR("YEAR"), MONTH("MONTH"), DAY("DAY");

	private final String value;

	private TipoComparativa(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
