package es.udc.citytrash.controller.idioma;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

//@RequestMapping("/cambioidioma")
//@Controller
public class CambioIdiomaController {

	final Logger logger = LoggerFactory.getLogger(CambioIdiomaController.class);

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			RequestContextUtils.getLocaleResolver(request).setLocale(request, response,
					new Locale(request.getParameter("lang")));
		} catch (Exception ex) {
		}
		logger.info("Cammbio de idioma page");
		return new ModelAndView("/");
	}
}
