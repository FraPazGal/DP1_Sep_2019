package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Paper;
import domain.Submission;

@Repository
public interface SubmissionRepository extends
		JpaRepository<Submission, Integer> {

	@Query("select s from Submission s where s.paper.id = ?1 or s.cameraReadyPaper.id = ?1")
	Submission findSubByPaper(int paperId);

	@Query("select s from Submission s where s.author.id = ?1")
	Collection<Submission> submissionsPerAuthor(int auhtorId);

	@Query("select s from Report r join r.submission s where r.reviewer.id = ?1")
	Collection<Submission> submissionsPerReviewer(int reviewerId);

	@Query("select s.author from Submission s where s.conference.id = ?1")
	Collection<Actor> findActorsWithSubmitions(Integer id);

	@Query("select r.submission from Report r")
	Collection<Submission> submissionsAssigned();

	@Query("select s from Submission s where s.conference.id = ?1")
	Collection<Submission> findConferenceSubmitions(Integer id);

	@Query("select case when (count(s) = 0) then true else false end from Submission s where s.ticker = ?1")
	boolean checkIfUniqueTicker(String ticker);

	@Query("select s from Submission s where s.status = 'ACCEPTED'")
	Collection<Submission> acceptedSubmissions();

	@Query("select s from Submission s where s.status = 'REJECTED'")
	Collection<Submission> rejectedSubmissions();

	@Query("select s from Submission s where s.status = 'UNDER-REVIEW'")
	Collection<Submission> underReviewSubmissions();

	@Query("select s from Submission s where s.author.id = ?1 and s.conference.id = ?2")
	Submission findOneByActorAndConference(Integer actorId, Integer conferenceId);

	@Query("select s.cameraReadyPaper from Submission s where s.conference.id = ?1")
	Collection<Paper> findCameraReadyPapersOfConference(int conferenceid);
	
	@Query("select case when (count(s) = 0) then true else false end from Submission s where s.conference.id = ?1 and s.author.id = ?2")
	boolean noPreviousSubmissions(Integer conferenceId, Integer authorId);
}
