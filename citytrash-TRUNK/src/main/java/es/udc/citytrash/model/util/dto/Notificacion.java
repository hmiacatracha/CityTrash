package es.udc.citytrash.model.util.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Notificacion {

	public Notificacion(@NotNull Object key, @NotNull String className) {
		this.key = key;
		this.className = className;
	}

	public Notificacion(@NotNull String className, @NotNull Object key, @Min(value = 1) List<String> mensajes) {
		this.className = className;
		this.key = key;
		this.mensajes = mensajes != null ? mensajes : new ArrayList<String>();
	}

	public List<String> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<String> tiposDeMensaje) {
		this.mensajes = tiposDeMensaje;
	}

	public void addMensaje(String tipoDeMensaje) {
		this.mensajes.add(tipoDeMensaje);
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@NotNull
	private Object key;

	@NotNull
	private String className;

	@Min(value = 1)
	private List<String> mensajes = new ArrayList<String>();

	@Override
	public String toString() {
		return "ListadoDeErrores [id=" + key + ", tiposDeMensaje=" + mensajes + "]";
	}
}
