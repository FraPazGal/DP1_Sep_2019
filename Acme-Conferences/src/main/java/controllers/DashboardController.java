package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.DashboardService;
import services.UtilityService;
import domain.Actor;

@Controller
@RequestMapping(value = "statistics")
public class DashboardController extends AbstractController{
	
	
	@Autowired
	private DashboardService dashboardService;	
	
	@Autowired
	private UtilityService utilityService;
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		Actor principal;
		ModelAndView result = null;
		
		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
			
			Double [] statsSubmissionsPerConference = this.dashboardService.StatsSubmissionsPerConference();
			Double [] statsRegistrationsPerConference = this.dashboardService.StatsRegistrationsPerConference();
			Double [] statsConferenceFees = this.dashboardService.StatsConferenceFees();
			Double [] statsDaysPerConference = this.dashboardService.StatsDaysPerConference();
			Double [] statsConferencesPerCategory = this.dashboardService.StatsConferencesPerCategory();
			Double [] statsCommentsPerConference = this.dashboardService.StatsCommentsPerConference();
			Double [] statsCommentsPerActivity = this.dashboardService.StatsCommentsPerActivity();
			
			result = new ModelAndView("administrator/statistics");

			result.addObject("requestURI", "statistics/display.do");
			result.addObject("statsSubmissionsPerConference",statsSubmissionsPerConference);
			result.addObject("statsRegistrationsPerConference",statsRegistrationsPerConference);
			result.addObject("statsConferenceFees",statsConferenceFees);
			result.addObject("statsDaysPerConference",statsDaysPerConference);
			result.addObject("statsConferencesPerCategory",statsConferencesPerCategory);
			result.addObject("statsCommentsPerConference",statsCommentsPerConference);
			result.addObject("statsCommentsPerActivity",statsCommentsPerActivity);
			
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do/");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		
		return result;
	}
	
}

