package es.udc.citytrash.util.enums;

import es.udc.citytrash.util.GlobalNames;

public enum TipoSensor {

	VOLUMEN(GlobalNames.DISCRIMINADOR_VOLUMEN), BATERIA(GlobalNames.DISCRIMINADOR_BATERIA), TEMPERATURA(
			GlobalNames.DISCRIMINADOR_TEMPERATURA);

	private final String value;

	private TipoSensor(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
