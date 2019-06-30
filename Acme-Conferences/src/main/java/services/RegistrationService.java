package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RegistrationRepository;
import domain.Actor;
import domain.Author;
import domain.CreditCard;
import domain.Registration;
import forms.RegistrationForm;

@Service
@Transactional
public class RegistrationService {

	// Managed repository ------------------------------
	@Autowired
	private RegistrationRepository registrationRepository;

	// Supporting services -----------------------
	
	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private Validator validator;

	// CRUD Methods ---------------
	
	public Registration create() {
		Registration result;
		Actor principal = this.utilityService.findByPrincipal();

		result = new Registration();
		result.setAuthor((Author)principal);

		return result;
	}

	public Collection<Registration> findAll() {
		Collection<Registration> result;
		result = this.registrationRepository.findAll();

		return result;
	}

	public Registration findOne(final int registrationId) {
		Registration result;
		result = this.registrationRepository.findOne(registrationId);

		return result;
	}

	public Registration save(final Registration registration) {
		Registration result;
		Actor principal;

		Assert.notNull(registration);
		Assert.isTrue(registration.getId() == 0);
		
		Assert.notNull(registration.getConference());
		Assert.notNull(registration.getCreditCard());
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(registration.getAuthor().equals(principal), "not.allowed");

		result = this.registrationRepository.save(registration);
		Assert.notNull(result);

		return result;
	}
	
	
	// Ancillary Methods ----------------
	
	public Collection<Registration> registrationsPerAuthor(int authorId) {
		
		return this.registrationRepository.registrationsPerAuthor(authorId);
	}
	
	public Registration reconstruct(RegistrationForm form,
			BindingResult binding) {
		
		Registration registration = this.create();
		
		Assert.isTrue(!this.isAlreadyRegistered(form.getConference().getId(), registration.getAuthor().getId()), "already.registered");
		Assert.isTrue(form.getConference().getIsFinal(), "wrong.conference");
		Assert.isTrue(form.getConference().getStartDate().after(new Date(System.currentTimeMillis() - 1)), "wrong.conference");
		registration.setConference(form.getConference());
		
		/* Creating credit card */
		CreditCard creditCard = this.creditCardService.create();
		
		creditCard.setHolder(form.getHolder());
		creditCard.setMake(form.getMake());
		creditCard.setNumber(form.getNumber());
		creditCard.setExpirationMonth(form.getExpirationMonth());
		creditCard.setExpirationYear(form.getExpirationYear());
		creditCard.setCVV(form.getCVV());
		
		this.validator.validate(creditCard, binding);
		
		if (!binding.hasErrors()) {
			CreditCard saved;
			saved = this.creditCardService.save(creditCard);
			
			registration.setCreditCard(saved);
		}
		
		this.validator.validate(registration, binding);
		
		try {
			Assert.notNull(form.getConference(), "no.conferences");
		} catch (Throwable oops) {
			binding.rejectValue("conference", "conferences.error");
		}

		/* Credit card */
		if (form.getNumber() != null) {
			try {
				Assert.isTrue(this.creditCardService
						.checkCreditCardNumber(creditCard.getNumber()),
						"card.number.error");
			} catch (Throwable oops) {
				binding.rejectValue("number", "number.error");
			}
		}

		if (creditCard.getExpirationMonth() != null
				&& creditCard.getExpirationYear() != null) {

			try {
				Assert.isTrue(
						!this.creditCardService.checkIfExpired(
								creditCard.getExpirationMonth(),
								creditCard.getExpirationYear()),
						"card.date.error");
			} catch (Throwable oops) {
				binding.rejectValue("expirationMonth", "card.date.error");
			}

			if (form.getCVV() != null) {
				try {
					Assert.isTrue(form.getCVV() < 999 && form.getCVV() > 100,
							"CVV.error");
				} catch (Throwable oops) {
					binding.rejectValue("CVV", "CVV.error");
				}
			}
		}
		return registration;
	}
	
	public boolean isAlreadyRegistered(int conferenceId, int actorId) {

		return this.registrationRepository.isAlreadyRegistered(conferenceId, actorId);
	}

}
