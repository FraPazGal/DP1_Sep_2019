package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Finder extends DomainEntity {

	/* Attributes */

	private String keyWord;
	private Double maximumFee;
	private Date minimumDate, maximumDate, searchMoment;
	private Category category;
	private Collection<Conference> results;
	private Actor actor;

	/* Getters and setters */

	@Length(max = 100, message = "The keyword is too long")
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@Min(value = 0L, message = "The value must be positive")
	public Double getMaximumFee() {
		return maximumFee;
	}

	public void setMaximumFee(Double maximumFee) {
		this.maximumFee = maximumFee;
	}

	public Date getMinimumDate() {
		return minimumDate;
	}

	public void setMinimumDate(Date minimumDate) {
		this.minimumDate = minimumDate;
	}

	public Date getMaximumDate() {
		return maximumDate;
	}

	public void setMaximumDate(Date maximumDate) {
		this.maximumDate = maximumDate;
	}

	public Date getSearchMoment() {
		return searchMoment;
	}

	public void setSearchMoment(Date searchMoment) {
		this.searchMoment = searchMoment;
	}

	@ManyToOne(optional = true)
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Valid
	@ManyToMany
	@NotNull
	public Collection<Conference> getResults() {
		return results;
	}

	public void setResults(Collection<Conference> results) {
		this.results = results;
	}

	@Valid
	@NotNull
	@OneToOne(optional = false)
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
}
