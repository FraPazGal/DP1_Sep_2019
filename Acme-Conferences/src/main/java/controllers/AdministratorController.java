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
import services.AdministratorService;
import services.UtilityService;
import domain.Actor;
import domain.Administrator;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	/* Services */

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private UtilityService utilityService;

	/* Methods */

	/**
	 * 
	 * Display admin
	 * 
	 * @params id (optional)
	 * 
	 * @return ModelAndView
	 * **/
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView res;
		Administrator toDisplay;
		String requestURI = "administrator/display.do";
		Boolean found = true;
		Boolean permission;

		try {
			if (id != null) {
				toDisplay = (Administrator) this.actorService.findOne(id);
				requestURI += "?id=" + id;
				if (toDisplay == null)
					found = false;
				permission = (toDisplay.getId() == this.utilityService
						.findByPrincipal().getId()) ? true : false;
			} else {
				toDisplay = (Administrator) this.utilityService
						.findByPrincipal();
				permission = true;
			}

			res = new ModelAndView("administrator/display");
			res.addObject("admin", toDisplay);
			res.addObject("found", found);
			res.addObject("requestURI", requestURI);
			res.addObject("permission", permission);
		} catch (final Throwable oops) {
			found = false;
			res = new ModelAndView("administrator/display");
			res.addObject("found", found);
		}

		return res;
	}

	/**
	 * 
	 * Register administrator GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/register", method = RequestMethod.GET)
	public ModelAndView registerNewAdministrator() {
		ModelAndView res;

		final ActorRegistrationForm actorRegistrationForm = new ActorRegistrationForm();

		res = this.createRegisterModelAndView(actorRegistrationForm);

		return res;
	}

	/**
	 * 
	 * Register administrator POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(
			@Valid final ActorRegistrationForm actorRegistrationForm,
			final BindingResult binding) {

		ModelAndView res;

		Administrator administrator = new Administrator();
		administrator = this.administratorService.create();

		administrator = this.administratorService.reconstruct(
				actorRegistrationForm, binding);

		if (binding.hasErrors())
			res = this.createRegisterModelAndView(actorRegistrationForm);
		else
			try {

				this.administratorService.save(administrator);

				res = new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				res = this.createRegisterModelAndView(actorRegistrationForm,
						"administrator.commit.error");

			}
		return res;
	}

	/**
	 * 
	 * Edit administrator GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/edit", method = RequestMethod.GET)
	public ModelAndView editAdministrator() {
		ModelAndView res;
		Actor principal;

		principal = this.utilityService.findByPrincipal();
		final ActorForm editionFormObject = new ActorForm(principal);

		res = this.createEditModelAndView(editionFormObject);

		return res;
	}

	/**
	 * 
	 * Edit administrator POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final ActorForm actorForm,
			final BindingResult binding) {

		ModelAndView res;

		try {

			Assert.isTrue(this.utilityService.findByPrincipal().getId() == actorForm
					.getId()
					&& this.actorService.findOne(this.utilityService
							.findByPrincipal().getId()) != null);

			Administrator administrator = new Administrator();
			administrator = this.administratorService.create();

			administrator = this.administratorService.reconstruct(actorForm,
					binding);

			if (binding.hasErrors()) {
				res = this.createEditModelAndView(actorForm);
			} else {
				try {
					this.administratorService.save(administrator);

					res = new ModelAndView("redirect:/");

				} catch (Throwable oops) {
					res = this.createEditModelAndView(actorForm,
							"administrator.commit.error");

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
			final ActorRegistrationForm actorRegistrationForm) {
		ModelAndView result;

		result = this.createRegisterModelAndView(actorRegistrationForm, null);

		return result;
	}

	protected ModelAndView createRegisterModelAndView(
			final ActorRegistrationForm actorRegistrationForm,
			final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("administrator/register");
		result.addObject("registerFormObject", actorRegistrationForm);
		result.addObject("message", messageCode);

		return result;
	}

	/* Edition related */
	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm,
			final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("administrator/edit");
		result.addObject("editionFormObject", actorForm);
		result.addObject("message", messageCode);

		return result;
	}

}