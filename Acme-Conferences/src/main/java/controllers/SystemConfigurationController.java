package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;
import services.UtilityService;
import domain.SystemConfiguration;

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

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			config = this.sysconfigService.findMySystemConfiguration();

			String voidES = config.getVoidWords().get("Español");
			String voidEN = config.getVoidWords().get("English");

			res = new ModelAndView("config/display");
			res.addObject("config", config);
			res.addObject("welcome", config.getWelcomeMessage());
			res.addObject("voidES", voidES);
			res.addObject("voidEN", voidEN);
			res.addObject("topics", config.getTopics());
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView res;
		SystemConfiguration config;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			config = this.sysconfigService.findMySystemConfiguration();

			String voidES = config.getVoidWords().get("Español");
			String voidEN = config.getVoidWords().get("English");

			res = new ModelAndView("config/edit");
			res.addObject("config", config);
			res.addObject("welcome", config.getWelcomeMessage());
			res.addObject("voidES", voidES);
			res.addObject("voidEN", voidEN);
			res.addObject("topics", config.getTopics());
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SystemConfiguration config,
			@RequestParam(value = "welcomeES") String welcomeES,
			@RequestParam(value = "welcomeEN") String welcomeEN,
			@RequestParam(value = "voidES") String voidES,
			@RequestParam(value = "voidEN") String voidEN,
			@RequestParam(value = "topicsES") String topicsES,
			@RequestParam(value = "topicsEN") String topicsEN,
			BindingResult binding) {
		ModelAndView res;
		SystemConfiguration reconstructed;

		try {
			reconstructed = this.sysconfigService.reconstruct(config,
					welcomeES, welcomeEN, voidES, voidEN, topicsES, topicsEN,
					binding);

			if (binding.hasErrors()) {
				res = new ModelAndView("config/edit");

				res.addObject("config", config);
				res.addObject("welcome", reconstructed.getWelcomeMessage());
				res.addObject("voidES",
						reconstructed.getVoidWords().get("Español"));
				res.addObject("voidEN",
						reconstructed.getVoidWords().get("English"));
				res.addObject("topics", reconstructed.getTopics());
				res.addObject("binding", binding);
			} else {
				Assert.isTrue(this.utilityService.checkAuthority(
						this.utilityService.findByPrincipal(), "ADMIN"));

				this.sysconfigService.save(reconstructed);

				res = new ModelAndView("redirect:display.do");
			}
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}
}
