//package services;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import javax.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.Assert;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.Validator;
//
//import repositories.SponsorRepository;
//import security.Authority;
//import security.UserAccount;
//import domain.Sponsor;
//import forms.ActorForm;
//
//@Transactional
//@Service
//public class SponsorService {
//
//	@Autowired
//	private SponsorRepository sponsorRepository;
//
//	@Autowired
//	private UtilityService utilityService;
//
//	@Autowired
//	private Validator validator;
//
//	public Sponsor create() {
//		Sponsor res;
//
//		UserAccount userAccount;
//		Authority auth;
//		Collection<Authority> authority;
//
//		auth = new Authority();
//		authority = new ArrayList<Authority>();
//		userAccount = new UserAccount();
//		res = new Sponsor();
//
//		auth.setAuthority(Authority.SPONSOR);
//		authority.add(auth);
//		userAccount.setAuthorities(authority);
//
//		res.setUserAccount(userAccount);
//
//		return res;
//	}
//
//	public Sponsor save(Sponsor sponsor) {
//		return this.sponsorRepository.save(sponsor);
//	}
//
//	public void delete(Sponsor sponsor) {
//		this.sponsorRepository.delete(sponsor);
//	}
//
//	public Sponsor reconstruct(ActorForm form, BindingResult binding) {
//		Sponsor res = this.create();
//
//		if (form.getId() != 0) {
//			res.setId(form.getId());
//			res.setVersion(form.getVersion());
//		}
//
//		try {
//			if (form.getEmail() != null) {
//				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
//						"SPONSOR"));
//				res.setEmail(form.getEmail());
//			}
//		} catch (Throwable oops) {
//			binding.rejectValue("email", "email.error");
//		}
//
//		res.setName(form.getName());
//		res.setMiddleName(form.getMiddleName());
//		res.setSurname(form.getSurname());
//		res.setPhoto(form.getPhoto());
//		res.setPhoneNumber(form.getPhoneNumber());
//		res.setAddress(form.getAddress());
//
//		validator.validate(res, binding);
//
//		return res;
//	}
//}

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

import repositories.SponsorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Sponsor;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@Transactional
@Service
public class SponsorService {

	/* Working repository */

	@Autowired
	private SponsorRepository sponsorRepository;

	/* Services */

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private UtilityService utilityService;

	/* Simple CRUD methods */

	/**
	 * Create an sponsor
	 * 
	 * @return Sponsor
	 */
	public Sponsor create() {
		Sponsor res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Sponsor();

		auth.setAuthority(Authority.SPONSOR);
		authority.add(auth);
		userAccount.setAuthorities(authority);
		res.setUserAccount(userAccount);

		return res;
	}

	/**
	 * Find an sponsor on the database
	 * 
	 * @param sponsorId
	 * 
	 * @return Sponsor
	 */
	public Sponsor findOne(final Integer sponsorId) {
		Sponsor res;

		res = this.sponsorRepository.findOne(sponsorId);

		return res;
	}

	/**
	 * Find all sponsors
	 * 
	 * @return Collection<Sponsor>
	 */
	public List<Sponsor> findAll() {
		return this.sponsorRepository.findAll();
	}

	/**
	 * Save an sponsor
	 * 
	 * @param Administator
	 * 
	 * @return Sponsor
	 */
	public Sponsor save(final Sponsor sponsor) {
		Sponsor res;
		Actor principal;

		Assert.notNull(sponsor);

		if (sponsor.getId() != 0) {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(principal.getId() == sponsor.getId(), "no.permission");

			sponsor.setUserAccount(principal.getUserAccount());

			res = this.sponsorRepository.save(sponsor);
		} else {
			res = this.sponsorRepository.save(sponsor);
		}
		return res;
	}

	/**
	 * Delete an sponsor
	 * 
	 * @param Administator
	 */
	public void delete(final Sponsor sponsor) {
		Actor principal;

		Assert.notNull(sponsor);

		principal = this.utilityService.findByPrincipal();

		Assert.isTrue(principal.getId() == sponsor.getId(), "no.permission");

		this.sponsorRepository.delete(sponsor.getId());
	}

	/**
	 * Reconstruct an sponsor from a register object form from the database
	 * 
	 * @param ActorRegistrationForm
	 * 
	 * @return Sponsor
	 */
	public Sponsor reconstruct(final ActorRegistrationForm form,
			final BindingResult binding) {

		/* Creating admin */
		final Sponsor res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		/* Creating user account */
		final UserAccount userAccount = new UserAccount();

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.SPONSOR);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		userAccount.setUsername(form.getUsername());

		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount
				.setPassword(encoder.encodePassword(form.getPassword(), null));

		res.setUserAccount(userAccount);

		if (form.getEmail() != null) {
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
						"SPONSOR"), "actor.email.error");

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
	 * Reconstruct an sponsor from the database
	 * 
	 * @param Administator
	 * 
	 * @return Sponsor
	 */
	public Sponsor reconstruct(final ActorForm form, final BindingResult binding) {

		/* Creating admin */
		final Sponsor res = this.create();

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
						"SPONSOR"), "actor.email.error");

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
		this.sponsorRepository.flush();
	}

}
