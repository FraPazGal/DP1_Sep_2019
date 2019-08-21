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
import services.SubmissionService;
import services.UtilityService;
import domain.Actor;
import domain.Report;
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

	@RequestMapping(value = "/reviewer/myreports", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Actor principal;
		Collection<Report> myReports;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"REVIEWER"));

			myReports = this.reviewService.findMyReports(principal.getId());

			res = new ModelAndView("review/mine");
			res.addObject("reports", myReports);

		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}

		return res;
	}

	@RequestMapping(value = "/admin/conferencereports", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "id") Integer id) {
		ModelAndView res;
		Collection<Report> conferenceReports;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			conferenceReports = this.reviewService.findConferenceReports(id);

			res = new ModelAndView("report/conference");
			res.addObject("reports", conferenceReports);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}

		return res;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(value = "id") Integer id) {
		ModelAndView res;
		Actor principal;
		Boolean isOwner;
		Report toShow;

		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal,
					"REVIEWER")
					|| this.utilityService.checkAuthority(principal, "ADMIN"));

			toShow = this.reviewService.findOne(id);

			isOwner = toShow.getReviewer().getId() == principal.getId();

			res = new ModelAndView("report/display");
			res.addObject("report", toShow);
			res.addObject("isOwner", isOwner);
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}

		return res;
	}

	@RequestMapping(value = "/admin/assign", method = RequestMethod.GET)
	public ModelAndView assignView(
			@RequestParam(value = "submissionid") Integer submissionid) {
		ModelAndView res;
		Collection<Reviewer> availableReviewers;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			availableReviewers = this.reviewService.findReviewersNotAssigned();

			res = new ModelAndView("review/assign");
			res.addObject("reviewers", availableReviewers);
			res.addObject("submissionid", submissionid);

		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}

		return res;
	}

	@RequestMapping(value = "/admin/assign", method = RequestMethod.POST)
	public ModelAndView assign(
			@RequestParam(value = "reviewerid") Integer[] reviewerids,
			@RequestParam(value = "submissionid") Integer submissionid) {
		ModelAndView res;
		Report newReport;

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
		Collection<Reviewer> reviewers;
		Collection<Submission> submissionsToAssign;

		try {
			Assert.isTrue(this.utilityService.checkAuthority(
					this.utilityService.findByPrincipal(), "ADMIN"));

			submissionsToAssign = this.submissionService.findAll();

			submissionsToAssign.remove(this.submissionService
					.submissionsAssigned());

			reviewers = this.reviewService.findReviewersNotAssigned();

			this.reviewService.assign(submissionsToAssign, reviewers);

			res = new ModelAndView(
					"redirect:/submission/list.do?catalog=underR");
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}

		return res;
	}

	@RequestMapping(value = "/reviewer/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(value = "reviewid") int reviewid) {
		ModelAndView res;
		Actor principal;
		Report toWrite;

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
			res = new ModelAndView("welcome/index");
		}

		return res;
	}

	@RequestMapping(value = "/reviewer/edit", method = RequestMethod.POST)
	public ModelAndView save(Report review, BindingResult binding) {
		ModelAndView res;
		Actor principal;
		Report toSave;

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
			res = new ModelAndView("welcome/index");
		}

		return res;
	}
}
