package services;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SubmissionRepository;
import domain.Actor;
import domain.Author;
import domain.Paper;
import domain.Submission;
import forms.SubmissionForm;

@Transactional
@Service
public class SubmissionService {

	// Managed repository ------------------------------------

	@Autowired
	private SubmissionRepository submissionRepository;

	// Supporting services -----------------------------------

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private PaperService paperService;

	@Autowired
	private Validator validator;

	// CRUD Methods ------------------------------------------

	public Submission create() {
		Actor principal;
		Submission result;

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "AUTHOR"),
				"not.allowed");

		result = new Submission();
		result.setSubmissionMoment(new Date(System.currentTimeMillis() - 1));
		result.setAuthor((Author) principal);
		result.setTicker(this.generateTicker(principal));
		result.setStatus("UNDER-REVIEW");

		return result;
	}

	public Collection<Submission> findAll() {
		return this.submissionRepository.findAll();
	}

	public Submission findOne(final int submissionId) {
		return this.submissionRepository.findOne(submissionId);
	}

	public Submission save(final Submission submission) {
		Actor principal;
		Submission result;

		Assert.notNull(submission.getTicker());
		Assert.notNull(submission.getStatus());
		Assert.notNull(submission.getSubmissionMoment());
		Assert.notNull(submission.getAuthor());
		Assert.notNull(submission.getConference());
		Assert.notNull(submission.getPaper());

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(principal.equals(submission.getAuthor()), "not.allowed");

		Assert.isTrue(
				submission.getSubmissionMoment().before(
						submission.getConference().getSubmissionDeadline()),
				"submissionDeadline.limit");

		result = this.submissionRepository.save(submission);

		return result;
	}

	public void delete(final Submission submission) {
		Actor principal;

		Assert.notNull(submission);
		Assert.isTrue(submission.getId() != 0, "wrong.id");

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(principal.equals(submission.getAuthor()));

		this.submissionRepository.delete(submission.getId());
	}

	// Other business methods -------------------------------

	public void flush() {
		this.submissionRepository.flush();
	}

	public Submission reconstruct(SubmissionForm form, BindingResult binding) {

		Date now = new Date(System.currentTimeMillis() - 1);
		Submission submission = this.create();

		Assert.isTrue(form.getConference().getIsFinal(), "wrong.conference");
		Assert.isTrue(
				submission.getSubmissionMoment().before(
						form.getConference().getSubmissionDeadline()),
				"submissionDeadline.limit");
		Assert.isTrue(
				now.before(form.getConference().getCameraReadyDeadline()),
				"cameraReadyDeadline.limit");

		/* Creating paper */
		Paper paper = this.paperService.create();

		if (form.getId() == 0) {
			paper.setTitle(form.getTitleP());
			paper.setAuthors(form.getAuthorsP());
			paper.setSummary(form.getSummaryP());
			paper.setPaperDocument(form.getPaperDocumentP());

			try {
				Assert.isTrue(!paper.getTitle().isEmpty());
			} catch (Throwable oops) {
				binding.rejectValue("titleP", "empty.string");
			}

			try {
				Assert.isTrue(!paper.getAuthors().isEmpty());
			} catch (Throwable oops) {
				binding.rejectValue("authorsP", "empty.string");
			}

			try {
				Assert.isTrue(!paper.getSummary().isEmpty());
			} catch (Throwable oops) {
				binding.rejectValue("summaryP", "empty.string");
			}

			try {
				Assert.isTrue(!paper.getPaperDocument().isEmpty());
			} catch (Throwable oops) {
				binding.rejectValue("paperDocumentP", "empty.string");
			}

		} else {
			paper.setTitle(form.getTitlePCR());
			paper.setAuthors(form.getAuthorsPCR());
			paper.setSummary(form.getSummaryPCR());
			paper.setPaperDocument(form.getPaperDocumentPCR());

			try {
				Assert.isTrue(!paper.getTitle().isEmpty());
			} catch (Throwable oops) {
				binding.rejectValue("titlePCR", "empty.string");
			}

			try {
				Assert.isTrue(!paper.getAuthors().isEmpty());
			} catch (Throwable oops) {
				binding.rejectValue("authorsPCR", "empty.string");
			}

			try {
				Assert.isTrue(!paper.getSummary().isEmpty());
			} catch (Throwable oops) {
				binding.rejectValue("summaryPCR", "empty.string");
			}

			try {
				Assert.isTrue(!paper.getPaperDocument().isEmpty());
			} catch (Throwable oops) {
				binding.rejectValue("paperDocumentPCR", "empty.string");
			}
		}

		if (!binding.hasErrors()) {
			Paper saved;

			if (form.getId() != 0) {
				submission = this.findOne(form.getId());
				Assert.isTrue(submission.getStatus().equals("ACCEPTED"),
						"submission.not.accepted");

				saved = this.paperService.save(paper);

				submission.setCameraReadyPaper(saved);
			} else {
				saved = this.paperService.save(paper);

				submission.setConference(form.getConference());
				submission.setPaper(saved);
			}
			this.validator.validate(submission, binding);
		}
		return submission;
	}

	private String generateTicker(Actor author) {
		String uniqueTicker = null;
		String nameInitial, middleNameInitial, surnameInitial, alphaNum, initials;
		boolean unique = false;

		nameInitial = author.getName().substring(0, 1);
		surnameInitial = author.getSurname().substring(0, 1);
		middleNameInitial = author.getMiddleName() == null ? "X" : author
				.getMiddleName().substring(0, 1);

		while (unique == false) {
			alphaNum = this.randomString();
			initials = nameInitial + middleNameInitial + surnameInitial;
			uniqueTicker = initials + "-" + alphaNum;
			for (final Submission submission : this.findAll())
				if (submission.getTicker().equals(uniqueTicker))
					continue;
			unique = true;
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

	public Submission findSubByPaper(int paperId) {
		return this.submissionRepository.findSubByPaper(paperId);
	}

	public Collection<Actor> findActorsWithSubmitions(Integer id) {
		return this.submissionRepository.findActorsWithSubmitions(id);
	}

	public Collection<Submission> submissionsPerReviewer(int reviewerId) {
		return this.submissionRepository.submissionsPerReviewer(reviewerId);
	}
	
	public Collection<Submission> submissionsPerAuthor(int authorId) {
		return this.submissionRepository.submissionsPerAuthor(authorId);
	}

	public Collection<Submission> findConferenceSubmitions(Integer id) {
		return this.submissionRepository.findConferenceSubmitions(id);
	}

	public Collection<Submission> submissionsAssigned() {
		return this.submissionRepository.submissionsAssigned();
	}
	
	public Collection<Submission> submissionsToAssign() {
		Collection<Submission> result = this.findAll();
		Collection<Submission> assigned = this.submissionsAssigned();
		
		result.removeAll(assigned);
		
		return result;
	}
	
	public boolean checkPrincipal(Author author) {
		boolean result = false;
		Actor principal = this.utilityService.findByPrincipal();
		if(principal.equals(author)) {
			result = true;
		}
		
		return result;
	}
}
