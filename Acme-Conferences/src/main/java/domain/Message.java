package domain;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Message extends DomainEntity {

	/* Attributes */

	private Date sendMoment;
	private String subject, body;
	private Map<String, String> topic;
	private Collection<Actor> reciever;
	private Actor sender;

	/* Getters and setters */

	@NotNull
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
	@NotEmpty
	@ElementCollection
	public Map<String, String> getTopic() {
		return topic;
	}

	public void setTopic(Map<String, String> topic) {
		this.topic = topic;
	}

	@Valid
	@NotNull
	@NotEmpty
	@ElementCollection
	@ManyToMany
	public Collection<Actor> getReciever() {
		return reciever;
	}

	public void setReciever(Collection<Actor> reciever) {
		this.reciever = reciever;
	}

	@Valid
	@NotNull
	@NotEmpty
	@ManyToOne(optional = false)
	public Actor getSender() {
		return sender;
	}

	public void setSender(Actor sender) {
		this.sender = sender;
	}

}
