package es.udc.citytrash.model.contenedorService;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloRegistroDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorRegistroDto;
import es.udc.citytrash.controller.util.dtos.estadisticas.ComparativaPorTipoReciclado;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.sensor.Sensor;
import es.udc.citytrash.model.sensorValor.Valor;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;
import es.udc.citytrash.util.enums.TipoComparativa;

/**
 * Servicio de Contenedores -CRUD CONTENEDORES -CRU MODELO DE CONTENEDORES
 * 
 * @author hmia
 *
 */
public interface ContenedorService {

	/**
	 * Busca un contenedor by id
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Contenedor buscarContenedorById(long id) throws InstanceNotFoundException;

	/**
	 * Buscar contenedor by nombre
	 * 
	 * @param nombre
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Contenedor buscarContenedorByNombre(String nombre) throws InstanceNotFoundException;

	/**
	 * Busca modelo by Id
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	ContenedorModelo buscarModeloById(int id) throws InstanceNotFoundException;

	/**
	 * devuelve tru si ya existe un modelo con ese nombre
	 * 
	 * @param nombre
	 * @return
	 * @throws InstanceNotFoundException
	 */
	ContenedorModelo buscarModeloContenedorByNombre(String nombre) throws InstanceNotFoundException;

	/**
	 * registrar contenedor
	 * 
	 * @param form
	 * @return
	 * @throws InstanceNotFoundException
	 * @throws DuplicateInstanceException
	 */
	Contenedor registrarContenedor(ContenedorRegistroDto form)
			throws InstanceNotFoundException, DuplicateInstanceException;

	/**
	 * modificar contenedor
	 * 
	 * @param form
	 * @return
	 * @throws InstanceNotFoundException
	 * @throws DuplicateInstanceException
	 */
	Contenedor modificarContenedor(ContenedorEditarDto form)
			throws InstanceNotFoundException, DuplicateInstanceException;

	/**
	 * Registrar modelo
	 * 
	 * @param form
	 * @return
	 * @throws DuplicateInstanceException
	 * @throws InvalidFieldException
	 */
	ContenedorModelo registrarModelo(ContenedorModeloRegistroDto form)
			throws DuplicateInstanceException, InvalidFieldException;

	/**
	 * Modificar modelo
	 * 
	 * @param form
	 * @return
	 * @throws InstanceNotFoundException
	 * @throws DuplicateInstanceException
	 * @throws InvalidFieldException
	 */
	ContenedorModelo modificarModelo(ContenedorModeloEditarDto form)
			throws InstanceNotFoundException, DuplicateInstanceException, InvalidFieldException;

	/**
	 * Eliminar un sensorId de un contenedor
	 * 
	 * @param sensorId
	 * @throws InstanceNotFoundException
	 */
	void eliminarSensorId(Long sensorId) throws InstanceNotFoundException;

	/**
	 * Buscar un sensor by id
	 * 
	 * @param sensorId
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Sensor buscarSensorById(Long sensorId) throws InstanceNotFoundException;

	/**
	 * Buscar valores by sensor
	 * 
	 * @param sensorId
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	List<Valor> buscarValoresBySensor(Long sensorId, Date fechaInicio, Date fechaFin);

	/**
	 * Cambia el estado de un contenedor (Lo habilita o Deshabilita)
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	boolean cambiarEstadoContenedor(long id) throws InstanceNotFoundException;

	/**
	 * Buscar contenedores
	 * 
	 * @param pageable
	 * @param formBusqueda
	 * @return Pageable
	 */
	Page<Contenedor> buscarContenedores(Pageable pageable, ContenedorFormBusq formBusqueda);

	/**
	 * Buscar modelos
	 * 
	 * @param pageable
	 * @param formBusqueda
	 * @return Pageable
	 */
	Page<ContenedorModelo> buscarModelos(Pageable pageable, ContenedorModeloFormBusq formBusqueda);

	/**
	 * Busca modelos by tipo de basura
	 * 
	 * @param tipos
	 *            tipos de basura
	 * @return
	 */
	List<ContenedorModelo> buscarTodosLosModelosOrderByModelo(List<Integer> tipos);

	/**
	 * Busca los tipos de basura
	 * 
	 * @return
	 */
	List<TipoDeBasura> buscarTiposDeBasura();

	/**
	 * Buscar tipo de basura por contenedor
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	TipoDeBasura buscarTiposDeBasuraByContenedor(long id) throws InstanceNotFoundException;

	/**
	 * Buscar contenedores por lista de ids
	 * 
	 * @param idsContenedores
	 * @return
	 */
	List<Contenedor> buscarContenedores(List<Long> idsContenedores);


	/**
	 * Buscar contenedores
	 * 
	 * @param formBusqueda
	 *            formulario de busqueda
	 * @return List
	 */
	List<Contenedor> buscarContenedores(ContenedorFormBusq form);

	/**
	 * Lista de contenedores disponibles para una ruta by ruta id
	 * 
	 * @param rutaId
	 * @param tiposDeBasura
	 * @return
	 */
	List<Contenedor> buscarContenedoresDiponiblesParaUnaRuta(int rutaId, List<Integer> tiposDeBasura);

	/**
	 * Buscar contenedores disponibles para una ruta by tipo de basura
	 * 
	 * @param tiposDeBasura
	 *            List de ids de tipos
	 * @return
	 */
	List<Contenedor> buscarContenedoresDiponiblesParaRutas(List<Integer> tiposDeBasura);

	/**
	 * 
	 * @param tiposDeBasura
	 * @return
	 */
	List<Contenedor> buscarContenedoresByTiposDeBasura(List<Integer> tiposDeBasura);
	
	/**
	 * Devuelve la comparativa por tipo de reciclado
	 * 
	 * @param tipoComparativa
	 * @param tiposDeBasura
	 * @return
	 */
	List<ComparativaPorTipoReciclado> comparativaPorTipoReciclado(TipoComparativa tipoComparativa,
			List<Integer> tiposDeBasura);


}
