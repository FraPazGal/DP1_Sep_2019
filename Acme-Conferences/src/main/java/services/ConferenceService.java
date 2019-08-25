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
import domain.Mensaje;
import domain.Review;
import domain.Submission;

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
	private SubmissionService submissionService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private Validator validator;

	// CRUD Methods ------------------------------------------

	public Conference create() {
		Actor principal = this.utilityService.findByPrincipal();
		Conference result = new Conference();

		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");

		result.setAdministrator((Administrator) principal);
		result.setStatus("DRAFT");

		return result;
	}

	public Collection<Conference> findAll() {
		return this.conferenceRepository.findAll();
	}

	public Conference findOne(final int conferenceId) {
		Conference result = this.conferenceRepository.findOne(conferenceId);
		Assert.notNull(result,"wrong.id");
		
		return result;
	}

	public Conference save(final Conference conference) {
		Actor principal = this.utilityService.findByPrincipal();
		
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

		Assert.isTrue(conference.getAdministrator().equals(principal), "not.allowed");

		Conference result = this.conferenceRepository.save(conference);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Conference conference) {
		Actor principal = this.utilityService.findByPrincipal();

		Assert.notNull(conference);
		Assert.isTrue(conference.getId() != 0, "wrong.id");
		Assert.isTrue(conference.getStatus().equals("DRAFT"), "conference.final");

		Assert.isTrue(conference.getAdministrator().equals(principal), "not.allowed");

		this.conferenceRepository.delete(conference.getId());
	}

	// Other business methods -------------------------------

	public Conference reconstruct(final Conference conference, BindingResult binding) {
		Conference result, aux = null;
		Actor principal = this.utilityService.findByPrincipal();

		result = this.create();

		if (conference.getId() != 0) {
			aux = this.findOne(conference.getId());
			Assert.isTrue(aux.getStatus().equals("DRAFT"), "conference.final");
			Assert.isTrue(aux.getAdministrator().equals((Administrator) principal), "not.allowed");

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
		Collection<Conference> lastFive, auxConf;
		Calendar hello = Calendar.getInstance();
		Calendar auxCal = Calendar.getInstance();
		hello.add(Calendar.DAY_OF_MONTH, -5);
		Date toCompare = hello.getTime();
		Date now = auxCal.getTime();

		lastFive = this.conferenceRepository.findSubmissionLastFive(toCompare);
		auxConf = this.conferenceRepository.findSubmissionLastZero(toCompare);
		
		for(Conference c : auxConf) {
			if(c.getSubmissionDeadline().after(now)) {
				lastFive.add(c);
			}
		}
		return lastFive;
	}

	public Collection<Conference> findNotificationInFive() {
		Collection<Conference> inFive, auxConf;
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();

		inFive = this.conferenceRepository.findNotificationInFive(now);
		auxConf = this.conferenceRepository.findNotificationInZero(now);
		
		for(Conference c : auxConf) {
			if(c.getSubmissionDeadline().before(now)) {
				inFive.add(c);
			}
		}
		return inFive;
	}

	public Collection<Conference> findCameraInFive() {
		Collection<Conference> inFive, auxConf;
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();

		inFive = this.conferenceRepository.findCameraInFive(now);
		auxConf = this.conferenceRepository.findCameraInZero(now);
		
		for(Conference c : auxConf) {
			if(c.getSubmissionDeadline().before(now)) {
				inFive.add(c);
			}
		}
		return inFive;
	}

	public Collection<Conference> findStartInFive() {
		Collection<Conference> inFive, auxConf;
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();

		inFive = this.conferenceRepository.findStartInFive(now);
		auxConf = this.conferenceRepository.findStartInZero(now);
		
		for(Conference c : auxConf) {
			if(c.getSubmissionDeadline().before(now)) {
				inFive.add(c);
			}
		}
		return inFive;
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

	public Collection<Conference> findConferencesUnpublishedAndMine(Integer adminId) {

		return this.conferenceRepository.findConferencesUnpublishedAndMine(adminId);
	}

	public Collection<Conference> conferencesRegisteredTo(Integer authorId) {

		return this.conferenceRepository.conferencesRegisteredTo(authorId);
	}

	public Collection<Conference> conferencesSubmittedTo(Integer authorId) {

		return this.conferenceRepository.conferencesSubmittedTo(authorId);
	}
	
	public Conference findOneToEdit(int conferenceId) {
		Conference result = this.findOne(conferenceId);
		Actor principal = this.utilityService.findByPrincipal();
		
		Assert.isTrue(result.getAdministrator().equals((Administrator) principal)
				&& result.getStatus().equals("DRAFT"), "not.allowed");
				
		return result;
	}
	
	public Conference findOneToDisplay(Integer conferenceId) {
		Conference result = this.findOne(conferenceId);
		
		if(result.getStatus().equals("DRAFT")) {
			Actor actor = this.utilityService.findByPrincipal();
			Assert.notNull(actor, "not.allowed");
			Assert.isTrue(result.getAdministrator().equals((Administrator) actor));
		} else {
			Assert.isTrue(!result.getStatus().equals("DRAFT"), "not.allowed");
		}
		return result;
	}
	
	public void reviewSubmissions (Integer conferenceId) {
		Assert.isTrue(this.utilityService.assertPrincipal("ADMIN"), "not.allowed");
		Conference conference = this.findOne(conferenceId);
		Assert.isTrue(conference.getStatus().equals("FINAL"), "not.allowed");
		
		Collection<Submission> toReview = this.submissionService.findConferenceSubmitions(conferenceId);
		
		for(Submission submission : toReview) {
			Submission aux = this.reviewASubmission(submission);
			this.submissionService.saveChangeStatus(aux);
		}
		
		conference.setStatus("DECISION-MADE");
		this.conferenceRepository.save(conference);
	}
	
	private Submission reviewASubmission(Submission submission) {
		Collection<Review> reports = this.reviewService.findReviewsOfSubmission(submission.getId());
		int reject = 0; int accept = 0;
		
		for(Review report : reports) {
			if (report.getStatus().equals("ACCEPTED"))
				accept ++;
			else if (report.getStatus().equals("REJECTED"))
				reject ++;
		}
		
		if(accept >= reject)
			submission.setStatus("ACCEPTED");
		else
			submission.setStatus("REJECTED");
		
		return submission;
	}
	
	public void notifySubmissions(Integer conferenceId) {
		Collection<Actor> toNotify = this.submissionService.findActorsWithSubmitions(conferenceId);
		Conference conference = this.findOneToDisplay(conferenceId);
		
		for(Actor actor : toNotify) {
			Submission submitted = this.submissionService.findOneByActorAndConference(actor.getId(),conferenceId);
			Mensaje toSend = this.messageService.create();
			
			Collection<Actor> receiver = new ArrayList<Actor>();
			receiver.add(actor);
			toSend.setReciever(receiver);
			
			toSend.setTopic("OTHER");
			toSend.setSubject("Submission notification");
			toSend.setBody("We want to inform you that your submittion to the conference " + 
					conference.getTitle() + " has been " + submitted.getStatus());
			
			this.messageService.save(toSend);
		}
		
		conference.setStatus("NOTIFIED");
		this.conferenceRepository.save(conference);
	}

}
