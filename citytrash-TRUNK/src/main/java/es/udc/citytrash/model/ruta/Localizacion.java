package es.udc.citytrash.model.ruta;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Localizacion {

	public Localizacion() {

	}

	public Localizacion(BigDecimal lat, BigDecimal lng) {
		this.lat = lat;
		this.lng = lng;
	}

	@NotNull
	private BigDecimal lat = new BigDecimal(0);

	@NotNull
	private BigDecimal lng = new BigDecimal(0);

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

	@Override
	public String toString() {
		return "[lat=" + lat + ", lng=" + lng + "]";
	}

}
