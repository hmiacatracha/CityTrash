package es.udc.citytrash.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class homeController {
	/*
	 * @RequestMapping(method = RequestMethod.GET) public String index(ModelMap
	 * model) { model.addAttribute("message", "Spring MVC XML Config Example");
	 * return "index"; }
	 */
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("recipient", "World");
		return "index.html";
	}

}
