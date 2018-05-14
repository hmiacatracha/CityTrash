package es.udc.citytrash.model.camionModeloTipoDeBasura;

import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

@Repository("camionModeloTipoDeBasuraDao")
public class CamionModeloTipoDeBasuraDaoHibernate
		extends GenericHibernateDAOImpl<CamionModeloTipoDeBasura, CamionModeloTipoDeBasuraPK>
		implements CamionModeloTipoDeBasuraDao {

}