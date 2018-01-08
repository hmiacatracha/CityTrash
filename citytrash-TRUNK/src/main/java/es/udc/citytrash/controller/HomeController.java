package es.udc.citytrash.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = { "/", "/home", "/index" })
public class HomeController {

	final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Show home page.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(@RequestParam(value = "expired", required = false) String expired,
			@RequestParam(value = "invalid", required = false) String invalid) {
		logger.info("Showing home page");
		Locale currentLocale = LocaleContextHolder.getLocale();
		logger.info("locale:" + currentLocale);
		return "home/index";
	}
}
