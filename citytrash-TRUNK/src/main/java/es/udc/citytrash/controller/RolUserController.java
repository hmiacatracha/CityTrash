package es.udc.citytrash.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.citytrash.business.service.trabajador.TrabajadorService;

@Controller
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("auth/user/")
public class RolUserController {
	
	@Autowired
	TrabajadorService tservicio;
	final Logger logger = LoggerFactory.getLogger(RolUserController.class);

}
