package es.udc.citytrash.model.rutaDiariaContenedores;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.rutaDiaria.RutaDiaria;
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