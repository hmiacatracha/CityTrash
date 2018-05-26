package es.udc.citytrash.controller.util.dtos.contenedor;

import java.math.BigDecimal;

public class Localizacion {

	private BigDecimal latitude;

	private BigDecimal longitude;

	public Localizacion() {

	}

	/**
	 * 
	 * @param latitude
	 *            latitud
	 * @param longitude
	 *            longitud
	 */
	public Localizacion(BigDecimal latitude, BigDecimal longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public BigDecimal getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public BigDecimal getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	private String latitudeString() {
		BigDecimal lat = getLatitude();
		String dir = "N";

		if (lat.compareTo(new BigDecimal(0)) == -1) {
			dir = "S";
			lat.multiply(new BigDecimal(-1));
			// *= -1;
		}

		return "" + lat + "°" + dir;
	}

	private String longitudeString() {
		BigDecimal lng = getLongitude();
		String dir = "E";
		/*
		 * if (lng < 0) { dir = "W"; lng *= -1; }
		 */
		if (lng.compareTo(new BigDecimal(0)) == -1) {
			dir = "W";
			lng.multiply(new BigDecimal(-1));
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
