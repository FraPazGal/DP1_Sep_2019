package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Conference;

@Repository
public interface ConferenceRepository extends
		JpaRepository<Conference, Integer> {
	
	@Query("select c from Conference c where datediff(?1,c.submissionDeadline)<=5 and datediff(?1,c.submissionDeadline)>=5 and c.isFinal = true")
	Collection<Conference> findSubmissionLastFive(Date toCompare);
	
	@Query("select c from Conference c where datediff(c.notificationDeadline, ?1)<=5 and datediff(c.notificationDeadline, ?1)>=5 and c.isFinal = true")
	Collection<Conference> findNotificationInFive(Date toCompare);
	
	@Query("select c from Conference c where datediff(c.cameraReadyDeadline, ?1)<=5 and datediff(c.cameraReadyDeadline, ?1)>=5 and c.isFinal = true")
	Collection<Conference> findCameraInFive(Date toCompare);
	
	@Query("select c from Conference c where datediff(c.startDate, ?1)<=5 and datediff(c.startDate, ?1)>=5 and c.isFinal = true")
	Collection<Conference> findStartInFive(Date toCompare);
	
	@Query("select c from Conference c where c.isFinal = true")
	Collection<Conference> publishedConferences();
	
	@Query("select c from Conference c where c.isFinal = false and c.administrator.id = ?1")
	Collection<Conference> findConferencesUnpublishedAndMine(Integer adminId);
	
	@Query("select c from Conference c where c.endDate < ?1 and c.isFinal = true")
	Collection<Conference> pastConferences(Date toCompare);
	
	@Query("select c from Conference c where c.startDate < ?1 and c.endDate > ?1 and c.isFinal = true")
	Collection<Conference> runningConferences(Date toCompare);
	
	@Query("select c from Conference c where c.startDate > ?1 and c.isFinal = true")
	Collection<Conference> futureConferences(Date toCompare);
	
	@Query("select c from Registration r join r.conference c where r.author.id = ?1")
	Collection<Conference> conferencesRegisteredTo(int authorId);
	
	@Query("select c from Submission s join s.conference c where s.author.id = ?1")
	Collection<Conference> conferencesSubmittedTo(int authorId);
	
}
