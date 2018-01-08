package es.udc.citytrash.business.service.trabajador;

import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.udc.citytrash.business.entity.trabajador.Trabajador;
import es.udc.citytrash.business.repository.trabajador.TrabajadorDao;
import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;

@Transactional
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private TrabajadorDao trabajadorProfileDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Trabajador t;
		try {
			t = trabajadorProfileDao.buscarTrabajadorPorEmail(username);
		} catch (InstanceNotFoundException e) {
			throw new UsernameNotFoundException("No user found with email: " + username);
		}

		User user = new User(t.getEmail(), t.getPassword(), !t.isEnabledCount(), true, true, true,
				Collections.singleton(new SimpleGrantedAuthority(t.getRol())));
		return user;
	}
}
