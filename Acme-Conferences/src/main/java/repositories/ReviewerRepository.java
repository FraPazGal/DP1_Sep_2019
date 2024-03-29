package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Review;
import domain.Reviewer;

@Repository
public interface ReviewerRepository extends JpaRepository<Reviewer, Integer> {

	@Query("select r from Review r where r.reviewer.id = ?1 and r.isWritten = 'true'")
	Collection<Review> isReviewing(Integer id);

	@Query("select r from Review r where r.reviewer.id = ?1 and r.isWritten = 'false'")
	Collection<Review> isAssigned(Integer id);

}
