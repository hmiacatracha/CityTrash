package es.udc.citytrash.model.trabajador;

import java.util.List;

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;

public interface RecolectorDao extends GenericDAO<Recolector, Long> {

	List<Recolector> buscarTrabajadoresOrderByApellidos(boolean mostrarSoloActivos);

	List<Recolector> buscarTrabajadoresOrderByNombre();

	Recolector buscarTrabajadorById(long id) throws InstanceNotFoundException;

}
