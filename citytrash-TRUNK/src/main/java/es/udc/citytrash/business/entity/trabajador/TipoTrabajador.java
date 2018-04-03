package es.udc.citytrash.business.entity.trabajador;

public enum TipoTrabajador {
	RECOLEC(1), CONDUCT(2), ADMIN(3);
	private final int value;
	private TipoTrabajador(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
