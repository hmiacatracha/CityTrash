package es.udc.citytrash.model.trabajador;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;
import es.udc.citytrash.util.enums.TipoTrabajador;

public interface ConductorDao extends GenericDAO<Conductor, Long> {

	List<Conductor> buscarTrabajadoresOrderByNombre();

	List<Conductor> buscarTrabajadoresOrderByApellidos(boolean mostrarSoloActivos);

	Conductor buscarTrabajadorById(long id) throws InstanceNotFoundException;

}
