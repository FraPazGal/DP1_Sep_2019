package domain;

import java.util.Collection;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Category extends DomainEntity {

	/* Attributes */

	private Map<String, String> title;
	private Category parentCategory;
	private Collection<Category> childCategories;
	private Collection<Conference> conferences;

	/* Getters and setters */

	@NotNull
	@NotEmpty
	@ElementCollection
	public Map<String, String> getTitle() {
		return title;
	}

	public void setTitle(Map<String, String> title) {
		this.title = title;
	}

	@Valid
	@ManyToOne(optional = true)
	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	@Valid
	@OneToMany
	@NotNull
	public Collection<Category> getChildCategories() {
		return childCategories;
	}

	public void setChildCategories(Collection<Category> childCategories) {
		this.childCategories = childCategories;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "category")
	public Collection<Conference> getConferences() {
		return conferences;
	}

	public void setConferences(Collection<Conference> conferences) {
		this.conferences = conferences;
	}

}
