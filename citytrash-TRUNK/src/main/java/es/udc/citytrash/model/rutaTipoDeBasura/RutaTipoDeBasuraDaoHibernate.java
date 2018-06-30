package es.udc.citytrash.model.rutaTipoDeBasura;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

/**
 * 
 * @author hmia
 *
 */
@Repository("RutaTipoDeBasuraDao")
public class RutaTipoDeBasuraDaoHibernate extends GenericHibernateDAOImpl<RutaTipoDeBasura, RutaTipoDeBasuraPK>
		implements RutaTipoDeBasuraDao {

	final Logger logger = LoggerFactory.getLogger(RutaTipoDeBasuraDaoHibernate.class);

}