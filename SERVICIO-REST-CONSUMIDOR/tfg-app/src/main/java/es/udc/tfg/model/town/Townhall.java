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
package es.udc.tfg.model.town;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.udc.tfg.model.province.*;
/**
 * Townhall entity
 * @author hmia
 *
 */
@Entity
@Table(name = "townhalls")
public class Townhall {

	private long id;
	private Province province;	
	private int townHallCode;
	private int dc;
	private String name;
	private float latitude;
	private float longitude;

	public Townhall() {

	}

	/**
	 * Townhall constructor
	 * @param province
	 * @param townCode
	 * @param dc
	 * @param name
	 * @param latitude
	 * @param longitude
	 */
	public Townhall(Province province, int townCode, int dc, String name, float latitude, float longitude) {
		this.province = province;
		this.townHallCode = townCode;
		this.dc = dc;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Townhall constructor
	 * @param province
	 * @param name
	 * @param latitude
	 * @param longitude
	 */
	public Townhall(Province province, String name, float latitude, float longitude) {
		this.province = province;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "province_id")
	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	@Column(name="town_hall_code")
	public int getTownHallCode() {
		return townHallCode;
	}

	public void setTownHallCode(int townCode) {
		this.townHallCode = townCode;
	}

	public int getDc() {
		return dc;
	}

	public void setDc(int dc) {
		this.dc = dc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "TownHall [id=" + id + ", province=" + province + ", townHallCode=" + townHallCode + ", dc=" + dc + ", name="
				+ name + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
}
