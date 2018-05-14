package es.udc.citytrash.model.camion;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.camion.CamionModeloDto;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;

/**
 * 
 * @author hmia
 * 
 */

public interface CamionDao extends GenericDAO<Camion, Long> {

	/**
	 * Buscar camion por matricula
	 * 
	 * @param matricula
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Camion buscarByMatricula(String matricula) throws InstanceNotFoundException;

	/**
	 * Buscar camion por nombre
	 * 
	 * @param Nombre
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Camion buscarByNombre(String nombre) throws InstanceNotFoundException;

	/**
	 * Buscar a un camion por vin
	 * 
	 * @param vin
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Camion buscarByVin(String vin) throws InstanceNotFoundException;

	/**
	 * 
	 * @param mostrarSoloActivos
	 *            camiones activos
	 * @param mostrarSoloCamionesDeAlta
	 *            camiones de baja
	 * @return
	 */
	List<Camion> buscarCamiones(boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta);

	/**
	 * Buscar camiones x paginacion
	 * 
	 * @param pageable
	 * @param mostrarSoloActivos
	 *            camiones activos
	 * @param mostrarSoloCamionesDeAlta
	 *            camiones dados de alta mostrar solo camiones activos
	 * @return
	 */

	Page<Camion> buscarCamiones(Pageable pageable, boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta);

	/**
	 * 
	 * @param modelo
	 *            modelo a buscar
	 * @param pageable
	 * @param mostrarSoloActivos
	 *            camiones activos
	 * @param mostrarSoloCamionesDeAlta
	 *            camiones de alta
	 * @return
	 */
	Page<Camion> buscarCamionesByModelo(Pageable pageable, CamionModelo modelo, boolean mostrarSoloActivos,
			boolean mostrarSoloCamionesDeAlta);

	/**
	 * Buscar camiones de un modelo y el nombre
	 * 
	 * @param pageable
	 * @param palabrasClaves
	 *            palabras claves de busqueda
	 * @param modelo
	 *            modelo
	 * @param mostrarSoloActivos
	 *            camiones activos
	 * @param mostrarSoloCamionesDeAlta
	 *            camiones de alta
	 * @return
	 */
	Page<Camion> buscarCamionesByModeloYNombre(Pageable pageable, String palabrasClaves, CamionModelo modelo,
			boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta);

	/**
	 * 
	 * @param pageable
	 * @param palabrasClaves
	 * @param modelo
	 * @param mostrarSoloActivos
	 *            camiones activos
	 * @param mostrarSoloCamionesDeAlta
	 *            camiones de alta
	 * @return
	 */
	Page<Camion> buscarCamionesByModeloYMatricula(Pageable pageable, String palabrasClaves, CamionModelo modelo,
			boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta);

	/**
	 * 
	 * @param pageable
	 * @param palabrasClaves
	 * @param modelo
	 * @param mostrarSoloActivos
	 *            mostrar camiones activos
	 * @param mostrarSoloCamionesDeAlta
	 *            mostrar camiones de alta
	 * @return
	 */
	Page<Camion> buscarCamionesByModeloYVin(Pageable pageable, String palabrasClaves, CamionModelo modelo,
			boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta);

}