
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
import services.SponsorshipService;
import services.UtilityService;
import domain.Actor;
import domain.Conference;
import domain.Sponsor;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Controller
@RequestMapping("/sponsorship")
public class SponsorshipController extends AbstractController {

	/* Services */
	
	@Autowired
	private UtilityService	utilityService;

	@Autowired
	private SponsorshipService		sponsorshipService;
	
	@Autowired
	private ConferenceService		conferenceService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int sponsorshipId) {
		ModelAndView result = new ModelAndView("sponsorship/display");

		try {
			Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			Actor principal = this.utilityService.findByPrincipal();
			Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) principal), "not.allowed");

			result.addObject("sponsorship", sponsorship);
			//result.addObject("requestURI", "sponsorship/display.do?sponsorshipId=" + sponsorshipId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do/");
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = new ModelAndView("sponsorship/list");
		Collection<Sponsorship> sponsorships = new ArrayList<>();

		try {
			Actor principal = this.utilityService.findByPrincipal();
			if (this.utilityService.checkAuthority(principal, "SPONSOR")) {
				sponsorships = this.sponsorshipService.sponsorshipsPerSponsor(principal.getId());
			}
						
			result.addObject("sponsorships", sponsorships);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createSponsorship() {
		ModelAndView result;
		
		try {
			result = this.createEditModelAndView(new SponsorshipForm());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}

		return result;
	}
	
	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		
		try {
			Actor principal = this.utilityService.findByPrincipal();
			Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) principal), "not.allowed");
			
			result = this.createEditModelAndView(new SponsorshipForm(sponsorship));
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
	
	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(SponsorshipForm editSponsorshipFormObject, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:list.do");

		try {
			Sponsorship sponsorship = this.sponsorshipService.reconstruct(editSponsorshipFormObject, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(editSponsorshipFormObject);
			}
			else {
				this.sponsorshipService.save(sponsorship);
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		
		return result;
	}
	
	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int sponsorshipId) {
		ModelAndView result = new ModelAndView("redirect:list.do");
		try {
			final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			this.sponsorshipService.delete(sponsorship);
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorshipForm) {
		return this.createEditModelAndView(sponsorshipForm, null);
	}

	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorshipForm, final String messageCode) {
		ModelAndView result;
		Collection<Conference> conferences = this.conferenceService.publishedConferences();
		
		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorshipForm", sponsorshipForm);
		result.addObject("errMsg", messageCode);
		result.addObject("conferences", conferences);

		return result;
	}

}
