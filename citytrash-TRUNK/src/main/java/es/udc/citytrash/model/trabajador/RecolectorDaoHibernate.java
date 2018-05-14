package es.udc.citytrash.model.trabajador;

import java.util.ArrayList;
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

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;
import es.udc.citytrash.util.enums.TipoTrabajador;

@Repository("RecolectorDao")
public class RecolectorDaoHibernate extends GenericHibernateDAOImpl<Recolector, Long> implements RecolectorDao {

	final Logger logger = LoggerFactory.getLogger(RecolectorDaoHibernate.class);

	@Override
	public Recolector buscarTrabajadorById(long id) throws InstanceNotFoundException {
		Recolector trabajador = (Recolector) getSession().createQuery("SELECT r FROM Recolector r WHERE r.id = (:id)")
				.setParameter("id", id).uniqueResult();
		if (trabajador == null) {
			throw new InstanceNotFoundException(id, Recolector.class.getName());
		} else {
			return trabajador;
		}
	}

	@Override
	public List<Recolector> buscarTrabajadoresOrderByNombre() {
		List<Recolector> recolectores = new ArrayList<Recolector>();
		String alias = "r";
		String hql = String.format("Select " + alias + " FROM Recolector " + alias + " ORDER BY " + alias + ".nombre");
		recolectores = getSession().createQuery(hql, Recolector.class).list();
		return recolectores;
	}

	@Override
	public List<Recolector> buscarTrabajadoresOrderByApellidos(boolean mostrarSoloActivos) {
		Query<Recolector> query;
		List<Recolector> recolectores = new ArrayList<Recolector>();
		String alias = "r";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Recolector " + alias);
		if (mostrarSoloActivos)
			hql.append(" WHERE (" + alias + ".activeWorker = :activo) ");
		hql.append(" ORDER BY " + alias + ".apellidos");
		query = getSession().createQuery(hql.toString(), Recolector.class);
		if (mostrarSoloActivos)
			query.setParameter("activo", true);
		recolectores = query.list();
		return recolectores;
	}
}
