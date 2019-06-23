
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

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
import domain.Conference;
import domain.Registration;
import forms.RegistrationForm;

@Controller
@RequestMapping("/registration")
public class RegistrationController extends AbstractController {

	@Autowired
	private UtilityService	utilityService;

	@Autowired
	private RegistrationService		registrationService;
	
	@Autowired
	private ConferenceService		conferenceService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int registrationId) {
		ModelAndView result;
		Registration registration;
		boolean isPrincipal = false;
		Actor principal;

		try {
			registration = this.registrationService.findOne(registrationId);
			try {
				principal = this.utilityService.findByPrincipal();
				if (registration.getAuthor().equals((Author) principal)) {
						isPrincipal = true;
				}
			} catch (final Throwable oops) {
				System.out.println(oops.getMessage());
			}

			result = new ModelAndView("registration/display");
			result.addObject("registration", registration);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "registration/display.do?registrationId=" + registrationId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do/");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("registration/list");
		Collection<Registration> registrations = new ArrayList<>();
		Actor principal = null;
		String isPrincipal = null;

		try {
			principal = this.utilityService.findByPrincipal();
			if (this.utilityService.checkAuthority(principal, "AUTHOR")) {
				registrations = this.registrationService.registrationsPerAuthor(principal.getId());
				isPrincipal = "AUTHOR";
			}
						
			result.addObject("registrations", registrations);
			result.addObject("isPrincipal", isPrincipal);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops);
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createRegistration(@RequestParam final int conferenceId) {
		ModelAndView res;

		final RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.setConference(this.conferenceService.findOne(conferenceId));

		res = this.createEditModelAndView(registrationForm);
		res.addObject("isPrincipal", true);

		return res;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int registrationId) {
		ModelAndView res;
		Registration registration;
		Actor principal = this.utilityService.findByPrincipal();
		boolean isPrincipal = false;
		
		try {
			registration = this.registrationService.findOne(registrationId);
			
			if(registration.getAuthor().equals((Author) principal)) {
				isPrincipal = true;
			}

			final RegistrationForm editRegistrationFormObject = new RegistrationForm(registration);

			res = this.createEditModelAndView(editRegistrationFormObject);
			res.addObject("isPrincipal", isPrincipal);
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:list.do");
		}

		return res;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final RegistrationForm registrationForm, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:list.do");
		boolean isPrincipal = true;

		try {
			Registration registration = this.registrationService.reconstruct(registrationForm, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(registrationForm);
				result.addObject("isPrincipal", true);
				
			} else {
				this.registrationService.save(registration);
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(registrationForm, oops.getMessage());
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}
	
	// Ancillary methods
	
	protected ModelAndView createEditModelAndView(final RegistrationForm registrationForm) {
		ModelAndView result;

		result = this.createEditModelAndView(registrationForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RegistrationForm registrationForm, final String messageCode) {
		ModelAndView result;
		Collection<Conference> conferences = this.conferenceService.publishedConferences();
		
		result = new ModelAndView("registration/edit");
		result.addObject("registrationForm", registrationForm);
		result.addObject("message", messageCode);
		result.addObject("conferences", conferences);

		return result;
	}

}
