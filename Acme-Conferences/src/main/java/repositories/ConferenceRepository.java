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
	
	@Query("select c from Conference c where c.submissionDeadline > ?2 and c.submissionDeadline < ?1 and c.status != 'DRAFT'")
	Collection<Conference> findSubmissionLastFive(Date now, Date nowMinusFive);

	@Query("select c from Conference c where c.notificationDeadline > ?1 and c.notificationDeadline < ?2 and c.status != 'DRAFT'")
	Collection<Conference> findNotificationInFive(Date now, Date nowPlusFive);

	@Query("select c from Conference c where c.cameraReadyDeadline > ?1 and c.cameraReadyDeadline < ?2 and c.status != 'DRAFT'")
	Collection<Conference> findCameraInFive(Date now, Date nowPlusFive);
	
	@Query("select c from Conference c where c.startDate > ?1 and c.startDate < ?2 and c.status != 'DRAFT'")
	Collection<Conference> findStartInFive(Date now, Date nowPlusFive);

	@Query("select c from Conference c where c.status != 'DRAFT'")
	Collection<Conference> publishedConferences();

	@Query("select c from Conference c where c.status = 'DRAFT' and c.administrator.id = ?1")
	Collection<Conference> findConferencesUnpublishedAndMine(Integer adminId);

	@Query("select c from Conference c where c.endDate < ?1 and c.status != 'DRAFT'")
	Collection<Conference> pastConferences(Date toCompare);

	@Query("select c from Conference c where c.startDate < ?1 and c.endDate > ?1 and c.status != 'DRAFT'")
	Collection<Conference> runningConferences(Date toCompare);

	@Query("select c from Conference c where c.startDate > ?1 and c.status != 'DRAFT'")
	Collection<Conference> futureConferences(Date toCompare);

	@Query("select c from Registration r join r.conference c where r.author.id = ?1")
	Collection<Conference> conferencesRegisteredTo(int authorId);

	@Query("select c from Submission s join s.conference c where s.author.id = ?1")
	Collection<Conference> conferencesSubmittedTo(int authorId);

	@Query("select c from Conference c where c.startDate > ?1 and c.status != 'DRAFT'")
	Collection<Conference> conferences4Score(Date toCompare);

}
