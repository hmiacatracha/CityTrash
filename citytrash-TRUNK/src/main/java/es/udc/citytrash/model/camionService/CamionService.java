package es.udc.citytrash.model.camionService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.camion.CamionDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionRegistroDto;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.FormBusquedaException;
import es.udc.citytrash.model.util.excepciones.InactiveResourceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;

/**
 * Servicio de Camiones -CRUD CAMIONES -CRU MODELO DE CAMIONES
 * 
 * @author hmia
 *
 */
public interface CamionService {

	/**
	 * comprobar si el modelo de un camion existe
	 * 
	 * @param modelo
	 *            id del modelo
	 * @return true si existe o false en caso contrario.
	 */
	boolean esModeloExistenteById(int modelo);

	/**
	 * Buscar modelo de camion por nombre
	 * 
	 * @param nombre
	 * @return
	 */
	boolean esModeloCamionByNombreExistente(String nombre);

	/**
	 * Verifica si la matricula existe ya en la base de datos
	 * 
	 * @param matricula
	 * @return true si ya existe o false en caso contrario.
	 */

	boolean esCamionByMatriculaExistente(String matricula);

	/**
	 * verifica si el nombre del camion ya existe
	 * 
	 * @param nombre
	 * @return
	 */
	boolean esCamionByNombreExistente(String nombre);

	/**
	 * Verifica si el vin del camion ya existe
	 * 
	 * @param vin
	 * @return
	 */
	boolean esCamionByVinExistente(String vin);

	/**
	 * Habilita o desactiva el estado de un camion
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 *             camion no existente
	 */
	boolean cambiarEstadoCamion(long id) throws InstanceNotFoundException;

	/**
	 * Lista de camiones
	 * 
	 * @param pageable
	 * @param formBusqueda
	 * @return
	 */
	Page<Camion> buscarCamiones(Pageable pageable, CamionFormBusq formBusqueda) throws FormBusquedaException;

	/**
	 * Listar todos los modelos de un camion por pageable y formde busqueda
	 * 
	 * @param pageable
	 *            pageable
	 * @param CamionModeloFormBusq
	 *            formulario de busqueda
	 * @return
	 */
	Page<CamionModelo> buscarModelos(Pageable pageable, CamionModeloFormBusq formBusqueda) throws FormBusquedaException;

	/**
	 * Buscar todos los modelos ordenados por nombre del modelo;
	 * 
	 * @return
	 */
	List<CamionModelo> buscarTodosLosModelosOrderByModelo();

	/**
	 * Registrar un camion
	 * 
	 * @param CamionRegistroDto
	 *            formulario
	 * @return
	 * @throws InstanceNotFoundException
	 *             devuelve esta excepcion en los siguientes casos: si el modelo
	 *             no existe. Si el conductor asignado no existe como conductor.
	 *             Si el conductor suplente asignado no existe como conductor.
	 *             Si el recolector asignado no existe como recolector.
	 * @throws DuplicateInstanceException
	 *             devuelve esta excepcion cuando una matricula esta duplicada,
	 *             es decir, la matricula ya existe en la base de datos.
	 * @throws InactiveResourceException
	 * @throws InvalidFieldException
	 */
	Camion registrarCamion(CamionRegistroDto form) throws InstanceNotFoundException, DuplicateInstanceException,
			InactiveResourceException, InvalidFieldException;

	/**
	 * Modifica los cambios de un camion
	 * 
	 * @param camionForm
	 *            formulario
	 * @return
	 * @throws InstanceNotFoundException
	 *             devuelve esta excepcion en los siguientes casos: Si el camion
	 *             no existe en la base de datos. Si el modelo no existe. Si el
	 *             conductor asignado no existe como conductor. Si el conductor
	 *             suplente asignado no existe como conductor. Si el recolector
	 *             asignado no existe como recolector.
	 * @throws DuplicateInstanceException
	 *             devuelve esta excepcion cuando una matricula esta duplicada,
	 *             es decir, la matricula ya existe en la base de datos.
	 * @throws InactiveResourceException
	 * @throws InvalidFieldException
	 */
	Camion modificarCamion(CamionDto camionForm) throws InstanceNotFoundException, DuplicateInstanceException,
			InactiveResourceException, InvalidFieldException;

	/**
	 * Ver los detalles de un camion
	 * 
	 * @param id
	 *            id del camion
	 * @return Camion Dto
	 * @throws InstanceNotFoundException
	 *             camion no encontrado
	 */
	Camion buscarCamionById(long id) throws InstanceNotFoundException;

	/**
	 * 
	 * @param form
	 *            camionModeloDto
	 * @return CamionModeloDto
	 * @throws DuplicateInstanceException
	 *             el nombre del modelo
	 */
	CamionModelo registrarModelo(CamionModeloDto form) throws DuplicateInstanceException;

	/**
	 * 
	 * @param form
	 * @return CamionModelo
	 * @throws InstanceNotFoundException
	 *             No se ha encontrado ningun modelo con este id
	 * @throws DuplicateInstanceException
	 *             El nombre ya existe en la base de datos de modelos
	 */
	CamionModelo modificarModelo(CamionModeloDto form) throws InstanceNotFoundException, DuplicateInstanceException;

	/**
	 * 
	 * @param id
	 *            id del modelo del camion
	 * @return Entidad camion
	 * @throws InstanceNotFoundException
	 *             no se encontro ningun modelo con estos
	 */
	CamionModelo buscarModeloById(int id) throws InstanceNotFoundException;

	/**
	 * Añadir o modificar tipo, Modelo si la capacidad <= 0 entonces se elimina,
	 * sino se modifica o se añade la capacidad.
	 * 
	 * @param tipo
	 * @param Modelo
	 */
	// void agregarOrModificarTipoModelo(int tipo, int Modelo, BigDecimal
	// capacidad) throws InstanceNotFoundException;

	/**
	 * Listar los tipos modelo basura de un modelo
	 * 
	 * @param idModelo
	 *            identificador del modelo
	 * @return
	 * @throws InstanceNotFoundException
	 */
	List<CamionModeloTipoDeBasura> buscarTipoDeBasuraByModelo(int idModelo) throws InstanceNotFoundException;

	/**
	 * Listar los tipos de basura
	 * 
	 * @return
	 */
	List<TipoDeBasura> buscarTiposDeBasura();

}
