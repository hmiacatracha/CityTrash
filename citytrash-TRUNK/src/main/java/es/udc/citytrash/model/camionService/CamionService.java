package es.udc.citytrash.model.camionService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.camion.CamionDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloDto;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.camion.CamionModeloTipoDeBasuraDto;
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
	 * Buscar a un camión por matricula
	 * 
	 * @param nombre
	 * @return camion
	 * @throws InstanceNotFoundException
	 */
	Camion buscarCamionByMatricula(String nombre) throws InstanceNotFoundException;

	/**
	 * Busca un camión por el vin
	 * 
	 * @param vin
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Camion buscarCamionByVin(String vin) throws InstanceNotFoundException;

	/**
	 * Buscar camion por el nombre del camión
	 * 
	 * @param nombre
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Camion buscarCamionByNombre(String nombre) throws InstanceNotFoundException;

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
	 * Buscar un modelo de camión por el nombre del modelo
	 * 
	 * @param nombre
	 * @return
	 * @throws InstanceNotFoundException
	 */
	CamionModelo buscarModeloCamionByNombre(String nombre) throws InstanceNotFoundException;

	/**
	 * Registrar un camion
	 * 
	 * @param ContenedorRegistroDto
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
	 * 
	 * @param form
	 *            camionModeloDto
	 * @return CamionModeloDto
	 * @throws DuplicateInstanceException
	 *             el nombre del modelo
	 * @throws InstanceNotFoundException
	 */
	CamionModelo registrarModelo(CamionModeloDto form) throws DuplicateInstanceException, InstanceNotFoundException;

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
	 * Guardar o Realizar cambios en los modelos tipos de basura
	 * 
	 * @param modeloId
	 * @param tipos
	 * @return
	 * @throws InstanceNotFoundException
	 */
	List<CamionModeloTipoDeBasura> guardarModeloTipoDeBasura(int modeloId, List<CamionModeloTipoDeBasuraDto> tipos)
			throws InstanceNotFoundException;

	/**
	 * Eliminar modelo tipo de basura
	 * 
	 * @param idModelo
	 * @param tipoId
	 * @throws InstanceNotFoundException
	 */
	void eliminarModeloTipoDeBasura(int idModelo, int tipoId) throws InstanceNotFoundException;

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
	 * Buscar camiones by tipos de basura
	 * 
	 * @param tiposDeBasura
	 * @return
	 */
	List<Camion> buscarCamionesByTipos(List<Integer> tiposDeBasura);

	
	/**
	 * Lista de camiones
	 * 
	 * @param pageable
	 * @param formBusqueda
	 * @return
	 */
	Page<Camion> buscarCamiones(Pageable pageable, CamionFormBusq formBusqueda) throws FormBusquedaException;

	
	/**
	 * Buscar camiones
	 * 
	 * @param mostrarSoloActivos
	 * @param mostrarSoloCamionesDeAlta
	 * @return
	 */
	List<Camion> buscarCamiones(boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta);


	/**
	 * Listar todos los modelos de un camion por pageable y formde busqueda
	 * 
	 * @param pageable
	 *            pageable
	 * @param ContenedorModeloFormBusq
	 *            formulario de busqueda
	 * @return
	 */
	Page<CamionModelo> buscarModelos(Pageable pageable, CamionModeloFormBusq formBusqueda) throws FormBusquedaException;

	/**
	 * Buscar todos los modelos ordenados por nombre del modelo y por tipos de
	 * basura si la lista es distinta de nulo
	 * 
	 * @return
	 */
	List<CamionModelo> buscarTodosLosModelosOrderByModelo(List<Integer> tiposDeBasura);


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
	 * Buscar camiones disponibles para una ruta by tipos de basura
	 * 
	 * @param tiposDeBasura
	 * @return
	 */
	List<Camion> buscarCamionesDisponiblesParaUnaRutaByTipos(List<Integer> tiposDeBasura);
}
