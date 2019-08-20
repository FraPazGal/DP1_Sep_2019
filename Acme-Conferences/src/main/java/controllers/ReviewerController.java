package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FinderService;
import services.ReviewerService;
import services.UtilityService;
import domain.Actor;
import domain.Finder;
import domain.Reviewer;
import forms.ReviewerForm;
import forms.ReviewerRegistrationForm;

@Controller
@RequestMapping("/reviewer")
public class ReviewerController extends AbstractController {

	/* Services */

	@Autowired
	private ReviewerService reviewerService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private FinderService finderService;

	@Autowired
	private UtilityService utilityService;

	/* Methods */

	/**
	 * 
	 * Display reviewer
	 * 
	 * @params id (optional)
	 * 
	 * @return ModelAndView
	 * **/
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView res;
		Reviewer toDisplay;
		String requestURI = "reviewer/display.do";
		Boolean found = true;
		Boolean permission;

		try {
			if (id != null) {
				toDisplay = (Reviewer) this.actorService.findOne(id);
				requestURI += "?id=" + id;
				if (toDisplay == null)
					found = false;
				permission = (toDisplay.getId() == this.utilityService
						.findByPrincipal().getId()) ? true : false;
			} else {
				toDisplay = (Reviewer) this.utilityService.findByPrincipal();
				permission = true;
			}

			res = new ModelAndView("reviewer/display");
			res.addObject("reviewer", toDisplay);
			res.addObject("found", found);
			res.addObject("requestURI", requestURI);
			res.addObject("permission", permission);
		} catch (final Throwable oops) {
			found = false;
			res = new ModelAndView("reviewer/display");
			res.addObject("found", found);
		}

		return res;
	}

	/**
	 * 
	 * Register reviewer GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerNewAdministrator() {
		ModelAndView res;

		final ReviewerRegistrationForm reviewerRegistrationForm = new ReviewerRegistrationForm();

		res = this.createRegisterModelAndView(reviewerRegistrationForm);

		return res;
	}

	/**
	 * 
	 * Register reviewer POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(
			final ReviewerRegistrationForm reviewerRegistrationForm,
			final BindingResult binding) {

		ModelAndView res;
		Finder finder;
		Reviewer saved;

		try {

			Reviewer reviewer = this.reviewerService.reconstruct(
					reviewerRegistrationForm, binding);

			if (binding.hasErrors()) {

				res = new ModelAndView("reviewer/register");
				res.addObject("registerFormObject", reviewerRegistrationForm);
				res.addObject("binding", binding);

			} else {
				try {

					saved = this.reviewerService.save(reviewer);

					finder = this.finderService.create(saved);
					this.finderService.save(finder);

					res = new ModelAndView("redirect:/");

				} catch (final Throwable oops) {
					res = this.createRegisterModelAndView(
							reviewerRegistrationForm, "reviewer.commit.error");

				}
			}

		} catch (Throwable oops) {
			res = new ModelAndView("redirect:/");
		}

		return res;
	}

	/**
	 * 
	 * Edit reviewer GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/reviewer/edit", method = RequestMethod.GET)
	public ModelAndView editAdministrator() {
		ModelAndView res;
		Actor principal;

		principal = this.utilityService.findByPrincipal();
		final ReviewerForm editionFormObject = new ReviewerForm(
				(Reviewer) principal);

		res = this.createEditModelAndView(editionFormObject);

		return res;
	}

	/**
	 * 
	 * Edit reviewer POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/reviewer/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final ReviewerForm reviewerForm,
			final BindingResult binding) {

		ModelAndView res;

		try {

			Assert.isTrue(this.utilityService.findByPrincipal().getId() == reviewerForm
					.getId()
					&& this.actorService.findOne(this.utilityService
							.findByPrincipal().getId()) != null);

			Reviewer reviewer = this.reviewerService.reconstruct(reviewerForm,
					binding);

			if (binding.hasErrors()) {

				res = new ModelAndView("reviewer/edit");
				res.addObject("editionFormObject", reviewerForm);
				res.addObject("binding", binding);

			} else {
				try {
					this.reviewerService.save(reviewer);

					res = new ModelAndView("redirect:/");

				} catch (Throwable oops) {
					res = this.createEditModelAndView(reviewerForm,
							"reviewer.commit.error");

				}

			}

		} catch (Throwable oops) {
			res = new ModelAndView("redirect:/");
		}

		return res;
	}

	/* Auxiliary methods */

	/* Registration related */
	protected ModelAndView createRegisterModelAndView(
			final ReviewerRegistrationForm reviewerRegistrationForm) {
		ModelAndView result;

		result = this
				.createRegisterModelAndView(reviewerRegistrationForm, null);

		return result;
	}

	protected ModelAndView createRegisterModelAndView(
			final ReviewerRegistrationForm reviewerRegistrationForm,
			final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("reviewer/register");
		result.addObject("registerFormObject", reviewerRegistrationForm);
		result.addObject("message", messageCode);

		return result;
	}

	/* Edition related */
	protected ModelAndView createEditModelAndView(
			final ReviewerForm reviewerForm) {
		ModelAndView result;

		result = this.createEditModelAndView(reviewerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(
			final ReviewerForm reviewerForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("reviewer/edit");
		result.addObject("editionFormObject", reviewerForm);
		result.addObject("message", messageCode);

		return result;
	}

}