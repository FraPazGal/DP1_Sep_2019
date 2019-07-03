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
import services.UtilityService;
import domain.Actor;
import domain.Category;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

	/* Services */

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UtilityService utilityService;

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Actor principal;
		Collection<Category> categories;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"ADMIN"));

			categories = this.categoryService.findAll();

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
		Actor principal;
		Category category;
		Collection<Category> categories;
		
		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"ADMIN"));

			category = this.categoryService.create();
			
			categories = this.categoryService.findAll();

			result = this.createEditModelAndView(category);
			result.addObject("categories", categories);
		} catch (Throwable oops) {
			result = new ModelAndView("category/list");

			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {
		ModelAndView result;
		Category category;
		Collection<Category> categories;

		try {
			category = this.categoryService.findOne(categoryId);
			Assert.notNull(category);
			
			categories = this.categoryService.findAll();

			result = this.createEditModelAndView(category);
			result.addObject("categories", categories);
		} catch (Throwable oops) {
			result = new ModelAndView("category/list");

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
		Actor principal;
		Category reconstructed;
		
		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"ADMIN"));
			reconstructed = this.categoryService.reconstruct(category, nameES,
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
		ModelAndView result;
		Actor principal;
		boolean canBeDeleted = true;
		
		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"ADMIN"));
			
			Category toDelete = this.categoryService.findOne(categoryId);
			
			Assert.isTrue(canBeDeleted);
			
			this.categoryService.delete(toDelete);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("category/list");

			result.addObject("errMsg", "category.cannot.delete");
		}
		return result;
	}
	
	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(Category category) {
		ModelAndView res;

		res = createEditModelAndView(category, null);

		return res;
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

