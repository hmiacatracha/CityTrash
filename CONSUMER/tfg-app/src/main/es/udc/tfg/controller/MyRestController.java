package es.udc.tfg.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {
	private static final Logger logger = LoggerFactory.getLogger(MyRestController.class);

	@RequestMapping(value = "/bytepit", method = RequestMethod.GET)
	public String Init(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.");
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		String html = "<HTML> <HEAD><TITLE>Bytepit-app</TITLE> </HEAD> " + "<BODY BGCOLOR='FFFFFF'>" + "<HR> "
				+ "<H1>Bytepit-app</H1>" + "<H2>API REST SERVICE</H2> "
				+ "<p>&copy; 2016-2017 Heidy M. Izaguirre Alvarez <p>" + "<BR> " + "<HR> " + "	</BODY> </HTML>";
		return html;
	}

}
