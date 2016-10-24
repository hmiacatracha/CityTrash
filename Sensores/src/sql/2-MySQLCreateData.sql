-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-------------------------------------------------------------------------------
--Tipos de contenedores
INSERT INTO Types(name, description) VALUE ('CONT_PAPER','Contenedor Papel');
INSERT INTO Types(name, description) VALUE ('CONT_GLASS','Contenedor Vidrio');
INSERT INTO Types(name, description) VALUE ('CONT_ORGAN','Contenedor Orgánica');
INSERT INTO Types(name, description) VALUE ('CONT_INORG','Contenedor Inorgánica');
INSERT INTO Types(name, description) VALUE ('CONT_PLAST','Contenedor Plástico');
--Tipos de Sensores
INSERT INTO Types(name, description) VALUE ('SEN_BAT','Sensor Nivel de Bateria');
INSERT INTO Types(name, description) VALUE ('SEN_TEM','Sensor Temperatura');
INSERT INTO Types(name, description) VALUE ('SEN_VOL','Sensor Nivel de Ocupación');

--Añadiendo provincias de España
INSERT INTO Provinces (provinceId, province) VALUES (2,'Albacete'),	(3,'Alicante/Alacant'),	(4,'Almería'),	(1,'Araba/Álava'),	(33,'Asturias'),
	(5,'Ávila'),(6,'Badajoz'),	(7,'Balears, Illes'),(8,'Barcelona'),(48,'Bizkaia'),(9,'Burgos'),(10,'Cáceres'),
	(11,'Cádiz'),(39,'Cantabria'), (12,'Castellón/Castelló'), (51,'Ceuta'),	(13,'Ciudad Real'),	(14,'Córdoba'),
	(15,'Coruña, A'),(16,'Cuenca'), (20,'Gipuzkoa'),(17,'Girona'),(18,'Granada'),(19,'Guadalajara'),
	(21,'Huelva'), (22,'Huesca'),(23,'Jaén'),(24,'León'),(27,'Lugo'),(25,'Lleida'),(28,'Madrid'),(29,'Málaga'),
	(52,'Melilla'),	(30,'Murcia'),(31,'Navarra'),(32,'Ourense'),(34,'Palencia'),(35,'Palmas, Las'), (36,'Pontevedra'),
	(26,'Rioja, La'), (37,'Salamanca'),(38,'Santa Cruz de Tenerife'),(40,'Segovia'),(41,'Sevilla'), (42,'Soria'),
	(43,'Tarragona'), (44,'Teruel'),(45,'Toledo'),(46,'Valencia/València'),	(47,'Valladolid'),(49,'Zamora'),
	(50,'Zaragoza');
	
--Añadiendo municipios de España
--https://github.com/alombarte/utilities/blob/master/sql/spain_municipios_INE.sql

INSERT INTO TownHalls (provinceId, townHallCode, DC, name, latitude, longitude)
VALUES (8,19,3,'Barcelona',41.3850639,2.1734034999999494),(15,30,8,'Coruña, A',43.3623436, -8.411540100000025);




	

	
