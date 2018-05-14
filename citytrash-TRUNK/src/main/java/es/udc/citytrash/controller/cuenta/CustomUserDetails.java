package es.udc.citytrash.controller.cuenta;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.udc.citytrash.controller.util.dtos.cuenta.PerfilDto;

/**
 * https://www.boraji.com/spring-security-5-custom-userdetailsservice-example,
 * http://www.baeldung.com/spring-security-authentication-with-a-database
 */

public class CustomUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Collection<? extends GrantedAuthority> authorities;
	private PerfilDto perfil;

	public CustomUserDetails() {

	}

	public CustomUserDetails(PerfilDto perfil, Collection<? extends GrantedAuthority> authorities) {
		this.perfil = perfil;
		this.authorities = authorities;
	}

	public PerfilDto getPerfil() {
		return this.perfil;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return perfil.getPassword();
	}

	public String getName() {
		return perfil.getNombre();
	}

	@Override
	public String getUsername() {
		return perfil.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return perfil.isCuentaActiva();
	}
}
