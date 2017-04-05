---
-- ========================LICENSE_START=================================
-- TFG BYTEPIT-APP
-- %%
-- Copyright (C) 2016 Heidy Mabel Izaguirre Alvarez
-- %%
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as
-- published by the Free Software Foundation, either version 3 of the
-- License, or (at your option) any later version.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
-- 
-- You should have received a copy of the GNU General Public
-- License along with this program.  If not, see
-- <http://www.gnu.org/licenses/gpl-3.0.html>.
-- =========================LICENSE_END==================================
---
/* Table for validation queries from the connection pool */
DROP TABLE IF EXISTS pingTable;
CREATE TABLE pingTable (foo CHAR(1));

/*SHOW FULL TABLES; DESC Types;
 * ALTER TABLE provinces CHANGE provinceId province_id smallint(6);
 * */

/*-----------------------------------------------------------------------------------*/
DROP TABLE IF EXISTS records;
DROP TABLE IF EXISTS sensors;
DROP TABLE IF EXISTS dumpsters;
DROP TABLE IF EXISTS townhalls;
DROP TABLE IF EXISTS provinces;
DROP TABLE IF EXISTS types;

/*------------------------------------- CONTAINER TYPE -----------------------------------------*/
CREATE TABLE types(
    name VARCHAR(30) UNIQUE,
    description VARCHAR(100) NOT NULL,    
    CONSTRAINT RecordsPK PRIMARY KEY (name)) ENGINE = InnoDB;    
    
	CREATE INDEX typeIndexByType ON types (name);

/*------------------------------TOWN HALLS --------------------------------------------*/
CREATE TABLE provinces (
  province_id INT,
  province VARCHAR(30) DEFAULT NULL,
  CONSTRAINT provincesPK PRIMARY KEY (province_id)) ENGINE=InnoDB;

  
CREATE TABLE townhalls(
  id BIGINT NOT NULL AUTO_INCREMENT,
  province_id INT NOT NULL,
  town_hall_code INT(11),
  dc int(11),
  name VARCHAR(100) NOT NULL DEFAULT '',
  latitude FLOAT NOT NULL,
  longitude FLOAT NOT NULL,
  CONSTRAINT towhallPK PRIMARY KEY (id)) ENGINE=InnoDB;
  
  ALTER TABLE townhalls ADD CONSTRAINT provinceFK FOREIGN KEY (province_id) 
  REFERENCES provinces(province_id) ON UPDATE CASCADE;

  CREATE INDEX townHallIndexById ON townhalls (id);


/*-- --------------------------------- Dumpsters -------------------------------------*/
CREATE TABLE dumpsters ( 
    dumpster_id BIGINT NOT NULL AUTO_INCREMENT,
    foreign_key_dumpster_id VARCHAR(70) NOT NULL,
    townhall BIGINT NOT NULL, 
    dumspter_type VARCHAR(40) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    tons_capacity FLOAT, -- valor en toneladas
    CONSTRAINT DumpstersPK PRIMARY KEY(dumpster_id, townhall)) ENGINE = InnoDB;
    
    ALTER TABLE dumpsters ADD CONSTRAINT dumpsterstownhallFK FOREIGN KEY(townhall)
    REFERENCES townhalls(id) ON UPDATE CASCADE;    
    
    ALTER TABLE dumpsters ADD CONSTRAINT dumpstersFK FOREIGN KEY(dumspter_type) 
    REFERENCES types(name) ON UPDATE CASCADE;
        
	CREATE INDEX dumpstersIndexByDumpsterId ON dumpsters (dumpster_id,townhall);


/*-- --------------------------------- Sensors -------------------------------------*/
CREATE TABLE sensors (  	
    sensor_id BIGINT NOT NULL AUTO_INCREMENT,
    foreign_key_sensor_id VARCHAR(60) NOT NULL,
    dumpster_id BIGINT NOT NULL,
    sensor_type VARCHAR(30) NOT NULL,
    unity VARCHAR(15),  
    CONSTRAINT SensorsPK PRIMARY KEY(sensor_id))ENGINE = InnoDB;
    
    ALTER TABLE sensors ADD CONSTRAINT sensorscontainerIdFK FOREIGN KEY(dumpster_id) 
    REFERENCES dumpsters(dumpster_id) ON UPDATE CASCADE;
    
    ALTER TABLE sensors ADD CONSTRAINT sensorstypeFK FOREIGN KEY(sensor_type) 
    REFERENCES types(name) ON UPDATE CASCADE; 

	CREATE INDEX sensorsIndexBySensorId ON sensors (sensor_id,dumpster_id);
	

/*-------------------------------------- Records -------------------------------------*/
CREATE TABLE records (      
    sensor_id BIGINT NOT NULL,
    timestamp_sample timestamp NOT NULL,
    timestamp_insert TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    current_value FLOAT NOT NULL, 
    CONSTRAINT RecordsPK PRIMARY KEY (timestamp_sample, sensor_id))ENGINE = InnoDB;
         
    ALTER TABLE records ADD CONSTRAINT recordsSensorsIdFK FOREIGN KEY(sensor_id) 
    REFERENCES sensors(sensor_id) ON UPDATE CASCADE;     

	CREATE INDEX recordsIndexBySensorId ON records (sensor_id,timestamp_sample);
   
