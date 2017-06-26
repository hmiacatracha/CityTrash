package es.udc.citytrash.model.repository.prioridad;

import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.entity.prioridad.Prioridad;
import es.udc.citytrash.model.repository.genericdoa.GenericHibernateDAOImpl;

@Repository("PrioridadDao")
public class PrioridadDaoHibernate extends GenericHibernateDAOImpl<Prioridad, String> implements PrioridadDao {

}