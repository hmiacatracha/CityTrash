package es.udc.citytrash.util.enums;

import es.udc.citytrash.util.GlobalNames;

public enum TipoTrabajador {

	NONE(GlobalNames.DISCRIMINADOR_X_DEFECTO), RECOLEC(GlobalNames.DISCRIMINADOR_RECOLECTOR), CONDUCT(GlobalNames.DICRIMINADOR_CONDUCTOR), ADMIN(
			GlobalNames.DISCRIMINADOR_ADMIN);

	private final String value;

	private TipoTrabajador(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
