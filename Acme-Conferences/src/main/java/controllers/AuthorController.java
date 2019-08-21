package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AuthorService;
import services.FinderService;
import services.UtilityService;
import domain.Actor;
import domain.Author;
import domain.Finder;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@Controller
@RequestMapping("/author")
public class AuthorController extends AbstractController {

	/* Services */

	@Autowired
	private AuthorService authorService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private FinderService finderService;

	@Autowired
	private UtilityService utilityService;

	/* Methods */

	/**
	 * 
	 * Display author
	 * 
	 * @params id (optional)
	 * 
	 * @return ModelAndView
	 * **/
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView res;
		Author toDisplay;
		String requestURI = "author/display.do";
		Boolean found = true;
		Boolean permission;

		try {
			if (id != null) {
				toDisplay = (Author) this.actorService.findOne(id);
				if (toDisplay == null)
					found = false;
				permission = (toDisplay.getId() == this.utilityService
						.findByPrincipal().getId()) ? true : false;
				requestURI += "?id=" + id;
			} else {
				toDisplay = (Author) this.utilityService.findByPrincipal();
				permission = true;
			}

			res = new ModelAndView("author/display");
			res.addObject("author", toDisplay);
			res.addObject("found", found);
			res.addObject("requestURI", requestURI);
			res.addObject("permission", permission);
		} catch (final Throwable oops) {
			found = false;
			res = new ModelAndView("author/display");
			res.addObject("found", found);
		}

		return res;
	}

	/**
	 * 
	 * Register author GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerNewAuthor() {
		ModelAndView res;

		final ActorRegistrationForm registerFormObject = new ActorRegistrationForm();

		res = this.createRegisterModelAndView(registerFormObject);

		return res;
	}

	/**
	 * 
	 * Register author POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(
			@Valid final ActorRegistrationForm registerFormObject,
			final BindingResult binding) {

		ModelAndView res;
		Finder finder;
		Author saved;

		try {
			Author author = this.authorService.reconstruct(registerFormObject,
					binding);

			if (binding.hasErrors()) {

				res = new ModelAndView("author/register");
				res.addObject("registerFormObject", registerFormObject);
				res.addObject("binding", binding);

			} else {
				try {

					saved = this.authorService.save(author);

					finder = this.finderService.create(saved);
					this.finderService.save(finder);

					res = new ModelAndView("redirect:/");

				} catch (final Throwable oops) {
					res = this.createRegisterModelAndView(registerFormObject,
							"author.commit.error");

				}
			}
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:/");
		}

		return res;
	}

	/**
	 * 
	 * Edit author GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/author/edit", method = RequestMethod.GET)
	public ModelAndView editAuthor() {
		ModelAndView res;
		Actor principal;

		principal = this.utilityService.findByPrincipal();
		final ActorForm editionFormObject = new ActorForm(principal);

		res = this.createEditModelAndView(editionFormObject);

		return res;
	}

	/**
	 * 
	 * Edit author POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/author/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final ActorForm editionFormObject,
			final BindingResult binding) {

		ModelAndView res;

		try {

			Assert.isTrue(this.utilityService.findByPrincipal().getId() == editionFormObject
					.getId()
					&& this.actorService.findOne(this.utilityService
							.findByPrincipal().getId()) != null);

			Author author = this.authorService.reconstruct(editionFormObject,
					binding);

			if (binding.hasErrors()) {

				res = new ModelAndView("author/edit");
				res.addObject("editionFormObject", editionFormObject);
				res.addObject("binding", binding);

			} else {
				try {
					this.authorService.save(author);

					res = new ModelAndView("redirect:/");

				} catch (Throwable oops) {
					res = this.createEditModelAndView(editionFormObject,
							"author.commit.error");
				}
			}
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:/");
		}

		return res;
	}

	/* Auxiliary methods */

	/* Registration related */
	protected ModelAndView createRegisterModelAndView(
			final ActorRegistrationForm registerFormObject) {
		ModelAndView result;

		result = this.createRegisterModelAndView(registerFormObject, null);

		return result;
	}

	protected ModelAndView createRegisterModelAndView(
			final ActorRegistrationForm registerFormObject,
			final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("author/register");
		result.addObject("registerFormObject", registerFormObject);
		result.addObject("message", messageCode);

		return result;
	}

	/* Edition related */
	protected ModelAndView createEditModelAndView(
			final ActorForm editionFormObject) {
		ModelAndView result;

		result = this.createEditModelAndView(editionFormObject, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(
			final ActorForm editionFormObject, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("author/edit");
		result.addObject("editionFormObject", editionFormObject);
		result.addObject("message", messageCode);

		return result;
	}

}