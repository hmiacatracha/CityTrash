package es.udc.citytrash.controller.excepciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.citytrash.controller.util.WebUtils;

@Controller
@RequestMapping("error")
public class CustomErrorController {

	final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

	@RequestMapping(value = WebUtils.URL_FORBIDDEN)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String forbidden() {
		return "error/403";
	}

	@RequestMapping(value = WebUtils.URL_NOT_FOUND)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String notFoundException() {
		return "error/404";
	}

	@RequestMapping(value = WebUtils.URL_BAD_REQUEST)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String badRequestException() {
		return "error/400";
	}

	@RequestMapping(value = WebUtils.URL_UNAUTHORIZED)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String conflicException() {
		return "error/400";
	}
}
