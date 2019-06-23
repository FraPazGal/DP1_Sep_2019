package forms;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import domain.Conference;
import domain.Sponsorship;

public class SponsorshipForm {

	/* Attributes */

	/* Sponsorship attributes */
	private int id;
	private int version;
	private String banner;
	private String targetPage;
	private Collection<Conference> conferences;

	/* Credit Card attributes */
	private int idCC;
	private String holder;
	private String make;
	private String number;
	private Integer expirationMonth;
	private Integer expirationYear;
	private Integer CVV;

	public SponsorshipForm() {

	}
	
	public SponsorshipForm(Sponsorship sponsorship) {
		this.id = sponsorship.getId();
		this.version = sponsorship.getVersion();
		this.banner = sponsorship.getBanner();
		this.targetPage = sponsorship.getTargetPage();
		this.conferences = sponsorship.getConferences();
		this.holder = sponsorship.getCreditCard().getHolder();
		this.make = sponsorship.getCreditCard().getMake();
		this.number = sponsorship.getCreditCard().getNumber();
		this.expirationMonth = sponsorship.getCreditCard().getExpirationMonth();
		this.expirationYear = sponsorship.getCreditCard().getExpirationYear();
		this.CVV = sponsorship.getCreditCard().getCVV();
		this.idCC = sponsorship.getCreditCard().getId();
	}
	
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
	@ManyToMany
	public Collection<Conference> getConferences() {
		return conferences;
	}
	public void setConferences(Collection<Conference> conferences) {
		this.conferences = conferences;
	}

	@NotBlank
	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	@NotBlank
	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	@NotBlank
	@CreditCardNumber
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@NotNull
	@Range(min = 1, max = 12)
	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@NotNull
	@Range(min = 0, max = 99)
	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	@NotNull
	@Range(min = 0, max = 999)
	public Integer getCVV() {
		return CVV;
	}

	public void setCVV(Integer CVV) {
		this.CVV = CVV;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	public int getIdCC() {
		return idCC;
	}

	public void setIdCC(int idCC) {
		this.idCC = idCC;
	}

}