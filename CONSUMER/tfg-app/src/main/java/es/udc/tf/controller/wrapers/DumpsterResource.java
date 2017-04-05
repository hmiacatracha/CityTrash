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
package es.udc.tf.controller.wrapers;

import org.springframework.hateoas.ResourceSupport;

import es.udc.tfg.model.town.Townhall;
import es.udc.tfg.model.type.Type;

/**
 * Wrapper of Dumpster entity
 * 
 * @author hmia
 *
 */

public class DumpsterResource extends ResourceSupport {

	private Long dumpsterId;
	private CityWraper townhall;
	private float latitude;
	private float longitude;
	private String type;

	public DumpsterResource() {

	}

	public DumpsterResource(Long dumpsterId, CityWraper townhall, float latitude, float longitude, String type) {
		this.dumpsterId = dumpsterId;
		this.townhall = townhall;
		this.latitude = latitude;
		this.longitude = longitude;
		this.setType(type);
	}

	public Long getDumpsterId() {
		return dumpsterId;
	}

	public void setDumpsterId(Long dumpsterId) {
		this.dumpsterId = dumpsterId;
	}

	public CityWraper getTownhall() {
		return townhall;
	}

	public void setTownhall(CityWraper townhall) {
		this.townhall = townhall;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
