package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Actor;
import domain.Comment;

@Transactional
@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	public Comment create() {
		Comment res = new Comment();
		Actor principal = utilityService.findByPrincipal();

		res.setWriter(principal);
		res.setPublishedDate(LocalDate.now().toDate());
		res.setAuthor(principal.getUserAccount().getUsername());

		return res;
	}

	public Comment save(Comment comment) {
		return this.commentRepository.save(comment);
	}

	public void delete(Comment comment) {
		this.commentRepository.delete(comment);
	}

	public Comment reconstruct(Comment comment, BindingResult binding) {
		Comment res;

		try {
			Assert.isTrue(comment.getConference() != null
					|| comment.getActivity() != null);
		} catch (Throwable oops) {
			binding.rejectValue("conference", "conference.error");
			binding.rejectValue("activity", "activity.error");
		}

		validator.validate(comment, binding);

		if (binding.hasErrors()) {
			res = comment;
		} else {
			res = this.create();
			res.setTitle(comment.getTitle());
			res.setBody(comment.getBody());
		}
		return res;
	}

	public Collection<Comment> getCommentsOfConference(Integer conferenceid) {
		return this.commentRepository.getCommentsOfConference(conferenceid);
	}

	public Collection<Comment> getCommentsOfActivity(Integer activityid) {
		return this.commentRepository.getCommentsOfConference(activityid);
	}
}
