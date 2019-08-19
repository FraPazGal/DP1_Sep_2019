package forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import domain.Reviewer;

public class ReviewerForm {

	/* Attributes */

	private int id, version;
	private String name, middleName, surname, photo, email, phoneNumber,
			address, keywords;

	/* Getters and setters */

	public ReviewerForm() {

	}

	public ReviewerForm(Reviewer actor) {
		this.id = actor.getId();
		this.version = actor.getVersion();
		this.name = actor.getName();
		this.middleName = actor.getMiddleName();
		this.surname = actor.getSurname();
		this.photo = actor.getPhoto();
		this.email = actor.getEmail();
		this.phoneNumber = actor.getPhoneNumber();
		this.address = actor.getAddress();
		this.keywords = actor.getKeywords();
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

	@NotBlank
	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@NotBlank
	@NotNull
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@NotBlank
	@NotNull
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@NotNull
	@NotBlank
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
