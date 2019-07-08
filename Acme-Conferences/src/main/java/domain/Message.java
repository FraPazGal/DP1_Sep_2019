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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Message extends DomainEntity {

	/* Attributes */

	private Date sendMoment;
	private String subject, body;
	private String topic;
	private Collection<Actor> reciever;
	private Actor sender;

	/* Getters and setters */

	@NotNull
	@DateTimeFormat(pattern = "dd-MM-yyy hh:mm")
	public Date getSendMoment() {
		return sendMoment;
	}

	public void setSendMoment(Date sendMoment) {
		this.sendMoment = sendMoment;
	}

	@NotNull
	@NotBlank
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@NotNull
	@NotBlank
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@NotNull
	@NotBlank
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Valid
	@NotNull
	@NotEmpty
	@ManyToMany
	public Collection<Actor> getReciever() {
		return reciever;
	}

	public void setReciever(Collection<Actor> reciever) {
		this.reciever = reciever;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getSender() {
		return sender;
	}

	public void setSender(Actor sender) {
		this.sender = sender;
	}

}
