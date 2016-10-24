/* Table for validation queries from the connection pool */
DROP TABLE IF EXISTS PingTable;
CREATE TABLE PingTable (foo CHAR(1));

/*SHOW FULL TABLES; DESC Types;*/

/*-----------------------------------------------------------------------------------*/
DROP TABLE IF EXISTS Records;
DROP TABLE IF EXISTS Sensors;
DROP TABLE IF EXISTS Dumpsters;
DROP TABLE IF EXISTS TownHalls;
DROP TABLE IF EXISTS Provinces;
DROP TABLE IF EXISTS Types;

/*------------------------------------- CONTAINER TYPE -----------------------------------------*/
CREATE TABLE Types(  
    name VARCHAR(30) UNIQUE,
    description VARCHAR(100) NOT NULL,    
    CONSTRAINT RecordsPK PRIMARY KEY (name)) ENGINE = InnoDB;    
    
	CREATE INDEX TypeIndexByType ON Types (name);

/*------------------------------TOWN HALLS --------------------------------------------*/
CREATE TABLE Provinces (
  provinceId SMALLINT(6),
  province VARCHAR(30) DEFAULT NULL,
  CONSTRAINT provincesPK PRIMARY KEY (provinceId)) ENGINE=InnoDB;

  
CREATE TABLE TownHalls(
  id BIGINT NOT NULL AUTO_INCREMENT,
  provinceId SMALLINT(6) NOT NULL,
  townHallCode INT(11),
  DC int(11),
  name VARCHAR(100) NOT NULL DEFAULT '',
  latitude FLOAT NOT NULL,
  longitude FLOAT NOT NULL,
  CONSTRAINT TowhallPK PRIMARY KEY (id)) ENGINE=InnoDB;
  
  ALTER TABLE TownHalls ADD CONSTRAINT provinceFK FOREIGN KEY (provinceId) 
  REFERENCES Provinces(provinceId) ON UPDATE CASCADE;

  CREATE INDEX TownHallIndexById ON TownHalls (id);


/*-- --------------------------------- Dumpsters -------------------------------------*/
CREATE TABLE Dumpsters ( 
    dumpstersId BIGINT NOT NULL AUTO_INCREMENT,
    foreign_key_dumpster_id VARCHAR(70) NOT NULL,
    townhall BIGINT NOT NULL, 
    dumspter_type VARCHAR(40) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    tons_capacity FLOAT, -- valor en toneladas
    CONSTRAINT DumpstersPK PRIMARY KEY(dumpstersId, townhall)) ENGINE = InnoDB;
    
    ALTER TABLE Dumpsters ADD CONSTRAINT DumpsterstownhallFK FOREIGN KEY(townhall)
    REFERENCES TownHalls(id) ON UPDATE CASCADE;    
    
    ALTER TABLE Dumpsters ADD CONSTRAINT DumpstersFK FOREIGN KEY(dumspter_type) 
    REFERENCES Types(name) ON UPDATE CASCADE;
        
	CREATE INDEX DumpstersIndexByDumpsterId ON Dumpsters (dumpstersId,townhall);


/*-- --------------------------------- Sensors -------------------------------------*/
CREATE TABLE Sensors (  	
    sensorId BIGINT NOT NULL AUTO_INCREMENT,
    foreign_key_sensor_id VARCHAR(60) NOT NULL,
    containerId BIGINT NOT NULL,
    sensor_type VARCHAR(30) NOT NULL,
    unity VARCHAR(5),  
    CONSTRAINT SensorsPK PRIMARY KEY(sensorId))ENGINE = InnoDB;
    
    ALTER TABLE Sensors ADD CONSTRAINT SensorscontainerIdFK FOREIGN KEY(containerId) 
    REFERENCES Dumpsters(dumpstersId) ON UPDATE CASCADE;
    
    ALTER TABLE Sensors ADD CONSTRAINT SensorstypeFK FOREIGN KEY(sensor_type) 
    REFERENCES Types(name) ON UPDATE CASCADE; 

	CREATE INDEX SensorsIndexBySensorId ON Sensors (sensorId,containerId);
	

/*-------------------------------------- Records -------------------------------------*/
CREATE TABLE Records ( 
    id BIGINT NOT NULL AUTO_INCREMENT UNIQUE,     
    sensorId BIGINT NOT NULL,
    timestamp_sample timestamp NOT NULL,
    timestamp_insert TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    current_value FLOAT NOT NULL, 
    CONSTRAINT RecordsPK PRIMARY KEY (timestamp_sample, sensorId))ENGINE = InnoDB;
         
    ALTER TABLE Records ADD CONSTRAINT RecordsSensorsIdFK FOREIGN KEY(sensorId) 
    REFERENCES Sensors(sensorId) ON UPDATE CASCADE;     

	CREATE INDEX RecordsIndexBySensorId ON Records (sensorId,timestamp_sample);
   
