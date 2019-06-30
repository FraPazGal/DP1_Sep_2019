package services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.Actor;
import domain.Conference;
import domain.CreditCard;
import domain.Sponsor;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Transactional
@Service
public class SponsorshipService {
	
	// Managed repository ------------------------------------
	
	@Autowired
	private SponsorshipRepository sponsorshipRepository;
	
	// Supporting services -----------------------------------
	
	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private Validator validator;
	
	public Sponsorship create() {
		Actor principal;
		Sponsorship result;
		Collection<Conference> conferences = new ArrayList<>();

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "SPONSOR"),
				"not.allowed");

		result = new Sponsorship();
		result.setConferences(conferences);
		result.setSponsor((Sponsor) principal);
		
		return result;
	}

	public Collection<Sponsorship> findAll() {
		Collection<Sponsorship> result;
		result = this.sponsorshipRepository.findAll();

		return result;
	}

	public Sponsorship findOne(final int sponsorshipId) {
		Sponsorship result;
		result = this.sponsorshipRepository.findOne(sponsorshipId);

		return result;
	}
	
	public Sponsorship save(final Sponsorship sponsorship) {
		Actor principal;
		Sponsorship result;

		principal = this.utilityService.findByPrincipal();
		
		Assert.isTrue(this.utilityService.checkAuthority(principal, "SPONSOR") ,
				"not.allowed");
		
		if(sponsorship.getId() != 0) {
			if (this.utilityService.checkAuthority(principal, "SPONSOR")) {
				Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) (principal)), "not.allowed");
			}
		}
		
		for(Conference conference : sponsorship.getConferences()) {
			Assert.isTrue(conference.getIsFinal());
		}
		
		Assert.notNull(sponsorship.getBanner());
		Assert.notNull(sponsorship.getTargetPage());
		Assert.notNull(sponsorship.getCreditCard());
		Assert.notNull(sponsorship.getSponsor());
		
		result = this.sponsorshipRepository.save(sponsorship);

		return result;
	}
	
	public void delete(final Sponsorship sponsorship) {
		Actor principal;

		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0, "wrong.id");

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) principal),
				"not.allowed");

		this.sponsorshipRepository.delete(sponsorship.getId());
	}
	
	// Other business methods -------------------------------
		
	public Sponsorship reconstruct(SponsorshipForm form,
			BindingResult binding) {
		
		Sponsorship sponsorship = this.create();

		/* Creating sponsorship */
		
		if(form.getId() != 0) {
			Sponsorship aux = this.findOne(form.getId());
			
			sponsorship.setId(aux.getId());
			sponsorship.setVersion(aux.getVersion());
		}
		
		sponsorship.setBanner(form.getBanner());
		sponsorship.setTargetPage(form.getTargetPage());
		sponsorship.setConferences(form.getConferences());

		/* Creating credit card */
		CreditCard creditCard = this.creditCardService.create();
		
		if(form.getIdCC() != 0) {
			CreditCard auxCC = this.creditCardService.findOne(form.getIdCC());
			
			creditCard.setId(auxCC.getId());
			creditCard.setVersion(auxCC.getVersion());
		}

		creditCard.setHolder(form.getHolder());
		creditCard.setMake(form.getMake());
		creditCard.setNumber(form.getNumber());
		creditCard.setExpirationMonth(form.getExpirationMonth());
		creditCard.setExpirationYear(form.getExpirationYear());
		creditCard.setCVV(form.getCVV());
		
		this.validator.validate(creditCard, binding);
		
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
		
		if (!binding.hasErrors()) {
			CreditCard saved;
			saved = this.creditCardService.save(creditCard);
			
			sponsorship.setCreditCard(saved);
		}
		
		this.validator.validate(sponsorship, binding);

		return sponsorship;
	}
	
	public Collection<Sponsorship> sponsorshipsPerConference(int conferenceId) {
		Collection<Sponsorship> res = new ArrayList<>();
		
		res = this.sponsorshipRepository.sponsorshipsPerConference(conferenceId);
		
		return res;
	}

	public Collection<Sponsorship> sponsorshipsPerSponsor(int sponsorId) {
		Collection<Sponsorship> res = new ArrayList<>();
		
		res = this.sponsorshipRepository.sponsorshipsPerSponsor(sponsorId);
		
		return res;
	}
	
	public Sponsorship findBanner(int conferenceId) {
		Sponsorship result = new Sponsorship();
		Collection<Sponsorship> sponsorships = this.sponsorshipsPerConference(conferenceId);
		
		if(!sponsorships.isEmpty()) {
			result = this.randomBanner(sponsorships);
		}
		
		return result;
	}
	
	public Sponsorship randomBanner(final Collection<Sponsorship> sponsorships) {
		Sponsorship result;
		final SecureRandom rnd = new SecureRandom();
		final List<Sponsorship> listSponsoships = new ArrayList<>(sponsorships);
		final List<Sponsorship> listAux = new ArrayList<>(listSponsoships);

		for (final Sponsorship s : listAux)
			try {
				if (this.creditCardService.checkIfExpired(s.getCreditCard().getExpirationMonth(), s.getCreditCard().getExpirationYear()))
					listSponsoships.remove(s);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}

		Integer a = (rnd.nextInt() % 10);
		while (a < 0 || a > (sponsorships.size() - 1))
			a = (rnd.nextInt() % 10);
		result = listSponsoships.get(a);
		
		this.receivePaymentForSponsorship(result);

		return result;
	}
	
	private void receivePaymentForSponsorship(Sponsorship sponsorship) {
		
		//Here would be our api call to the selected payment method
	}

}

