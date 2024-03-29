package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Sponsorship extends DomainEntity {

	/* Attributes */

	private String banner, targetPage;
	private Sponsor sponsor;
	private CreditCard creditCard;
	private Collection<Conference> conferences;

	/* Getters and setters */

	@URL
	@NotBlank
	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	@URL
	@NotBlank
	public String getTargetPage() {
		return targetPage;
	}

	public void setTargetPage(String targetPage) {
		this.targetPage = targetPage;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@Valid
	@NotNull
	@ManyToMany
	public Collection<Conference> getConferences() {
		return conferences;
	}

	public void setConferences(Collection<Conference> conferences) {
		this.conferences = conferences;
	}
	
	
}
