package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.LocalDate;
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
import services.ConferenceService;
import services.SectionService;
import services.SubmissionService;
import services.UtilityService;
import domain.Activity;
import domain.Actor;
import domain.Comentario;
import domain.Conference;
import domain.Paper;
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
	private SubmissionService submissionService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ConferenceService conferenceService;

	// Displaying an activity

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int activityid) {
		ModelAndView res;
		Activity activity;
		Collection<Section> sections;
		Collection<Comentario> comments;
		Boolean permission;

		try {
			permission = this.utilityService.isAdmin();

			activity = this.activityService.findOne(activityid);

			sections = (activity != null) ? this.sectionService
					.getSectionsOfActivity(activity.getId())
					: new ArrayList<Section>();

			comments = (activity != null) ? this.commentService
					.getCommentsOfActivity(activityid)
					: new ArrayList<Comentario>();

			res = new ModelAndView("activity/display");
			res.addObject("activity", activity);
			res.addObject("sections", sections);
			res.addObject("comments", comments);
			res.addObject("permission", permission);
			res.addObject(
					"conferenceStarted",
					LocalDate.now().toDate()
							.before(activity.getConference().getStartDate()));
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
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
			res = new ModelAndView("redirect:../welcome/index.do");
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
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	// Creating activity

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam Integer conferenceid) {
		ModelAndView res;
		Activity newActivity;
		Actor principal;
		Collection<Paper> crPapers;
		boolean permission = false;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			Conference conference = this.conferenceService
					.findOne(conferenceid);

			Assert.isTrue(conference.getAdministrator().getId() == principal
					.getId());

			permission = true;

			newActivity = this.activityService.create(conferenceid);

			Assert.isTrue(LocalDate.now().toDate()
					.before(newActivity.getConference().getStartDate()));

			crPapers = this.submissionService
					.findCameraReadyPapersOfConference(conferenceid);

			res = new ModelAndView("activity/edit");
			res.addObject("activity", newActivity);
			res.addObject("crPapers", crPapers);
			res.addObject("permission", permission);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	// Editing activity

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam Integer activityid) {
		ModelAndView res;
		Activity toEdit;
		Actor principal;
		Collection<Paper> crPapers;
		boolean permission = false;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			toEdit = this.activityService.findOne(activityid);

			Conference conference = this.conferenceService.findOne(toEdit
					.getConference().getId());

			Assert.isTrue(conference.getAdministrator().getId() == principal
					.getId());

			permission = true;

			Assert.isTrue(LocalDate.now().toDate()
					.before(toEdit.getConference().getStartDate()));

			crPapers = this.submissionService
					.findCameraReadyPapersOfConference(toEdit.getConference()
							.getId());

			res = new ModelAndView("activity/edit");
			res.addObject("activity", toEdit);
			res.addObject("permission", permission);
			res.addObject("crPapers", crPapers);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	// Saving activity
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(
			Activity activity,
			@RequestParam(required = false, value = "crpaperid") Integer crpaperid,
			BindingResult binding) {
		ModelAndView res;
		Actor principal;

		try {
			this.activityService.validate(activity, binding);
			if (binding.hasErrors()) {
				Collection<Paper> crPapers = this.submissionService
						.findCameraReadyPapersOfConference(activity
								.getConference().getId());

				res = new ModelAndView("activity/edit");
				res.addObject(activity);
				res.addObject("permission", true);
				res.addObject("crPapers", crPapers);
			} else {
				principal = this.utilityService.findByPrincipal();
				Assert.isTrue(this.utilityService.checkAuthority(principal,
						"ADMIN"));

				if (activity.getType().equalsIgnoreCase("PRESENTATION")) {
					activity.setSubmission(this.submissionService
							.findSubByPaper(crpaperid));
				}

				this.activityService.save(activity);
				res = new ModelAndView("redirect:list.do?conferenceid="
						+ activity.getConference().getId());
			}
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
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
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

}
