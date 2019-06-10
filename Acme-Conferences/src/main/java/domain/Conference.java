package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Conference extends DomainEntity {

	/* Attributes */

	private String title, acronym, summary, venue;
	private Date submissionDeadline, notificationDealine, cameraReadyDeadline,
			startDate, endDate;
	private Double entryFee;
	private Boolean isFinal;
	private Category category;
	private Administrator administrator;

	/* Getters and setters */

	@NotNull
	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotNull
	@NotBlank
	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	@NotNull
	@NotBlank
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@NotNull
	@NotBlank
	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	@NotNull
	public Date getSubmissionDeadline() {
		return submissionDeadline;
	}

	public void setSubmissionDeadline(Date submissionDeadline) {
		this.submissionDeadline = submissionDeadline;
	}

	@NotNull
	public Date getNotificationDealine() {
		return notificationDealine;
	}

	public void setNotificationDealine(Date notificationDealine) {
		this.notificationDealine = notificationDealine;
	}

	@NotNull
	public Date getCameraReadyDeadline() {
		return cameraReadyDeadline;
	}

	public void setCameraReadyDeadline(Date cameraReadyDeadline) {
		this.cameraReadyDeadline = cameraReadyDeadline;
	}

	@NotNull
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@NotNull
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@NotNull
	public Double getEntryFee() {
		return entryFee;
	}

	public void setEntryFee(Double entryFee) {
		this.entryFee = entryFee;
	}

	@NotNull
	public Boolean getIsFinal() {
		return isFinal;
	}

	public void setIsFinal(Boolean isFinal) {
		this.isFinal = isFinal;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Administrator getAdministrator() {
		return administrator;
	}

	public void setAdministrator(Administrator administrator) {
		this.administrator = administrator;
	}
}
