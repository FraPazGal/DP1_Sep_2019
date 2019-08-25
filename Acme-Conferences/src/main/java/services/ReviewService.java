package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReviewRepository;
import domain.Actor;
import domain.Review;
import domain.Reviewer;
import domain.Submission;

@Transactional
@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reportRepository;

	@Autowired
	private ReviewerService reviewerService;

	@Autowired
	private SubmissionService submissionService;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	public Review create(Integer reviewerid, Integer submissionid) {
		Reviewer reviewer;
		Submission submission;
		Review res = new Review();

		reviewer = this.reviewerService.findOne(reviewerid);
		Assert.notNull(reviewer);
		res.setReviewer(reviewer);

		submission = this.submissionService.findOne(submissionid);
		Assert.notNull(submission);
		res.setSubmission(submission);

		res.setIsWritten(false);

		return res;
	}

	public Review create(Review report) {
		Review res = new Review();

		res.setOriginalityScore(report.getOriginalityScore());
		res.setQualityScore(report.getQualityScore());
		res.setReadabilityScore(report.getReadabilityScore());
		res.setStatus(report.getStatus());
		res.setComments(report.getComments());
		res.setSubmission(report.getSubmission());
		res.setReviewer(report.getReviewer());
		res.setId(report.getId());
		res.setVersion(report.getVersion());

		return res;
	}

	public Review save(Review report) {
		return this.reportRepository.save(report);
	}

	public void delete(Review report) {
		this.reportRepository.delete(report);
	}

	public Review reconstruct(Review report, BindingResult binding) {
		Review res;

		try {
			Assert.notNull(report.getSubmission());
		} catch (Throwable oops) {
			binding.rejectValue("submission", "submission.error");
		}

		try {
			Assert.notNull(report.getOriginalityScore());
		} catch (Throwable oops) {
			binding.rejectValue("originalityScore", "originality.error");
		}

		try {
			Assert.notNull(report.getQualityScore());
		} catch (Throwable oops) {
			binding.rejectValue("qualityScore", "quality.error");
		}

		try {
			Assert.notNull(report.getReadabilityScore());
		} catch (Throwable oops) {
			binding.rejectValue("readabilityScore", "readability.error");
		}

		try {
			Assert.isTrue(!report.getComments().isEmpty());
		} catch (Throwable oops) {
			binding.rejectValue("comments", "comments.error");
		}

		validator.validate(report, binding);

		if (binding.hasErrors()) {
			res = report;
		} else {
			res = this.create(report);
		}
		return res;
	}

	public Collection<Review> findMyReports(Integer id) {
		return this.reportRepository.findMyReports(id);
	}

	public Collection<Review> findConferenceReports(Integer id) {
		return this.reportRepository.findConferenceReports(id);
	}

	public Review findOne(Integer id) {
		return this.reportRepository.findOne(id);
	}

	public Collection<Reviewer> findReviewersNotAssigned() {
		Collection<Reviewer> res;
		Collection<Review> reports;

		res = this.reviewerService.findAll();
		reports = this.findAll();

		for (Review r : reports) {
			if (res.contains(r.getReviewer()) && !r.getIsWritten()) {
				res.remove(r.getReviewer());
			}
		}

		return res;
	}

	private Collection<Review> findAll() {
		return this.reportRepository.findAll();
	}

	public void assign(Collection<Submission> submissions,
			Collection<Reviewer> reviewers) {

		for (Submission s : submissions) {
			int i = 0;
			for (Reviewer r : reviewers) {
				if (this.reviewerService.isNotAssigned(r.getId())) {
					String[] keywords = r.getKeywords().toLowerCase()
							.split(",");
					for (String keyword : keywords) {
						if (s.getConference().getTitle().toLowerCase()
								.contains(keyword)
								|| s.getConference().getSummary().toLowerCase()
										.contains(keyword)) {
							Review report = this.create(r.getId(), s.getId());
							this.save(report);
							this.reportRepository.flush();
							i++;
							break;
						}
					}
					if (i == 3)
						break;
				}
			}
		}

	}

	public boolean isAssigned(int id) {
		return !this.reportRepository.findSubmissionReport(id).isEmpty();
	}

	public Collection<Review> findReportsOfSubmission(Integer submissionId) {
		return this.reportRepository.findReportsOfSubmission(submissionId);
	}

	public boolean checkIfAssigned(Integer submissionId) {
		Actor principal = this.utilityService.findByPrincipal();

		return this.reportRepository.checkIfAssigned(submissionId,
				principal.getId());
	}
}
