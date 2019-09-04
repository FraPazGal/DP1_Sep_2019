package services;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.QuoletRepository;
import domain.Actor;
import domain.Conference;
import domain.Quolet;

@Transactional
@Service
public class QuoletService {

	// Managed repository

	@Autowired
	private QuoletRepository quoletRepository;

	// Managed services

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private Validator validator;

	// CRUDS

	public Quolet create() {
		Actor principal = this.utilityService.findByPrincipal();
		Quolet res = new Quolet();

		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));

		res.setTicker(this.generateTicker(principal));
		res.setPublicationMoment(new Date(System.currentTimeMillis() - 1));
		res.setIsDraft(false);

		return res;
	}

	public Quolet create(int conferenceid) {
		Actor principal = this.utilityService.findByPrincipal();
		Quolet res = new Quolet();
		Conference conference = this.conferenceService.findOne(conferenceid);

		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");
		Assert.isTrue(
				conference.getAdministrator().getId() == principal.getId(),
				"not.owner");

		res.setTicker(this.generateTicker(principal));
		res.setPublicationMoment(new Date(System.currentTimeMillis() - 1));
		res.setConference(conference);

		return res;
	}

	public Quolet findOne(int quoletid) {
		return this.quoletRepository.findOne(quoletid);
	}

	public Collection<Quolet> findAll() {
		return this.quoletRepository.findAll();
	}

	public Quolet save(Quolet quolet) {
		Actor principal = this.utilityService.findByPrincipal();
		Quolet res;

		Assert.notNull(quolet, "null.quolet");
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");
		Assert.isTrue(
				quolet.getConference().getAdministrator().getId() == principal
						.getId(), "not.owner");

		res = this.quoletRepository.save(quolet);

		return res;
	}

	public void delete(Quolet quolet) {
		Actor principal = this.utilityService.findByPrincipal();

		Assert.notNull(quolet, "null.quolet");
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");
		Assert.isTrue(
				quolet.getConference().getAdministrator().getId() == principal
						.getId(), "not.owner");

		this.quoletRepository.delete(quolet);
	}

	// Ancillary methods

	public Quolet reconstruct(Quolet quolet, BindingResult binding) {
		Quolet res;

		// No creo que sea necesario utilizar un form ya que únicamente se
		// modifican tres atributos

		// Poner comprobaciones de los atributos aqui (esta comprobación no
		// tiene sentido porque publicationMoment es un valor que modificamos
		// nosotros, pero lo pongo de ejemplo)

		try {
			Assert.isTrue(
					quolet.getPublicationMoment().before(
							LocalDate.now().toDate()), "quolet.date.error");
		} catch (Throwable oops) {
			binding.rejectValue("publicationMoment", "publication.before.now");
		}

		this.validator.validate(quolet, binding);

		if (!binding.hasErrors() && quolet.getId() == 0) {
			res = this.create();

			/*
			 * aqui habria que poner los demás atributos
			 */
		} else if (!binding.hasErrors() && quolet.getId() != 0) {
			res = this.findOne(quolet.getId());

			/*
			 * Aqui se modifican los demás atributos
			 */
		} else {
			res = quolet;
			/* Se devuelve el quolet con los errores */
		}

		return res;
	}

	/* ESTOS SON LOS METODOS DE SUBMISSION --> HAY QUE CAMBIARLOS EN EL EXAMEN */
	private String generateTicker(Actor admin) {
		String uniqueTicker = null;
		String nameInitial, middleNameInitial, surnameInitial, alphaNum, initials;
		boolean unique = false;

		nameInitial = admin.getName().substring(0, 1);
		surnameInitial = admin.getSurname().substring(0, 1);
		middleNameInitial = admin.getMiddleName() == null ? "X" : admin
				.getMiddleName().substring(0, 1);

		initials = nameInitial + middleNameInitial + surnameInitial;

		while (unique == false) {
			alphaNum = this.randomString();
			uniqueTicker = initials + "-" + alphaNum;
			unique = this.checkIfUniqueTicker(uniqueTicker);
		}
		return uniqueTicker;
	}

	private String randomString() {

		final String possibleChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final SecureRandom rnd = new SecureRandom();
		final int length = 4;

		final StringBuilder stringBuilder = new StringBuilder(length);

		for (int i = 0; i < length; i++)
			stringBuilder.append(possibleChars.charAt(rnd.nextInt(possibleChars
					.length())));
		return stringBuilder.toString();

	}

	private boolean checkIfUniqueTicker(String ticker) {
		return this.quoletRepository.checkIfUniqueTicker(ticker);
	}

	public Collection<Quolet> findConferenceQuolets(Integer conferenceid) {
		return this.quoletRepository.findConferenceQuolets(conferenceid);
	}
}
