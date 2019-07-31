package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ConferenceRepository;
import domain.Actor;
import domain.Administrator;
import domain.Conference;

@Transactional
@Service
public class ConferenceService {

	// Managed repository ------------------------------------

	@Autowired
	private ConferenceRepository conferenceRepository;

	// Supporting services -----------------------------------

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	// CRUD Methods ------------------------------------------

	public Conference create() {
		Actor principal;
		Conference result;

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");

		result = new Conference();
		result.setAdministrator((Administrator) principal);
		result.setIsFinal(false);

		return result;
	}

	public Collection<Conference> findAll() {
		return this.conferenceRepository.findAll();
	}

	public Conference findOne(final int conferenceId) {
		return this.conferenceRepository.findOne(conferenceId);
	}

	public Conference save(final Conference conference) {
		Actor principal;
		Conference result;

		Assert.notNull(conference);
		Assert.notNull(conference.getTitle());
		Assert.notNull(conference.getAcronym());
		Assert.notNull(conference.getSummary());
		Assert.notNull(conference.getVenue());
		Assert.notNull(conference.getSubmissionDeadline());
		Assert.notNull(conference.getNotificationDeadline());
		Assert.notNull(conference.getCameraReadyDeadline());
		Assert.notNull(conference.getStartDate());
		Assert.notNull(conference.getEndDate());
		Assert.notNull(conference.getEntryFee());
		Assert.notNull(conference.getCategory());

		principal = this.utilityService.findByPrincipal();

		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");

		result = this.conferenceRepository.save(conference);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Conference conference) {
		Actor principal;

		Assert.notNull(conference);
		Assert.isTrue(conference.getId() != 0, "wrong.id");
		Assert.isTrue(!conference.getIsFinal(), "conference.final");

		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");

		Assert.isTrue(conference.getAdministrator().equals(principal),
				"not.allowed");

		this.conferenceRepository.delete(conference.getId());
	}

	// Other business methods -------------------------------

	public Conference reconstruct(final Conference conference,
			BindingResult binding) {
		Conference result, aux = null;
		Actor principal = this.utilityService.findByPrincipal();

		result = this.create();

		if (conference.getId() != 0) {
			aux = this.findOne(conference.getId());
			Assert.isTrue(!aux.getIsFinal(), "conference.final");
			Assert.isTrue(aux.getAdministrator().equals(
					(Administrator) principal), "not.allowed");

			result.setId(aux.getId());
			result.setVersion(aux.getVersion());
			result.setAdministrator(aux.getAdministrator());
		}

		result.setTitle(conference.getTitle());
		result.setVenue(conference.getVenue());
		result.setAcronym(conference.getAcronym());
		result.setSummary(conference.getSummary());
		result.setSubmissionDeadline(conference.getSubmissionDeadline());
		result.setNotificationDeadline(conference.getNotificationDeadline());
		result.setCameraReadyDeadline(conference.getCameraReadyDeadline());
		result.setStartDate(conference.getStartDate());
		result.setEndDate(conference.getEndDate());
		result.setEntryFee(conference.getEntryFee());
		result.setCategory(conference.getCategory());

		this.validator.validate(result, binding);

		if(!binding.hasErrors()) {
			
			try {
				Assert.isTrue(
						conference.getSubmissionDeadline().before(
								conference.getNotificationDeadline()),
						"submission.before.notification");
			} catch (final Exception e) {
				binding.rejectValue("submissionDeadline",
						"submission.before.notification");
			}

			try {
				Assert.isTrue(
						conference.getNotificationDeadline().before(
								conference.getCameraReadyDeadline()),
						"notification.before.camera");
			} catch (final Exception e) {
				binding.rejectValue("notificationDeadline",
						"notification.before.camera");
			}

			try {
				Assert.isTrue(
						conference.getCameraReadyDeadline().before(
								conference.getStartDate()), "camera.before.start");
			} catch (final Exception e) {
				binding.rejectValue("cameraReadyDeadline", "camera.before.start");
			}

			try {
				Assert.isTrue(
						conference.getStartDate().before(conference.getEndDate()),
						"start.before.end");
			} catch (final Exception e) {
				binding.rejectValue("startDate", "start.before.end");
			}

			try {
				Date now = new Date(System.currentTimeMillis() - 1);
				Assert.isTrue(conference.getSubmissionDeadline().after(now),
						"submission.after.now");
			} catch (final Exception e) {
				binding.rejectValue("submissionDeadline", "submission.after.now");
			}
		}
		
		this.flush();

		return result;
	}

	public void flush() {
		this.conferenceRepository.flush();
	}

	public Collection<Conference> findSubmissionLastFive() {
		Collection<Conference> result = new ArrayList<>();
		Calendar hello = Calendar.getInstance();
		hello.add(Calendar.DAY_OF_MONTH, -5);
		Date toCompare = hello.getTime();

		result = this.conferenceRepository.findSubmissionLastFive(toCompare);

		return result;
	}

	public Collection<Conference> findNotificationInFive() {
		Collection<Conference> result = new ArrayList<>();
		Calendar hello = Calendar.getInstance();
		hello.add(Calendar.DAY_OF_MONTH, -5);
		Date toCompare = hello.getTime();

		result = this.conferenceRepository.findNotificationInFive(toCompare);

		return result;
	}

	public Collection<Conference> findCameraInFive() {
		Collection<Conference> result = new ArrayList<>();
		Calendar hello = Calendar.getInstance();
		hello.add(Calendar.DAY_OF_MONTH, -5);
		Date toCompare = hello.getTime();

		result = this.conferenceRepository.findCameraInFive(toCompare);

		return result;
	}

	public Collection<Conference> findStartInFive() {
		Collection<Conference> result = new ArrayList<>();
		Calendar hello = Calendar.getInstance();
		hello.add(Calendar.DAY_OF_MONTH, -5);
		Date toCompare = hello.getTime();

		result = this.conferenceRepository.findStartInFive(toCompare);

		return result;
	}

	public Collection<Conference> publishedConferences() {

		return this.conferenceRepository.publishedConferences();
	}

	public Collection<Conference> pastConferences() {
		Collection<Conference> result = new ArrayList<>();
		Calendar hello = Calendar.getInstance();
		Date toCompare = hello.getTime();

		result = this.conferenceRepository.pastConferences(toCompare);

		return result;
	}

	public Collection<Conference> runningConferences() {
		Collection<Conference> result = new ArrayList<>();
		Calendar hello = Calendar.getInstance();
		Date toCompare = hello.getTime();

		result = this.conferenceRepository.runningConferences(toCompare);

		return result;
	}

	public Collection<Conference> futureConferences() {
		Collection<Conference> result = new ArrayList<>();
		Calendar hello = Calendar.getInstance();
		Date toCompare = hello.getTime();

		result = this.conferenceRepository.futureConferences(toCompare);

		return result;
	}

	public Collection<Conference> findConferencesUnpublishedAndMine(
			Integer adminId) {

		return this.conferenceRepository
				.findConferencesUnpublishedAndMine(adminId);
	}

	public Collection<Conference> conferencesRegisteredTo(Integer authorId) {

		return this.conferenceRepository.conferencesRegisteredTo(authorId);
	}

	public Collection<Conference> conferencesSubmittedTo(Integer authorId) {

		return this.conferenceRepository.conferencesSubmittedTo(authorId);
	}

}
