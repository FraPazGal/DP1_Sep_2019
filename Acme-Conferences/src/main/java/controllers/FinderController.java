package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.FinderService;
import services.SystemConfigurationService;
import services.UtilityService;
import domain.Actor;
import domain.Conference;
import domain.Finder;

@Controller
@RequestMapping("/finder")
public class FinderController extends AbstractController {

	// Services

	@Autowired
	private FinderService finderService;

	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// Constructors

	public FinderController() {
		super();
	}

	// DELETE
	@RequestMapping(value = "/search", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Finder finder, final BindingResult binding) {

		ModelAndView result;
		try {

			this.finderService.delete(finder);
			result = new ModelAndView("redirect:search.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(finder, "finder.commit.error");
		}

		return result;
	}

	// search
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result;
		Finder finder;
		Actor principal;
		Date maxLivedMoment = new Date();
		boolean isPrincipal = false;
		
		try {
			principal = this.utilityService.findByPrincipal();
			finder = this.finderService.findFinderByActorId(principal.getId());
			
			isPrincipal = true;
			
			if (finder.getSearchMoment() != null) {
				final int timeChachedFind = this.systemConfigurationService.findMySystemConfiguration().getTimeResultsCached();
				maxLivedMoment = DateUtils.addHours(maxLivedMoment, -timeChachedFind);

				if (finder.getSearchMoment().before(maxLivedMoment))
					this.finderService.deleteExpiredFinder(finder);
			}
			result = new ModelAndView("finder/search");
			result.addObject("finder", finder);

			result.addObject("conferences", finder.getResults());
			result.addObject("categories", this.categoryService.findAll());
			
		} catch (Throwable oops) {
			result = new ModelAndView("finder/search");
		}
		result.addObject("requestUri", "finder/search.do");
		result.addObject("isPrincipal", isPrincipal);

		return result;
	}
	
	// searchAnon
	@RequestMapping(value = "/anon/search", method = RequestMethod.GET)
	public ModelAndView searchAnon(@RequestParam (required = false) String keyWord) {
		ModelAndView result;
		Collection<Conference> conferences = new ArrayList<>();
		
		try {
			result = new ModelAndView("finder/anon/search");

			conferences = this.finderService.searchAnon(keyWord);
			
			result.addObject("conferences", conferences);

			result.addObject("requestUri", "finder/anon/search.do");
		} catch (Throwable oopsie) {
			result = new ModelAndView("conferences/list?catalog=unpublished");

			result.addObject("errMsg", oopsie);
		}
		return result;
		
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, params = "save")
	public ModelAndView search(final Finder finder, BindingResult binding) {
		ModelAndView result;
		Finder aux;
		
		try {
			aux = this.finderService.reconstruct(finder, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(finder);

			} else {
				try {

					this.finderService.search(aux);
					result = new ModelAndView(
							"redirect:/finder/search.do");

				} catch (final Throwable oops) {
					result = this.createEditModelAndView(finder,
							"finder.commit.error");
				}
			}
		} catch (Exception e) {
			result = new ModelAndView(
					"redirect:/finder/search.do");
		}
		result.addObject("categories", this.categoryService.findAll());
		
		return result;
	}

	// ancillary methods

	protected ModelAndView createEditModelAndView(final Finder finder) {
		ModelAndView result;

		result = this.createEditModelAndView(finder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder,
			final String messageCode) {
		ModelAndView result;
		final Collection<Conference> conferences;
		conferences = finder.getResults();

		result = new ModelAndView("finder/search");
		result.addObject("message", messageCode);
		result.addObject("finder", finder);
		result.addObject("conferences", conferences);

		return result;
	}

}