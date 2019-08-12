package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.UtilityService;
import domain.Comentario;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private UtilityService utilityService;

	// Creating comment

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(required = false) Integer conferenceid,
			@RequestParam(required = false) Integer activityid) {
		ModelAndView res;
		Comentario newComment;

		try {

			try {
				Assert.notNull(this.utilityService.findByPrincipal());
				try {
					newComment = this.commentService.createComment(
							conferenceid, null);
				} catch (Throwable oops) {
					newComment = this.commentService.createComment(null,
							activityid);
				}
			} catch (Throwable oops) {
				newComment = this.commentService.createAnonymous(conferenceid,
						activityid);
			}
			res = new ModelAndView("comment/create");
			res.addObject("comment", newComment);

		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Comentario comment, BindingResult binding) {
		ModelAndView res;
		Comentario validated;

		try {

			validated = this.commentService.validate(comment, binding);

			if (binding.hasErrors()) {
				res = new ModelAndView("comment/create");
				res.addObject("comment", comment);
				res.addObject("binding", binding);
			} else {
				this.commentService.save(validated);

				if (validated.getActivity() != null) {
					res = new ModelAndView(
							"redirect:/activity/display.do?activityid="
									+ validated.getActivity().getId());
				} else {
					res = new ModelAndView(
							"redirect:/conference/display.do?conferenceId="
									+ validated.getConference().getId());
				}

			}

		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}
}
