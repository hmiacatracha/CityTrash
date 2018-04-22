package es.udc.citytrash.model.trabajadorService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.TrabajadorBusqFormDto;
import es.udc.citytrash.controller.util.dtos.TrabajadorRegistroFormDto;
import es.udc.citytrash.controller.util.dtos.TrabajadorUpdateFormDto;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.util.excepciones.ActiveCountException;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.ExpiredTokenException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.PasswordInvalidException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;

public interface TrabajadorService {

	/****
	 * Registrar a un trabajador
	 * 
	 * @param usuario
	 * @return
	 * @throws DuplicateInstanceException
	 *             en caso que el email o documento del trabajador que registra
	 *             ya exista en nuestra base de datos
	 */

	Trabajador registrar(TrabajadorRegistroFormDto usuario) throws DuplicateInstanceException;

	/***
	 * actulizar trabajadorId
	 * 
	 * @param usuario
	 * @return
	 * @throws DuplicateInstanceException
	 * @throws InstanceNotFoundException
	 */
	Trabajador actualizarDatosTrabajador(TrabajadorUpdateFormDto usuario)
			throws InstanceNotFoundException, DuplicateInstanceException;

	/**
	 * Buscar trabajador por Id
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Trabajador buscarTrabajador(long id) throws InstanceNotFoundException;

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
	 *            pageable
	 * @return
	 */

	Page<Trabajador> buscarTrabajadores(Pageable pageable);

	/**
	 * Busqueda de trabajador con un formulario de búsqueda
	 * 
	 * @param pageable
	 * @param formBusqueda
	 * @throws FormBusquedaException
	 *             => error en los campos de busqueda
	 * @return
	 */
	Page<Trabajador> buscarTrabajadores(Pageable pageable, TrabajadorBusqFormDto formBusqueda)
			throws FormBusquedaException;

	/**
	 * Da de alta al trabajador de nuevo (Desactiva al trabajador y la activa)
	 * 
	 * @param trabajadorId
	 * @throws InstanceNotFoundException
	 */
	void activarTrabajador(long trabajadorId) throws InstanceNotFoundException;

	/**
	 * Da de baja al trabajador (Desactiva la cuenta y al trabajador)
	 * 
	 * @param trabajadorId
	 * @throws InstanceNotFoundException
	 */
	void desactivarTrabajador(long trabajadorId) throws InstanceNotFoundException;

	/**
	 * Comprueba si el trabajador esta activo, es decir no está de baja
	 * 
	 * @param trabajadorId
	 * @return
	 * @throws InstanceNotFoundException
	 */
	boolean esUnTrabajadorActivo(long trabajadorId) throws InstanceNotFoundException;

}
