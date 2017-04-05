/*-
 * ========================LICENSE_START=================================
 * TFG BYTEPIT-APP
 * %%
 * Copyright (C) 2016 Heidy Mabel Izaguirre Alvarez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * =========================LICENSE_END==================================
 */
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

/**
 * Rest controller default root
 * 
 * @author hmia
 *         http://www.jtech.ua.es/j2ee/publico/spring-2012-13/sesion04-apuntes.html
 */
@RestController
public class MyRestController {
	private static final Logger logger = LoggerFactory.getLogger(MyRestController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
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
