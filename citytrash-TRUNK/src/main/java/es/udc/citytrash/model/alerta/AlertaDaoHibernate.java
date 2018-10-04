package es.udc.citytrash.model.alerta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import es.udc.citytrash.model.rutaDiaria.RutaDiaria;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

/**
 * 
 * @author hmia
 *
 */
@Repository("alertaDao")
public class AlertaDaoHibernate extends GenericHibernateDAOImpl<Alerta, Long> implements AlertaDao {

	final Logger logger = LoggerFactory.getLogger(AlertaDaoHibernate.class);

	@Override
	public Page<Alerta> buscarAlertas(Pageable pageable) {
		logger.info("buscarAlertasDao 1");
		Date fecha = Calendar.getInstance().getTime();
		Query<Alerta> query;
		List<Alerta> alertas = new ArrayList<Alerta>();
		Page<Alerta> page = new PageImpl<Alerta>(alertas, pageable, alertas.size());

		StringBuilder hql = new StringBuilder(
				"Select distinct (a) FROM Alerta a  WHERE DAY(a.fechaHora) = DAY(:fecha) ");
		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> "a." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);
		logger.info("buscarAlertasDao 2");
		query = getSession().createQuery(hql.toString(), Alerta.class).setParameter("fecha", fecha);
		alertas = query.list();
		logger.info("buscarAlertasDao 3");
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > alertas.size() ? alertas.size() : (start + pageable.getPageSize());
		logger.info("buscarAlertasDao 4");
		boolean rangoExistente = (alertas.size() - start >= 0) && (alertas.size() - end >= 0);
		if (rangoExistente) {
			page = new PageImpl<Alerta>(alertas.subList(start, end), pageable, alertas.size());
		} else {
			List<Alerta> rutasAux = new ArrayList<Alerta>();
			page = new PageImpl<Alerta>(rutasAux, pageable, rutasAux.size());
		}
		logger.info("buscarAlertasDao 5");
		return page;
	}

	@Override
	public int obtenerNumeroAlertas() {
		Date fecha = Calendar.getInstance().getTime();
		int count = 0;
		StringBuilder hql = new StringBuilder(
				"Select distinct (a) FROM Alerta a  WHERE DAY(a.fechaHora) = DAY(:fecha) ");
		List<Alerta> resultado = getSession().createQuery(hql.toString(), Alerta.class).setParameter("fecha", fecha)
				.getResultList();
		if (resultado != null)
			count = resultado.size();
		return count;
	}

}