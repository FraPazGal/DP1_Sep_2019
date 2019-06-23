
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

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

	@Autowired
	private UtilityService	utilityService;

	@Autowired
	private SponsorshipService		sponsorshipService;
	
	@Autowired
	private ConferenceService		conferenceService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;
		boolean isPrincipal = false;
		Actor principal;

		try {
			sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			try {
				principal = this.utilityService.findByPrincipal();
				if (sponsorship.getSponsor().equals((Sponsor) principal)) {
						isPrincipal = true;
				}
			} catch (final Throwable oops) {
				System.out.println(oops.getMessage());
			}

			result = new ModelAndView("sponsorship/display");
			result.addObject("sponsorship", sponsorship);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "sponsorship/display.do?sponsorshipId=" + sponsorshipId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do/");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("sponsorship/list");
		Collection<Sponsorship> sponsorships = new ArrayList<>();
		Actor principal = null;
		String isPrincipal = null;

		try {
			principal = this.utilityService.findByPrincipal();
			if (this.utilityService.checkAuthority(principal, "SPONSOR")) {
				sponsorships = this.sponsorshipService.sponsorshipsPerSponsor(principal.getId());
				isPrincipal = "SPONSOR";
			}
						
			result.addObject("sponsorships", sponsorships);
			result.addObject("isPrincipal", isPrincipal);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops);
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createSponsorship() {
		ModelAndView res;

		final SponsorshipForm sponsorshipForm = new SponsorshipForm();

		res = this.createEditModelAndView(sponsorshipForm);
		res.addObject("isPrincipal", true);

		return res;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView res;
		Sponsorship sponsorship;
		Actor principal = this.utilityService.findByPrincipal();
		boolean isPrincipal = false;
		
		try {
			sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			
			if(sponsorship.getSponsor().equals((Sponsor) principal)) {
				isPrincipal = true;
			}

			final SponsorshipForm editSponsorshipFormObject = new SponsorshipForm(sponsorship);

			res = this.createEditModelAndView(editSponsorshipFormObject);
			res.addObject("isPrincipal", isPrincipal);
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:list.do");
		}

		return res;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final SponsorshipForm editSponsorshipFormObject, BindingResult binding) {
		Actor principal;
		ModelAndView res;
		boolean isPrincipal = false;

		try {
			principal = this.utilityService.findByPrincipal();
			Sponsorship sponsorship = new Sponsorship();
			sponsorship = this.sponsorshipService.create();

			sponsorship = this.sponsorshipService.reconstruct(editSponsorshipFormObject, binding);
			
			if (binding.hasErrors()) {
				res = this.createEditModelAndView(editSponsorshipFormObject);
				res.addObject("isPrincipal", true);
			}
			else
				try {
					Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) principal), "not.allowed");

					this.sponsorshipService.save(sponsorship);

					res = new ModelAndView("redirect:list.do");

				} catch (final Throwable oops) {
					if(sponsorship.getSponsor().equals((Sponsor)principal)) {
						isPrincipal = true;
					}
					res = this.createEditModelAndView(editSponsorshipFormObject, "sponsorship.commit.error");
					res.addObject("isPrincipal", isPrincipal);

				}
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:list.do");
		}
		return res;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		try {
			final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			this.sponsorshipService.delete(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", oops.getMessage());
		}
		return result;
	}

	// Ancillary methods
	
	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorshipForm) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorshipForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorshipForm, final String messageCode) {
		ModelAndView result;
		Collection<Conference> conferences = this.conferenceService.publishedConferences();
		
		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorshipForm", sponsorshipForm);
		result.addObject("message", messageCode);
		result.addObject("conferences", conferences);

		return result;
	}

}
