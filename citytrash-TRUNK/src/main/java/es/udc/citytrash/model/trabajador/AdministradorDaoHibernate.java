package es.udc.citytrash.model.trabajador;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

@Repository("AdministradorDao")
public class AdministradorDaoHibernate extends GenericHibernateDAOImpl<Administrador, Long>
		implements AdministradorDao {

	final Logger logger = LoggerFactory.getLogger(AdministradorDaoHibernate.class);

	@Override
	public Administrador buscarTrabajadorById(long id) throws InstanceNotFoundException {
		Administrador trabajador = (Administrador) getSession()
				.createQuery("SELECT a FROM Administrador a WHERE a.id = (:id)").setParameter("id", id).uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorDocumento: documento no encontrado");
			throw new InstanceNotFoundException(id, Administrador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorDocumento: documento encontrado => " + id);
			logger.info("buscarTrabajadorPorDocumento: trabajador encontrado => " + trabajador);
			return trabajador;
		}
	}

	@Override
	public List<Administrador> buscarTrabajadoresOrderByNombre() {
		List<Administrador> administradores = new ArrayList<Administrador>();
		String alias = "a";
		String hql = String
				.format("Select " + alias + " FROM Administrador " + alias + " ORDER BY " + alias + ".nombre");
		administradores = getSession().createQuery(hql, Administrador.class).list();
		return administradores;
	}

	@Override
	public List<Administrador> buscarTrabajadoresOrderByNombre(boolean mostrarSoloActivos) {
		Query<Administrador> query;
		List<Administrador> administradores = new ArrayList<Administrador>();
		String alias = "t";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Administrador " + alias);
		if (mostrarSoloActivos)
			hql.append(" WHERE (" + alias + ".activeWorker = :activo) ");
		hql.append(" ORDER BY " + alias + ".nombre");
		query = getSession().createQuery(hql.toString(), Administrador.class);
		if (mostrarSoloActivos)
			query.setParameter("activo", true);
		administradores = query.list();
		return administradores;
	}
}
