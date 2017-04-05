package es.udc.tfg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.tfg.model.type.Type;
import es.udc.tfg.model.type.TypeDao;
import es.udc.tfg.util.exceptions.DuplicateInstanceException;

@Service("consumerService")
public class ConsumerServiceImpl implements ConsumerService {

	@Autowired
	private TypeDao typeDao;

	@Override
	public Type registerType(String type, String description) throws DuplicateInstanceException {
		Type t;

		t = typeDao.findOne(type);
		if (t != null)
			throw new DuplicateInstanceException(t.getName(), t.getClass().getName());
		t = new Type(type, description);
		typeDao.save(t);
		return t;
	}
}
