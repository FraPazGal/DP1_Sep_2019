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
		ModelAndView result;
		Collection<Category> categories;

		try {
			categories = this.categoryService.findAllAsAdmin();

			result = new ModelAndView("category/list");
			result.addObject("categories", categories);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		
		try {
			Category category = this.categoryService.create();
			
			Collection<Category> categories = this.categoryService.findAll();

			result = this.createEditModelAndView(category);
			result.addObject("categories", categories);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:list.do");

			result.addObject("errMsg", oops.getMessage());
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
			
			Collection<Category> categories = this.categoryService.findAll();

			result = this.createEditModelAndView(category);
			result.addObject("categories", categories);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:list.do");

			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Category category,
			@RequestParam("nameES") String nameES,
			@RequestParam("nameEN") String nameEN, BindingResult binding) {
		ModelAndView res;
		
		try {
			Category reconstructed = this.categoryService.reconstruct(category, nameES,
					nameEN, binding);
			
			if (binding.hasErrors()) {
				Collection<Category> categories = this.categoryService.findAll();
				res = this.createEditModelAndView(category);
				res.addObject("categories", categories);
				
			} else {
				this.categoryService.save(reconstructed);
				res = new ModelAndView("redirect:list.do");
			}
		} catch (Throwable oops) {
			Collection<Category> categories = this.categoryService.findAll();
			res = this.createEditModelAndView(category,
					oops.getMessage());
			res.addObject("categories", categories);
		}
		return res;
	}

	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int categoryId) {
		ModelAndView result = new ModelAndView("redirect:list.do");
		try {
			Category toDelete = this.categoryService.findOne(categoryId);
			
			this.categoryService.delete(toDelete);
		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}
	
	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(Category category) {
		return createEditModelAndView(category, null);
	}

	protected ModelAndView createEditModelAndView(Category category,
			String messageCode) {
		ModelAndView res;

		res = new ModelAndView("category/edit");
		res.addObject("category", category);
		res.addObject("errMsg", messageCode);

		return res;
	}
}

