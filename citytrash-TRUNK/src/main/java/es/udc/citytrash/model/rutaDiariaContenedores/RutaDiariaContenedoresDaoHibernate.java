package es.udc.citytrash.model.rutaDiariaContenedores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

/**
 * 
 * @author hmia
 *
 */
@Repository("RutaDiariaContenedoresDao")
public class RutaDiariaContenedoresDaoHibernate extends
		GenericHibernateDAOImpl<RutaDiariaContenedores, RutaDiariaContenedoresPK> implements RutaDiariaContenedoresDao {

	final Logger logger = LoggerFactory.getLogger(RutaDiariaContenedoresDaoHibernate.class);

}