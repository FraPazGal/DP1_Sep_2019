package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ReviewService;
import services.UtilityService;
import domain.Actor;
import domain.Report;

@Controller
@RequestMapping("/review")
public class ReviewController extends AbstractController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private UtilityService utilityService;

	@RequestMapping(value = "/myreports", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Actor principal;
		Collection<Report> myReports;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"REVIEWER"));

			myReports = this.reviewService.findMyReports(principal.getId());

			res = new ModelAndView("report/mine");
			res.addObject("reports", myReports);

		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}

		return res;
	}

	// @RequestMapping(value = "/conferencereports", method = RequestMethod.GET)

}
