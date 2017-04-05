/*-
 * ========================LICENSE_START=================================
 * TFG BYTEPIT-APP
 * %%
 * Copyright (C) 2016 Heidy Mabel Izaguirre Alvarez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * =========================LICENSE_END==================================
 */
package es.udc.tfg.model.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Component Barcelona api
 * 
 * @author hmia
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class Component {

	private String icon;
	private String id;
	private String type;
	private List<CoordinatesBarcelona> coordinates;
	private CoordinatesBarcelona centroid;

	public Component() {

	}

	/**
	 * Default component barcelona api
	 * 
	 * @param icon
	 * @param id
	 * @param type
	 * @param coordinates
	 * @param centroid
	 */
	public Component(String icon, String id, String type, List<CoordinatesBarcelona> coordinates,
			CoordinatesBarcelona centroid) {
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

	public List<CoordinatesBarcelona> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<CoordinatesBarcelona> coordinates) {
		this.coordinates = coordinates;
	}

	public CoordinatesBarcelona getCentroid() {
		return centroid;
	}

	public void setCentroid(CoordinatesBarcelona centroid) {
		this.centroid = centroid;
	}

	@Override
	public String toString() {
		return "[icon=" + icon + ", id=" + id + ", type=" + type + ", coordinates=" + coordinates + ", centroid="
				+ centroid + "]";
	}
}
