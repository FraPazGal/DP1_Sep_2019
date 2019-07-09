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

@Transactional
@Service
public class ReviewService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	public Report create() {
		Report res = new Report();

		res.setReviewer((Reviewer) this.utilityService.findByPrincipal());

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
	
	public Collection<Report> findMyReports(Integer id){
		return this.reportRepository.findMyReports(id);
	}
}
