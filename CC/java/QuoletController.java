package controllers;

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
import services.QuoletService;
import services.UtilityService;
import domain.Actor;
import domain.Conference;
import domain.Quolet;

@Controller
@RequestMapping("/quolet")
public class QuoletController extends AbstractController {

	@Autowired
	private QuoletService quoletService;

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private UtilityService utilityService;

	/* Display */
	/* Asumo que el display es para todos los actores del sistema */

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(
			@RequestParam(value = "quoletid", required = true) Integer quoletid) {
		ModelAndView res;
		Quolet toDisplay;
		Boolean found;

		try {
			toDisplay = this.quoletService.findOne(quoletid);

			found = (toDisplay != null) ? true : false;

			res = new ModelAndView("quolet/display");
			res.addObject("quolet", toDisplay);
			res.addObject("found", found);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:/welcome/index.do");
		}

		return res;
	}

	/* List */
	/*
	 * Asumo que habrá distintas perspectivas para la misma vista, por lo que si
	 * el usuario es un admin sera el 'principal' y tendrá derecho a
	 * modificarlas
	 */

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "conferenceid", required = true) Integer conferenceid) {
		ModelAndView res;
		Actor principal;
		Conference conference;
		Collection<Quolet> quolets;
		Boolean isPrincipal;

		try {
			quolets = this.quoletService.findConferenceQuolets(conferenceid);
			principal = this.utilityService.findByPrincipal();
			conference = this.conferenceService.findOne(conferenceid);

			isPrincipal = conference.getAdministrator().getId() == principal
					.getId();

			res = new ModelAndView("quolet/list");
			res.addObject("quolets", quolets);
			res.addObject("isPrincipal", isPrincipal);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:/welcome/index.do");
		}

		return res;
	}

	/* Create */

	@RequestMapping(value = "/admin/create", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "conferenceid", required = true) Integer conferenceid) {
		ModelAndView res;
		Actor principal;
		Quolet quolet;

		try {
			principal = this.utilityService.findByPrincipal();
			Conference conference = this.conferenceService
					.findOne(conferenceid);

			Assert.notNull(conference, "null.conference");
			Assert.isTrue(
					conference.getAdministrator().getId() == principal.getId(),
					"not.allowed");

			quolet = new Quolet();

			res = this.createEditModelAndView(quolet);

		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:/welcome/index.do");
		}

		return res;
	}

	/* Edit */

	@RequestMapping(value = "/admin/edit", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "quoletid", required = true) Integer quoletid) {
		ModelAndView res;
		Actor principal;
		Quolet quolet;

		try {
			principal = this.utilityService.findByPrincipal();
			quolet = this.quoletService.findOne(quoletid);

			Assert.notNull(quolet, "null.quolet");
			Assert.isTrue(quolet.getIsDraft(), "not.draft.mode");
			Assert.isTrue(
					quolet.getConference().getAdministrator().getId() == principal
							.getId(), "not.allowed");

			res = this.createEditModelAndView(quolet);

		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:/welcome/index.do");
		}

		return res;
	}

	/* Save */

	@RequestMapping(value = "/admin/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Quolet quolet, BindingResult binding) {
		ModelAndView res;
		Quolet toSave;

		try {
			toSave = this.quoletService.reconstruct(quolet, binding);

			if (binding.hasErrors()) {
				res = this.createEditModelAndView(quolet);
			} else {
				this.quoletService.save(toSave);

				res = new ModelAndView("redirect:/quolet/display.do?quoletid="
						+ quolet.getId());
			}

		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:/welcome/index.do");
		}
		return res;
	}

	/* Deletion */

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(
			@RequestParam(value = "quoletid", required = true) Integer quoletid) {
		ModelAndView res;

		try {
			Quolet quolet = this.quoletService.findOne(quoletid);

			res = new ModelAndView("redirect:/quolet/list.do?conferenceid="
					+ quolet.getConference().getId());

			this.quoletService.delete(quolet);

		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:/welcome/index.do");
		}
		return res;
	}

	/* Ancillary methods */

	private ModelAndView createEditModelAndView(Quolet quolet) {
		return this.createEditModelAndView(quolet, null);
	}

	private ModelAndView createEditModelAndView(Quolet quolet,
			String messageCode) {
		ModelAndView res = new ModelAndView("quolet/edit");

		res.addObject("quolet", quolet);
		res.addObject("errMsg", messageCode);

		return res;
	}
}
