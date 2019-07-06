package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;

@Repository
public interface DashboardRepository extends
		JpaRepository<Administrator, Integer> {

	@Query("select max(1.0*(select count(*) from Submission s where s.conference = c)),min(1.0*(select count(*) from Submission s where s.conference = c)),avg(1.0*(select count(*) from Submission s where s.conference = c)),stddev(1.0*(select count(*) from Submission s where s.conference = c)) from Conference c")
	Double[] statsSubmissionsPerConference();

	@Query("select max(1.0*(select count(*) from Registration r where r.conference = c)),min(1.0*(select count(*) from Registration r where r.conference = c)),avg(1.0*(select count(*) from Registration r where r.conference = c)),stddev(1.0*(select count(*) from Registration r where r.conference = c)) from Conference c")
	Double[] statsRegistrationsPerConference();

	@Query("select max(c.entryFee),min(c.entryFee),avg(c.entryFee),stddev(c.entryFee) from Conference c")
	Double[] statsConferenceFees();

	@Query("select max(datediff(c.endDate, c.startDate)),min(datediff(c.endDate, c.startDate)),avg(datediff(c.endDate, c.startDate)),stddev(datediff(c.endDate, c.startDate)) from Conference c")
	Double[] statsDaysPerConference();

	@Query("select max(c.conferences.size),min(c.conferences.size),avg(c.conferences.size),stddev(c.conferences.size) from Category c")
	Double[] statsConferencesPerCategory();

	@Query("select max(1.0*(select count(*) from Comment com where com.conference = c)),min(1.0*(select count(*) from Comment com where com.conference = c)),avg(1.0*(select count(*) from Comment com where com.conference = c)),stddev(1.0*(select count(*) from Comment com where com.conference = c)) from Conference c")
	Double[] statsCommentsPerConference();

	@Query("select max(1.0*(select count(*) from Comment com where com.activity = a)),min(1.0*(select count(*) from Comment com where com.activity = a)),avg(1.0*(select count(*) from Comment com where com.activity = a)),stddev(1.0*(select count(*) from Comment com where com.activity = a)) from Activity a")
	Double[] statsCommentsPerActivity();

}
