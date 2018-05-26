package es.udc.citytrash.model.contenedorService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloEditarDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloFormBusq;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorModeloRegistroDto;
import es.udc.citytrash.controller.util.dtos.contenedor.ContenedorRegistroDto;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.DuplicateInstanceException;
import es.udc.citytrash.model.util.excepciones.InactiveResourceException;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.InvalidFieldException;

/**
 * Servicio de Contenedores -CRUD CONTENEDORES -CRU MODELO DE CONTENEDORES
 * 
 * @author hmia
 *
 */
public interface ContenedorService {

	/**
	 * Devuelve true si el modelo existe
	 * 
	 * @param modelo
	 * @return
	 */
	boolean esModeloExistenteById(int modelo);

	/**
	 * Cambia el estado de un contenedor (Lo habilita o Deshabilita)
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	boolean cambiarEstadoContenedor(long id) throws InstanceNotFoundException;

	/**
	 * devuelve true si ya hay un contenedor con ese nombre
	 * 
	 * @param nombre
	 * @return
	 */
	boolean esContenedorByNombreExistente(String nombre);

	/**
	 * devuelve tru si ya existe un modelo con ese nombre
	 * 
	 * @param nombre
	 * @return
	 */
	boolean esModeloContenedorByNombreExistente(String nombre);

	/**
	 * Busca un contenedor by id
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Contenedor buscarContenedorById(long id) throws InstanceNotFoundException;

	/**
	 * Busca modelo by Id
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	ContenedorModelo buscarModeloById(int id) throws InstanceNotFoundException;

	/**
	 * Busca todos los modelos ordenados por nombre del modelo
	 * 
	 * @return
	 */
	List<ContenedorModelo> buscarTodosLosModelosOrderByModelo();

	/**
	 * Busca los tipos de basura
	 * 
	 * @return
	 */
	List<TipoDeBasura> buscarTiposDeBasura();

	/**
	 * Busca el tipo de basura de un modelo
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	TipoDeBasura buscarTipoDeBasuraByModelo(int id) throws InstanceNotFoundException;

	/**
	 * Buscar tipo de basura por contenedor
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	TipoDeBasura buscarTiposDeBasuraByContenedor(long id) throws InstanceNotFoundException;

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
	 * @throws InactiveResourceException
	 * @throws InvalidFieldException
	 */
	Contenedor modificarContenedor(ContenedorEditarDto form) throws InstanceNotFoundException, DuplicateInstanceException,
			InactiveResourceException, InvalidFieldException;

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
	 * Buscar modelos
	 * 
	 * @param pageable
	 * @param formBusqueda
	 * @return Pageable
	 */
	Page<ContenedorModelo> buscarModelos(Pageable pageable, ContenedorModeloFormBusq formBusqueda);

	/**
	 * Buscar contenedores
	 * 
	 * @param pageable
	 * @param formBusqueda
	 * @return Pageable
	 */
	Page<Contenedor> buscarContenedores(Pageable pageable, ContenedorFormBusq formBusqueda);

	/**
	 * Buscar contenedores
	 * @param formBusqueda formulario de busqueda
	 * @return List
	 */
	List<Contenedor> buscarContenedores(ContenedorFormBusq form);

}
