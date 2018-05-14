package es.udc.citytrash.model.util.excepciones;

public class InactiveResourceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 188392139123L;

	private Object key;

	private String className;

	public InactiveResourceException(Object key, String className) {
		super("Inactive resource with the key:" + key + " of " + className);
		this.key = key;
		this.className = className;
	}

	public Object getKey() {
		return key;
	}

	public String getClassName() {
		return className;
	}
}
