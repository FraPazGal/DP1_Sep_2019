package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConferenceService;
import services.ReviewService;
import services.SubmissionService;
import services.UtilityService;
import domain.Actor;
import domain.Author;
import domain.Conference;
import domain.Submission;
import forms.SubmissionForm;

@Controller
@RequestMapping("/submission")
public class SubmissionController extends AbstractController {

	/* Services */

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private SubmissionService submissionService;

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private ReviewService reviewService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(
			@RequestParam(required = false) final Integer submissionId) {
		ModelAndView result = new ModelAndView("submission/display");
		Submission submission;
		boolean isAssigned = false;
		Actor principal;

		try {
			Assert.notNull(submissionId);
			submission = this.submissionService.findOne(submissionId);
			principal = this.utilityService.findByPrincipal();

			if (this.utilityService.checkAuthority(principal, "REVIEWER")) {
				Assert.isTrue(this.reviewService.checkIfAssigned(submissionId));
			} else if (this.utilityService.checkAuthority(principal, "ADMIN")) {
				isAssigned = this.reviewService.isAssigned(submissionId);
			} else if (this.utilityService.checkAuthority(principal, "AUTHOR")) {
				Assert.isTrue(submission.getAuthor().getId() == principal
						.getId());
			}
			result.addObject("submission", submission);
			result.addObject("isAssigned", isAssigned);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(required = false) final String catalog) {
		ModelAndView result = new ModelAndView("submission/list");
		Collection<Submission> submissions = null;
		String isPrincipal = null;

		try {
			Actor principal = this.utilityService.findByPrincipal();

			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN")
					|| this.utilityService.checkAuthority(principal, "AUTHOR"));

			if (this.utilityService.checkAuthority(principal, "AUTHOR")) {
				submissions = this.submissionService
						.submissionsPerAuthor(principal.getId());
				isPrincipal = "AUTHOR";

			} else if (this.utilityService.checkAuthority(principal, "ADMIN")) {
				isPrincipal = "ADMIN";

				switch (catalog) {
				case "underR":
					submissions = this.submissionService
							.underReviewSubmissions();
					break;

				case "accepted":
					submissions = this.submissionService.acceptedSubmissions();
					break;

				case "rejected":
					submissions = this.submissionService.rejectedSubmissions();
					break;
				}
				result.addObject("listSub", true);

			}
			Assert.notNull(submissions, "not.allowed");

			result.addObject("submissions", submissions);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("catalog", catalog);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}

		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createSubmission(
			@RequestParam(required = false) final Integer conferenceId) {
		ModelAndView result;
		try {
			Assert.notNull(conferenceId);
			Conference conference = this.conferenceService
					.findOne(conferenceId);

			Assert.isTrue(
					this.submissionService.noPreviousSubmissions(conference),
					"not.allowed");

			SubmissionForm submissionForm = new SubmissionForm();
			submissionForm.setConference(conference);

			result = this.createEditModelAndView(submissionForm);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}

		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(required = false) final Integer submissionId) {
		ModelAndView result;
		Actor principal = this.utilityService.findByPrincipal();

		try {
			Assert.notNull(submissionId);
			Submission submission = this.submissionService
					.findOne(submissionId);

			Assert.isTrue(submission.getAuthor().equals((Author) principal));
			final SubmissionForm submissionForm = new SubmissionForm(submission);

			result = this.createEditModelAndView(submissionForm);
			result.addObject("cameraReady", true);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(SubmissionForm submissionForm,
			BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:list.do");
		try {
			Submission submission = this.submissionService.reconstruct(
					submissionForm, binding);

			if (binding.hasErrors()) {
				result = this.createEditModelAndView(submissionForm);
				if (submission.getId() != 0) {
					result.addObject("cameraReady", true);
				}
			} else {
				this.submissionService.save(submission);
			}

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(
			@RequestParam(required = false) final Integer submissionId) {
		ModelAndView result = new ModelAndView("redirect:list.do");
		try {
			Assert.notNull(submissionId);
			final Submission submission = this.submissionService
					.findOne(submissionId);

			this.submissionService.delete(submission);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(
			final SubmissionForm submissionForm) {
		return this.createEditModelAndView(submissionForm, null);
	}

	protected ModelAndView createEditModelAndView(
			final SubmissionForm submissionForm, final String messageCode) {
		ModelAndView result = new ModelAndView("submission/edit");

		result.addObject("submissionForm", submissionForm);
		result.addObject("errMsg", messageCode);
		result.addObject("conferences",
				this.conferenceService.publishedConferences());

		return result;
	}

}
