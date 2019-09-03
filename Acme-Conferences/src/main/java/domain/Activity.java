package domain;

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
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Activity extends DomainEntity {

	/* Attributes */

	private String title, speakersInvolved, summary, usedRoom, attachement,
			type, duration;
	private String startMoment;
	private Conference conference;
	private Submission submission;

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
	public String getSpeakersInvolved() {
		return speakersInvolved;
	}

	public void setSpeakersInvolved(String speakersInvolved) {
		this.speakersInvolved = speakersInvolved;
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
	public String getUsedRoom() {
		return usedRoom;
	}

	public void setUsedRoom(String usedRoom) {
		this.usedRoom = usedRoom;
	}

	@URL
	public String getAttachement() {
		return attachement;
	}

	public void setAttachement(String attachement) {
		this.attachement = attachement;
	}

	@NotBlank
	@NotNull
	@Pattern(regexp = "^PANEL|TUTORIAL|PRESENTATION", message = "type.error")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@NotNull
	@NotBlank
	public String getStartMoment() {
		return startMoment;
	}

	public void setStartMoment(String startMoment) {
		this.startMoment = startMoment;
	}

	@NotNull
	@NotBlank
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
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
	@OneToOne(optional = true)
	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}
}
