package es.udc.citytrash.model.util.excepciones;

public class InstanceNotFoundException extends InstanceException {

	private static final long serialVersionUID = -5517845719446393579L;
	private Object key;
	private String className;

	public InstanceNotFoundException(Object key, String className) {
		super("Instance not found", key, className);
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
