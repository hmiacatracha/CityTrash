package es.udc.citytrash.controller.error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Employee not found")
public class EmployeeNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3332292346834265371L;

	public EmployeeNotFoundException(long id) {
		super("EmployeeNotFoundException with id=" + id);
	}

}
