package es.udc.citytrash.business.repository.trabajador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.repository.genericdao.GenericHibernateDAOImpl;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.business.util.excepciones.TokenInvalidException;

@Repository("TrabajadorDao")
public class TrabajadorDaoHibernate extends GenericHibernateDAOImpl<Trabajador, Long> implements TrabajadorDao {

	final Logger logger = LoggerFactory.getLogger(TrabajadorDaoHibernate.class);

	public Trabajador buscarTrabajadorPorEmail(String email) throws InstanceNotFoundException {
		Trabajador trabajador = (Trabajador) getSession()
				.createQuery("SELECT t FROM Trabajador t WHERE lower(t.email) like lower(:email)")
				.setParameter("email", email).uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorEMail: email no encontrado");
			throw new InstanceNotFoundException(email, Trabajador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorEMail: email encontrado");
			return trabajador;
		}
	}

	public Trabajador buscarTrabajadorPorToken(String token) throws TokenInvalidException {
		Trabajador trabajador = (Trabajador) getSession()
				.createQuery("SELECT t FROM Trabajador t WHERE t.token like :token").setParameter("token", token)
				.uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorEMail: email no encontrado");
			throw new TokenInvalidException(token, Trabajador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorToken: token encontrado");
			return trabajador;
		}
	}

	@Override
	public Trabajador buscarTrabajadorPorDocumentoId(String documentoId) throws InstanceNotFoundException {
		Trabajador trabajador = (Trabajador) getSession()
				.createQuery("SELECT t FROM Trabajador t WHERE lower(t.docId) like lower(:documento)")
				.setParameter("documento", documentoId).uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorDocumento: documento no encontrado");
			throw new InstanceNotFoundException(documentoId, Trabajador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorDocumento: documento encontrado");
			return trabajador;
		}
	}
}
