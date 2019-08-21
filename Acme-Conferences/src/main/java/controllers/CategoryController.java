package controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import domain.Category;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

	/* Services */

	@Autowired
	private CategoryService categoryService;


	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = new ModelAndView("category/list");

		try {
			result.addObject("categories", this.categoryService.findAllAsAdmin());
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView("redirect:list.do");
		
		try {
			Category category = this.categoryService.create();

			result = this.createEditModelAndView(category);
			
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {
		ModelAndView result;

		try {
			Category category = this.categoryService.findOne(categoryId);
			Assert.notNull(category);
			
			result = this.createEditModelAndView(category);
			result.addObject("nameEN", category.getTitle().get("English"));
			result.addObject("nameES", category.getTitle().get("Español"));
			
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Category category,
			@RequestParam("nameES") String nameES,
			@RequestParam("nameEN") String nameEN, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:list.do");
		
		try {
			Category reconstructed = this.categoryService.reconstruct(category, nameES,
					nameEN, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(category);
				result.addObject("nameES", nameES);
				result.addObject("nameEN", nameEN);
			} else {
				this.categoryService.save(reconstructed);
			}
		} catch (Throwable oops) {
			result = this.createEditModelAndView(category, oops.getMessage());
		}
		
		return result;
	}

	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int categoryId) {
		ModelAndView result = new ModelAndView("redirect:list.do");
		try {
			Category toDelete = this.categoryService.findOne(categoryId);
			
			this.categoryService.delete(toDelete);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
	
	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(Category category) {
		return createEditModelAndView(category, null);
	}

	protected ModelAndView createEditModelAndView(Category category, String messageCode) {
		ModelAndView result = new ModelAndView("category/edit");

		result.addObject("category", category);
		result.addObject("categories", this.categoryService.findAllAsAdmin());
		result.addObject("errMsg", messageCode);
		
		return result;
	}
}