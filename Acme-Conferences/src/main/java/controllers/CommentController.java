package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repositories.ConferenceRepository;
import services.ActivityService;
import services.CommentService;
import services.UtilityService;
import domain.Activity;
import domain.Actor;
import domain.Comment;
import domain.Conference;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	@Autowired
	private CommentService commentService;

	// @Autowired
	// private ConferenceService conferenceService;

	@Autowired
	private ConferenceRepository conferenceRepository; // TODO cambiar a service

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UtilityService utilityService;

	// Creating comment

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(required = false) Integer conferenceid,
			@RequestParam(required = false) Integer activityid) {
		ModelAndView res;
		Comment newComment;
		Actor principal;
		Activity activity;
		Conference conference;
		boolean permission = false;

		try {
			try {
				principal = this.utilityService.findByPrincipal();
				Assert.notNull(principal);

				activity = this.activityService.findOne(activityid);
				Assert.notNull(activity);

				newComment = this.commentService.create();
				newComment.setActivity(activity);

				permission = true;
			} catch (Throwable oops) {
				principal = this.utilityService.findByPrincipal();
				Assert.notNull(principal);

				conference = this.conferenceRepository.findOne(conferenceid);
				Assert.notNull(conference);

				newComment = this.commentService.create();
				newComment.setConference(conference);

				permission = true;
			}
			res = new ModelAndView("comment/create");
			res.addObject("newComment", newComment);
			res.addObject("permission", permission);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Comment comment, BindingResult binding) {
		ModelAndView res;
		Comment toSave;

		try {
			toSave = this.commentService.reconstruct(comment, binding);

			if (binding.hasErrors()) {
				res = new ModelAndView("comment/create");
				res.addObject(toSave);
				res.addObject("permission", true);
			} else {
				this.commentService.save(toSave);
				res = new ModelAndView("welcome/index");
			}
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}
}
