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

import services.ActivityService;
import services.SectionService;
import services.UtilityService;
import domain.Activity;
import domain.Actor;
import domain.Section;

@Controller
@RequestMapping("/section")
public class SectionController extends AbstractController {

	@Autowired
	private SectionService sectionService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UtilityService utilityService;

	// Listing activities of a conference

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer activityid) {
		ModelAndView res;
		Collection<Section> sections;
		Actor principal;
		boolean permission = false;

		try {
			Assert.notNull(activityid);
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			permission = true;

			sections = this.sectionService.getSectionsOfActivity(activityid);
			sections = (sections == null) ? sections = new ArrayList<>()
					: sections;

			res = new ModelAndView("section/list");
			res.addObject("permission", permission);
			res.addObject("sections", sections);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	// Creating section

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(required = false) Integer activityid) {
		ModelAndView res;
		Section newSection;
		Activity a;
		Actor principal;
		boolean permission = false;

		try {
			Assert.notNull(activityid);
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			a = this.activityService.findOne(activityid);
			Assert.isTrue(a.getType().equalsIgnoreCase("TUTORIAL"));

			permission = true;

			newSection = this.sectionService.create(activityid);

			res = new ModelAndView("section/edit");
			res.addObject("section", newSection);
			res.addObject("permission", permission);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	// Editing section

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = false) Integer sectionid) {
		ModelAndView res;
		Section toEdit;
		Actor principal;
		boolean permission = false;

		try {
			Assert.notNull(sectionid);
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			permission = true;

			toEdit = this.sectionService.findOne(sectionid);

			res = new ModelAndView("section/edit");
			res.addObject("section", toEdit);
			res.addObject("permission", permission);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	// Saving section

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Section section, BindingResult binding) {
		ModelAndView res;
		Actor principal;

		try {
			if (binding.hasErrors()) {
				res = new ModelAndView("section/edit");
				res.addObject(section);
				res.addObject("permission", true);
			} else {
				principal = this.utilityService.findByPrincipal();
				Assert.isTrue(this.utilityService.checkAuthority(principal,
						"ADMIN"));

				this.sectionService.save(section);
				res = new ModelAndView(
						"redirect:/activity/display.do?activityid="
								+ section.getActivity().getId());
			}
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}

	// Deleting section

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Section section) {
		ModelAndView res;
		Actor principal;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService
					.checkAuthority(principal, "ADMIN"));

			this.sectionService.delete(section);
			res = new ModelAndView("redirect:/activity/display.do?activityid="
					+ section.getActivity().getId());
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}
		return res;
	}
}
