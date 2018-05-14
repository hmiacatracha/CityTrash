package es.udc.citytrash.model.trabajador;

import java.util.List;

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;

public interface AdministradorDao extends GenericDAO<Administrador, Long> {

	/**
	 * Busca administrador by id
	 * 
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Administrador buscarTrabajadorById(long id) throws InstanceNotFoundException;

	/**
	 * buscar la lista de administradores ordenados por nombre
	 * 
	 * @return
	 */
	List<Administrador> buscarTrabajadoresOrderByNombre();

	/**
	 * buscar la lista de administradores, mostrar solo los administradores
	 * activos.
	 * 
	 * @param mostrarSoloActivos
	 * @return
	 */
	List<Administrador> buscarTrabajadoresOrderByNombre(boolean mostrarSoloActivos);

}
