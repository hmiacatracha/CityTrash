package es.udc.citytrash.business.repository.prioridad;

import org.springframework.stereotype.Repository;

import es.udc.citytrash.business.entity.prioridad.Prioridad;
import es.udc.citytrash.business.repository.genericdao.GenericHibernateDAOImpl;

@Repository("PrioridadDao")
public class PrioridadDaoHibernate extends GenericHibernateDAOImpl<Prioridad, String> implements PrioridadDao {

}