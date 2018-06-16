package es.udc.citytrash.model.camionModeloTipoDeBasura;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

@Repository("camionModeloTipoDeBasuraDao")
public class CamionModeloTipoDeBasuraDaoHibernate
		extends GenericHibernateDAOImpl<CamionModeloTipoDeBasura, CamionModeloTipoDeBasuraPK>
		implements CamionModeloTipoDeBasuraDao {

	final Logger logger = LoggerFactory.getLogger(CamionModeloTipoDeBasuraDaoHibernate.class);

	@Override
	public List<CamionModeloTipoDeBasura> buscarTiposDeBasuraByModelo(int modeloId) {

		Query<CamionModeloTipoDeBasura> query;
		List<CamionModeloTipoDeBasura> tipos = new ArrayList<CamionModeloTipoDeBasura>();
		String hql = String.format("Select t FROM CamionModeloTipoDeBasura t WHERE t.pk.modelo.id = (:modeloId) ");
		query = getSession().createQuery(hql.toString(), CamionModeloTipoDeBasura.class);
		query.setParameter("modeloId", modeloId);
		tipos = query.list();
		return tipos;
	}
}