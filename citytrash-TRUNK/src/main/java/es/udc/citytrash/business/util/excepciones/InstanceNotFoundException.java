package es.udc.citytrash.business.util.excepciones;

public class InstanceNotFoundException extends InstanceException {

	private static final long serialVersionUID = -5517845719446393579L;

	public InstanceNotFoundException(Object key, String className) {
		super("Instance not found", key, className);
	}
}
