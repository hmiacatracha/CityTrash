package es.udc.citytrash.business.util.excepciones;

import java.util.Calendar;

public class ExpiredTokenException extends Exception {

	private static final long serialVersionUID = 8813633570761819259L;

	private Calendar tokenDate;

	public ExpiredTokenException(Calendar tokenDate) {
		super("Time after tokenDate => startingDate = " + tokenDate);
		this.tokenDate = tokenDate;
	}

	public Calendar getStartingDate() {
		return tokenDate;
	}
}
