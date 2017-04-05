package es.udc.tfg.service;

import es.udc.tfg.util.exceptions.DuplicateInstanceException;
import es.udc.tfg.model.type.*;

public interface ConsumerService {

	public Type registerType(String type, String description) throws DuplicateInstanceException;
}
