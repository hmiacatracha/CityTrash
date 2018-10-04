package es.udc.citytrash.util.enums;

public enum Mensaje {

	TRABAJADOR_ASIGNADO_EN_MAS_DE_UNA_RUTA("alertas.trabajador.asignadoVariasVeces"), RUTA_SIN_CONDUCTOR_ASIGNADO(
			"alertas.ruta.noConductoresAsignados"), RUTA_SIN_RECOLECTORES_ASIGNADOS(
					"alertas.ruta.noRecolectoresAsignados"), CAMBIO_BRUSCO_DE_VOLUMEN(
							"alertas.contenedores.cambioBruscoDeVolumen"), CONTENEDOR_LLENO_COMPLETO(
									"alertas.contenedores.LlenoPorCompleto");
	private final String value;

	private Mensaje(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
