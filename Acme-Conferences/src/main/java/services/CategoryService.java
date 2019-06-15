package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import domain.Actor;
import domain.Category;
import domain.SystemConfiguration;

@Service
@Transactional
public class CategoryService {

	// Managed repository ------------------------------
	@Autowired
	private CategoryRepository categoryRepository;

	// Supporting services -----------------------

	@Autowired
	private UtilityService	utilityService;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
//	@Autowired
//	private ConferenceService conferenceService;
	
	@Autowired
	private Validator validator;

	// CRUD methods -----------------------------------

	public Category create() {
		Category result;
		Actor principal;
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(
				this.utilityService.checkAuthority(principal, "ADMINISTRATOR"),
				"not.allowed");

		result = new Category();
		
		return result;
	}
	
	public Category findOne(final int categoryId) {
		Category result;

		result = this.categoryRepository.findOne(categoryId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Category> findAll() {
		Collection<Category> result;

		result = this.categoryRepository.findAll();
		Assert.notNull(result);

		return result;
	}
	
	public Category save(final Category category) {
		Category result;
		SystemConfiguration systemConf;
		Set<String> idiomasCategory;
		Actor principal;
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(
				this.utilityService.checkAuthority(principal, "ADMINISTRATOR"),
				"not.allowed");
		
		//root = this.findRoot();
		//Assert.isTrue(category.getId() != root.getId());
		//Assert.notNull(category.getParentCategory());
		Assert.notNull(category.getTitle());
		
		systemConf = systemConfigurationService.findMySystemConfiguration();
		Set<String> idiomasSystemConf = new HashSet<String>(systemConf
				.getWelcomeMessage().keySet());
		idiomasCategory = category.getTitle().keySet();
		Assert.isTrue(idiomasSystemConf.equals(idiomasCategory));

		result = this.categoryRepository.save(category);
		Assert.notNull(result);
		
		return result;
	}

	public void delete(final Category category) {
		Actor principal;
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(
				this.utilityService.checkAuthority(principal, "ADMINISTRATOR"),
				"not.allowed");
		
		Assert.notNull(category);
		Assert.isTrue(category.getId() != 0);
		
		this.categoryRepository.delete(category);
	}

	// Other business methods
	
	public Category reconstruct(Category category, String nameES,
			String nameEN, BindingResult binding) {
		Category res;

		if (category.getId() == 0) {
			category.setTitle(new HashMap<String, String>());

			category.getTitle().put("Español", nameES);
			category.getTitle().put("English", nameEN);
			
			res = category;
		} else {
			res = this.categoryRepository.findOne(category.getId());

			category.setTitle(new HashMap<String, String>());

			category.getTitle().put("Español", nameES);
			category.getTitle().put("English", nameEN);

			res.setTitle(category.getTitle());
			
		}
		this.validator.validate(res, binding);

		return res;
	}
	
	//TODO: pass conferences with a category to it's parentCat
	
	public Collection<Category> parseCategorys (String [] array) {
		Collection<Category> result = new ArrayList<>();
		String a = null;
		Integer n = 0;
		Category pos = null;
		
		for (int i = 0; i <= array.length - 1; i++) {
			a = array[i];
			n = Integer.parseInt(a);
			pos = this.findOne(n);
			result.add(pos);
		}
		this.flush();
		return result;
	}
	
	public void flush(){
		this.categoryRepository.flush();
	}
}

