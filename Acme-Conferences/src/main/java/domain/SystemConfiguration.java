package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SystemConfiguration extends DomainEntity {

	/* Attributes */

	private String systemName, banner, countryCode, makes, timeResultsCached,
			maxResults;
	private Map<String, String> welcomeMessage, topics, voidWords;

	/* Getters and setters */

	@NotNull
	@NotBlank
	public String getMakes() {
		return makes;
	}

	public void setMakes(String makes) {
		this.makes = makes;
	}

	@NotNull
	@NotBlank
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@NotNull
	@NotBlank
	@URL(message = "url.error")
	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	@NotNull
	@NotBlank
	@Pattern(regexp = "[+]\\d{3}", message = "cc.error")
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@NotNull
	@NotEmpty
	@ElementCollection
	public Map<String, String> getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(Map<String, String> welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@NotNull
	@NotEmpty
	@ElementCollection
	public Map<String, String> getTopics() {
		return topics;
	}

	public void setTopics(Map<String, String> topics) {
		this.topics = topics;
	}

	@NotNull
	@NotEmpty
	@ElementCollection
	@Column(length = 510)
	public Map<String, String> getVoidWords() {
		return voidWords;
	}

	public void setVoidWords(Map<String, String> voidWords) {
		this.voidWords = voidWords;
	}

	@NotNull
	public String getTimeResultsCached() {
		return timeResultsCached;
	}

	public void setTimeResultsCached(String timeResultsCached) {
		this.timeResultsCached = timeResultsCached;
	}

	@NotNull
	public String getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(String maxResults) {
		this.maxResults = maxResults;
	}
}
