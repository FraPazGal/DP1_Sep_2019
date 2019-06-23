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

	// Services

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UtilityService utilityService;

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Actor principal;
		Collection<Category> categories;
		Boolean err;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"ADMIN"));

			categories = this.categoryService.findAll();

			res = new ModelAndView("category/list");
			res.addObject("categories", categories);
		} catch (Throwable oopsie) {
			res = new ModelAndView("category/list");
			err = true;

			res.addObject("errMsg", oopsie);
			res.addObject("err", err);
		}
		return res;
	}

	/* Creating a category */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Actor principal;
		Category category;
		Boolean err;
		Collection<Category> categories;
		
		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"ADMIN"));

			category = this.categoryService.create();
			
			categories = this.categoryService.findAll();

			res = this.createEditModelAndView(category);
			res.addObject("categories", categories);
		} catch (Throwable oopsie) {

			res = new ModelAndView("category/list");
			err = true;

			res.addObject("errMsg", oopsie);
			res.addObject("err", err);
		}
		return res;
	}

	/* Editing a category */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {
		ModelAndView result;
		Category category;
		Collection<Category> categories;

		category = this.categoryService.findOne(categoryId);
		Assert.notNull(category);
		
		categories = this.categoryService.findAll();

		result = this.createEditModelAndView(category);
		result.addObject("categories", categories);
		return result;
	}

	/* Saving a category */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Category category,
			@RequestParam("nameES") String nameES,
			@RequestParam("nameEN") String nameEN, BindingResult binding) {
		ModelAndView res;
		Actor principal;
		
			try {
				principal = this.utilityService.findByPrincipal();
				Assert.isTrue(this.utilityService.checkAuthority(principal,
						"ADMIN"));
				category = this.categoryService.reconstruct(category, nameES,
						nameEN, binding);
				
				if (binding.hasErrors()) {
					res = this.createEditModelAndView(category, binding.toString());
				
				} else {

					this.categoryService.save(category);
				}

				res = new ModelAndView("redirect:list.do");
			} catch (Throwable oopsie) {
				res = this.createEditModelAndView(category,
						"category.commit.error");
				Collection<Category> categories = this.categoryService.findAll();
				res.addObject("categories", categories);
			}
		return res;
	}

	/* Delete category */
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
			result = this.createEditModelAndView(
					this.categoryService.findOne(categoryId),
					"category.cannot.delete");
		}
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Category category, BindingResult binding) {
		ModelAndView result;
		Actor principal;
		boolean canBeDeleted = true;
		
		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"ADMIN"));
			
			Category toDelete = this.categoryService.findOne(category.getId());
			
			Assert.isTrue(canBeDeleted);
			
			this.categoryService.delete(toDelete);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(
					this.categoryService.findOne(category.getId()),
					"category.cannot.delete");
		}
		return result;
	}

	// Manage methods
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
		res.addObject("message", messageCode);

		return res;
	}
}

