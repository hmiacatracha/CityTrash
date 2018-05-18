package es.udc.citytrash.controller.util.dtos.contenedor;

import javax.validation.constraints.NotNull;

public class Localizacion {

	@NotNull
	private double latitude;

	@NotNull
	private double longitude;

	public Localizacion() {

	}

	/**
	 * 
	 * @param latitude
	 *            latitud
	 * @param longitude
	 *            longitud
	 */
	public Localizacion(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	private String latitudeString() {
		double lat = getLatitude();
		String dir = "N";

		if (lat < 0) {
			dir = "S";
			lat *= -1;
		}

		return "" + lat + "°" + dir;
	}

	private String longitudeString() {
		double lng = getLongitude();
		String dir = "E";

		if (lng < 0) {
			dir = "W";
			lng *= -1;
		}
		return "" + lng + "°" + dir;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return latitudeString() + ", " + longitudeString();
	}

}
