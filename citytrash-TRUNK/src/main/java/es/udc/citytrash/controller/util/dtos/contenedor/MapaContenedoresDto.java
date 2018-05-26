package es.udc.citytrash.controller.util.dtos.contenedor;

public class MapaContenedoresDto {

	public MapaContenedoresDto() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Localizacion getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipoDeBasura) {
		this.tipo = tipoDeBasura;
	}

	private long id;
	private String nombre;
	private Localizacion localizacion = new Localizacion();
	private String tipo;
}
