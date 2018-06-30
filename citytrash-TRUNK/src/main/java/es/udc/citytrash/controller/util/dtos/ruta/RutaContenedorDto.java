package es.udc.citytrash.controller.util.dtos.ruta;

import javax.validation.constraints.NotNull;

public class RutaContenedorDto {

	public RutaContenedorDto() {
	}

	public RutaContenedorDto(@NotNull long id, @NotNull int posicion) {
		this.id = id;
		this.posicion = posicion;
	}

	@NotNull
	long id;

	@NotNull
	int posicion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RutaContenedorDto other = (RutaContenedorDto) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RutaContenedorDto [id=" + id + ", posicion=" + posicion + "]";
	}
}
