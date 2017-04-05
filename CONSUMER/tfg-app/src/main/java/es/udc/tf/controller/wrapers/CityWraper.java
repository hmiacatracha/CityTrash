package es.udc.tf.controller.wrapers;

import org.springframework.hateoas.ResourceSupport;

public class CityWraper extends ResourceSupport {

	private String city;
	private float latitude;
	private float longitude;

	public CityWraper(String city, float latitude, float longitude) {
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public CityWraper() {
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}
