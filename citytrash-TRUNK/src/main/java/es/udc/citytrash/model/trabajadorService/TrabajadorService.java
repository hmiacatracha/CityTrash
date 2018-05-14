package es.udc.citytrash.model.trabajadorService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorBusqFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorRegistroFormDto;
import es.udc.citytrash.controller.util.dtos.trabajador.TrabajadorUpdateFormDto;
import es.udc.citytrash.model.trabajador.Conductor;
import es.udc.citytrash.model.trabajador.Recolector;
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
	Trabajador modificarTrabajador(TrabajadorUpdateFormDto usuario)
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
	Trabajador buscarTrabajadorByEmail(String email) throws InstanceNotFoundException;

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
	 * @param mostrar
	 *            trabajadores de baja
	 * @return
	 */

	Page<Trabajador> buscarTrabajadores(Pageable pageable, Boolean mostrarTodos);

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

	/**
	 * Verifica que es un trabajador recolector
	 * 
	 * @param id
	 * @return
	 */
	boolean esTrabajadorRecolector(long id);

	/**
	 * Verifica que es un trabajador conductor
	 * 
	 * @param id
	 * @return
	 */
	boolean esTrabajadorConductor(long id);
	
	/**
	 * Obtiene la lista de recolectores ordenados por apellidos
	 * @param mostrarSoloActivos mostrar solo los trabajadores activos o todos
	 * @return
	 */
	List<Recolector> buscarRecolectores(Boolean mostrarSoloActivos);

	/**
	 * Obtiene la lista de conductores ordenados por apellidos
	 * @param mostrarSoloActivos mostrar solo los activos o todos
	 * @return
	 */
	List<Conductor> buscarConductores(Boolean mostrarSoloActivos);

	/**
	 * Buscar la lista de trabajadores ordenados por apellidos
	 * @param mostrarSoloActivos
	 * @return
	 */
	List<Trabajador> buscarTrabajadores(Boolean mostrarSoloActivos);

}
