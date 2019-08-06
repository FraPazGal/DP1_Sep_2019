package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Submission;

@Repository
public interface SubmissionRepository extends
		JpaRepository<Submission, Integer> {

	@Query("select s from Submission s where s.paper.id = ?1 or s.cameraReadyPaper.id = ?1")
	Submission findSubByPaper(int paperId);

	@Query("select s from Submission s where s.author.id = ?1")
	Collection<Submission> submissionsPerAuthor(int auhtorId);

	@Query("select s.author from Submission s where s.conference.id = ?1")
	Collection<Actor> findActorsWithSubmitions(Integer id);

	@Query("select s from Submission s where s.conference.id = ?1")
	Collection<Submission> findConferenceSubmitions(Integer id);
}
