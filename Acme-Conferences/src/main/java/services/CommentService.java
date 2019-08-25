package services;

import java.util.Collection;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Activity;
import domain.Actor;
import domain.Comentario;
import domain.Conference;

@Transactional
@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private Validator validator;

	public Comentario createAnonymous(Integer conferenceid, Integer activityid) {
		Comentario res = new Comentario();

		res = new Comentario();

		try {
			Conference conference = this.conferenceService
					.findOne(conferenceid);
			Assert.notNull(conference);

			res.setAuthor("[Anonymous]");
			res.setWriter(this.utilityService.findByUsername("[Anonymous]"));
			res.setConference(conference);
			res.setPublishedDate(LocalDate.now().toDate());

		} catch (Throwable oops) {
			Activity activity = this.activityService.findOne(activityid);
			Assert.notNull(activity);

			res.setAuthor("[Anonymous]");
			res.setWriter(this.utilityService.findByUsername("[Anonymous]"));
			res.setActivity(activity);
			res.setPublishedDate(LocalDate.now().toDate());
		}

		return res;

	}

	public Comentario createComment(Integer conferenceid, Integer activityid) {
		Comentario res = new Comentario();
		Actor principal;
		Conference conference;
		Activity activity;

		res.setPublishedDate(LocalDate.now().toDate());

		try {
			if (conferenceid != null) {

				principal = this.utilityService.findByPrincipal();
				Assert.notNull(principal);

				conference = this.conferenceService.findOne(conferenceid);
				Assert.notNull(conference);

				res.setWriter(principal);
				res.setAuthor(principal.getUserAccount().getUsername());
				res.setConference(conference);

			} else {

				principal = this.utilityService.findByPrincipal();
				Assert.notNull(principal);

				activity = this.activityService.findOne(activityid);
				Assert.notNull(activity);

				res.setWriter(principal);
				res.setAuthor(principal.getUserAccount().getUsername());
				res.setActivity(activity);

			}
		} catch (Throwable oops) {

		}
		return res;
	}

	public Comentario save(Comentario comment) {
		return this.commentRepository.save(comment);
	}

	public void delete(Comentario comment) {
		this.commentRepository.delete(comment);
	}

	public Comentario validate(Comentario comment, BindingResult binding) {

		try {
			Assert.isTrue(comment.getConference() != null
					|| comment.getActivity() != null);
		} catch (Throwable oops) {
			binding.rejectValue("conference", "conference.error");
			binding.rejectValue("activity", "activity.error");
		}

		try {
			Assert.isTrue(!comment.getBody().trim().isEmpty());
		} catch (Throwable oops) {
			binding.rejectValue("body", "body.error");
		}

		try {
			Assert.isTrue(!comment.getTitle().trim().isEmpty());
		} catch (Throwable oops) {
			binding.rejectValue("title", "title.error");
		}

		this.validator.validate(comment, binding);

		return comment;
	}

	public Collection<Comentario> getCommentsOfConference(Integer conferenceid) {
		return this.commentRepository.getCommentsOfConference(conferenceid);
	}

	public Collection<Comentario> getCommentsOfActivity(Integer activityid) {
		return this.commentRepository.getCommentsOfActivity(activityid);
	}
}
