package es.udc.tfg.model.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Component {

	private String icon;
	private String id;
	private String type;
	private List<Coordinates> coordinates;
	private Coordinates centroid;

	public Component() {

	}

	public Component(String icon, String id, String type, List<Coordinates> coordinates, Coordinates centroid) {
		super();
		this.icon = icon;
		this.id = id;
		this.type = type;
		this.coordinates = coordinates;
		this.centroid = centroid;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Coordinates> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinates> coordinates) {
		this.coordinates = coordinates;
	}

	public Coordinates getCentroid() {
		return centroid;
	}

	public void setCentroid(Coordinates centroid) {
		this.centroid = centroid;
	}

	@Override
	public String toString() {
		return "[icon=" + icon + ", id=" + id + ", type=" + type + ", coordinates=" + coordinates + ", centroid="
				+ centroid + "]";
	}
}
