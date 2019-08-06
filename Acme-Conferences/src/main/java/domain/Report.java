package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Report extends DomainEntity {

	/* Attributes */

	private Double originalityScore, qualityScore, readabilityScore;
	private String status, comments;
	private Submission submission;
	private Reviewer reviewer;
	private boolean isWritten;

	/* Getters and setters */

	@Range(min = 0, max = 10, message = "range.error")
	public Double getOriginalityScore() {
		return originalityScore;
	}

	public void setOriginalityScore(Double originalityScore) {
		this.originalityScore = originalityScore;
	}

	@Range(min = 0, max = 10, message = "range.error")
	public Double getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(Double qualityScore) {
		this.qualityScore = qualityScore;
	}

	@Range(min = 0, max = 10, message = "range.error")
	public Double getReadabilityScore() {
		return readabilityScore;
	}

	public void setReadabilityScore(Double readabilityScore) {
		this.readabilityScore = readabilityScore;
	}

	@Pattern(regexp = "^BORDER-LINE|ACCEPTED|REJECTED$", message = "status.error")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Valid
	@NotNull
	@OneToOne(optional = false)
	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	@Valid
	@NotNull
	@OneToOne(optional = false)
	public Reviewer getReviewer() {
		return reviewer;
	}

	public void setReviewer(Reviewer reviewer) {
		this.reviewer = reviewer;
	}

	@NotNull
	public boolean getIsWritten() {
		return isWritten;
	}

	public void setIsWritten(boolean isWritten) {
		this.isWritten = isWritten;
	}
}
