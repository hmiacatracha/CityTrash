package es.udc.citytrash.business.repository.trabajador;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
				.createQuery("SELECT t FROM Trabajador t WHERE TRIM(UPPER(t.email)) like TRIM(UPPER(:email))")
				.setParameter("email", email).uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorEMail: email no encontrado");
			throw new InstanceNotFoundException(email, Trabajador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorEMail: email encontrado => " + email);
			logger.info("buscarTrabajadorPorEMail: trabajador encontrado => " + trabajador);
			return trabajador;
		}
	}

	public Trabajador buscarTrabajadorIdToken(long id, String token) throws TokenInvalidException {
		Trabajador trabajador = (Trabajador) getSession()
				.createQuery("SELECT t FROM Trabajador t WHERE t.id = :id and t.token like :token")
				.setParameter("token", token).setParameter("id", id).uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorEMail: token no encontrado =>" + token);
			throw new TokenInvalidException(token, Trabajador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorToken: token encontrado");
			return trabajador;
		}
	}

	@Override
	public Trabajador buscarTrabajadorPorDocumentoId(String documentoId) throws InstanceNotFoundException {
		Trabajador trabajador = (Trabajador) getSession()
				.createQuery("SELECT t FROM Trabajador t WHERE TRIM(UPPER(t.docId)) like TRIM(UPPER(:documento))")
				.setParameter("documento", documentoId).uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorDocumento: documento no encontrado");
			throw new InstanceNotFoundException(documentoId, Trabajador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorDocumento: documento encontrado => " + documentoId);
			logger.info("buscarTrabajadorPorDocumento: trabajador encontrado => " + trabajador);
			return trabajador;
		}
	}

	@Override
	public Page<Trabajador> buscarTrabajadores(Pageable pageable) {
		@SuppressWarnings("unchecked")
		List<Trabajador> trabajadores = getSession().createQuery("Select t FROM Trabajador t").list();
		Page<Trabajador> page = new PageImpl<Trabajador>(trabajadores, pageable, trabajadores.size());
		return page;
	}
}
