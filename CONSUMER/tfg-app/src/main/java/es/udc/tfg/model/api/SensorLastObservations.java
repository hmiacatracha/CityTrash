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

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.udc.tfg.util.CalendarDeserializer;
import es.udc.tfg.util.CalendarSerializer;

/**
 * Barcelona Sensor api
 * 
 * @author hmia
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SensorLastObservations {

	private static final float DEFAULT = 0;

	@JsonDeserialize(using = CalendarDeserializer.class)
	@JsonSerialize(using = CalendarSerializer.class)
	private Calendar timestamp;

	private String sensor;

	private String unit;

	private String dataType;

	private String sensorType;

	private float value;

	private boolean found;

	private String sensorState;

	public String getSensor() {
		return sensor;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;

	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		if (String.valueOf(value) == null)
			this.value = DEFAULT;
		else
			this.value = value;
	}

	public boolean getFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public String getSensorState() {
		return sensorState;
	}

	public void setSensorState(String sensorState) {
		this.sensorState = sensorState;
	}

	@Override
	public String toString() {
		return "{timestamp = " + timestamp + ", sensor = " + sensor + ", unit = " + unit + ", dataType = " + dataType
				+ ", sensorType = " + sensorType + ", value = " + value + ", found = " + found + ", sensorState = "
				+ sensorState + "}";
	}
}
