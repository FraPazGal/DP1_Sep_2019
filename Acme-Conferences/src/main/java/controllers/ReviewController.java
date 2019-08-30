package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ReviewService;
import services.ReviewerService;
import services.SubmissionService;
import services.UtilityService;
import domain.Actor;
import domain.Review;
import domain.Reviewer;
import domain.Submission;

@Controller
@RequestMapping("/review")
public class ReviewController extends AbstractController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private SubmissionService submissionService;

	@Autowired
	private ReviewerService reviewerService;

	@RequestMapping(value = "/reviewer/myreports", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Actor principal;
		Collection<Review> myReports;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"REVIEWER"));

			myReports = this.reviewService.findMyReviews(principal.getId());

			res = new ModelAndView("review/mine");
			res.addObject("reports", myReports);

		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}

	@RequestMapping(value = "/admin/conferencereports", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "id") Integer id) {
		ModelAndView res;
		Collection<Review> conferenceReports;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			conferenceReports = this.reviewService.findConferenceReviews(id);

			res = new ModelAndView("report/conference");
			res.addObject("reports", conferenceReports);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int submissionId) {
		ModelAndView result = new ModelAndView("review/list");

		try {
			Submission submission = this.submissionService
					.findOne(submissionId);
			if (this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "AUTHOR")) {
				Assert.isTrue(submission.getStatus().equals("ACCEPTED")
						|| submission.getStatus().equals("REJECTED"));
			}
			result.addObject("reviews",
					this.reviewService.findReviewsOfSubmission(submissionId));
			result.addObject("submission", submission);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(value = "id") Integer id) {
		ModelAndView result = new ModelAndView("review/display");

		try {
			Actor principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"REVIEWER")
					|| this.utilityService.checkAuthority(principal, "ADMIN")
					|| this.utilityService.checkAuthority(principal, "AUTHOR"));

			Review toShow = this.reviewService.findOne(id);

			if (this.utilityService.checkAuthority(principal, "AUTHOR")) {
				Assert.isTrue(toShow.getSubmission().getStatus()
						.equals("ACCEPTED")
						|| toShow.getSubmission().getStatus()
								.equals("REJECTED"));
				Assert.isTrue(toShow.getSubmission().getAuthor()
						.equals(principal));

			} else if (this.utilityService
					.checkAuthority(principal, "REVIEWER")) {
				Assert.isTrue(this.reviewService.checkIfAssigned(toShow
						.getSubmission().getId()));
			}
			result.addObject("review", toShow);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/admin/assign", method = RequestMethod.GET)
	public ModelAndView assignView(
			@RequestParam(value = "submissionid") Integer submissionid) {
		ModelAndView res;
		Collection<Reviewer> availableReviewers;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			availableReviewers = this.reviewerService.findAll();

			res = new ModelAndView("review/assign");
			res.addObject("reviewers", availableReviewers);
			res.addObject("submissionid", submissionid);

		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}

	@RequestMapping(value = "/admin/assign", method = RequestMethod.POST)
	public ModelAndView assign(
			@RequestParam(value = "reviewerid") Integer[] reviewerids,
			@RequestParam(value = "submissionid") Integer submissionid) {
		ModelAndView res;
		Review newReport;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			for (Integer i : reviewerids) {
				newReport = this.reviewService.create(i, submissionid);
				this.reviewService.save(newReport);
			}

			res = new ModelAndView(
					"redirect:/submission/list.do?catalog=underR");
		} catch (Throwable oops) {
			Collection<Reviewer> availableReviewers = this.reviewService
					.findReviewersNotAssigned();

			res = new ModelAndView("review/assign");
			res.addObject("reviewers", availableReviewers);
			res.addObject("submissionid", submissionid);
			res.addObject("errormesage", "transaction.error");
		}

		return res;
	}

	@RequestMapping(value = "/admin/automaticassign")
	public ModelAndView assign() {
		ModelAndView res;
		String mensaje = null;
		Collection<Reviewer> reviewers;
		Collection<Submission> submissionsToAssign;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			submissionsToAssign = this.submissionService.findAll();

			submissionsToAssign.remove(this.submissionService
					.submissionsAssigned());

			reviewers = this.reviewerService.findAll();

			this.reviewService.assign(submissionsToAssign, reviewers);

			res = new ModelAndView(
					"redirect:/submission/list.do?catalog=underR");

			submissionsToAssign = this.submissionService.findAll();

			submissionsToAssign.remove(this.submissionService
					.submissionsAssigned());

			if (!submissionsToAssign.isEmpty()) {
				mensaje = "submissions.not.assign";
			}

			res.addObject("mensaje", mensaje);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}

	@RequestMapping(value = "/reviewer/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(value = "reviewid") int reviewid) {
		ModelAndView res;
		Actor principal;
		Review toWrite;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"REVIEWER"));

			toWrite = this.reviewService.findOne(reviewid);
			Assert.notNull(toWrite);
			Assert.isTrue(toWrite.getReviewer().getId() == principal.getId());

			res = new ModelAndView("review/edit");
			res.addObject("review", toWrite);

		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}

	@RequestMapping(value = "/reviewer/edit", method = RequestMethod.POST)
	public ModelAndView save(Review review, BindingResult binding) {
		ModelAndView res;
		Actor principal;
		Review toSave;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"REVIEWER"));
			Assert.isTrue(review.getReviewer().getId() == principal.getId());

			toSave = this.reviewService.reconstruct(review, binding);

			if (binding.hasErrors()) {
				res = new ModelAndView("review/edit");
				res.addObject("review", review);
				res.addObject("binding", binding);
			} else {
				toSave.setIsWritten(true);
				this.reviewService.save(toSave);

				res = new ModelAndView("redirect:myreports.do");

			}
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../welcome/index.do");
		}

		return res;
	}
}
