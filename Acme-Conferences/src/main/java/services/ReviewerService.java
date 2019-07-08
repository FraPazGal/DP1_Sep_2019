package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.ReviewerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Reviewer;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@Transactional
@Service
public class ReviewerService {

	/* Working repository */

	@Autowired
	private ReviewerRepository reviewerRepository;

	/* Services */

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private UtilityService utilityService;

	/* Simple CRUD methods */

	/**
	 * Create an reviewer
	 * 
	 * @return Reviewer
	 */
	public Reviewer create() {
		Reviewer res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Reviewer();

		auth.setAuthority(Authority.REVIEWER);
		authority.add(auth);
		userAccount.setAuthorities(authority);
		res.setUserAccount(userAccount);

		return res;
	}

	/**
	 * Find an reviewer on the database
	 * 
	 * @param reviewerId
	 * 
	 * @return Reviewer
	 */
	public Reviewer findOne(final Integer reviewerId) {
		Reviewer res;

		res = this.reviewerRepository.findOne(reviewerId);

		return res;
	}

	/**
	 * Find all reviewers
	 * 
	 * @return Collection<Reviewer>
	 */
	public List<Reviewer> findAll() {
		return this.reviewerRepository.findAll();
	}

	/**
	 * Save an reviewer
	 * 
	 * @param Administator
	 * 
	 * @return Reviewer
	 */
	public Reviewer save(final Reviewer reviewer) {
		Reviewer res;
		Actor principal;

		Assert.notNull(reviewer);

		if (reviewer.getId() != 0) {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(principal.getId() == reviewer.getId(),
					"no.permission");

			reviewer.setUserAccount(principal.getUserAccount());

			res = this.reviewerRepository.save(reviewer);
		} else {
			res = this.reviewerRepository.save(reviewer);
		}
		return res;
	}

	/**
	 * Delete an reviewer
	 * 
	 * @param Administator
	 */
	public void delete(final Reviewer reviewer) {
		Actor principal;

		Assert.notNull(reviewer);

		principal = this.utilityService.findByPrincipal();

		Assert.isTrue(principal.getId() == reviewer.getId(), "no.permission");

		this.reviewerRepository.delete(reviewer.getId());
	}

	/**
	 * Reconstruct an reviewer from a register object form from the database
	 * 
	 * @param ActorRegistrationForm
	 * 
	 * @return Reviewer
	 */
	public Reviewer reconstruct(final ActorRegistrationForm form,
			final BindingResult binding) {

		/* Creating admin */
		final Reviewer res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		/* Creating user account */
		final UserAccount userAccount = new UserAccount();

		final List<Authority> reviewerities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.REVIEWER);
		reviewerities.add(authority);
		userAccount.setAuthorities(reviewerities);

		userAccount.setUsername(form.getUsername());

		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount
				.setPassword(encoder.encodePassword(form.getPassword(), null));

		res.setUserAccount(userAccount);

		if (form.getEmail() != null) {
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
						"REVIEWER"), "actor.email.error");

			} catch (Throwable oops) {
				binding.rejectValue("email", "email.error");
			}
		}

		/* Username */
		if (form.getUsername() != null)
			try {
				Assert.isTrue(
						this.actorService.existsUsername(form.getUsername()),
						"username.error");
			} catch (final Throwable oops) {
				binding.rejectValue("username", "username.error");
			}

		if (form.getPhoneNumber() != null) {
			try {
				final char[] phoneArray = form.getPhoneNumber().toCharArray();
				if ((!form.getPhoneNumber().equals(null) && !form
						.getPhoneNumber().equals("")))
					if (phoneArray[0] != '+'
							&& Character.isDigit(phoneArray[0])) {
						final String cc = this.systemConfigurationService
								.findMySystemConfiguration().getCountryCode();
						form.setPhoneNumber(cc + " " + form.getPhoneNumber());
					}
			} catch (Throwable oops) {
				binding.rejectValue("phoneNumber", "phone.error");
			}
		}

		return res;
	}

	/**
	 * Reconstruct an reviewer from the database
	 * 
	 * @param Administator
	 * 
	 * @return Reviewer
	 */
	public Reviewer reconstruct(final ActorForm form,
			final BindingResult binding) {

		/* Creating admin */
		final Reviewer res = this.create();

		res.setId(form.getId());
		res.setVersion(form.getVersion());
		res.setName(form.getName());
		res.setMiddleName(form.getMiddleName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		if (form.getEmail() != null) {
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
						"REVIEWER"), "actor.email.error");

			} catch (Throwable oops) {
				binding.rejectValue("email", "email.error");
			}
		}

		/* Managing phone number */
		if (form.getPhoneNumber() != null) {
			try {
				final char[] phoneArray = form.getPhoneNumber().toCharArray();
				if ((!form.getPhoneNumber().equals(null) && !form
						.getPhoneNumber().equals("")))
					if (phoneArray[0] != '+'
							&& Character.isDigit(phoneArray[0])) {
						final String cc = this.systemConfigurationService
								.findMySystemConfiguration().getCountryCode();
						form.setPhoneNumber(cc + " " + form.getPhoneNumber());
					}
			} catch (Throwable oops) {
				binding.rejectValue("phoneNumber", "phone.error");
			}
		}

		return res;
	}

	public void flush() {
		this.reviewerRepository.flush();
	}

}
