package es.udc.citytrash.model.util.excepciones;

public class GeoLocationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11231901230471031L;
	private String msg;

	public GeoLocationException() {
		super();
	}

	public GeoLocationException(String msg) {
		this.msg = System.currentTimeMillis() + ": " + msg;
	}

	public String getMsg() {
		return msg;
	}

}