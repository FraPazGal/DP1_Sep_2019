package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReviewerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Report;
import domain.Reviewer;
import forms.ReviewerForm;
import forms.ReviewerRegistrationForm;

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

	@Autowired
	private Validator validator;

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
	public Reviewer reconstruct(final ReviewerRegistrationForm form,
			final BindingResult binding) {

		final Reviewer res;

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

		try {
			Assert.isTrue(!form.getKeywords().trim().isEmpty());
		} catch (Throwable oops) {
			binding.rejectValue("keywords", "keywords.error");
		}

		this.validator.validate(form, binding);

		if (!binding.hasErrors()) {
			/* Creating admin */
			res = this.create();

			res.setName(form.getName());
			res.setSurname(form.getSurname());
			res.setPhoto(form.getPhoto());
			res.setEmail(form.getEmail());
			res.setPhoneNumber(form.getPhoneNumber());
			res.setAddress(form.getAddress());
			res.setKeywords(form.getKeywords());

			Md5PasswordEncoder encoder;
			encoder = new Md5PasswordEncoder();
			res.getUserAccount().setPassword(
					encoder.encodePassword(form.getPassword(), null));
			res.getUserAccount().setUsername(form.getUsername());

		} else {
			res = new Reviewer();
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
	public Reviewer reconstruct(final ReviewerForm form,
			final BindingResult binding) {

		final Reviewer res;

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

		try {
			Assert.isTrue(!form.getKeywords().trim().isEmpty());
		} catch (Throwable oops) {
			binding.rejectValue("keywords", "keywords.error");
		}

		this.validator.validate(form, binding);

		if (!binding.hasErrors()) {

			res = this.create();

			res.setId(form.getId());
			res.setVersion(form.getVersion());
			res.setName(form.getName());
			res.setMiddleName(form.getMiddleName());
			res.setSurname(form.getSurname());
			res.setPhoto(form.getPhoto());
			res.setEmail(form.getEmail());
			res.setPhoneNumber(form.getPhoneNumber());
			res.setAddress(form.getAddress());
			res.setKeywords(form.getKeywords());

		} else {
			res = new Reviewer();
		}

		return res;
	}

	public void flush() {
		this.reviewerRepository.flush();
	}

	public boolean isReviewing(Integer reviewerid) {
		Collection<Report> reports = this.reviewerRepository
				.isReviewing(reviewerid);
		return (reports == null || reports.size() == 0);
	}

}
