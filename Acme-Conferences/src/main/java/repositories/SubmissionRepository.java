package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Submission;

@Repository
public interface SubmissionRepository extends
		JpaRepository<Submission, Integer> {
	
	@Query("select s from Submission s where s.paper.id = ?1 or s.cameraReadyPaper.id = ?1")
	Submission findSubByPaper(int paperId);

}
