package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReportRepository;
import domain.Report;
import domain.Reviewer;
import domain.Submission;

@Transactional
@Service
public class ReviewService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ReviewerService reviewerService;

	@Autowired
	private SubmissionService submissionService;

	@Autowired
	private Validator validator;

	public Report create(Integer reviewerid, Integer submissionid) {
		Reviewer reviewer;
		Submission submission;
		Report res = new Report();

		reviewer = this.reviewerService.findOne(reviewerid);
		Assert.notNull(reviewer);
		res.setReviewer(reviewer);

		submission = this.submissionService.findOne(submissionid);
		Assert.notNull(submission);
		res.setSubmission(submission);

		res.setIsWritten(false);

		return res;
	}

	public Report create(Report report) {
		Report res = new Report();

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

	public Report save(Report report) {
		return this.reportRepository.save(report);
	}

	public void delete(Report report) {
		this.reportRepository.delete(report);
	}

	public Report reconstruct(Report report, BindingResult binding) {
		Report res;

		try {
			Assert.notNull(report.getSubmission());
		} catch (Throwable oops) {
			binding.rejectValue("submission", "submission.error");
		}

		validator.validate(report, binding);

		if (binding.hasErrors()) {
			res = report;
		} else {
			res = this.create(report);
		}
		return res;
	}

	public Collection<Report> findMyReports(Integer id) {
		return this.reportRepository.findMyReports(id);
	}

	public Collection<Report> findConferenceReports(Integer id) {
		return this.reportRepository.findConferenceReports(id);
	}

	public Report findOne(Integer id) {
		return this.reportRepository.findOne(id);
	}

	public Collection<Reviewer> findReviewersNotAssigned() {
		Collection<Reviewer> res;
		Collection<Report> reports;

		res = this.reviewerService.findAll();
		reports = this.findAll();

		for (Report r : reports) {
			if (res.contains(r.getReviewer()) && !r.getIsWritten()) {
				res.remove(r.getReviewer());
			}
		}

		return res;
	}

	private Collection<Report> findAll() {
		return this.reportRepository.findAll();
	}

	public void assign(Collection<Submission> submissions,
			Collection<Reviewer> reviewers) {

		for (Submission s : submissions) {
			int i = 0;
			for (Reviewer r : reviewers) {
				if (!this.reviewerService.isReviewing(r.getId())) {
					Report report = this.create(r.getId(), s.getId());
					this.save(report);
					this.reportRepository.flush();
					i++;
					if (i == 3)
						break;
				}
			}
		}

	}

	public boolean isAssigned(int id) {
		return this.reportRepository.findSubmissionReport(id) != null;
	}
}
