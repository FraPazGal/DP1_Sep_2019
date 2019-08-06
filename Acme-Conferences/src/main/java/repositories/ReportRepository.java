package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

	@Query("select r from Report r where r.reviewer.id = ?1")
	Collection<Report> findMyReports(Integer id);

	@Query("select r from Report r where r.submission.conference.id = ?1")
	Collection<Report> findConferenceReports(Integer id);
}
