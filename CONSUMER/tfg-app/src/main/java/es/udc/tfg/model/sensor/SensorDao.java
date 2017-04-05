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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * Sensor dao with JpaRepository
 * @author hmia
 *
 */
@Repository("SensorDao")
public interface SensorDao extends JpaRepository<Sensor, Long> {

	/**
	 * FInd dumpster by key and dumpster id
	 * @param public_key
	 * @param dumspterId
	 * @return
	 */
	@Query(value = "SELECT * FROM sensors WHERE lower(foreign_key_sensor_id) = lower(?1) AND  dumpster_id=?2", nativeQuery = true)
	public Sensor findByKeyAndDumpster(String public_key, long dumspterId);
}
