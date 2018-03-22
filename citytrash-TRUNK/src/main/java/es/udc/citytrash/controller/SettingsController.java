package es.udc.citytrash.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class SettingsController {

	@RequestMapping("/settings")
	public String viewSettings(Model model) {
	  // do stuff
	  model.addAttribute("classActiveSettings","active");
	  return "settings";
	}
}
