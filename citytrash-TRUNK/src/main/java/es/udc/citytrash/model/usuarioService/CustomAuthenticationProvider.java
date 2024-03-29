package es.udc.citytrash.model.usuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.udc.citytrash.controller.cuenta.CustomUserDetails;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	/* http://www.baeldung.com/spring-security-authentication-provider */
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication a) throws AuthenticationException {

		logger.info("PASA POR CustomAuthenticationProvider metodo authenticate");

		CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(a.getName());

		if (userDetails == null) {
			throw new UsernameNotFoundException(String.format("User %s does not exist!", a.getName()));

		} else if (!passwordEncoder.matches(a.getCredentials().toString(), userDetails.getPassword())) {
			throw new BadCredentialsException("Bad credentials");
		} else if (!userDetails.getPerfil().getTrabajadorActivo()) {
			logger.info("trabajador inactivo");
			throw new DisabledException("User has been disabled.");

		} else if (!userDetails.getPerfil().getCuentaActiva() && userDetails.getPerfil().getTrabajadorActivo()) {
			logger.info("el trabajador esta activo peor aun no ha activado la cuenta");
			throw new DisabledException("User has been disabled.");
		} else if (!userDetails.isAccountNonExpired()) {
			throw new AccountExpiredException("Account has been expired.");
		} else if (!userDetails.isAccountNonLocked()) {
			throw new LockedException("Account has been locked.");
		} else if (!userDetails.isCredentialsNonExpired()) {
			throw new LockedException("Credentials has been expired.");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
