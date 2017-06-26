package es.udc.citytrash.service.recursoshumanos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.model.entity.prioridad.Prioridad;
import es.udc.citytrash.model.repository.prioridad.PrioridadDao;

@Service("RecursosHumanosService")
@Transactional
public class RecursosHumanosServiceImpl implements RecursosHumanosService {

	@Autowired
	private PrioridadDao prioridadDao;

	@Override
	public List<Prioridad> getAllPrioridades() {
		return prioridadDao.findAll();
	}
}
