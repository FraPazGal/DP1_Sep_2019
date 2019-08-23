package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.CategoryService;
import services.CommentService;
import services.ConferenceService;
import services.RegistrationService;
import services.SponsorshipService;
import services.SubmissionService;
import services.UtilityService;
import domain.Activity;
import domain.Actor;
import domain.Category;
import domain.Comentario;
import domain.Conference;
import domain.Sponsorship;

@Controller
@RequestMapping("/conference")
public class ConferenceController extends AbstractController {

	/* Services */

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private SponsorshipService sponsorshipService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private SubmissionService submissionService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int conferenceId) {

		ModelAndView result = new ModelAndView("conference/display");
		boolean isPrincipal = false;
		boolean hasSubcriptions = false;
		boolean hasSubmittions = false;
		Map<String, String> titleCat = new HashMap<>();
		Conference conference = null;
		try {
			Actor principal = null;
			Sponsorship spoBanner = this.sponsorshipService
					.findBanner(conferenceId);
			Category category = this.categoryService
					.findOneByConferenceId(conferenceId);
			titleCat.putAll(category.getTitle());
			Collection<Activity> activities = this.activityService
					.getActivitiesOfConference(conferenceId);
			Collection<Comentario> comments = this.commentService
					.getCommentsOfConference(conferenceId);
			
			hasSubcriptions = (this.registrationService.findActorsRegisteredTo(conferenceId).isEmpty()) ? false	: true;
			hasSubmittions = (this.submissionService.findActorsWithSubmitions(conferenceId).isEmpty()) ? false : true;
			
			try {
				principal = this.utilityService.findByPrincipal();
				if (this.utilityService.checkAuthority(principal, "ADMIN"))
					isPrincipal = true;
				
			} catch (final Throwable oops) {}
			
			conference = this.conferenceService.findOneToDisplay(conferenceId);

			result.addObject("conference", conference);
			result.addObject("spoBanner", spoBanner);
			result.addObject("titleCat", titleCat);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("activities", activities);
			result.addObject("comments", comments);
			result.addObject("hasSubcriptions", hasSubcriptions);
			result.addObject("hasSubmittions", hasSubmittions);
			result.addObject("requestURI",
					"conference/display.do?conferenceId=" + conferenceId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam (required = false) final String catalog, 
			@RequestParam (required = false) final Integer categoryId) {
		ModelAndView result = new ModelAndView("conference/list");
		Collection<Conference> conferences = null;
		Collection<Conference> conferencesRegisteredTo = new ArrayList<>();
		Collection<Conference> conferencesSubmittedTo = new ArrayList<>();
		Actor principal = null;
		String isPrincipal = null;

		try {
			if (categoryId != null) {
				Category category = this.categoryService.findOne(categoryId);
				conferences = category.getConferences();
				
				result.addObject("category", category);
			} else {
				switch (catalog) {
				case "future":
					conferences = this.conferenceService.futureConferences();
					break;

				case "past":
					conferences = this.conferenceService.pastConferences();
					break;

				case "running":
					conferences = this.conferenceService.runningConferences();
					break;
				}
				try {
					principal = this.utilityService.findByPrincipal();
					if (this.utilityService.checkAuthority(principal, "ADMIN")) {
						isPrincipal = "ADMIN";
						if (conferences == null) {
							switch (catalog) {
							case "unpublished":
								conferences = this.conferenceService
									.findConferencesUnpublishedAndMine(principal.getId());
								break;

							case "5sub":
								conferences = this.conferenceService
										.findSubmissionLastFive();
								break;

							case "5noti":
								conferences = this.conferenceService
										.findNotificationInFive();
								break;

							case "5cam":
								conferences = this.conferenceService
										.findCameraInFive();
								break;

							case "5org":
								conferences = this.conferenceService
										.findStartInFive();
								break;
							}
						}
					} else if (this.utilityService.checkAuthority(principal, "AUTHOR")) {
						isPrincipal = "AUTHOR";
						
						conferencesRegisteredTo = this.conferenceService
								.conferencesRegisteredTo(principal.getId());
						conferencesSubmittedTo = this.conferenceService
								.conferencesSubmittedTo(principal.getId());
					}
				} catch (Throwable oops) {
				}
			}
			if (conferences == null) {
				result = new ModelAndView("redirect:/welcome/index.do");
			} else {
				result.addObject("conferences", conferences);
				result.addObject("conferencesRegisteredTo",
						conferencesRegisteredTo);
				result.addObject("conferencesSubmittedTo",
						conferencesSubmittedTo);
				result.addObject("isPrincipal", isPrincipal);
				result.addObject("catalog", catalog);
				result.addObject("listConf", true);
			}

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = null;
		try {
			final Conference conference = this.conferenceService.create();

			result = this.createEditModelAndView(conference);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int conferenceId) {
		ModelAndView result;
		try {
			result = this.createEditModelAndView(this.conferenceService
					.findOneToEdit(conferenceId));

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
		}
		return result;
	}

	/* Save as Draft */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Conference conference, BindingResult binding) {
		ModelAndView result = new ModelAndView(
				"redirect:list.do?catalog=unpublished");

		try {
			Conference toSave = this.conferenceService.reconstruct(conference,
					binding);
			if (binding.hasErrors()) {
				conference.setStatus("DRAFT");

				result = this.createEditModelAndView(conference);
			} else {
				this.conferenceService.save(toSave);
			}

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(conference, oops.getMessage());
		}
		return result;
	}

	/* Save as Final */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(final Conference conference,
			final BindingResult binding) {
		ModelAndView result = new ModelAndView(
				"redirect:list.do?catalog=future");

		try {
			Conference toSave = this.conferenceService.reconstruct(conference,
					binding);
			if (binding.hasErrors()) {

				conference.setStatus("DRAFT");

				result = this.createEditModelAndView(conference);
			} else
				toSave.setStatus("FINAL");
			this.conferenceService.save(toSave);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(conference, oops.getMessage());
		}
		return result;
	}

	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int conferenceId) {
		ModelAndView result = new ModelAndView(
				"redirect:list.do?catalog=unpublished");
		try {
			final Conference conference = this.conferenceService
					.findOne(conferenceId);
			this.activityService.deleteAll(this.activityService
					.getActivitiesOfConference(conferenceId));
			this.conferenceService.delete(conference);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
		}
		return result;
	}

	/* Review submissions */
	@RequestMapping(value = "/review", method = RequestMethod.GET)
	public ModelAndView reviewSubmissions(@RequestParam final int conferenceId) {
		ModelAndView result = new ModelAndView("redirect:list.do?catalog=5noti");

		try {
			this.conferenceService.reviewSubmissions(conferenceId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
		}
		return result;
	}

	/* Review submissions */
	@RequestMapping(value = "/notify", method = RequestMethod.GET)
	public ModelAndView notifySubmissions(@RequestParam final int conferenceId) {
		ModelAndView result = new ModelAndView("redirect:list.do?catalog=5noti");

		try {
			this.conferenceService.notifySubmissions(conferenceId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
		}
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final Conference conference) {
		return this.createEditModelAndView(conference, null);
	}

	protected ModelAndView createEditModelAndView(final Conference conference,
			final String messageCode) {
		ModelAndView result = new ModelAndView("conference/edit");

		result.addObject("conference", conference);
		result.addObject("categories", this.categoryService.findAll());

		if (conference.getCategory() != null)
			result.addObject("catId", conference.getCategory().getId());

		result.addObject("errMsg", messageCode);

		return result;
	}
}
