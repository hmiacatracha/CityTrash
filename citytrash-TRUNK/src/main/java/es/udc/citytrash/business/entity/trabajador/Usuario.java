package es.udc.citytrash.business.entity.trabajador;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class Usuario extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;
	private Trabajador user;

	public Usuario(Trabajador trabajador) {
		super(trabajador.getEmail(), trabajador.getPassword(), getAuthorities(trabajador));
		this.user = trabajador;
	}

	public Trabajador getUser() {
		return user;
	}

	private static Collection<? extends GrantedAuthority> getAuthorities(Trabajador trabajador) {
		Set<String> roleAndPermissions = new HashSet<>();
		String rol = trabajador.getRol();
		roleAndPermissions.add(rol);
		String[] roleNames = new String[roleAndPermissions.size()];
		Collection<GrantedAuthority> authorities = AuthorityUtils
				.createAuthorityList(roleAndPermissions.toArray(roleNames));
		return authorities;
	}
}
