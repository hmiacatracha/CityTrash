package es.udc.citytrash.controller.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.citytrash.controller.trabajador.TrabajadorController;

@Controller
@RequestMapping("error")
public class CustomErrorController {

	private static final String URL_FORBIDDEN = "/403";
	private static final String URL_NOT_FOUND = "/404";

	final Logger logger = LoggerFactory.getLogger(TrabajadorController.class);

	@RequestMapping(value = URL_FORBIDDEN)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String forbidden() {
		return "error/403";
	}

	@RequestMapping(value = URL_NOT_FOUND)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String notFoundException() {
		return "error/404";
	}
}
