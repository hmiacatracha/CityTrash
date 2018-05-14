package es.udc.citytrash.controller.excepciones;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Recourse not found")
public class ResourceNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3332292346834265371L;

	public ResourceNotFoundException(long id) {
		super("ResourceNotFoundException with id=" + id);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
