package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Paper extends DomainEntity {

	/* Attributes */

	private String title, authors, summary, paperDocument;

	/* Getters and setters */

	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	@Type(type="text")
	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	@NotBlank
	@Type(type="text")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@NotBlank
	@Type(type="text")
	public String getPaperDocument() {
		return paperDocument;
	}

	public void setPaperDocument(String paperDocument) {
		this.paperDocument = paperDocument;
	}
}
