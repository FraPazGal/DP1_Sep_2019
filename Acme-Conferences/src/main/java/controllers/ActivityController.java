package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.CommentService;
import services.SectionService;
import services.UtilityService;
import domain.Activity;
import domain.Actor;
import domain.Comment;
import domain.Section;

@Controller
@RequestMapping("/activity")
public class ActivityController extends AbstractController {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private SectionService sectionService;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private CommentService commentService;

	// Displaying an activity

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int activityid) {
		ModelAndView res;
		Activity activity;
		Collection<Section> sections;
		Collection<Comment> comments;
		Boolean permission;

		try {
			permission = this.utilityService.isAdmin();

			activity = this.activityService.findOne(activityid);

			sections = (activity != null) ? this.sectionService
					.getSectionsOfActivity(activity.getId())
					: new ArrayList<Section>();

			comments = (activity != null) ? this.commentService
					.getCommentsOfActivity(activityid)
					: new ArrayList<Comment>();

			res = new ModelAndView("activity/display");
			res.addObject("activity", activity);
			res.addObject("sections", sections);
			res.addObject("comments", comments);
			res.addObject("permission", permission);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	// Listing all activities

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll() {
		ModelAndView res;
		Collection<Activity> activities;
		Actor principal;
		String requestUri;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			activities = this.activityService.listAll();
			activities = (activities == null) ? activities = new ArrayList<>()
					: activities;

			requestUri = "activity/listAll.do";

			res = new ModelAndView("activity/list");
			res.addObject("activities", activities);
			res.addObject("requestUri", requestUri);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	// Listing activities of a conference

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int conferenceid) {
		ModelAndView res;
		Collection<Activity> activities;
		String requestUri;

		try {
			activities = this.activityService
					.getActivitiesOfConference(conferenceid);
			activities = (activities == null) ? activities = new ArrayList<>()
					: activities;

			requestUri = "activity/list.do?conferenceid=" + conferenceid;

			res = new ModelAndView("activity/list");
			res.addObject("activities", activities);
			res.addObject("requestUri", requestUri);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	// Creating activity

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam Integer conferenceid) {
		ModelAndView res;
		Activity newActivity;
		Actor principal;
		boolean permission = false;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			permission = true;

			newActivity = this.activityService.create(conferenceid);

			res = new ModelAndView("activity/edit");
			res.addObject("activity", newActivity);
			res.addObject("permission", permission);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	// Editing activity

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam Integer activityid) {
		ModelAndView res;
		Activity toEdit;
		Actor principal;
		boolean permission = false;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			permission = true;

			toEdit = this.activityService.findOne(activityid);

			res = new ModelAndView("activity/edit");
			res.addObject("activity", toEdit);
			res.addObject("permission", permission);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	// Saving activity
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Activity activity, BindingResult binding) {
		ModelAndView res;
		Actor principal;

		try {
			if (binding.hasErrors()) {
				res = new ModelAndView("activity/edit");
				res.addObject(activity);
				res.addObject("permission", true);
			} else {
				principal = this.utilityService.findByPrincipal();
				Assert.isTrue(this.utilityService.checkAuthority(principal,
						"ADMIN"));

				this.activityService.save(activity);
				res = new ModelAndView("redirect:list.do?conferenceid="
						+ activity.getConference().getId());
			}
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	// Deleting section

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Activity activity) {
		ModelAndView res;
		Actor principal;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			this.activityService.delete(activity);
			res = new ModelAndView("redirect:list.do?conferenceid="
					+ activity.getConference().getId());
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

}
