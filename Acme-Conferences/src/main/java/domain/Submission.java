package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Submission extends DomainEntity {

	/* Attributes */

	private String ticker, status;
	private Date submissionMoment;
	private Conference conference;
	private Author author;
	private Paper paper, cameraReadyPaper;

	/* Getters and setters */

	@NotBlank
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	@Pattern(regexp = "^UNDER-REVIEW|ACCEPTED|REJECTED$")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@NotNull
	public Date getSubmissionMoment() {
		return submissionMoment;
	}

	public void setSubmissionMoment(Date submissionMoment) {
		this.submissionMoment = submissionMoment;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	@Valid
	@NotNull
	@OneToOne(optional = false)
	public Paper getPaper() {
		return paper;
	}

	public void setPaper(Paper paper) {
		this.paper = paper;
	}

	@Valid
	@OneToOne(optional = true)
	public Paper getCameraReadyPaper() {
		return cameraReadyPaper;
	}

	public void setCameraReadyPaper(Paper cameraReadyPaper) {
		this.cameraReadyPaper = cameraReadyPaper;
	}

}
