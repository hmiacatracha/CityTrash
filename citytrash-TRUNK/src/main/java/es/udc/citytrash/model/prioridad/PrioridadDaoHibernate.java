package es.udc.citytrash.model.prioridad;

import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

@Repository("PrioridadDao")
public class PrioridadDaoHibernate extends GenericHibernateDAOImpl<Prioridad, String> implements PrioridadDao {

}