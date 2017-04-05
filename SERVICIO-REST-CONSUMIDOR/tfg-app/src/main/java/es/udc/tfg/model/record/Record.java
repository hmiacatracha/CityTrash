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
package es.udc.tfg.model.record;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;
//https://jaehoo.wordpress.com/2012/03/25/hibernate-composite-keys-with-annotations/

/**
 * 
 * @author hmia Last observation of sensor from barcelona api
 */
@Entity
@Immutable
@Table(name = "records")
public class Record {

	@EmbeddedId
	private RecordSensorPK recordId;

	@Column(name = "timestamp_insert", nullable = false)
	private Calendar timestampInsert;
	@Column(name = "current_value", nullable = false)
	private float currentValue;

	public Record() {

	}

	/**
	 * Constructor record
	 * 
	 * @param sensor
	 * @param timestamp
	 * @param value
	 */
	public Record(RecordSensorPK recordId, float value) {
		this.recordId = recordId;
		this.currentValue = value;
	}

	public RecordSensorPK getRecordId() {
		return recordId;
	}

	public void setRecordId(RecordSensorPK recordId) {
		this.recordId = recordId;
	}

	public float getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(float value) {
		this.currentValue = value;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getTimestampInsert() {
		return timestampInsert;
	}

	public void setTimestampInsert(Calendar timestampInsert) {
		this.timestampInsert = timestampInsert;
	}

	@Override
	public String toString() {
		return "Record [recordId=" + recordId + ", value=" + currentValue + "]";
	}

}
