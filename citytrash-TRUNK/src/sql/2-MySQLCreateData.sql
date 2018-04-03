
-- INSERT A DEFAULT USER => TIENE PARA ACTIVAR LA CUENTA UN DÍA con el siguiente enlace:
--citytrashAdmi123 => contraseñas gmail y la cuenta de cityTrash
--citytrashaRecol123 => contraseñas gmail y la cuenta de citYtRASH
-- http://localhost:8080/citytrash/cuenta/activar?id=1&token=347 
--http://localhost:8080/citytrash/cuenta/activar?id=2&token=169

INSERT INTO TBL_TRABAJADORES(TRABAJADOR_TYPE, ROL, DOC_ID, NOMBRE, APELLIDOS, EMAIL, TOKEN, FECHA_EXPIRACION_TOKEN) VALUES 
(3,'ROLE_ADMIN','12345678J','Selenium','ADMINISTRADOR','citytrashadmi@gmail.com', '347', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR)),
(1,'ROLE_USER','Y1234567K','Selenium','RECOLECTOR','citytrasharecol@gmail.com', '169', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 24 HOUR));

