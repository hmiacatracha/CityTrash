package es.udc.citytrash.model.trabajador;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

@Repository("ConductorDao")
public class ConductorDaoHibernate extends GenericHibernateDAOImpl<Conductor, Long> implements ConductorDao {

	final Logger logger = LoggerFactory.getLogger(ConductorDaoHibernate.class);

	@Override
	public Conductor buscarTrabajadorById(long id) throws InstanceNotFoundException {
		Conductor trabajador = (Conductor) getSession().createQuery("SELECT c FROM Conductor c WHERE c.id = (:id)")
				.setParameter("id", id).uniqueResult();
		if (trabajador == null) {
			throw new InstanceNotFoundException(id, Conductor.class.getName());
		} else {
			return trabajador;
		}
	}

	@Override
	public List<Conductor> buscarTrabajadoresOrderByNombre() {
		List<Conductor> conductores = new ArrayList<Conductor>();
		String alias = "c";
		String hql = String.format("Select " + alias + " FROM Conductor " + alias + " ORDER BY " + alias + ".nombre");
		conductores = getSession().createQuery(hql, Conductor.class).list();
		return conductores;
	}

	@Override
	public List<Conductor> buscarTrabajadoresOrderByApellidos(boolean mostrarSoloActivos) {
		Query<Conductor> query;
		List<Conductor> conductor = new ArrayList<Conductor>();
		String alias = "c";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Conductor " + alias);
		if (mostrarSoloActivos)
			hql.append(" WHERE (" + alias + ".activeWorker = :activo) ");
		hql.append(" ORDER BY " + alias + ".apellidos");
		query = getSession().createQuery(hql.toString(), Conductor.class);
		if (mostrarSoloActivos)
			query.setParameter("activo", true);
		conductor = query.list();
		return conductor;
	}
}
