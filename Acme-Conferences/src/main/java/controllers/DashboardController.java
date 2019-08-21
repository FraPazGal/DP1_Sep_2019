package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.DashboardService;
import services.UtilityService;

@Controller
@RequestMapping("/statistics")
public class DashboardController extends AbstractController{
	
	/* Services */
	
	@Autowired
	private DashboardService dashboardService;	
	
	@Autowired
	private UtilityService utilityService;
	
	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result = new ModelAndView("administrator/statistics");;
		
		try {
			this.utilityService.assertPrincipal("ADMIN");
			
			Double [] statsSubmissionsPerConference = this.dashboardService.StatsSubmissionsPerConference();
			Double [] statsRegistrationsPerConference = this.dashboardService.StatsRegistrationsPerConference();
			Double [] statsConferenceFees = this.dashboardService.StatsConferenceFees();
			Double [] statsDaysPerConference = this.dashboardService.StatsDaysPerConference();
			Double [] statsConferencesPerCategory = this.dashboardService.StatsConferencesPerCategory();
			Double [] statsCommentsPerConference = this.dashboardService.StatsCommentsPerConference();
			Double [] statsCommentsPerActivity = this.dashboardService.StatsCommentsPerActivity();
			
			result.addObject("statsSubmissionsPerConference",statsSubmissionsPerConference);
			result.addObject("statsRegistrationsPerConference",statsRegistrationsPerConference);
			result.addObject("statsConferenceFees",statsConferenceFees);
			result.addObject("statsDaysPerConference",statsDaysPerConference);
			result.addObject("statsConferencesPerCategory",statsConferencesPerCategory);
			result.addObject("statsCommentsPerConference",statsCommentsPerConference);
			result.addObject("statsCommentsPerActivity",statsCommentsPerActivity);

		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
}

