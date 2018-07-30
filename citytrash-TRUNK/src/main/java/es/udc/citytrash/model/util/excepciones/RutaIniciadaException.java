package es.udc.citytrash.model.util.excepciones;

import java.util.Calendar;

public class RutaIniciadaException extends Exception {

	private static final long serialVersionUID = 8813633570761819259L;

	private Calendar fechaHoraInicio;
	
	public RutaIniciadaException(Calendar tokenDate) {
		super("Ruta ya iniciada " + tokenDate);
		this.fechaHoraInicio = tokenDate;
	}

	public Calendar getStartingDate() {
		return fechaHoraInicio;
	}
}
