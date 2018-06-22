package es.udc.citytrash.model.ruta;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class Localizacion {

	Localizacion() {

	}

	public Localizacion(BigDecimal lat, BigDecimal lng) {
		this.lat = lat;
		this.lng = lng;
	}

	private BigDecimal lat;

	private BigDecimal lng;

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

}
