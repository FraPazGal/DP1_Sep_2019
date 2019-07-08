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
//import repositories.AdministratorRepository;
//import security.Authority;
//import security.UserAccount;
//import domain.Administrator;
//import forms.ActorForm;
//
//@Transactional
//@Service
//public class AdministratorService {
//
//	@Autowired
//	private AdministratorRepository administratorRepository;
//
//	@Autowired
//	private UtilityService utilityService;
//
//	@Autowired
//	private Validator validator;
//
//	public Administrator create() {
//		Administrator res;
//
//		UserAccount userAccount;
//		Authority auth;
//		Collection<Authority> authority;
//
//		auth = new Authority();
//		authority = new ArrayList<Authority>();
//		userAccount = new UserAccount();
//		res = new Administrator();
//
//		auth.setAuthority(Authority.ADMIN);
//		authority.add(auth);
//		userAccount.setAuthorities(authority);
//
//		res.setUserAccount(userAccount);
//
//		return res;
//	}
//
//	public Administrator save(Administrator administrator) {
//		return this.administratorRepository.save(administrator);
//	}
//
//	public void delete(Administrator administrator) {
//		this.administratorRepository.delete(administrator);
//	}
//
//	public Administrator reconstruct(ActorForm form, BindingResult binding) {
//		Administrator res = this.create();
//
//		if (form.getId() != 0) {
//			res.setId(form.getId());
//			res.setVersion(form.getVersion());
//		}
//
//		try {
//			if (form.getEmail() != null) {
//				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
//						"ADMIN"));
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
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@Transactional
@Service
public class AdministratorService {

	/* Working repository */

	@Autowired
	private AdministratorRepository administratorRepository;

	/* Services */

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	/* Simple CRUD methods */

	/**
	 * Create an administrator
	 * 
	 * @return Administrator
	 */
	public Administrator create() {
		Administrator res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Administrator();

		auth.setAuthority(Authority.ADMIN);
		authority.add(auth);
		userAccount.setAuthorities(authority);
		res.setUserAccount(userAccount);

		return res;
	}

	/**
	 * Find an administrator on the database
	 * 
	 * @param administratorId
	 * 
	 * @return Administrator
	 */
	public Administrator findOne(final Integer administratorId) {
		Administrator res;

		res = this.administratorRepository.findOne(administratorId);

		return res;
	}

	/**
	 * Find all administrators
	 * 
	 * @return Collection<Administrator>
	 */
	public List<Administrator> findAll() {
		return this.administratorRepository.findAll();
	}

	/**
	 * Save an administrator
	 * 
	 * @param Administator
	 * 
	 * @return Administrator
	 */
	public Administrator save(final Administrator administrator) {
		Administrator res;
		Actor principal;

		Assert.notNull(administrator);

		principal = this.utilityService.findByPrincipal();

		if (administrator.getId() == 0) {

			Assert.isTrue(
					this.utilityService.checkAuthority(principal, "ADMIN"),
					"no.permission");

			res = this.administratorRepository.save(administrator);

		} else {

			Assert.isTrue(principal.getId() == administrator.getId(),
					"no.permission");

			administrator.setUserAccount(principal.getUserAccount());

			res = this.administratorRepository.save(administrator);
		}

		return res;
	}

	/**
	 * Reconstruct an administrator from a register object form from the
	 * database
	 * 
	 * @param RegisterFormObject
	 * 
	 * @return Administrator
	 */
	public Administrator reconstruct(final ActorRegistrationForm form,
			final BindingResult binding) {

		/* Creating admin */
		final Administrator res = this.create();

		res.setName(form.getName());
		res.setMiddleName(form.getMiddleName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		/* Creating user account */
		final UserAccount userAccount = new UserAccount();

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
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
						"ADMIN"), "actor.email.error");

			} catch (Throwable oops) {
				binding.rejectValue("email", "email.error");
			}
		}

		/* Username */
		if (form.getUsername() != null)
			try {
				Assert.isTrue(
						this.utilityService.existsUsername(form.getUsername()),
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
	 * Reconstruct an administrator from the database
	 * 
	 * @param Administator
	 * 
	 * @return Administrator
	 */
	public Administrator reconstruct(final ActorForm form,
			final BindingResult binding) {

		/* Creating admin */
		final Administrator res = this.create();

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
						"ADMIN"), "actor.email.error");

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

		this.validator.validate(res, binding);

		return res;
	}

	public Administrator findByUsername(final String username) {
		return this.administratorRepository.findByUsername(username);
	}

	public void flush() {
		this.administratorRepository.flush();
	}

}