
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
import services.RegistrationService;
import services.UtilityService;
import domain.Actor;
import domain.Author;
import domain.Registration;
import forms.RegistrationForm;

@Controller
@RequestMapping("/registration")
public class RegistrationController extends AbstractController {

	/* Services */
	
	@Autowired
	private UtilityService	utilityService;

	@Autowired
	private RegistrationService		registrationService;
	
	@Autowired
	private ConferenceService		conferenceService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int registrationId) {
		ModelAndView result;
		Registration registration;
		Actor principal;

		try {
			registration = this.registrationService.findOne(registrationId);
			principal = this.utilityService.findByPrincipal();
			if (! registration.getAuthor().equals((Author) principal)) {
				result = new ModelAndView("redirect:../misc/403.do");
			} else {
				result = new ModelAndView("registration/display");
				result.addObject("registration", registration);
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("registration/list");

			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = new ModelAndView("registration/list");
		Collection<Registration> registrations = new ArrayList<>();

		try {
			Actor principal = this.utilityService.findByPrincipal();
			if (this.utilityService.checkAuthority(principal, "AUTHOR")) {
				registrations = this.registrationService.registrationsPerAuthor(principal.getId());
			}
			result.addObject("registrations", registrations);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createRegistration(@RequestParam final int conferenceId) {
		ModelAndView result;

		try {
			final RegistrationForm registrationForm = new RegistrationForm();
			registrationForm.setConference(this.conferenceService.findOne(conferenceId));

			result = this.createEditModelAndView(registrationForm);
		} catch (Throwable oops) {
			result = new ModelAndView("registration/list");

			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}
	
	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int registrationId) {
		ModelAndView result;
		Registration registration;
		
		try {
			Actor principal = this.utilityService.findByPrincipal();
			registration = this.registrationService.findOne(registrationId);
			
			if (! registration.getAuthor().equals((Author) principal)) {
				result = new ModelAndView("redirect:../misc/403.do");
			} else {
				final RegistrationForm editRegistrationFormObject = new RegistrationForm(registration);

				result = this.createEditModelAndView(editRegistrationFormObject);
			}
			
		} catch (final Throwable oops) {
			result = new ModelAndView("registration/list");

			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}
	
	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(RegistrationForm registrationForm, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:list.do");

		try {
			Registration registration = this.registrationService.reconstruct(registrationForm, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(registrationForm);
				
			} else {
				this.registrationService.save(registration);
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(registrationForm, oops.getMessage());
		}
		return result;
	}
	
	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final RegistrationForm registrationForm) {
		ModelAndView result;

		result = this.createEditModelAndView(registrationForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RegistrationForm registrationForm, final String messageCode) {
		ModelAndView result;
		
		result = new ModelAndView("registration/edit");
		result.addObject("registrationForm", registrationForm);
		result.addObject("errMsg", messageCode);

		return result;
	}
}
