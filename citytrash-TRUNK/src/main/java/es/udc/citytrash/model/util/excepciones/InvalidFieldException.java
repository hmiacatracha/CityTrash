package es.udc.citytrash.model.util.excepciones;

@SuppressWarnings("serial")
public class InvalidFieldException extends Exception {

	private Object field;
	private String className;
	private String reason;

	public InvalidFieldException(String reason, Object field, String className) {

		super(reason + " (key = '" + field + "' - className = '" + className + "')");
		this.reason = reason;
		this.field = field;
		this.className = className;
	}

	public Object getKey() {
		return field;
	}

	public String getClassName() {
		return className;
	}

	public String getReason() {
		return reason;
	}
}
