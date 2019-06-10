package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SystemConfiguration extends DomainEntity {

	/* Attributes */

	private String systemName, banner, countryCode;
	private Map<String, String> welcomeMessage, topics, voidWords;
	private Integer timeResultsCached, maxResults;

	/* Getters and setters */

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
	@URL
	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	@NotNull
	@NotBlank
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@NotNull
	@NotEmpty
	public Map<String, String> getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(Map<String, String> welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@NotNull
	@NotEmpty
	public Map<String, String> getTopics() {
		return topics;
	}

	public void setTopics(Map<String, String> topics) {
		this.topics = topics;
	}

	@NotNull
	@NotEmpty
	public Map<String, String> getVoidWords() {
		return voidWords;
	}

	public void setVoidWords(Map<String, String> voidWords) {
		this.voidWords = voidWords;
	}

	@NotNull
	@Range(min = 1, max = 24)
	public Integer getTimeResultsCached() {
		return timeResultsCached;
	}

	public void setTimeResultsCached(Integer timeResultsCached) {
		this.timeResultsCached = timeResultsCached;
	}

	@NotNull
	@Range(min = 0, max = 100)
	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}
}
