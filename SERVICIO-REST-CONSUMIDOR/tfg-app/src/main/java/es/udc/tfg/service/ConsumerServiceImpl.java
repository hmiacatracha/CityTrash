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

import java.util.Calendar;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import es.udc.tfg.model.dumpster.Dumpster;
import es.udc.tfg.model.province.Province;
import es.udc.tfg.model.record.Record;
import es.udc.tfg.model.record.RecordSensorPK;
import es.udc.tfg.model.sensor.Sensor;
import es.udc.tfg.model.town.Townhall;
import es.udc.tfg.util.ContainerType;
import es.udc.tfg.util.SensorType;
import es.udc.tfg.util.Unity;
import es.udc.tfg.util.exceptions.DuplicateInstanceException;
import es.udc.tfg.util.exceptions.InstanceNotFoundException;

@Service("ConsumerService")
public class ConsumerServiceImpl implements ConsumerService {

	@Inject
	private DumpsterService service;

	//private static final Logger log = LoggerFactory.getLogger(TaskScheduler.class);

	@Override
	public Sensor registerSensor(String sensorKey, String dumpsterKey, long townId, String typeId, String unity) {
		Sensor s = null;
		Dumpster d = null;
		SensorType st = null;
		Unity u = null;
		try {
			d = service.findDumpster(dumpsterKey, townId);
		} catch (InstanceNotFoundException e) {
			return s;
		}
		try {
			st = convertToSensorType(typeId);
			u = convertToUnity(unity);
			if (st != SensorType.SEN_NONE && u != Unity.NONE_UNITY) {
				s = service.findSensor(sensorKey, d.getDumpsterId());
			}
		} catch (InstanceNotFoundException e2) {
			try {
				s = service.registerSensor(sensorKey, d.getDumpsterId(), st, u);
				return s;
			} catch (InstanceNotFoundException | DuplicateInstanceException e1) {
				return s;
			}
		}
		return s;
	}

	@Override
	public Record registerRecord(String sensorKey, String dumpsterKey, long townId, Calendar timestampSample,
			float value) {
		Record r = null;
		Sensor s = null;
		Dumpster d = null;

		try {
			d = service.findDumpster(dumpsterKey, townId);
			s = service.findSensor(sensorKey, d.getDumpsterId());
		} catch (InstanceNotFoundException e) {
			return r;
		}

		try {
			RecordSensorPK pk = new RecordSensorPK();							
			if (timestampSample == null) {
				timestampSample = Calendar.getInstance();
			}

			pk.setSensorId(s.getSensorId());
			pk.settTimestampSample(timestampSample);
			r = service.findRecord(pk);

		} catch (InstanceNotFoundException e2) {
			try {
				r = service.registerRecord(sensorKey, d.getDumpsterId(), timestampSample, value);
			} catch (InstanceNotFoundException | DuplicateInstanceException e1) {
			}
		}
		return r;
	}

	@Override
	public Dumpster registerDumpster(String key, String type, Province p, long townId, float latitude, float longitude,
			float tons) {
		Dumpster d = null;
		ContainerType typeId = converToDumpsterType(type);
		if (typeId != ContainerType.NONE) {
			try {
				d = service.findDumpster(key, townId);
			} catch (InstanceNotFoundException e1) {
				try {
					d = service.registerDumpster(townId, key, typeId, latitude, longitude, tons);
				} catch (InstanceNotFoundException | DuplicateInstanceException e) {
				}
			}
		}
		return d;
	}

	@Override
	public Province registerProvince(int id, String name) {
		Province province = null;
		try {
			province = service.findProvince(8);
		} catch (InstanceNotFoundException e) {
			try {
				province = service.registerProvince(id, name);
			} catch (DuplicateInstanceException e1) {
			}
		}
		return province;
	}

	@Override
	public Townhall registerCity(Province p, String name, float latitude, float longitude) {
		Townhall townHall = null;
		try {
			townHall = service.findTown(name);
		} catch (InstanceNotFoundException e) {
			try {
				townHall = service.registerTown(name, p.getProvince(), latitude, longitude);
			} catch (DuplicateInstanceException | InstanceNotFoundException e1) {

			}
		}
		return townHall;
	}

	@Override
	public void addTypes() {
		for (ContainerType t : ContainerType.values()) {
			try {
				service.registerType(t.name(), t.getDescripcion());
			} catch (DuplicateInstanceException e) {
			}
		}
	}

	@Override
	public SensorType convertToSensorType(String type) {
		switch (type.toLowerCase().trim()) {
		case "nivell bateria":
			return SensorType.SEN_BAT;
		case "temperature":
			return SensorType.SEN_TEM;
		case "container occupation level":
			return SensorType.SEN_VOL;
		default:
			return SensorType.SEN_NONE;
		}
	}

	@Override
	public Unity convertToUnity(String type) {
		switch (type.toLowerCase().trim()) {
		case "%":
			return Unity.PERCENTAGE;
		case "ºC":
			return Unity.CELSIUS;
		default:
			return Unity.NONE_UNITY;
		}
	}

	@Override
	public ContainerType converToDumpsterType(String type) {
		switch ((type.toLowerCase().trim())) {
		case "container_refuse":
			return ContainerType.CONT_INORG;
		case "container_organic":
			return ContainerType.CONT_ORGAN;
		case "container_paper":
			return ContainerType.CONT_PAPER;
		case "container_plastic":
			return ContainerType.CONT_PLAST;
		case "container_glass":
			return ContainerType.CONT_GLASS;
		default:
			return ContainerType.NONE;
		}
	}

	@Override
	public Province findProvince(String name) throws InstanceNotFoundException {
		Province province = null;
		province = service.findProvince(name);
		return province;
	}

	@Override
	public Townhall finTownHall(String name) throws InstanceNotFoundException {
		Townhall townHall = null;
		townHall = service.findTown(name);
		return townHall;
	}

}
