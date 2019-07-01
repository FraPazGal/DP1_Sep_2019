package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import domain.Comment;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	@Autowired
	private CommentService commentService;

	// Creating comment

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(required = false) Integer conferenceid,
			@RequestParam(required = false) Integer activityid) {
		ModelAndView res;
		Comment newComment;

		try {
			if (conferenceid != null)
				newComment = this.commentService.create(conferenceid, null);
			else
				newComment = this.commentService.create(null, activityid);

			res = new ModelAndView("comment/create");
			res.addObject("newComment", newComment);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Comment comment, BindingResult binding) {
		ModelAndView res;
		Comment validated;

		try {

			validated = this.commentService.validate(comment, binding);

			if (binding.hasErrors()) {
				res = new ModelAndView("comment/create");
				res.addObject("comment", validated);
			} else {
				this.commentService.save(validated);

				if (validated.getActivityId() != null) {
					res = new ModelAndView(
							"redirect:activiy/display.do?activityid="
									+ validated.getActivityId());
				} else {
					res = new ModelAndView(
							"redirect:conference/display.do?conferenceId="
									+ validated.getConferenceId());
				}

			}

		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}
}
