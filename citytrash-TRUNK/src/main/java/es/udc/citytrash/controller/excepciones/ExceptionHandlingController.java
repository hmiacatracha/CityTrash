package es.udc.citytrash.controller.excepciones;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thymeleaf.exceptions.TemplateInputException;

@ControllerAdvice
public class ExceptionHandlingController {

	// https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
	// http://www.baeldung.com/exception-handling-for-rest-with-spring
	private static Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation exception")
	public String conflict(final DataIntegrityViolationException exception, Model model) {
		logger.error("Exception 409 DataIntegrityViolationException during execution of SpringSecurity application",
				exception);
		String errorMessage = (exception != null ? exception.getCause().toString() : "Unknown error");
		model.addAttribute("errorMessage", errorMessage);
		return "error/500";
	}

	@ExceptionHandler({ SQLException.class, DataAccessException.class })
	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data access exception")
	public String databaseError(Exception exception, Model model) {
		logger.error("Exception 409 DataAccessException during execution of SpringSecurity application",
				exception.getCause());
		String errorMessage = (exception != null ? exception.getCause().toString() : "Unknown error");
		model.addAttribute("errorMessage", errorMessage);
		return "error/500";
	}

	@ExceptionHandler({ TemplateInputException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "TemplateInputException")
	public String templateError(TemplateInputException exception, Model model) {
		logger.error("Exception 409 DataAccessException during execution of SpringSecurity application",
				exception.getCause());
		String errorMessage = (exception != null ? exception.getLocalizedMessage() : "Unknown error");
		model.addAttribute("errorMessage", errorMessage);
		return "error/500";
	}

	@ExceptionHandler({ HibernateException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "HibernateException")
	public String databaseError(HibernateException exception, Model model) {
		logger.error("Exception 409 DataAccessException during execution of SpringSecurity application",
				exception.getCause());
		String errorMessage = (exception != null ? exception.getLocalizedMessage() : "Unknown error");
		model.addAttribute("errorMessage", errorMessage);
		return "error/500";
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String exception500(final Throwable throwable, Model model) {
		logger.error("Exception 500 during execution of SpringSecurity application", throwable.getCause());
		String errorMessage = (throwable != null ? throwable.getCause().toString() : "Unknown error");
		model.addAttribute("errorMessage", errorMessage);
		return "error/500";
	}

}