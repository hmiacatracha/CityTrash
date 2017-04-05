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
package es.udc.tfg.service;

import es.udc.tfg.util.ContainerType;
import es.udc.tfg.util.SensorType;
import es.udc.tfg.util.Unity;
import es.udc.tfg.util.exceptions.DuplicateInstanceException;
import es.udc.tfg.util.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;

import es.udc.tfg.model.dumpster.Dumpster;
import es.udc.tfg.model.province.Province;
import es.udc.tfg.model.record.Record;
import es.udc.tfg.model.record.RecordSensorPK;
import es.udc.tfg.model.sensor.Sensor;
import es.udc.tfg.model.town.Townhall;
import es.udc.tfg.model.type.*;

/**
 * Consumer service for saving external data
 * 
 * @author hmia
 *
 */
public interface DumpsterService {

	/**
	 * Add a new type
	 * 
	 * @param type
	 * @param description
	 * @return Type
	 * @throws DuplicateInstanceException
	 */
	public Type registerType(String type, String description) throws DuplicateInstanceException;

	/**
	 * Add a new townhall
	 * 
	 * @param name
	 * @param province
	 * @param latitude
	 * @param longitude
	 * @return Townhall
	 * @throws DuplicateInstanceException
	 * @throws InstanceNotFoundException
	 */
	public Townhall registerTown(String name, String province, float latitude, float longitude)
			throws DuplicateInstanceException, InstanceNotFoundException;

	/**
	 * find a townhall by name
	 * 
	 * @param name
	 * @return Townhall
	 * @throws InstanceNotFoundException
	 */
	public Townhall findTown(String name) throws InstanceNotFoundException;

	/**
	 * Find a townhall by id
	 * 
	 * @param id
	 * @return Townhall
	 * @throws InstanceNotFoundException
	 */
	public Townhall findTown(long id) throws InstanceNotFoundException;

	/**
	 * Find a province by name
	 * 
	 * @param name
	 * @return Province
	 * @throws InstanceNotFoundException
	 */
	public Province findProvince(String name) throws InstanceNotFoundException;

	/**
	 * Add a province
	 * 
	 * @param id
	 * @param name
	 * @return Province
	 * @throws DuplicateInstanceException
	 */
	public Province registerProvince(int id, String name) throws DuplicateInstanceException;

	/**
	 * Find a province by id
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	public Province findProvince(int id) throws InstanceNotFoundException;

	/**
	 * 
	 * @param townId
	 * @param key
	 * @param ContainerType
	 * @param latitude
	 * @param longitude
	 * @param tons
	 * @return
	 * @throws InstanceNotFoundException
	 * @throws DuplicateInstanceException
	 */
	public Dumpster registerDumpster(long townId, String key, ContainerType typeId, float latitude, float longitude,
			float tons) throws InstanceNotFoundException, DuplicateInstanceException;

	/**
	 * Find a dumpster by external key and town id
	 * 
	 * @param key
	 * @param townId
	 * @return Dumpter
	 * @throws InstanceNotFoundException
	 */
	public Dumpster findDumpster(String key, long townId) throws InstanceNotFoundException;

	/**
	 * Find a sensor by external key and dumpsterId
	 * 
	 * @param key
	 * @param dumspterId
	 * @return
	 * @throws InstanceNotFoundException
	 */
	public Sensor findSensor(String key, long dumspterId) throws InstanceNotFoundException;

	/**
	 * Add a sensor
	 * 
	 * @param key
	 * @param dumpsterId
	 * @param type
	 * @param unity
	 * @return
	 */
	public Sensor registerSensor(String key, long dumpsterId, SensorType type, Unity unity)
			throws InstanceNotFoundException, DuplicateInstanceException;

	/**
	 * Find by record id
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	public Record findRecord(RecordSensorPK id) throws InstanceNotFoundException;

	/**
	 * Add a new sensor record value
	 * 
	 * @param sensorPublicKey
	 * @param dumpsterId
	 * @param time
	 * @param value
	 * @return
	 * @throws InstanceNotFoundException
	 * @throws DuplicateInstanceException
	 */

	public Record registerRecord(String sensorPublicKey, long dumpsterId, Calendar time, float value)
			throws InstanceNotFoundException, DuplicateInstanceException;

	/**
	 * 
	 * @return
	 * Return all dumpsters
	 */
	public List<Dumpster> findDumpsters();
}
