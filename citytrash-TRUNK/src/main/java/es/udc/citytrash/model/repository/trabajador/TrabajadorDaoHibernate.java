package es.udc.citytrash.model.repository.trabajador;

import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.entity.trabajador.Trabajador;
import es.udc.citytrash.model.repository.genericdoa.GenericHibernateDAOImpl;
import es.udc.citytrash.util.excepciones.InstanceNotFoundException;

@Repository("TrabajadorDao")
public class TrabajadorDaoHibernate extends GenericHibernateDAOImpl<Trabajador, Long> implements TrabajadorDao {

	public Trabajador findByLoginEmail(String email) throws InstanceNotFoundException {
		Trabajador trabajador = (Trabajador) getSession()
				.createQuery("SELECT t FROM Trabajador t WHERE t.email = :email").setParameter("email", email)
				.uniqueResult();
		if (trabajador == null) {
			throw new InstanceNotFoundException(email, Trabajador.class.getName());
		} else {
			return trabajador;
		}
	}
}
