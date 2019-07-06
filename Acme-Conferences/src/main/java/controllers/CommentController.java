package controllers;

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
		Comment newComment = this.commentService.create();

		try {
			res = new ModelAndView("comment/create");
			res.addObject("comment", newComment);

			if (conferenceid != null) {
				res.addObject("id", conferenceid);
			} else {
				res.addObject("id", activityid);
			}
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Comment comment,
			@RequestParam(required = false) int id, BindingResult binding) {
		ModelAndView res;
		Comment validated;

		try {

			try {
				this.commentService.createComment(id, null);
			} catch (Throwable oops) {
				this.commentService.createComment(null, id);
			}

			validated = this.commentService.validate(comment, binding);

			if (binding.hasErrors()) {
				res = new ModelAndView("comment/create");
				res.addObject("comment", validated);
			} else {
				this.commentService.save(validated);

				if (validated.getActivity() != null) {
					res = new ModelAndView(
							"redirect:activiy/display.do?activityid="
									+ validated.getActivity().getId());
				} else {
					res = new ModelAndView(
							"redirect:conference/display.do?conferenceId="
									+ validated.getConference().getId());
				}

			}

		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}
}
