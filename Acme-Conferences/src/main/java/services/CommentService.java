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
import domain.Comment;
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

	public Comment create() {
		Comment res = new Comment();
		Actor principal;

		res.setPublishedDate(LocalDate.now().toDate());

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.notNull(principal);

			res.setWriter(principal);
			res.setAuthor(principal.getUserAccount().getUsername());
		} catch (Throwable oops) {
			res.setAuthor("[Anonymous]");
			res.setWriter(null);
		}

		return res;

	}

	public Comment createComment(Integer conferenceid, Integer activityid) {
		Comment res = new Comment();
		Actor principal;
		Conference conference;
		Activity activity;

		res.setPublishedDate(LocalDate.now().toDate());

		try {
			if (conferenceid != null) {
				try {
					principal = this.utilityService.findByPrincipal();
					Assert.notNull(principal);

					conference = this.conferenceService.findOne(conferenceid);
					Assert.notNull(conference);

					res.setWriter(principal);
					res.setAuthor(principal.getUserAccount().getUsername());
					res.setConference(conference);
				} catch (Throwable oops) {
					conference = this.conferenceService.findOne(conferenceid);
					Assert.notNull(conference);

					res.setAuthor("[Anonymous]");
					res.setConference(conference);
				}
			} else {
				try {
					principal = this.utilityService.findByPrincipal();
					Assert.notNull(principal);

					activity = this.activityService.findOne(activityid);
					Assert.notNull(activity);

					res.setWriter(principal);
					res.setAuthor(principal.getUserAccount().getUsername());
					res.setActivity(activity);
				} catch (Throwable oops) {
					activity = this.activityService.findOne(activityid);
					Assert.notNull(activity);

					res.setAuthor("[Anonymous]");
					res.setActivity(activity);
				}
			}
		} catch (Throwable oops) {

		}

		return res;
	}

	public Comment save(Comment comment) {
		return this.commentRepository.save(comment);
	}

	public void delete(Comment comment) {
		this.commentRepository.delete(comment);
	}

	public Comment validate(Comment comment, BindingResult binding) {

		try {
			Assert.isTrue(comment.getConference() != null
					|| comment.getActivity() != null);
		} catch (Throwable oops) {
			binding.rejectValue("conference", "conference.error");
			binding.rejectValue("activity", "activity.error");
		}

		validator.validate(comment, binding);

		return comment;
	}

	public Collection<Comment> getCommentsOfConference(Integer conferenceid) {
		return this.commentRepository.getCommentsOfConference(conferenceid);
	}

	public Collection<Comment> getCommentsOfActivity(Integer activityid) {
		return this.commentRepository.getCommentsOfConference(activityid);
	}
}
