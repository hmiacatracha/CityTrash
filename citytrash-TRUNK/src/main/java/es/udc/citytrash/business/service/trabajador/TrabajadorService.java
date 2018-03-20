package es.udc.citytrash.business.service.trabajador;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.util.excepciones.ActiveCountException;
import es.udc.citytrash.business.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.business.util.excepciones.ExpiredTokenException;
import es.udc.citytrash.business.util.excepciones.InactiveCountException;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.business.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.business.util.excepciones.TokenInvalidException;
import es.udc.citytrash.controller.util.dtos.TrabajadorFormDto;

public interface TrabajadorService {

	/****
	 * Registrar a un trabajador
	 * 
	 * @param usuario
	 * @param appUrl
	 * @return
	 * @throws DuplicateInstanceException
	 *             en caso que el email o documento del trabajador que registra
	 *             ya exista en nuestra base de datos
	 */

	Trabajador registrar(TrabajadorFormDto usuario, String appUrl) throws DuplicateInstanceException;

	/***
	 * Buscar trabajador por email
	 * 
	 * @param email
	 * @return
	 * @throws InstanceNotFoundException
	 *             en caso que no exista el email
	 */
	Trabajador buscarTrabajadorEmail(String email) throws InstanceNotFoundException;

	/***
	 * Buscar trabajador por email
	 * 
	 * @param documento
	 * @return
	 * @throws InstanceNotFoundException
	 *             en caso que no exista el documento
	 */
	Trabajador buscarTrabajadorDocumento(String documento) throws InstanceNotFoundException;

	/***
	 * Mostrar la lista de trabajadores
	 * 
	 * @param pageable
	 * @return
	 */

	Page<Trabajador> buscarTrabajadores(Pageable pageable);
}
