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
//import repositories.AuthorRepository;
//import security.Authority;
//import security.UserAccount;
//import domain.Author;
//import forms.ActorForm;
//
//@Transactional
//@Service
//public class AuthorService {
//
//	@Autowired
//	private AuthorRepository authorRepository;
//
//	@Autowired
//	private UtilityService utilityService;
//
//	@Autowired
//	private Validator validator;
//
//	public Author create() {
//		Author res;
//
//		UserAccount userAccount;
//		Authority auth;
//		Collection<Authority> authority;
//
//		auth = new Authority();
//		authority = new ArrayList<Authority>();
//		userAccount = new UserAccount();
//		res = new Author();
//
//		auth.setAuthority(Authority.AUTHOR);
//		authority.add(auth);
//		userAccount.setAuthorities(authority);
//
//		res.setUserAccount(userAccount);
//
//		return res;
//	}
//
//	public Author save(Author author) {
//		return this.authorRepository.save(author);
//	}
//
//	public void delete(Author author) {
//		this.authorRepository.delete(author);
//	}
//
//	public Author reconstruct(ActorForm form, BindingResult binding) {
//		Author res = this.create();
//
//		if (form.getId() != 0) {
//			res.setId(form.getId());
//			res.setVersion(form.getVersion());
//		}
//
//		try {
//			if (form.getEmail() != null) {
//				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
//						"AUTHOR"));
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

import repositories.AuthorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Author;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@Transactional
@Service
public class AuthorService {

	/* Working repository */

	@Autowired
	private AuthorRepository authorRepository;

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
	 * Create an author
	 * 
	 * @return Author
	 */
	public Author create() {
		Author res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Author();

		auth.setAuthority(Authority.AUTHOR);
		authority.add(auth);
		userAccount.setAuthorities(authority);
		res.setUserAccount(userAccount);

		return res;
	}

	/**
	 * Find an author on the database
	 * 
	 * @param authorId
	 * 
	 * @return Author
	 */
	public Author findOne(final Integer authorId) {
		Author res;

		res = this.authorRepository.findOne(authorId);

		return res;
	}

	/**
	 * Find all authors
	 * 
	 * @return Collection<Author>
	 */
	public List<Author> findAll() {
		return this.authorRepository.findAll();
	}

	/**
	 * Save an author
	 * 
	 * @param Administator
	 * 
	 * @return Author
	 */
	public Author save(final Author author) {
		Author res;
		Actor principal;

		Assert.notNull(author);

		if (author.getId() != 0) {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(principal.getId() == author.getId(), "no.permission");

			author.setUserAccount(principal.getUserAccount());

			res = this.authorRepository.save(author);
		} else {
			res = this.authorRepository.save(author);
		}
		return res;
	}

	/**
	 * Delete an author
	 * 
	 * @param Administator
	 */
	public void delete(final Author author) {
		Actor principal;

		Assert.notNull(author);

		principal = this.utilityService.findByPrincipal();

		Assert.isTrue(principal.getId() == author.getId(), "no.permission");

		this.authorRepository.delete(author.getId());
	}

	/**
	 * Reconstruct an author from a register object form from the database
	 * 
	 * @param ActorRegistrationForm
	 * 
	 * @return Author
	 */
	public Author reconstruct(final ActorRegistrationForm form,
			final BindingResult binding) {

		final Author res;

		if (form.getEmail() != null) {
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
						"AUTHOR"), "actor.email.error");

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

		this.validator.validate(form, binding);

		if (!binding.hasErrors()) {
			res = this.create();

			res.setName(form.getName());
			res.setSurname(form.getSurname());
			res.setPhoto(form.getPhoto());
			res.setEmail(form.getEmail());
			res.setPhoneNumber(form.getPhoneNumber());
			res.setAddress(form.getAddress());

			Md5PasswordEncoder encoder;
			encoder = new Md5PasswordEncoder();
			res.getUserAccount().setPassword(
					encoder.encodePassword(form.getPassword(), null));

			res.getUserAccount().setUsername(form.getUsername());
		} else {
			res = new Author();
		}

		return res;
	}

	/**
	 * Reconstruct an author from the database
	 * 
	 * @param Administator
	 * 
	 * @return Author
	 */
	public Author reconstruct(final ActorForm form, final BindingResult binding) {

		final Author res;

		if (form.getEmail() != null) {
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(),
						"AUTHOR"), "actor.email.error");

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
		} else {
			res = new Author();
		}
		/* Creating admin */

		return res;
	}

	public void flush() {
		this.authorRepository.flush();
	}

}
