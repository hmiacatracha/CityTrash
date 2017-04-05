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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.tfg.TaskScheduler;
import es.udc.tfg.model.dumpster.Dumpster;
import es.udc.tfg.model.dumpster.DumpsterDao;
import es.udc.tfg.model.province.Province;
import es.udc.tfg.model.province.ProvinceDao;
import es.udc.tfg.model.record.Record;
import es.udc.tfg.model.record.RecordDao;
import es.udc.tfg.model.record.RecordSensorPK;
import es.udc.tfg.model.sensor.Sensor;
import es.udc.tfg.model.sensor.SensorDao;
import es.udc.tfg.model.town.Townhall;
import es.udc.tfg.model.town.TownHallDao;
import es.udc.tfg.model.type.Type;
import es.udc.tfg.model.type.TypeDao;
import es.udc.tfg.util.ContainerType;
import es.udc.tfg.util.SensorType;
import es.udc.tfg.util.Unity;
import es.udc.tfg.util.exceptions.DuplicateInstanceException;
import es.udc.tfg.util.exceptions.InstanceNotFoundException;

@Service("DumpsterService")
public class DumpsterServiceImpl implements DumpsterService {

	@Autowired
	private TypeDao typeDao;

	@Autowired
	private ProvinceDao provinceDao;

	@Autowired
	private TownHallDao townhallDao;

	@Autowired
	private DumpsterDao dumpsterDao;

	@Autowired
	private SensorDao sensorDao;

	@Autowired
	private RecordDao recordDao;

	private static final Logger log = LoggerFactory.getLogger(TaskScheduler.class);

	@Override
	public Type registerType(String type, String description) throws DuplicateInstanceException {
		Type t;

		t = typeDao.findOne(type);
		if (t != null)
			throw new DuplicateInstanceException(t.getName(), t.getClass().getName());
		t = new Type(type, description);
		typeDao.save(t);
		return t;
	}

	public Townhall registerTown(String name, String province, float latitude, float longitude)
			throws DuplicateInstanceException, InstanceNotFoundException {
		Townhall town = null;
		Province pv = null;
		pv = provinceDao.findByName(province);
		town = townhallDao.findByName(name);
		if (pv == null)
			throw new InstanceNotFoundException(province, Townhall.class.getName());
		if (town != null)
			throw new DuplicateInstanceException(town.getId(), Townhall.class.getName());
		town = new Townhall(pv, name, latitude, longitude);
		townhallDao.save(town);
		return town;
	}

	@Override
	public Province findProvince(String name) throws InstanceNotFoundException {
		Province p = null;
		p = provinceDao.findByName(name);
		if (p == null)
			throw new InstanceNotFoundException(name, Province.class.getName());
		return p;
	}

	@Override
	public Province findProvince(int id) throws InstanceNotFoundException {
		Province p = null;
		p = provinceDao.findOne(id);
		if (p == null)
			throw new InstanceNotFoundException(id, Province.class.getName());
		return p;
	}

	@Override
	public Province registerProvince(int id, String name) throws DuplicateInstanceException {
		Province pv = null;
		pv = provinceDao.findOne(id);
		if (pv != null)
			throw new DuplicateInstanceException(pv.getProvinceId(), Townhall.class.getName());
		pv = new Province(id, name);
		provinceDao.save(pv);
		return pv;
	}

	@Override
	public Townhall findTown(String name) throws InstanceNotFoundException {
		Townhall town = null;
		town = townhallDao.findByName(name);
		if (town == null)
			throw new InstanceNotFoundException(name, Townhall.class.getName());
		return town;
	}

	@Override
	public Townhall findTown(long id) throws InstanceNotFoundException {
		Townhall town = null;
		town = townhallDao.findOne(id);
		if (town == null)
			throw new InstanceNotFoundException(id, Townhall.class.getName());
		return town;
	}

	@Override
	public Dumpster findDumpster(String key, long townId) throws InstanceNotFoundException {
		Dumpster dumpster = null;
		dumpster = dumpsterDao.findByForeignKeyDumpsterIdAndTownhall(key, townId);
		if (dumpster == null)
			throw new InstanceNotFoundException(key, Dumpster.class.getName());
		return dumpster;
	}

	@Override
	public Sensor findSensor(String key, long dumspterId) throws InstanceNotFoundException {
		Sensor sensor = null;
		sensor = sensorDao.findByKeyAndDumpster(key, dumspterId);
		if (sensor == null)
			throw new InstanceNotFoundException(key, Dumpster.class.getName());
		return sensor;
	}

	@Override
	public Dumpster registerDumpster(long townId, String key, ContainerType typeId, float latitude, float longitude,
			float tons) throws InstanceNotFoundException, DuplicateInstanceException {
		Dumpster dumpster = null;
		Townhall town = null;
		Type type = null;

		town = townhallDao.findOne(townId);
		type = typeDao.findOne(typeId.name());

		if (town == null)
			throw new InstanceNotFoundException(townId, Townhall.class.getName());
		if (type == null)
			throw new InstanceNotFoundException(typeId, Type.class.getName());
		dumpster = dumpsterDao.findByForeignKeyDumpsterIdAndTownhall(key, town.getId());
		if (dumpster != null)
			throw new DuplicateInstanceException(key, Dumpster.class.getName());
		dumpster = new Dumpster(key, town, type, latitude, longitude, tons);
		dumpsterDao.save(dumpster);
		return dumpster;
	}

	@Override
	public Sensor registerSensor(String key, long dumpsterId, SensorType typeId, Unity unity)
			throws InstanceNotFoundException, DuplicateInstanceException {
		Sensor s = null;
		Type type = null;
		Dumpster dumpster = null;
		log.info("TYPO");
		type = typeDao.findOne(typeId.name());
		log.info("DUMPSTER");
		dumpster = dumpsterDao.findOne((Long) dumpsterId);
		if (type == null) {
			log.info("INSTANCE NOT FOUND EXCEPTION: TYPE");
			throw new InstanceNotFoundException(typeId, Type.class.getName());
		}
		if (dumpster == null) {
			log.info("INSTANCE NOT FOUND EXCEPTION DUMPSTERID");
			throw new InstanceNotFoundException(dumpsterId, Dumpster.class.getName());
		}

		s = sensorDao.findByKeyAndDumpster(key, dumpsterId);
		if (s != null) {
			log.info("DUPLICATE SENSOR");
			throw new DuplicateInstanceException(key, Dumpster.class.getName());
		}
		s = new Sensor(key, dumpster, type, unity.name());
		s = sensorDao.save(s);
		return s;
	}

	@Override
	public Record findRecord(RecordSensorPK id) throws InstanceNotFoundException {
		Record r = null;
		r = recordDao.findOne(id);
		if (r == null)
			throw new InstanceNotFoundException(id, Record.class.getName());
		return r;
	}

	@Override
	public Record registerRecord(String sensorPublicKey, long dumpsterId, Calendar time, float value)
			throws InstanceNotFoundException, DuplicateInstanceException {

		Record r = null;
		Sensor s = null;

		/* No añadimos a los contenedores con valor menor igual a cero */
		if (value <= 0)
			return r;

		s = sensorDao.findByKeyAndDumpster(sensorPublicKey, dumpsterId);
		if (s == null)
			throw new InstanceNotFoundException(sensorPublicKey, Sensor.class.getName());

		RecordSensorPK pk = new RecordSensorPK();
		pk.setSensorId(s.getSensorId());
		pk.settTimestampSample(time);

		r = recordDao.findOne(pk);
		if (r != null)
			throw new DuplicateInstanceException(r, Dumpster.class.getName());
		r = new Record(pk, value);
		recordDao.save(r);
		return r;
	}

	@Override
	public List<Dumpster> findDumpsters() {
		return dumpsterDao.findAll();
	}

}
