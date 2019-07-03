
package controllers;

import java.util.ArrayList;
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
	private UtilityService	utilityService;

	@Autowired
	private SubmissionService		submissionService;
	
	@Autowired
	private ConferenceService		conferenceService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int submissionId) {
		ModelAndView result;
		Submission submission;
		boolean isPrincipal = false;
		Actor principal;

		try {
			submission = this.submissionService.findOne(submissionId);
			try {
				principal = this.utilityService.findByPrincipal();
				if (submission.getAuthor().equals((Author) principal)) {
						isPrincipal = true;
				}
			} catch (final Throwable oops) {
				System.out.println(oops.getMessage());
			}

			result = new ModelAndView("submission/display");
			result.addObject("submission", submission);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "submission/display.do?submissionId=" + submissionId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do/");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("submission/list");
		Collection<Submission> submissions = new ArrayList<>();
		Actor principal = null;
		String isPrincipal = null;

		try {
			principal = this.utilityService.findByPrincipal();
			if (this.utilityService.checkAuthority(principal, "AUTHOR")) {
				submissions = this.submissionService.submissionsPerAuthor(principal.getId());
				isPrincipal = "AUTHOR";
			}
			
			
			//TODO: listado de submissions para un reviewer
			
//			if (this.utilityService.checkAuthority(principal, "REVIEWER")) {
//				submissions = this.submissionService.submissionsPerReviewer(principal.getId());
//				isPrincipal = "REVIEWER";
//			}
						
			result.addObject("submissions", submissions);
			result.addObject("isPrincipal", isPrincipal);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops);
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createSubmission(@RequestParam final int conferenceId) {
		ModelAndView res;
		Conference conference = this.conferenceService.findOne(conferenceId);

		final SubmissionForm submissionForm = new SubmissionForm();
		submissionForm.setConference(conference);

		res = this.createEditModelAndView(submissionForm);
		res.addObject("isPrincipal", true);

		return res;
	}
	
	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int submissionId) {
		ModelAndView res;
		Submission submission;
		Actor principal = this.utilityService.findByPrincipal();
		boolean isPrincipal = false;
		
		try {
			submission = this.submissionService.findOne(submissionId);
			
			if(submission.getAuthor().equals((Author) principal)) {
				isPrincipal = true;
			}

			final SubmissionForm submissionForm = new SubmissionForm(submission);

			res = this.createEditModelAndView(submissionForm);
			res.addObject("isPrincipal", isPrincipal);
			res.addObject("cameraReady", true);
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:list.do");
		}

		return res;
	}
	
	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(SubmissionForm submissionForm, BindingResult binding) {
		Actor principal;
		ModelAndView res;
		boolean isPrincipal = false;

		try {
			principal = this.utilityService.findByPrincipal();
			Submission submission = this.submissionService.reconstruct(submissionForm, binding);
			
			if (binding.hasErrors()) {
				res = this.createEditModelAndView(submissionForm);
				res.addObject("isPrincipal", true);
			}
			else
				try {
					Assert.isTrue(submission.getAuthor().equals((Author) principal), "not.allowed");

					this.submissionService.save(submission);

					res = new ModelAndView("redirect:list.do");

				} catch (final Throwable oops) {
					if(submission.getAuthor().equals((Author)principal)) {
						isPrincipal = true;
					}
					res = this.createEditModelAndView(submissionForm, "submission.commit.error");
					res.addObject("isPrincipal", isPrincipal);

				}
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:list.do");
		}
		return res;
	}
	
	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int submissionId) {
		ModelAndView result;
		try {
			final Submission submission = this.submissionService.findOne(submissionId);
			this.submissionService.delete(submission);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", oops.getMessage());
		}
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final SubmissionForm submissionForm) {
		ModelAndView result;

		result = this.createEditModelAndView(submissionForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SubmissionForm submissionForm, final String messageCode) {
		ModelAndView result;
		Collection<Conference> conferences = this.conferenceService.publishedConferences();
		
		result = new ModelAndView("submission/edit");
		result.addObject("submissionForm", submissionForm);
		result.addObject("message", messageCode);
		result.addObject("conferences", conferences);

		return result;
	}

}
