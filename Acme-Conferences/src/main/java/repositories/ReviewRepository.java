package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

	@Query("select r from Report r where r.reviewer.id = ?1")
	Collection<Review> findMyReports(Integer id);

	@Query("select r from Report r where r.submission.conference.id = ?1")
	Collection<Review> findConferenceReports(Integer id);

	@Query("select r from Report r where r.submission.id = ?1")
	Collection<Review> findReportsOfSubmission(Integer submissionId);

	@Query("select r from Report r where r.submission.id = ?1")
	Collection<Review> findSubmissionReport(Integer id);

	@Query("select case when (count(r) = 1) then true else false end from Report r where r.submission.id = ?1 and r.reviewer.id = ?2")
	boolean checkIfAssigned(Integer submissionId, Integer reviewerId);
}
