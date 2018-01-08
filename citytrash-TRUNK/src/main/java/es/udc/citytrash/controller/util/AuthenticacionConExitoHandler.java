package es.udc.citytrash.controller.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import es.udc.citytrash.business.service.trabajador.TrabajadorServiceImpl;

public class AuthenticacionConExitoHandler implements AuthenticationSuccessHandler {

	final Logger logger = LoggerFactory.getLogger(TrabajadorServiceImpl.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		logger.info("PASA POR AUTENTICACIONCONEXITOHANDLER");
		HttpSession session = request.getSession();

		/* Set some session variables */
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		session.setAttribute("uname", authUser.getUsername());
		session.setAttribute("authorities", authentication.getAuthorities());
		/* */
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect("/");
	}
}
