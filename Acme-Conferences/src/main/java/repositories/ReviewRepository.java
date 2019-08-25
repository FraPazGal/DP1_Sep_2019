package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

	@Query("select r from Review r where r.reviewer.id = ?1")
	Collection<Review> findMyReviews(Integer id);

	@Query("select r from Review r where r.submission.conference.id = ?1")
	Collection<Review> findConferenceReviews(Integer id);

	@Query("select r from Review r where r.submission.id = ?1")
	Collection<Review> findReviewsOfSubmission(Integer submissionId);

	@Query("select r from Review r where r.submission.id = ?1")
	Collection<Review> findSubmissionReview(Integer id);

	@Query("select case when (count(r) = 1) then true else false end from Review r where r.submission.id = ?1 and r.reviewer.id = ?2")
	boolean checkIfAssigned(Integer submissionId, Integer reviewerId);
}
