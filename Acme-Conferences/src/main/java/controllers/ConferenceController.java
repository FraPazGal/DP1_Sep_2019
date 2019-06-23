
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ConferenceService;
import services.SponsorshipService;
import services.UtilityService;
import domain.Actor;
import domain.Administrator;
import domain.Category;
import domain.Conference;
import domain.Sponsorship;

@Controller
@RequestMapping("/conference")
public class ConferenceController extends AbstractController {

	@Autowired
	private UtilityService	utilityService;

	@Autowired
	private ConferenceService		conferenceService;
	
	@Autowired
	private SponsorshipService		sponsorshipService;
	
	@Autowired
	private CategoryService		categoryService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int conferenceId) {
		ModelAndView result;
		Conference conference;
		boolean isPrincipal = false;
		Actor principal;
		Sponsorship spoBanner = null;
		Category category = new Category();
		Map<String,String> titleCat = new HashMap<>();
		
		try {
			conference = this.conferenceService.findOne(conferenceId);
			spoBanner = this.sponsorshipService.findBanner(conferenceId);
			category = this.categoryService.findOneByConferenceId(conferenceId);
			titleCat.putAll(category.getTitle());
			try {
				principal = this.utilityService.findByPrincipal();
				if (this.utilityService.checkAuthority(principal, "ADMIN"))
					isPrincipal = true;
				
			} catch (final Throwable oops) {}

			result = new ModelAndView("conference/display");
			result.addObject("conference", conference);
			result.addObject("spoBanner", spoBanner);
			result.addObject("titleCat", titleCat);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "conference/display.do?conferenceId=" + conferenceId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "conference.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final String catalog) {
		final ModelAndView result = new ModelAndView("conference/list");
		Collection<Conference> conferences  = new ArrayList<>();
		Collection<Conference> conferencesRegisteredTo = new ArrayList<>();
		Actor principal = null;
		String isPrincipal = null;

		try {
			switch (catalog) {
	        	case "future":
	        		conferences = this.conferenceService.futureConferences();
	        		break;
	             
	        	case "past":
	        		conferences = this.conferenceService.pastConferences();
	        		break;
	             
	        	case "running":
	        		conferences = this.conferenceService.runningConferences();
	        		break;
			}
			try {
				principal = this.utilityService.findByPrincipal();
				if (this.utilityService.checkAuthority(principal, "ADMIN")) {
					isPrincipal = "ADMIN";
					
					if(conferences.isEmpty()) {
						switch (catalog) {
				        	case "unpublished":
				        		conferences = this.conferenceService.findConferencesUnpublishedAndMine(principal.getId());
				        		break;
				             
				        	case "5sub":
				        		conferences = this.conferenceService.findSubmissionLastFive();
				        		break;
				             
				        	case "5not":
				        		conferences = this.conferenceService.findNotificationInFive();
				        		break;
				             
				        	case "5cam":
				        		conferences = this.conferenceService.findCameraInFive();
				        		break;
						}
					}
				} else if (this.utilityService.checkAuthority(principal, "AUTHOR")) {
					isPrincipal = "AUTHOR";
					conferencesRegisteredTo = this.conferenceService.conferencesRegisteredTo(principal.getId());
				}
					
			} catch (Exception e) {}
						
			result.addObject("conferences", conferences);
			result.addObject("conferencesRegisteredTo", conferencesRegisteredTo);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("catalog", catalog);
			result.addObject("listConf", true);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops);
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = null;
		try {
			final Conference conference = this.conferenceService.create();

			result = this.createEditModelAndView(conference);
		} catch (final Throwable oops) {
			System.out.println(oops.getMessage());
		}
		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int conferenceId) {
		ModelAndView result;
		Conference conference;
		Actor principal = null;
		boolean isPrincipal = false;
		
		try {
			conference = this.conferenceService.findOne(conferenceId);
			Assert.notNull(conference);
			
			try {
				principal = this.utilityService.findByPrincipal();
				if(conference.getAdministrator().equals((Administrator) principal) || 
						(!conference.getIsFinal() && this.utilityService.checkAuthority(principal, "ADMIN"))) {
					isPrincipal = true;
				}
			} catch (Exception e) {}
			
			result = this.createEditModelAndView(conference);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("conferenceId", conferenceId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Conference conference, BindingResult binding) {
		ModelAndView result;
		Conference toSave = this.conferenceService.create();
		Actor principal = null;
		boolean isPrincipal = false;
		
		try {
			principal = this.utilityService.findByPrincipal();
			if(this.utilityService.checkAuthority(principal, "ADMIN")) {
				isPrincipal = true;
			}
			
			toSave = this.conferenceService.reconstruct(conference, binding);
			if (binding.hasErrors()) {
				
				conference.setIsFinal(false);

				result = new ModelAndView("conference/edit");
				result.addObject("conference", conference);
				result.addObject("binding", binding);
				result.addObject("isPrincipal", isPrincipal);
				result.addObject("categories", this.categoryService.findAll());
			} else
				try {		
					this.conferenceService.save(toSave);
					result = new ModelAndView("redirect:list.do?catalog=unpublished");
					
				} catch (final Throwable oops) {
					result = new ModelAndView("conference/edit");
					result.addObject("conference", toSave);
					result.addObject("messageCode", oops.getMessage());
				}
		} catch (final Throwable oops) {
			isPrincipal = true;
			result = this.createEditModelAndView(conference);
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(final Conference conference, final BindingResult binding) {
		ModelAndView result;
		Conference toSave = this.conferenceService.create();
		Actor principal = null;
		boolean isPrincipal = false;
		
		try {
			principal = this.utilityService.findByPrincipal();
			if(this.utilityService.checkAuthority(principal, "ADMIN")) {
				isPrincipal = true;
			}
			
			toSave = this.conferenceService.reconstruct(conference, binding);
			if (binding.hasErrors()) {
				
				conference.setIsFinal(false);

				result = new ModelAndView("conference/edit");
				result.addObject("conference", conference);
				result.addObject("binding", binding);
				result.addObject("isPrincipal", isPrincipal);
				result.addObject("categories", this.categoryService.findAll());
			} else
				try {
					toSave.setIsFinal(true);
					this.conferenceService.save(toSave);
					result = new ModelAndView("redirect:list.do?catalog=future");
					
				} catch (final Throwable oops) {
					result = new ModelAndView("conference/edit");
					result.addObject("conference", toSave);
					result.addObject("messageCode", oops.getMessage());
				}
		} catch (final Throwable oops) {
			isPrincipal = true;
			result = this.createEditModelAndView(conference);
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int conferenceId) {
		ModelAndView result;
		try {
			final Conference conference = this.conferenceService.findOne(conferenceId);
			this.conferenceService.delete(conference);
			result = new ModelAndView("redirect:list.do?catalog=future");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do?catalog=future");
			result.addObject("messageCode", oops.getMessage());
		}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Conference conference) {
		ModelAndView result;

		result = this.createEditModelAndView(conference, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Conference conference, final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = true;
		Collection<Category> categories = this.categoryService.findAll();

		if (messageCode == null) {
			principal = this.utilityService.findByPrincipal();

			if (!this.utilityService.checkAuthority(principal, "ADMIN"))
				isPrincipal = false;
		}

		result = new ModelAndView("conference/edit");
		result.addObject("conference", conference);
		result.addObject("categories", categories);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);

		return result;
	}
}
