
-- INSERT A DEFAULT USER => TIENE PARA ACTIVAR LA CUENTA UN DÍA con el siguiente enlace:
--citytrashAdmi123 => contraseñas gmail y la cuenta de cityTrash
--citytrashaRecol123 => contraseñas gmail y la cuenta de citYtRASH
-- http://localhost:8080/citytrash/cuenta/activar?id=1&token=347 
--http://localhost:8080/citytrash/cuenta/activar?id=2&token=169


INSERT INTO TBL_TRABAJADORES(TRABAJADOR_TYPE, ROL, DOC_ID, NOMBRE, APELLIDOS, EMAIL,VIA,NUMERO,PISO,PUERTA,RESTO_DIRECCION,CP,LOCALIDAD, PROVINCIA,TOKEN, FECHA_EXPIRACION_TOKEN) VALUES 
('ADMIN','ROLE_ADMIN','03077018D','Selenium','ADMINISTRADOR','citytrashadmi@gmail.com','CALLE LUIS JORGE CASTAÑOS', 23,4,'Dcha','URBANIZACIÓN LAS CASCAJUELAS',28999,'VALDECILLAS DE JARAMA', 'madrid', '347', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('RECOLEC','ROLE_USER','10441351H','Selenium','RECOLECTOR','citytrasharecol@gmail.com', 'Rúa Méjico', 12,3,'A',null,15004,'A CORUÑA', 'A CORUÑA','169', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('ADMIN','ROLE_ADMIN','65197466X','HMIA','IZAGUIRRE','citytrasharecol9@gmail.com', 'C JUANA DE VEGA', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','12', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('CONDUCT','ROLE_USER','Y1235567K','EVA','LOPEZ','citytrasharecol1@gmail.com', 'Rúa Pla y Cancela', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','123', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('RECOLEC','ROLE_USER','08337662R','JUAN','PEREZ','citytrasharecol2@gmail.com', 'Rúa Ciudad de Lugo', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','1234', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('RECOLEC','ROLE_USER','01025555P','MARIA','RAMIREZ','citytrasharecol3@gmail.com', 'Avenida Alfonso Molina', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','12345', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('CONDUCT','ROLE_USER','50997056F','JOSE','ALVAREZ','citytrasharecol4@gmail.com', 'C Ribeira Sacra', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','123456', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('RECOLEC','ROLE_USER','03548986V','LUCAS','PEREZ','citytrasharecol5@gmail.com', 'C JUANA DE VEGA', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','1234567', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('CONDUCT','ROLE_USER','48079620Y','IVAN','LOPEZ','citytrasharecol6@gmail.com', 'C JUANA DE VEGA', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','12345678', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('RECOLEC','ROLE_USER','58641411J','LUCIA','PEREZ','citytrasharecol7@gmail.com', 'C JUANA DE VEGA', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','123456789', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('CONDUCT','ROLE_USER','60292210X','RAMON','CARBALLEIRA','citytrasharecol8@gmail.com', 'C JUANA DE VEGA', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','12345678910', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('CONDUCT','ROLE_USER','17600290T','LUIS','ALVAREZ','citytrasharecol10@gmail.com', 'C JUANA DE VEGA', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','123456789101112', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
('RECOLEC','ROLE_USER','78259115M','PATRICIA','LOPEZ','citytrasharecol11@gmail.com', 'C JUANA DE VEGA', 10,6,'A',null,15004,'A CORUÑA', 'A CORUÑA','12345678910111213', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR));


INSERT INTO TBL_MODELOS_CAMION(MODELO,VOLUMEN_TOLVA,ANCHO,ALTURA,LONGITUD,DISTANCIA,PMA) VALUES 
('OL10N', 2.8, 2230,2490,4785,3200,16),
('OL14N',2.8, 2230,2490,5585,3900,16),
('OL13W',3.2, 2530,2700,5035,3500,16),
('OL17W',3.2, 2530,2700,4785,4200,19),
('OL13HCT',3.5, 2530,2490,5335,null,16),
('OL16N',2.8, 2230,2490,6235,4600,19),
('OL25HCT',2.8, 2230,2490,4785,3200,16),
('OL27HCT',3.5, 2530,2490,8235,null,32),
('OL25W',3.2, 2530,2700,7735,4600,26),
('OL20W',3.2, 2530,2700,6535,3500,26),
('OL17HCT',3.5, 2530,2490,7135,4000,18);


INSERT INTO TBL_TIPOS_BASURA(COLOR, TIPO) VALUES
('04B431','GLASS'),
('FFFF00','INORG'),
('61380B','ORGAN'),
('2E9AFE','PAPER'),
('FF0000','PLAST');

INSERT INTO TBL_MC_TB(MODELO_CAMION,TIPO_BASURA,CAPACIDAD) VALUES 
(1, 2, 5.3),
(1, 3, 5),
(2, 1, 13.6),
(3, 2, 5),
(3, 3, 4),
(3, 5, 4),
(4, 2, 16.5),
(5, 2, 13.4),
(6, 1, 8),
(6, 4, 8),
(7, 2, 10),
(7, 3, 10),
(7, 5, 10),
(8, 1, 16),
(9, 1, 10),
(9, 4, 10),
(10, 2, 12),
(10, 3, 12),
(10, 5, 4),
(11, 1, 16.9);


INSERT INTO TBL_CAMIONES(VIN,NOMBRE,MATRICULA, FECHA_BAJA,MODELO_CAMION,RECOGEDOR_ASIGNADO1,RECOGEDOR_ASIGNADO2,CODUCTOR_ASIGNADO,CONDUCTOR_SUPLENTE, ACTIVO) VALUES
('WDDGF8BB3CR296129',		'CAMIONA1', 		'C-0003-A',DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 MINUTE),		2,		5,		NULL,		4,		NULL,		0),
('3ABBBFHA9XS067513',		'CAMIONB4', 		'C-0005-A',		NULL,	5,		6,		8,		4,		7,	 	1),
('WBABD33476P119917',		'CAMIONC6', 		'C-0006-A',		NULL,	7,		8, 		10,		4,		7,	 	0),
('JN8HD17Y7PW130412',		'CAMIOND7', 		'C-0007-A',		NULL,	2,		10,		13,		4,		7,	 	1),
('1D7HA18228J186891',		'CAMIONE8', 		'C-0008-A',		NULL,	6,		13, 	2,		4,		7,	 	1),
('1GCEC14Z66Z132146',		'CAMIONF9', 		'C-0009-A',		NULL,	8,		2,  	5,		4,		7,	 	0),
('1FUYTWEB7XHA73350',		'CAMIONF1', 		'C-0001-A',		NULL,	1, 		2, 		5,		4,		7,	 	1),
('ZBBBS00A2G7106759',		'CAMIONH2', 		'C-0002-A',		NULL,	3, 		6, 		NULL,	11, 	NULL,	1),
('1HTMPAFM86H153669',		'CAMIONI9',			'C-0002-D',		NULL,	9, 		2, 		NULL, 	4, 		NULL,	0),
('1GNES16S636227202',		'CAMIONJ4', 		'C-0004-A',		NULL,	4, 		2, 		5,		4,		NULL,	1),
('1MELM66L0SK671941',		'CAMIONK1',			'C-0001-B',		NULL,	1,		6, 		8,		4,		NULL,	0),
('JH4CC2559NC006386',		'CAMIONL1',			'C-0002-B',		NULL,	2, 		10, 	13,		4,		NULL,	1),
('1FMJU1FT4FEF19530',		'CAMIONM1',			'C-0003-B',		NULL,	3, 		13, 	2,		4,		NULL,	0),
('KMHDU4AD5AU015016',		'CAMIONN3',			'C-0004-B',		NULL,	4, 		2, 		5,		4,		NULL,	1),
('1F1NU40S65EC33466',		'CAMIONO4',			'C-0005-B',		NULL,	5, 		6, 		8,		4,		NULL,	0),
('1B7GG69X4LS627578',		'CAMIONP5',			'C-0007-B',		NULL,	6, 		10, 	13,		4,		NULL,	1),
('JKBVNCA10YB525758',		'CAMIONR6',			'C-0008-B',		NULL,	7, 		13, 	2,		4,		NULL,	0),
('SCFFBAAE8AGA16643',		'CAMIONS7',			'C-0009-B',		NULL,	8, 		2, 		5,		4,		NULL,	0),
('1GKFK63818J154378',		'CAMIONT8',			'C-0001-D',		NULL,	6, 		5, 		6,		4,		NULL,	1),
('1FAHP58U83A273226',		'CAMIONU0',			'C-0003-D',		NULL,	4, 		5,		NULL,	7,		NULL,	1),
('1HTSHADR2WH515272',		'CAMION21',			'C-0004-D',		NULL,	10,		6, 		NULL,	9,		NULL,	1),
('1FAHP3K23CL149932',		'CAMION22',			'C-0005-D',		NULL,	2, 		8, 		NULL,	11,		NULL,	1),
('WBAPM7C56AA382241',		'CAMION23',			'C-0006-D',		NULL,	4, 		10,		NULL,	12,		NULL,	1);


INSERT INTO TBL_MODELOS_CONTENEDOR(NOMBRE,CAPACIDAD_NOMINAL,CARGA_NOMINAL,PROFUNDIDAD,ALTURA,ANCHURA,PESO_VACIO,TIPO_BASURA) VALUES 
('MODELO1_V',	3200,	880,	16020,	1755,	1880,	150,	1),
('MODELO1_I',	2200, 	990,	15020,	1755,	1380,	120,	2),
('MODELO1_O',	2500, 	880,	15020,	1755,	1380,	120,	3),
('MODELO1_PAP',	3270, 	1280,	16020,	1755,	1580,	150,	4),	
('MODELO1_PLAS',2600, 	1280,	15020,	1755,	1380,	120,	5),
('MODELO2_PLAS',2700, 	1280,	15020,	1755,	1380,	120,	5),
('MODELO2_PAP',	3200, 	1500,	16020,	1755,	1580,	150,	4),
('MODELO2_I',	3200, 	1500,	15020,	1755,	1580,	150,	3),
('MODELO2_O',	2200, 	900,	15020,	1755,	1380,	120,	2),
('MODELO2_V',	3500, 	1400,	16020,	1755,	1580,	150,	1),
('MODELO3_V', 	2800, 	1100,	15020,	1755,	1380,	120,	1),
('MODELO3_I',	2900, 	1100,	15020,	1755,	1380,	120,	2),
('MODELO3_O',	3000, 	1400,	16020,	1755,	1580,	150,	3),
('MODELO3_PLAS',3700, 	1700,	16020,	1755,	1580,	150,	5),
('MODELO4_PAP',	2700, 	1100,	16020,	1755,	1380,	120,	4);









