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
package es.udc.tfg.model.sensor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import es.udc.tfg.model.dumpster.Dumpster;
import es.udc.tfg.model.type.Type;

/**
 * 
 * @author hmia
 *
 */
@Entity
@Immutable
@Table(name = "sensors")
public class Sensor {

	private Long sensorId;	
	private String foreignKeySensorId;
	private Dumpster dumpster;
	private Type sensorType;
	private String unity;

	public Sensor() {

	}

	/**
	 * Constructor sensor entity
	 * 
	 * @param foreign_key_sensor_id
	 * @param dumpster
	 * @param sensor_type
	 * @param unity
	 */
	public Sensor(String foreign_key_sensor_id, Dumpster dumpster, Type sensor_type, String unity) {
		this.foreignKeySensorId = foreign_key_sensor_id;
		this.dumpster = dumpster;
		this.sensorType = sensor_type;
		this.unity = unity;
	}

	// @SequenceGenerator(name = "sensorIdGenerator", sequenceName =
	// "sensorSeq")
	// @GeneratedValue(strategy = GenerationType.AUTO, generator =
	// "sensorIdGenerator")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "sensor_id")
	public Long getSensorId() {
		return sensorId;
	}

	public void setSensorId(Long sensorId) {
		this.sensorId = sensorId;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "dumpster_id")
	public Dumpster getDumpster() {
		return dumpster;
	}

	public void setDumpster(Dumpster dumpster) {
		this.dumpster = dumpster;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "sensor_type")
	public Type getSensorType() {
		return sensorType;
	}

	public void setSensorType(Type sensor_type) {
		this.sensorType = sensor_type;
	}

	@Column(name = "foreign_key_sensor_id", nullable = false)
	public String getForeignKeySensorId() {
		return foreignKeySensorId;
	}

	public void setForeignKeySensorId(String foreign_key_sensor_id) {
		this.foreignKeySensorId = foreign_key_sensor_id;
	}

	public String getUnity() {
		return unity;
	}

	public void setUnity(String unity) {
		this.unity = unity;
	}
}
