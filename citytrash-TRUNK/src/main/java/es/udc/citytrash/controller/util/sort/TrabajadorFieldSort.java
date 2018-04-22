package es.udc.citytrash.controller.util.sort;

public enum TrabajadorFieldSort {

	ID("id"), NAME("nombre"), SURNAME("apellidos"), ROL("rol"), TYPE("trabajadorType");

	private String propertie;

	TrabajadorFieldSort(String propertie) {
		this.propertie = propertie;
	}

	public String propertie() {
		return propertie;
	}
}
