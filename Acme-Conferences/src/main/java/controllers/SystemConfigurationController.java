package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.SystemConfiguration;

import services.SystemConfigurationService;
import services.UtilityService;

@Controller
@RequestMapping("/config/admin")
public class SystemConfigurationController extends AbstractController {

	@Autowired
	private SystemConfigurationService sysconfigService;

	@Autowired
	private UtilityService utilityService;

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView res;
		SystemConfiguration config;
		boolean permission = false;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			permission = true;

			config = this.sysconfigService.findMySystemConfiguration();

			res = new ModelAndView("config/display");
			res.addObject("config", config);
			res.addObject("permission", permission);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}
	
	
}
