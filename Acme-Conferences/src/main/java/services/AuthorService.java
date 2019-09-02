package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import domain.Conference;
import domain.Paper;
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

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private SubmissionService submissionService;

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

	public Boolean computeScore() {
		Boolean res;
		Collection<Conference> conferences;
		String words = "";
		Collection<String> splittedWords;
		Collection<String> voidWords;
		Collection<String> buzzWords = new ArrayList<>();
		Map<String, Integer> counter = new HashMap<>();
		Integer fmax = 0;
		Double ratioBuzzWords = 0.;

		Collection<Author> authors;
		Map<Author, Double> points = new HashMap<>();
		Integer pmax = 0;

		// Getting conferences
		conferences = this.conferenceService.findConferencesForScore();

		if (!conferences.isEmpty()) {
			// Getting all the words
			for (Conference c : conferences) {
				words += c.getTitle() + " " + c.getSummary() + " ";
			}

			// Splitting the words into an array
			words.toLowerCase();
			words.replaceAll(",.;:-", "");
			splittedWords = new ArrayList<>(Arrays.asList(words.split("\\s")));

			// Getting the void words
			voidWords = new ArrayList<>(
					Arrays.asList(this.systemConfigurationService
							.findMySystemConfiguration().getVoidWords()
							.get("Español").toLowerCase().split(",")));
			voidWords.addAll(Arrays.asList(this.systemConfigurationService
					.findMySystemConfiguration().getVoidWords().get("English")
					.toLowerCase().split(",")));

			// Deleting the void words
			Collection<String> copyWords = new ArrayList<>(splittedWords);
			for (String s : copyWords) {
				if (voidWords.contains(s.toLowerCase())) {
					splittedWords.remove(s);
				}
			}

			// Counting words and getting fmax
			for (String s : splittedWords) {
				if (!counter.containsKey(s)) {
					counter.put(s, 1);
				} else {
					int aux = counter.get(s) + 1;
					counter.put(s, aux);
					if (fmax < aux) {
						fmax = aux;
					}
				}
			}

			// Calculating the ratio to get the buzz words
			ratioBuzzWords = fmax - (0.2 * fmax);

			// Getting the buzz words
			for (String s : counter.keySet()) {
				if (counter.get(s) > ratioBuzzWords) {
					buzzWords.add(s);
				}
			}

			/*
			 * Here ends the process of getting the buzz words and now we have
			 * to compute the score of the authors
			 */

			// Getting all authors of the system
			authors = this.authorRepository.findAll();

			// Getting their points
			for (Author a : authors) {

				// Getting the CR papers
				Collection<Paper> papers = this.submissionService
						.findCRPapers(a.getId());

				if (!papers.isEmpty()) {
					for (Paper p : papers) {

						// Getting the words of the CR papers and splitting them
						words += p.getTitle() + " " + p.getSummary() + " ";

						words.toLowerCase();
						words.replaceAll(",.;:-", "");
						splittedWords = new ArrayList<>(Arrays.asList(words
								.split("\\s")));

					}

					// Deleting the void words
					copyWords = new ArrayList<>(splittedWords);
					for (String s : copyWords) {
						if (voidWords.contains(s.toLowerCase())) {
							splittedWords.remove(s);
						}
					}

					// Counting
					int aux = 0;
					for (String b : buzzWords) {
						for (String s : splittedWords) {
							if (b.equalsIgnoreCase(s)) {
								aux++;
							}
						}
					}
					points.put(a, aux * 1.);

					if (aux > pmax) {
						pmax = aux;
					}
				} else {
					// If he/she does not have any CR paper --> score = 0
					points.put(a, 0.);
				}

			}

			// Calculating the score of the authors
			for (Author a : points.keySet()) {
				Double score = points.get(a) / pmax;
				a.setScore(score);
				this.authorRepository.save(a);
				this.authorRepository.flush();
			}

			res = true;
		} else {
			res = false;
		}
		return res;
	}

	public void flush() {
		this.authorRepository.flush();
	}

}
