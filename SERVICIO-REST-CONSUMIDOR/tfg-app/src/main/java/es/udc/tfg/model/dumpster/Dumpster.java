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
package es.udc.tfg.model.dumpster;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.udc.tfg.model.town.Townhall;
import es.udc.tfg.model.type.Type;

/**
 * Dumpter entity
 * 
 * @author hmia
 *
 */

@Entity
@Immutable
@Table(name = "dumpsters")
@XmlRootElement
//@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Dumpster {

	/* Atributtes */
	@XmlElement
	private Long dumpsterId;
	private String foreignKeyDumpsterId;
	@XmlElement
	private Townhall townhall;
	@XmlElement
	private Type dumspterType;
	@XmlElement
	private float latitude;
	@XmlElement
	private float longitude;
	@XmlElement
	private float tonsCapacity;

	public Dumpster() {

	}

	/**
	 * Dumpster constructor
	 * 
	 * @param key
	 * @param town
	 * @param type
	 * @param latitude
	 * @param longitude
	 * @param tons
	 */

	public Dumpster(String key, Townhall town, Type type, float latitude, float longitude, float tons) {
		this.foreignKeyDumpsterId = key;
		this.townhall = town;
		this.dumspterType = type;
		this.longitude = longitude;
		this.latitude = latitude;
		this.tonsCapacity = tons;
	}

	@Id
	// @SequenceGenerator(name = "DumpsterIdGenerator", sequenceName =
	// "dumpsterSeq")
	// @GeneratedValue(strategy = GenerationType.AUTO, generator =
	// "DumpsterIdGenerator")
	// http://www.developerscrappad.com/408/java/java-ee/ejb3-jpa-3-ways-of-generating-primary-key-through-generatedvalue/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dumpster_id")
	public Long getDumpsterId() {
		return dumpsterId;
	}

	public void setDumpsterId(Long dumpsterId) {
		this.dumpsterId = dumpsterId;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "townhall")
	public Townhall getTownhall() {
		return townhall;
	}

	public void setTownhall(Townhall town) {
		this.townhall = town;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "dumspter_type")
	public Type getDumspterType() {
		return dumspterType;
	}

	public void setDumspterType(Type dumspterType) {
		this.dumspterType = dumspterType;
	}

	@Column(name = "foreign_key_dumpster_id")
	public String getForeignKeyDumpsterId() {
		return foreignKeyDumpsterId;
	}

	public void setForeignKeyDumpsterId(String foreignKeyDumpsterId) {
		this.foreignKeyDumpsterId = foreignKeyDumpsterId;
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

	@Column(name = "tons_capacity")
	public float getTonsCapacity() {
		return tonsCapacity;
	}

	public void setTonsCapacity(float tons) {
		this.tonsCapacity = tons;
	}

}
