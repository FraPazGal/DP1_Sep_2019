/*
 * WelcomeController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.UtilityService;
import domain.Actor;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	// Constructors -----------------------------------------------------------

	@Autowired
	private UtilityService utilityService;

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------

	@RequestMapping(value = "/index")
	public ModelAndView index(
			@RequestParam(required = false, defaultValue = "John Doe") final String name) {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
		Actor principal;

		try {

			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(principal != null);

			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			moment = formatter.format(new Date());

			result = new ModelAndView("welcome/index");
			result.addObject("name",
					principal.getName() + " " + principal.getSurname());
			result.addObject("moment", moment);

		} catch (Exception e) {
			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			moment = formatter.format(new Date());

			result = new ModelAndView("welcome/index");
			result.addObject("name", name);
			result.addObject("moment", moment);
		}

		return result;
	}
}
