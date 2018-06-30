package es.udc.citytrash.model.rutaDiaria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

/**
 * 
 * @author hmia
 *
 */
@Repository("rutaDiariaDao")
public class RutaDiariaDaoHibernate extends GenericHibernateDAOImpl<RutaDiaria, Long> implements RutaDiariaDao {

	final Logger logger = LoggerFactory.getLogger(RutaDiariaDaoHibernate.class);

}