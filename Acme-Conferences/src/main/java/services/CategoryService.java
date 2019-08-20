package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import domain.Category;
import domain.Conference;
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
	
	@Autowired
	private ConferenceService conferenceService;
	
	@Autowired
	private Validator validator;

	// CRUD methods -----------------------------------

	public Category create() {
		Category result;
		
		this.utilityService.assertPrincipal("ADMIN");

		result = new Category();
		result.setTitle(new HashMap<String,String>());
		result.setChildCategories(new ArrayList<Category>());
		result.setConferences(new ArrayList<Conference>());
		
		return result;
	}
	
	public Category findOne(final int categoryId) {
		Category result = this.categoryRepository.findOne(categoryId);
		Assert.notNull(result, "wrong.id");
		
		return result;
	}

	public Collection<Category> findAll() {
		return this.categoryRepository.findAll();
	}
	
	public Category save(final Category category) {
		Category result,aux;
		SystemConfiguration systemConf;
		Set<String> idiomasCategory;
		
		this.utilityService.assertPrincipal("ADMIN");
		
		Assert.notNull(category.getChildCategories());
		Assert.notNull(category.getConferences());
		Assert.notNull(category.getTitle());
		
		systemConf = systemConfigurationService.findMySystemConfiguration();
		Set<String> idiomasSystemConf = new HashSet<String>(systemConf
				.getWelcomeMessage().keySet());
		idiomasCategory = category.getTitle().keySet();
		Assert.isTrue(idiomasSystemConf.equals(idiomasCategory));
		
		if(category.getTitle().containsValue("CONFERENCE")) {
			aux = this.findOne(category.getId());
			category.setParentCategory(aux.getParentCategory());
			category.setTitle(aux.getTitle());
		} else {
			Assert.notNull(category.getParentCategory());
		}
		
		result = this.categoryRepository.save(category);
		Assert.notNull(result);
		
		if(category.getId() == 0) {
			this.addNewChild(result);
		}
		
		return result;
	}

	public void delete(final Category category) {
		
		this.utilityService.assertPrincipal("ADMIN");
		
		Assert.notNull(category);
		Assert.isTrue(category.getId() != 0);
		
		this.deleteFromParent(category);
		
		if(!category.getChildCategories().isEmpty()) {
			this.transferChildren(category);
		}
		
		if(!category.getConferences().isEmpty()) {
			this.transferConferences(category);
		}
		
		this.categoryRepository.delete(category);
	}

	// Other business methods -------------------------------
	
	public Category reconstruct(Category category, String nameES, String nameEN, BindingResult binding) {
		Category result = this.create();
		Map<String,String> title = new HashMap<String,String>();
		
		this.utilityService.assertPrincipal("ADMIN");
		
		try {
			Assert.isTrue(!nameEN.isEmpty(), "no.both.names");
			Assert.isTrue(!nameES.isEmpty(), "no.both.names");
		} catch (Throwable oops) {
			binding.rejectValue("title", "no.both.names");
		}
		
		title.put("Español", nameES);
		title.put("English", nameEN);
		
		if (category.getId() != 0) {
			Category aux = this.categoryRepository.findOne(category.getId());
			
			result.setId(aux.getId());
			result.setVersion(aux.getVersion());
			result.setChildCategories(aux.getChildCategories());
			result.setConferences(aux.getConferences());
			result.setParentCategory(aux.getParentCategory());
			
		} else {
			result.setParentCategory(category.getParentCategory());
		}
		result.setTitle(title);

		this.validator.validate(result, binding);

		return result;
	}
	
	private void transferConferences(Category category) {
		Category parent = category.getParentCategory();
		Collection<Conference> conferencesToTransfer = category.getConferences();
		Collection<Conference> conferences = parent.getConferences();
		
		conferences.addAll(conferencesToTransfer);
		parent.setConferences(conferences);
		
		for(Conference conference : conferencesToTransfer) {
			conference.setCategory(parent);
			this.conferenceService.save(conference);
		}
		this.save(parent);
	}
	
	private void transferChildren(Category category) {
		Category parent = category.getParentCategory();
		Collection<Category> childrenToTransfer = category.getChildCategories();
		Collection<Category> children = parent.getChildCategories();
		
		children.addAll(childrenToTransfer);
		parent.setChildCategories(children);
		
		for(Category cat : childrenToTransfer) {
			cat.setParentCategory(parent);
			this.save(cat);
			//this.addNewChild(cat);
		}
		this.save(parent);
	}
	
	private void deleteFromParent(Category category) {
		Category parent = category.getParentCategory();
		Collection<Category> aux = parent.getChildCategories();
		aux.remove(category);
		parent.setChildCategories(aux);
		
		this.save(parent);
	}
	
	private void addNewChild(Category category) {
		Collection<Category> children;
		Category parent = this.findOne(category.getParentCategory().getId());
		
		children = parent.getChildCategories();
		children.add(category);
		parent.setChildCategories(children);
		this.save(parent);
	}
	
	public Collection<Category> parseCategories (String [] array) {
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
	
	public Category findOneByConferenceId(int conferenceId) {
		
		return this.categoryRepository.findOneByConferenceId(conferenceId);
	}
	
	public void addNewConference(Collection<Category> categories, Conference conference) {
		for(Category cat : categories) {
			Collection<Conference> collCon = cat.getConferences();
			collCon.add(conference);
			cat.setConferences(collCon);
		}
	}
	
	public Collection<Category> findAllAsAdmin() {
		this.utilityService.assertPrincipal("ADMIN");

		return this.categoryRepository.findAll();
	}
}

