
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
			principal = this.utilityService.findByPrincipal();
			if (submission.getAuthor().equals((Author) principal) || 
					this.utilityService.checkAuthority(principal, "REVIEWER")) {
					isPrincipal = true;
			}
			result = new ModelAndView("submission/display");
			result.addObject("submission", submission);
			result.addObject("isPrincipal", isPrincipal);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
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
			
			if (this.utilityService.checkAuthority(principal, "REVIEWER")) {
				submissions = this.submissionService.submissionsPerReviewer(principal.getId());
				isPrincipal = "REVIEWER";
			}
			
			if (this.utilityService.checkAuthority(principal, "ADMIN")) {
				submissions = this.submissionService.submissionsToAssign();
				isPrincipal = "ADMIN";
			}
						
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
		ModelAndView result = null;
		try {
			Conference conference = this.conferenceService.findOne(conferenceId);

			SubmissionForm submissionForm = new SubmissionForm();
			submissionForm.setConference(conference);
			
			result = this.createEditModelAndView(submissionForm);
			result.addObject("isPrincipal", true);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");

			result.addObject("errMsg", oops.getMessage());
		}

		return result;
	}
	
	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int submissionId) {
		ModelAndView result;
		Submission submission;
		Actor principal = this.utilityService.findByPrincipal();
		boolean isPrincipal = false;
		
		try {
			submission = this.submissionService.findOne(submissionId);
			
			if(submission.getAuthor().equals((Author) principal)) {
				isPrincipal = true;
			}
			final SubmissionForm submissionForm = new SubmissionForm(submission);

			result = this.createEditModelAndView(submissionForm);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("cameraReady", true);
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");

			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}
	
	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(SubmissionForm submissionForm, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:list.do");
		try {
			Submission submission = this.submissionService.reconstruct(submissionForm, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(submissionForm);
				result.addObject("isPrincipal", true);
			}
			else {
				this.submissionService.save(submission);
			}

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");

			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}
	
	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int submissionId) {
		ModelAndView result = new ModelAndView("redirect:list.do");
		try {
			final Submission submission = this.submissionService.findOne(submissionId);
			this.submissionService.delete(submission);
			
		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final SubmissionForm submissionForm) {
		return this.createEditModelAndView(submissionForm, null);
	}

	protected ModelAndView createEditModelAndView(final SubmissionForm submissionForm, final String messageCode) {
		ModelAndView result;
		Collection<Conference> conferences = this.conferenceService.publishedConferences();
		
		result = new ModelAndView("submission/edit");
		result.addObject("submissionForm", submissionForm);
		result.addObject("errMsg", messageCode);
		result.addObject("conferences", conferences);

		return result;
	}

}
