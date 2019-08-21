
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConferenceService;
import services.RegistrationService;
import domain.Registration;
import forms.RegistrationForm;

@Controller
@RequestMapping("/registration")
public class RegistrationController extends AbstractController {

	/* Services */

	@Autowired
	private RegistrationService		registrationService;
	
	@Autowired
	private ConferenceService		conferenceService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int registrationId) {
		ModelAndView result = new ModelAndView("registration/display");
		try {
			result.addObject("registration", this.registrationService.findOneByAuthor(registrationId));
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = new ModelAndView("registration/list");
		try {
			result.addObject("registrations", this.registrationService.registrationsPerAuthor());

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
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
	
	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int registrationId) {
		ModelAndView result;
		try {
			Registration registration = this.registrationService.findOneByAuthor(registrationId);
			
			final RegistrationForm editRegistrationFormObject = new RegistrationForm(registration);

			result = this.createEditModelAndView(editRegistrationFormObject);
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
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
		return this.createEditModelAndView(registrationForm, null);
	}

	protected ModelAndView createEditModelAndView(final RegistrationForm registrationForm, final String messageCode) {
		ModelAndView result = new ModelAndView("registration/edit");
		
		result.addObject("registrationForm", registrationForm);
		result.addObject("errMsg", messageCode);

		return result;
	}
}
