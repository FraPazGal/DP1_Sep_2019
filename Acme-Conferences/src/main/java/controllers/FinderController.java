package controllers;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.FinderService;
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
	private SystemConfigurationService systemConfigurationService;

	// Constructors

	public FinderController() {
		super();
	}

	// DELETE
	@RequestMapping(value = "/filmEnthusiast/search", method = RequestMethod.POST, params = "delete")
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

		principal = this.utilityService.findByPrincipal();
		finder = this.finderService.findFinderByActorId(principal.getId());
		 
		 Date maxLivedMoment = new Date();
		 
			if (finder.getSearchMoment() != null) {
				final int timeChachedFind = this.systemConfigurationService.findMySystemConfiguration().getTimeResultsCached();
				maxLivedMoment = DateUtils.addHours(maxLivedMoment, -timeChachedFind);

				if (finder.getSearchMoment().before(maxLivedMoment))
					this.finderService.deleteExpiredFinder(finder);
			}
		result = new ModelAndView("finder/search");
		result.addObject("finder", finder);

		result.addObject("conferences", finder.getResults());

		result.addObject("requestUri", "finder/search.do");
		return result;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, params = "save")
	public ModelAndView search(@Valid final Finder finder,
			final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			final List<ObjectError> errors = binding.getAllErrors();
			for (final ObjectError e : errors)
				System.out.println(e.toString());
			result = this.createEditModelAndView(finder);

		} else {
			try {

				this.finderService.search(finder);
				result = new ModelAndView(
						"redirect:/finder/search.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(finder,
						"finder.commit.error");
			}
		}
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