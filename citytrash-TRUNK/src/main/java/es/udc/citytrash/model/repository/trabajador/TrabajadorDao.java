package es.udc.citytrash.model.repository.trabajador;

import es.udc.citytrash.model.entity.trabajador.Trabajador;
import es.udc.citytrash.model.repository.genericdoa.GenericDAO;
import es.udc.citytrash.util.excepciones.InstanceNotFoundException;

public interface TrabajadorDao extends GenericDAO<Trabajador, Long> {

	Trabajador findByLoginEmail(String email) throws InstanceNotFoundException;
}
