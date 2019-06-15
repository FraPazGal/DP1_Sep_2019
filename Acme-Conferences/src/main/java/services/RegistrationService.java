package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RegistrationRepository;
import domain.Actor;
import domain.Author;
import domain.Registration;

@Service
@Transactional
public class RegistrationService {

	// Managed repository ------------------------------
	@Autowired
	private RegistrationRepository registrationRepository;

	// Supporting services -----------------------
	@Autowired
	private UtilityService utilityService;

	// /CREATE
	public Registration create() {
		Registration result;
		Actor principal = this.utilityService.findByPrincipal();

		result = new Registration();
		result.setAuthor((Author)principal);

		return result;
	}

	// /FINDONE
	public Registration findOne(final int registrationId) {
		Registration result;

		result = this.registrationRepository.findOne(registrationId);
		Assert.notNull(result);

		return result;
	}

	// //SAVE

	public Registration save(final Registration registration) {
		Registration result;
		Actor principal;

		Assert.notNull(registration);
		Assert.isTrue(registration.getId() == 0);
		
		Assert.notNull(registration.getConference());
		Assert.notNull(registration.getCreditCard());
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(registration.getAuthor().equals(principal));

		result = this.registrationRepository.save(registration);
		Assert.notNull(result);

		return result;
	}

}
